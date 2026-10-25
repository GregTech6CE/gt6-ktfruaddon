"""Stable provenance headers and validated recipe adaptation rules."""
import json
import re

import paths


def load_mappings():
    data = json.loads((paths.ROOT / "tools/mappings.json").read_text(encoding="utf-8"))
    if set(data) != {"block_aliases", "gt_substitutions", "form_substitutions"}:
        raise ValueError("mappings.json: unexpected or missing sections")
    for section in ("block_aliases", "gt_substitutions"):
        if not isinstance(data[section], dict) or not data[section]:
            raise ValueError(f"{section}: expected nonempty object")
        for source, target in data[section].items():
            if not all(isinstance(x, str) and re.fullmatch(r"[a-z0-9_]+", x) for x in (source, target)):
                raise ValueError(f"{section}: invalid registry path")
    seen = set()
    if not isinstance(data["form_substitutions"], list):
        raise ValueError("form_substitutions: expected list")
    for row in data["form_substitutions"]:
        if not isinstance(row, dict) or set(row) != {"prefix", "material", "item"}:
            raise ValueError("form_substitutions: expected prefix, material and item")
        if not all(isinstance(x, str) and re.fullmatch(r"[A-Za-z][A-Za-z0-9_]*", x) for x in row.values()):
            raise ValueError("form_substitutions: invalid identifier")
        key = (row["prefix"], row["material"])
        if key in seen:
            raise ValueError("form_substitutions: duplicate form")
        seen.add(key)
    return data


def header(script, mappings=None):
    lines = ["/*", " * GENERATED FILE - DO NOT EDIT. 勿手改。",
             f" * Upstream: {paths.UPSTREAM_URL} @ {paths.UPSTREAM_COMMIT}",
             f" * GT6CE: {paths.PIN_FILE.read_text(encoding='utf-8').strip()}",
             f" * Generate from repository root: python tools/{script}"]
    if mappings is None:
        lines.append(" * Adaptations: none in this file.")
    else:
        lines.append(" * Adaptations from tools/mappings.json (aliases and substitutions):")
        for section in ("block_aliases", "gt_substitutions"):
            for source, target in sorted(mappings[section].items()):
                lines.append(f" * {section}: {source} -> {target}")
        for row in sorted(mappings["form_substitutions"], key=lambda r: (r["prefix"], r["material"])):
            lines.append(f" * form_substitutions: {row['prefix']} {row['material']} -> minecraft:{row['item'].lower()}")
    return "\n".join([*lines, " */", ""])


def java_mappings(data):
    result = {}
    for section in ("block_aliases", "gt_substitutions"):
        result[section] = "\n".join(f'            case "{source}" -> "{target}";' for source, target in sorted(data[section].items()))
    result["form_substitutions"] = "\n".join(
        f'        if ("{row["prefix"]}".equals(prefix) && "{row["material"]}".equals(material)) return new ItemStack(Items.{row["item"]}, count);'
        for row in sorted(data["form_substitutions"], key=lambda r: (r["prefix"], r["material"])))
    return result
