"""One-shot reader for the pinned kTFRUAddon material list. Not shipped in the mod jar."""
import json
import re
import sys
from pathlib import Path

import paths

ROOT = paths.ROOT
SRC = paths.UPSTREAM / "src/main/java/cn/kuzuanpa/ktfruaddon/api/material/materialPreInit.java"
GT = paths.GT6_ROOT / "core" / "src" / "main" / "java"
OUT_JAVA = ROOT / "src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruMaterials.java"
OUT_EN = ROOT / "src/main/resources/assets/gt6ce/lang/en_us.json"
OUT_ZH = ROOT / "src/main/resources/assets/gt6ce/lang/zh_cn.json"
LANG_ZH = paths.UPSTREAM / "src/main/resources/assets/ktfruaddon/lang/zh_CN.lang"
SKIP = {"HenSoPretty"}
SUB = "₀₁₂₃₄₅₆₇₈₉"

def gt_names():
    names = set()
    pat = re.compile(r'\b(?:dust|metal|gas|liquid|ore|gem|fluid|plasma|wood|food|any)\(\s*\d+\s*,\s*"([^"]+)"')
    pat2 = re.compile(r'GTMaterial\s+(\w+)\s*=')
    for path in GT.rglob("*.java"):
        text = path.read_text(encoding="utf-8", errors="replace")
        names.update(pat.findall(text))
        names.update(pat2.findall(text))
    return names

def chinese_names():
    names = {}
    for line in LANG_ZH.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, value = line.split("=", 1)
        if key.startswith("gt.material."):
            names[key[len("gt.material."):]] = value
    return names

def comment_names(text):
    names = {}
    for match in re.finditer(r'//([^\n]+)\n\s*matList\.(\w+)\.', text):
        comment = match.group(1).strip()
        if not comment or "：" in comment or ":" in comment or len(comment) > 16:
            continue
        names[match.group(2)] = comment
    return names

def formula(expr):
    if expr.strip() == "null":
        return ""
    parts = re.split(r'\s*\+\s*', expr.strip())
    out = []
    for part in parts:
        if part.startswith('"'):
            out.append(part[1:-1])
        else:
            sub = re.fullmatch(r'NUM_SUB\[(\d+)\]', part)
            sym = re.fullmatch(r'Symbols\[(\d+)\]', part)
            if sub:
                out.append("".join(SUB[int(d)] for d in sub.group(1)))
            elif sym:
                out.append("≡" if sym.group(1) == "0" else "⌬")
            else:
                raise SystemExit(f"formula token {part}")
    return "".join(out)

def parse(text):
    chunks = re.split(r'\n\s*matList\.', text)[1:]
    rows = []
    for chunk in chunks:
        head = re.match(r'(\w+)\s*\.\s*(registerWithDust|registerC|register)\(', chunk, re.S)
        if not head:
            raise SystemExit(chunk[:200])
        field, kind = head.group(1), head.group(2)
        start = head.end()
        depth = 1
        quote = False
        end = None
        for index in range(start, len(chunk)):
            ch = chunk[index]
            if ch == '"':
                quote = not quote
            elif not quote:
                if ch == '(':
                    depth += 1
                elif ch == ')':
                    depth -= 1
                    if depth == 0:
                        end = index
                        break
        if end is None:
            raise SystemExit(chunk[:200])
        args = chunk[start:end]
        tail = chunk[end + 1:]
        # arguments are comma-separated, but formula contains commas inside quotes only
        bits = []
        buf = ""
        quote = False
        depth = 0
        for ch in args:
            if ch == '"':
                quote = not quote
                buf += ch
            elif ch == ',' and not quote and depth == 0:
                bits.append(buf.strip())
                buf = ""
            else:
                if ch == '[':
                    depth += 1
                elif ch == ']':
                    depth -= 1
                buf += ch
        if buf.strip():
            bits.append(buf.strip())
        ident, display, melt, boil, r, g, b = bits[1], bits[2], bits[3], bits[4], bits[5], bits[6], bits[7]
        form = formula(bits[9])
        flags = set(re.findall(r'TD\.ItemGenerator\.(\w+)|TD\.Prefix\.(\w+)', tail.split("matList.", 1)[0]))
        flat = {a or b for a, b in flags}
        melt_k = int(melt) + (273 if kind == "registerC" else 0)
        boil_k = int(boil) + (273 if kind == "registerC" else 0)
        rows.append({
            "field": field,
            "name": ident.strip('"'),
            "display": display.strip('"'),
            "melt": melt_k,
            "boil": boil_k,
            "color": (int(r) << 16) | (int(g) << 8) | int(b),
            "formula": form,
            "dust": "DUSTS" in flat or kind == "registerWithDust",
            "liquid": "LIQUID" in flat,
            "gas": "GASES" in flat,
            "metal": "INGOTS" in flat or "INGOT_BASED" in flat,
            "plates": "PLATES" in flat or "FOILS" in flat,
            "ores": "ORES" in flat,
            "kind": kind,
        })
    return rows

