"""Generate or check owned outputs without modifying the checkout in check mode."""
import argparse
import hashlib
import os
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile

import generated
import paths

SCRIPTS = ("generate_items.py", "generate_layouts.py", "generate_structure_assets.py",
           "port_block_textures.py", "extract_machine_recipes.py")
JAVA = Path("src/main/java/com/lombinaxmasson/gt6ktfruaddon/generated")
HEADERS = {"KtfruItems.java": SCRIPTS[0], "GeneratedLayouts.java": SCRIPTS[1], "SourceRecipes.java": SCRIPTS[4]}


def check_headers():
    mappings = generated.load_mappings()
    for name, script in HEADERS.items():
        expected = generated.header(script, mappings if name == "SourceRecipes.java" else None)
        actual = (paths.ROOT / JAVA / name).read_text(encoding="utf-8")
        if not actual.startswith(expected):
            raise ValueError(f"{JAVA / name}: missing or stale generated header; run python tools/regenerate.py")
    print("Generated headers and adaptation mappings: OK", flush=True)


def available():
    # Existing but mismatched checkouts are failures even when another input is missing.
    checks = {"upstream": paths.UPSTREAM.is_dir(), "gt6": (paths.GT6_ROOT / "core/src").is_dir(),
              "gt6_ref": (paths.GT6_REF / paths.GT6_REF_LOADER).is_file(), "runtime_jar": paths.RUNTIME_JAR.is_file()}
    previous = os.environ.pop("ALLOW_INPUT_MISMATCH", None)
    try:
        paths.require_inputs(**checks)
    finally:
        if previous is not None:
            os.environ["ALLOW_INPUT_MISMATCH"] = previous
    missing = [name for name, found in checks.items() if not found]
    if missing:
        print("Full regeneration inputs unavailable: " + ", ".join(missing), flush=True)
    return not missing


def run_pipeline(root, env=None):
    for script in SCRIPTS:
        done = subprocess.run([sys.executable, str(root / "tools" / script)], cwd=root,
                              env=env, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
        if done.returncode:
            raise RuntimeError(f"{script} failed ({done.returncode}):\n{done.stdout}")
        print(script + ": OK", flush=True)


def snapshot(root):
    files = [*(root / "src").rglob("*"), root / "tools/own_items.json"]
    return {p.relative_to(root).as_posix(): hashlib.sha256(p.read_bytes()).hexdigest()
            for p in files if p.is_file()}


def check_full():
    baseline = snapshot(paths.ROOT)
    with tempfile.TemporaryDirectory(prefix="ktfru-generated-") as directory:
        staging = Path(directory)
        shutil.copytree(paths.ROOT / "src", staging / "src")
        shutil.copytree(paths.ROOT / "tools", staging / "tools", ignore=shutil.ignore_patterns("__pycache__"))
        (staging / "libs/gt6ce").mkdir(parents=True)
        (staging / "build").mkdir()
        shutil.copy2(paths.PIN_FILE, staging / "libs/gt6ce/build-commit.txt")
        shutil.copy2(paths.RUNTIME_JAR, staging / "libs/gt6ce" / paths.RUNTIME_JAR.name)
        env = dict(os.environ, KTFRU_UPSTREAM=str(paths.UPSTREAM), GT6CE_SOURCE=str(paths.GT6_ROOT), GT6_REF=str(paths.GT6_REF))
        env.pop("ALLOW_INPUT_MISMATCH", None)
        run_pipeline(staging, env)
        first = snapshot(staging)
        differences = [name for name in sorted(baseline.keys() | first.keys()) if baseline.get(name) != first.get(name)]
        if differences:
            raise ValueError("Regenerated outputs differ (added/deleted/modified):\n" + "\n".join(differences))
        run_pipeline(staging, env)
        if snapshot(staging) != first:
            raise ValueError("Second generation changed outputs: pipeline is not idempotent")
        # The structure-assets writer alone must not revert texture-owned models.
        done = subprocess.run([sys.executable, str(staging / "tools/generate_structure_assets.py")], cwd=staging,
                              env=env, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
        if done.returncode or snapshot(staging) != first:
            raise ValueError("Standalone structure assets changed final outputs:\n" + done.stdout)
    print("Full regeneration: byte-identical; repeat and standalone resource checks: OK", flush=True)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true", help="check headers and regenerate in isolation when inputs exist")
    mode = parser.add_mutually_exclusive_group()
    mode.add_argument("--full", action="store_true", help="require all inputs in check mode")
    mode.add_argument("--headers-only", action="store_true", help="only validate headers and mappings")
    args = parser.parse_args()
    if args.full or args.headers_only or args.check:
        check_headers()
        if args.headers_only:
            return
        if available():
            check_full()
        elif args.full:
            paths.require_inputs(upstream=True, gt6=True, gt6_ref=True, runtime_jar=True)
        else:
            print("Check mode: headers only (see tools/README.md for full input setup)")
    else:
        paths.require_inputs(upstream=True, gt6=True, gt6_ref=True, runtime_jar=True)
        generated.load_mappings()
        run_pipeline(paths.ROOT)


if __name__ == "__main__":
    try:
        main()
    except (ValueError, RuntimeError, OSError) as error:
        sys.exit(str(error))
