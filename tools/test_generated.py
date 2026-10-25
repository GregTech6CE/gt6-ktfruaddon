"""Regression checks for generation validation and isolation failures."""
import copy
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

import generated
import paths
import regenerate


class GenerationChecks(unittest.TestCase):
    def setUp(self):
        self.rules = generated.load_mappings()
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        (self.root / 'tools').mkdir()
        (self.root / 'tools/mappings.json').write_text(json.dumps(self.rules), encoding='utf-8')
        pin = self.root / 'pin.txt'
        pin.write_text('a' * 40, encoding='utf-8')
        jar = self.root / 'runtime.jar'
        jar.write_bytes(b'fixture')
        for attribute, value in (('PIN_FILE', pin), ('RUNTIME_JAR', jar)):
            fixture_patch = patch.object(paths, attribute, value)
            fixture_patch.start()
            self.addCleanup(fixture_patch.stop)
        self.patch = patch.object(paths, 'ROOT', self.root)
        self.patch.start()
        self.addCleanup(self.patch.stop)

    def test_invalid_mapping_cannot_emit_java(self):
        self.rules['block_aliases']['bad'] = 'bad"; injected'
        (self.root / 'tools/mappings.json').write_text(json.dumps(self.rules), encoding='utf-8')
        with self.assertRaises(ValueError):
            generated.load_mappings()

    def test_duplicate_form_rejected(self):
        self.rules['form_substitutions'].append(copy.copy(self.rules['form_substitutions'][0]))
        (self.root / 'tools/mappings.json').write_text(json.dumps(self.rules), encoding='utf-8')
        with self.assertRaises(ValueError):
            generated.load_mappings()

    def headers(self):
        folder = self.root / regenerate.JAVA
        folder.mkdir(parents=True)
        for name, script in regenerate.HEADERS.items():
            (folder / name).write_text(generated.header(script, self.rules if name == 'SourceRecipes.java' else None) + 'class Body {}', encoding='utf-8')
        return folder

    def test_missing_or_stale_header_rejected(self):
        folder = self.headers()
        regenerate.check_headers()
        (folder / 'KtfruItems.java').write_text('class Body {}', encoding='utf-8')
        with self.assertRaisesRegex(ValueError, 'KtfruItems'):
            regenerate.check_headers()

    def test_mapping_change_invalidates_header(self):
        self.headers()
        self.rules['gt_substitutions']['casing_machine_naquadria'] = 'other'
        (self.root / 'tools/mappings.json').write_text(json.dumps(self.rules), encoding='utf-8')
        with self.assertRaisesRegex(ValueError, 'SourceRecipes'):
            regenerate.check_headers()

    def test_missing_input_falls_back_explicitly(self):
        with patch.object(paths, 'UPSTREAM', self.root / 'absent'), patch.object(paths, 'require_inputs') as preflight:
            self.assertFalse(regenerate.available())
            self.assertFalse(preflight.call_args.kwargs['upstream'])

    def test_bad_existing_input_does_not_fall_back(self):
        with patch.object(paths, 'require_inputs', side_effect=SystemExit('wrong commit')):
            with self.assertRaisesRegex(SystemExit, 'wrong commit'):
                regenerate.available()

    def test_unreadable_commit_is_rejected(self):
        with patch.object(paths, '_head', return_value=None), patch.dict('os.environ', {}, clear=True):
            problems = []
            paths._check_commit('upstream', self.root, 'a' * 40, problems)
            self.assertEqual(len(problems), 1)

    def staging_inputs(self):
        (self.root / 'src').mkdir()
        (self.root / 'src/sample').write_bytes(b'original')
        (self.root / 'tools/own_items.json').write_text('{}', encoding='utf-8')

    def test_changed_output_is_detected_without_touching_workspace(self):
        self.staging_inputs()
        def mutate(root, env):
            (root / 'src/sample').write_bytes(b'changed')
            (root / 'src/added').write_bytes(b'new')
        with patch.object(regenerate, 'run_pipeline', side_effect=mutate):
            with self.assertRaisesRegex(ValueError, 'src/added.*\n.*src/sample'):
                regenerate.check_full()
        self.assertEqual((self.root / 'src/sample').read_bytes(), b'original')
        self.assertFalse((self.root / 'src/added').exists())

    def test_deleted_output_is_detected(self):
        self.staging_inputs()
        with patch.object(regenerate, 'run_pipeline', side_effect=lambda root, env: (root / 'src/sample').unlink()):
            with self.assertRaisesRegex(ValueError, 'src/sample'):
                regenerate.check_full()

    def test_second_generation_drift_is_detected(self):
        self.staging_inputs()
        calls = []
        def drift(root, env):
            calls.append(root)
            if len(calls) == 2:
                (root / 'src/sample').write_bytes(b'drift')
        with patch.object(regenerate, 'run_pipeline', side_effect=drift):
            with self.assertRaisesRegex(ValueError, 'not idempotent'):
                regenerate.check_full()


if __name__ == '__main__':
    unittest.main()