def java_string(value):
    return '"' + value.replace("\\", "\\\\").replace('"', '\\"') + '"'

def main():
    known = gt_names()
    source = SRC.read_text(encoding="utf-8")
    rows = parse(source)
    comments = comment_names(source)
    kept, skipped = [], []
    notes = []
    for row in rows:
        if row["name"] in SKIP:
            skipped.append(row["name"] + " (joke)")
            continue
        if row["name"] in known or row["name"].lower() in {n.lower() for n in known}:
            skipped.append(row["name"] + " (built-in)")
            continue
        if row["metal"]:
            row["dust"] = False
        if row["plates"] and not row["metal"]:
            notes.append(row["name"] + " plates/foils")
        if row["ores"]:
            notes.append(row["name"] + " ore")
        if row["melt"] < 0 or row["boil"] < 0:
            skipped.append(row["name"] + " (negative kelvin)")
            continue
        kept.append(row)
    lines = [
        "package com.lombinaxmasson.gt6ktfruaddon;",
        "",
        "import java.util.List;",
        "",
        "import com.gregtech.gregtech.api.addon.AddonMaterialContributor;",
        "import com.gregtech.gregtech.api.addon.MaterialDeclaration;",
        "import com.gregtech.gregtech.api.addon.MaterialFormRequest;",
        "",
        "/**",
        " * Chemical materials from kTFRUAddon materialPreInit at 6404ab69. Temperatures",
        " * follow the source: Celsius entries are stored as Celsius plus 273. Density is 1",
        " * because that list does not carry one. Plate, foil and ore requests stay empty:",
        " * a form the material flags do not allow fails startup, and ore is not a generated prefix.",
        " */",
        "public final class Gt6KtfruMaterials implements AddonMaterialContributor {",
        "    @Override public String addonId() { return Gt6KtfruAddon.MODID; }",
        "",
        "    @Override public List<MaterialDeclaration> materials() {",
        "        return List.of(",
    ]
    for i, row in enumerate(kept):
        comma = "," if i + 1 < len(kept) else ""
        calls = []
        if row["metal"]:
            calls.append(".metal()")
        elif row["dust"]:
            calls.append(".dust()")
        if row["liquid"]:
            calls.append(".liquid()")
        if row["gas"]:
            calls.append(".gas()")
        form = "".join(calls)
        lines.append(
            f"                MaterialDeclaration.builder({java_string(row['name'])})"
            f".displayName({java_string(row['display'])})"
            f".color(0x{row['color']:06X})"
            f".chemicalFormula({java_string(row['formula'])})"
            f".meltingPointKelvin({row['melt']}).boilingPointKelvin({row['boil']}).density(1F)"
            f"{form}.build(){comma}"
        )
    lines += [
        "        );",
        "    }",
        "",
        "    @Override public List<MaterialFormRequest> forms() { return List.of(); }",
        "}",
        "",
    ]
    OUT_JAVA.write_text("\n".join(lines), encoding="utf-8", newline="\n")
    en = {f"material.gt6ce.{row['name'].lower()}": row["display"] for row in kept}
    zh_src = chinese_names()
    folded = {key.lower(): value for key, value in zh_src.items()}
    zh = {}
    missing = []
    for row in kept:
        translated = zh_src.get(row["name"]) or folded.get(row["name"].lower()) or comments.get(row["field"])
        key = f"material.gt6ce.{row['name'].lower()}"
        if translated:
            zh[key] = translated
        else:
            zh[key] = row["display"]
            missing.append(row["name"])
    OUT_EN.write_text(json.dumps(en, indent=2, ensure_ascii=False) + "\n", encoding="utf-8", newline="\n")
    OUT_ZH.write_text(json.dumps(zh, indent=2, ensure_ascii=False) + "\n", encoding="utf-8", newline="\n")
    print(f"zh translated {len(kept) - len(missing)} missing {len(missing)}")
    for name in missing:
        print(" untranslated", name)
    print(f"kept {len(kept)} skipped {len(skipped)}")
    for item in skipped:
        print(" skip", item)
    for item in notes:
        print(" note", item)

if __name__ == "__main__":
    if "--force" not in sys.argv[1:]:
        sys.exit(
            "gen_materials.py was a one-shot. Gt6KtfruMaterials.java and assets/gt6ce/lang/*.json have been edited "
            "by hand since (prefixes(), .polymer(), the Bittern liquid, fluid flags), so regenerating loses those "
            "edits.\nPass --force to overwrite them anyway.")
    paths.require_inputs(upstream=True, gt6=True)
    main()
