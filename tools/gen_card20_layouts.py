# Transcribes kTFRUAddon layer structures into Gt6KtfruGeneratedLayouts.java.
# Expandable layers are written once. Run from the repo root.

import re
from pathlib import Path

import paths

ROOT = paths.ROOT
SRC = paths.UPSTREAM / "src" / "main" / "java" / "cn" / "kuzuanpa" / "ktfruaddon"
OUT = ROOT / "src" / "main" / "java" / "com" / "lombinaxmasson" / "gt6ktfruaddon" / "generated" / "Gt6KtfruGeneratedLayouts.java"

MATERIALS = {
    "Al": "Aluminium",
    "StainlessSteel": "StainlessSteel",
    "TungstenSteel": "Tungstensteel",
    "Os": "Osmium",
    "Pt": "Platinum",
    "Ir": "Iridium",
    "Ti": "Titanium",
    "Pb": "Lead",
    "Cu": "Copper",
    "Ni": "Nickel",
    "Cr": "Chromium",
    "Au": "Gold",
    "Fe": "Iron",
    "Steel": "Steel",
    "Nq_522": "Naquadria",
    "Nq": "Naquadah",
    "Naquadah": "Naquadah",
    "Trinaquadalloy": "Trinaquadalloy",
    "Graphene": "Graphene",
    "W": "Tungsten",
    "ElectricalSteel": "ElectricalSteel",
}

# Ids that already have a layout part in this addon, or a GregTech shared part.
KNOWN = {
    31000: "aluminium_wall",
    31200: "catalytic_reactor_casing",
    31040: "magnetic_conductor",
    31041: "battery_cabin",
    18002: "tank_wall",
    18006: "titanium_wall",
    18009: "steel_wall",
    18041: "large_niobium_titanium_coil",
}
SHARED = {18002, 18006, 18009, 18041, 31000, 31200, 31040, 31041}

CONST = {
    "mWall": 18006,
    "mCoil": 18041,
    "mCond": 31040,
    "mBatt": 31041,
    "SHIELD_WALL": 31204,
    "MASS_RING": 31205,
    "GYRO_ANCHOR": 31206,
    "ACCESS_HATCH": 31207,
    "ENERGY_PORT": 31208,
    "WALL": 18002,
    "GLASS": 31056,
}

NORMAL = "gt6ktfruaddon:wireless_compute_node_aluminium"
QUANTUM = "gt6ktfruaddon:quantum_compute_node_naquadah"

# (layout name, source path, compute block or "")
JOBS = [
    ("optical_microscope", "tile/multiblock/research/OpticalMicroscope.java", ""),
    ("electron_microscope", "tile/multiblock/research/ElectronMicroscope.java", ""),
    ("wireless_compute_controller", "tile/computerCluster/controller/ControllerWireless.java", NORMAL),
    ("wireless_compute_controller_large", "tile/computerCluster/controller/ControllerWirelessLarge.java", NORMAL),
    ("wireless_compute_controller_huge", "tile/computerCluster/controller/ControllerWirelessHuge.java", NORMAL),
    ("biology_compute_t1", "tile/computerCluster/controller/BiologyComputeControllerT1.java", NORMAL),
    ("biology_compute_t2", "tile/computerCluster/controller/BiologyComputeControllerT2.java", NORMAL),
    ("biology_compute_t3", "tile/computerCluster/controller/BiologyComputeControllerT3.java", NORMAL),
    ("biology_compute_t4", "tile/computerCluster/controller/BiologyComputeControllerT4.java", NORMAL),
    ("quantum_compute_t1", "tile/computerCluster/controller/QuantumComputeControllerT1.java", QUANTUM),
    ("quantum_compute_t2", "tile/computerCluster/controller/QuantumComputeControllerT2.java", QUANTUM),
    ("quantum_compute_t3", "tile/computerCluster/controller/QuantumComputeControllerT3.java", QUANTUM),
    ("quantum_compute_t4", "tile/computerCluster/controller/QuantumComputeControllerT4.java", QUANTUM),
    ("quantum_compute_center", "tile/computerCluster/controller/QuantumComputeCenterController.java", QUANTUM),
    ("spacetime_compute_t1", "tile/computerCluster/controller/SpacetimeComputeControllerT1.java", QUANTUM),
    ("spacetime_compute_t2", "tile/computerCluster/controller/SpacetimeComputeControllerT2.java", QUANTUM),
    ("wireless_battery_inductive", "tile/multiblock/energy/storage/WirelessBatteryInductive.java", ""),
    ("wireless_battery_quantum", "tile/multiblock/energy/storage/WirelessBatteryQuantum.java", ""),
    ("fusion_tokamak_exp", "tile/multiblock/machine/FusionReactorTokamakExp.java", NORMAL),
    ("fusion_tokamak_t1", "tile/multiblock/machine/FusionReactorTokamakT1.java", NORMAL),
    ("laser_fusion_experimental", "tile/multiblock/machine/laser/LaserFusionExperimental.java", ""),
    ("laser_fusion_industrial", "tile/multiblock/machine/laser/LaserFusionIndustrial.java", ""),
    ("catalytic_reactor_ev", "tile/multiblock/machine/CatalyticReactor.java", ""),
    ("catalytic_reactor_iv", "tile/multiblock/machine/CatalyticReactor.java", ""),
    ("quantum_gold_chamber", "tile/multiblock/machine/QuantumGoldSynthesisChamber.java", ""),
    ("quantum_observation_tower", "tile/multiblock/machine/QuantumObservationTower.java", ""),
    ("microgravity_crystal_growth", "tile/multiblock/machine/MicrogravityCrystalGrowthMachine.java", ""),
    ("centrifugal_microgravity", "tile/multiblock/machine/CentrifugalMicrogravityController.java", ""),
    ("ai_flow_control", "tile/multiblock/ai/AiFlowControlCenter.java", ""),
    ("ai_fill_planning", "tile/multiblock/ai/AiFillPlanningCenter.java", ""),
    ("ai_pattern_recognition", "tile/multiblock/ai/AiPatternRecognitionCenter.java", ""),
]


def slug(name):
    text = name.replace("T§be§r", "Exp").replace("§", "")
    text = text.lower().replace("nq-522", "naquadria")
    text = re.sub(r"[^a-z0-9]+", "_", text).strip("_")
    if len(text) > 60:
        text = text[:60].strip("_")
    if not text or not text[0].isalpha():
        text = "part_" + text
    return text


def registry():
    found = {}
    mat = "Steel"
    hardness = 6.0
    init = (SRC / "tile" / "tileEntityInit0.java").read_text(encoding="utf-8")
    for line in init.splitlines():
        stripped = line.strip()
        if stripped.startswith("//"):
            continue
        mat_match = re.search(r"aMat\s*=\s*MT\.(\w+)", line)
        if mat_match:
            mat = mat_match.group(1)
        hard_match = re.search(r"NBT_HARDNESS\s*,\s*([0-9.]+)F", line)
        if hard_match:
            hardness = float(hard_match.group(1))
        add = re.search(r'kRegistry0\.add\(\s*"([^"]+)"\s*,\s*"[^"]+"\s*,\s*(\d+)', line)
        if not add:
            continue
        found[int(add.group(2))] = (add.group(1), MATERIALS.get(mat, mat), hardness)
    return found


def existing_names():
    names = set(KNOWN.values())
    for path in (ROOT / "src" / "main" / "java").rglob("*.java"):
        if path.name == "Gt6KtfruGeneratedLayouts.java":
            continue
        text = path.read_text(encoding="utf-8")
        names.update(re.findall(r'(?:casing|part|builder)\(\s*"([a-z0-9_]+)"', text))
    return names


def read_strings(body, index):
    rows = []
    depth = 1
    i = index
    while i < len(body) and depth:
        char = body[i]
        if char == '"':
            end = body.index('"', i + 1)
            rows.append(body[i + 1:end])
            i = end + 1
            continue
        if char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
        i += 1
    return rows, i


def layers_of(text):
    rule = re.search(r'layerRule\(\s*"([^"]+)"\s*\)', text)
    if not rule:
        raise SystemExit("no layerRule")
    region = text[rule.start():]
    offset = re.search(r"setOffset\(\s*(-?\d+)\s*,\s*(-?\d+)\s*,\s*(-?\d+)\s*\)", region)
    if not offset:
        raise SystemExit("no offset")
    body = region[:offset.end()]
    found = {}
    i = 0
    while True:
        fixed = re.search(r"fixedLayer\(\s*'([^'])'\s*,", body[i:])
        expandable = re.search(
            r"\.layer\(\s*'([^'])'\s*,\s*new ExpandableLayer\(\s*\d+\s*\)\.variation\(\s*",
            body[i:])
        if not fixed and not expandable:
            break
        use_fixed = fixed and (not expandable or fixed.start() < expandable.start())
        match = fixed if use_fixed else expandable
        rows, nxt = read_strings(body, i + match.end())
        found[match.group(1)] = rows
        i = nxt
    sequence = []
    for symbol in rule.group(1):
        if symbol not in found:
            raise SystemExit(f"layer {symbol} missing")
        sequence.append(found[symbol])
    return (int(offset.group(1)), int(offset.group(2)), int(offset.group(3))), sequence


def wheres(text):
    found = []
    index = 0
    while True:
        at = text.find(".where(", index)
        if at < 0:
            break
        match = re.match(r"\.where\(\s*'([^'])'\s*,\s*new\s+(\w+)\(", text[at:])
        if not match:
            index = at + 6
            continue
        start = at + match.end()
        depth = 1
        i = start
        while i < len(text) and depth:
            if text[i] == "(":
                depth += 1
            elif text[i] == ")":
                depth -= 1
            i += 1
        found.append((match.group(1), match.group(2), text[start:i - 1]))
        index = i
    return found


def role_of(args):
    flags = (
        ("ONLY_ITEM_FLUID_ENERGY_IN", "ITEM_FLUID_ENERGY_INPUT"),
        ("ONLY_ITEM_FLUID_ENERGY", "ITEM_FLUID_ENERGY"),
        ("ONLY_ITEM_FLUID_OUT", "ITEM_FLUID_OUTPUT"),
        ("ONLY_ITEM_FLUID_IN", "ITEM_FLUID_INPUT"),
        ("ONLY_ITEM_FLUID", "ITEM_FLUID_IO"),
        ("ONLY_ITEM_OUT", "ITEM_IO"),
        ("ONLY_ITEM_IN", "ITEM_IO"),
        ("ONLY_FLUID_OUT", "FLUID_OUTPUT"),
        ("ONLY_FLUID_IN", "FLUID_INPUT"),
        ("ONLY_ENERGY_OUT", "ENERGY_OUTPUT"),
        ("ONLY_ENERGY", "ENERGY_INPUT"),
    )
    for flag, role in flags:
        if flag in args:
            return role
    return "CASING"


def field_values(text):
    found = dict(CONST)
    for match in re.finditer(r"(\w+)\s*=\s*(\d+)", text):
        found[match.group(1)] = int(match.group(2))
    return found


def split_args(text):
    parts = []
    depth = 0
    start = 0
    for index, char in enumerate(text):
        if char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
        elif char == "," and depth == 0:
            parts.append(text[start:index].strip())
            start = index + 1
    parts.append(text[start:].strip())
    return [part for part in parts if part]


def part_id(args, fields):
    tile = re.search(r"TileDesc\((.*)\)\s*$", args, re.S)
    if not tile:
        if re.search(r"\bWALL\b", args):
            return fields["WALL"]
        if re.search(r"\bGLASS\b", args):
            return fields["GLASS"]
        return None
    meta = split_args(tile.group(1))[1]
    if meta.isdigit():
        return int(meta)
    name = meta.split(".")[-1]
    if name not in fields:
        raise SystemExit(f"unknown tile constant {name} in {args}")
    return fields[name]


def cell(kind, args, compute, names, fields):
    if kind == "ControllerPredicate":
        return "controller"
    if kind == "AirPredicate":
        return "air"
    if kind == "ComputePartPredicate":
        if not compute:
            raise SystemExit("compute cell without a node block")
        if args.strip():
            fallback = part_id(args, fields)
            if fallback is None:
                raise SystemExit(f"no fallback part in {args}")
            return ("oneof", [("block", compute, "CASING"),
                              ("part", names[fallback], "CASING")], "CASING")
        return ("block", compute, "CASING")
    if kind == "BlockPredicate":
        if "glass" not in args.lower():
            raise SystemExit(f"unknown block {args}")
        return ("block", "minecraft:glass", "CASING")
    if kind != "PartPredicate":
        raise SystemExit(f"unknown predicate {kind}")
    if "Blocks.glass" in args or "Blocks.glass" in args:
        return ("block", "minecraft:glass", "CASING")
    ident = part_id(args, fields)
    if ident is None:
        raise SystemExit(f"no part id in {args}")
    return ("part", names[ident], role_of(args))


def java_string(text):
    return '"' + text.replace("\\", "\\\\").replace('"', '\\"') + '"'


def main():
    registered = registry()
    taken = existing_names()
    names = dict(KNOWN)
    assigned = set(taken)
    for ident, (title, material, hardness) in sorted(registered.items()):
        if ident in names:
            continue
        base = slug(title)
        candidate = base
        if candidate in assigned:
            candidate = f"{base}_{ident}"
        assigned.add(candidate)
        names[ident] = candidate
    parsed = []
    used_ids = set()
    for layout, path, compute in JOBS:
        text = (SRC / path).read_text(encoding="utf-8")
        offset, layers = layers_of(text)
        fields = field_values(text)
        specs = []
        seen = set()
        for mark, kind, args in wheres(text):
            if mark in seen:
                continue
            seen.add(mark)
            spec = cell(kind, args, compute, names, fields)
            specs.append((mark, spec))
            if isinstance(spec, tuple) and spec[0] == "part":
                used_ids.add(next(i for i, n in names.items() if n == spec[1]))
            elif isinstance(spec, tuple) and spec[0] == "oneof":
                for member, value, _ in spec[1]:
                    if member == "part":
                        used_ids.add(next(i for i, n in names.items() if n == value))
        chars = set()
        for layer in layers:
            for row in layer:
                chars.update(ch for ch in row if ch != " ")
        missing = chars - seen
        if missing:
            raise SystemExit(f"{layout} missing where for {missing}")
        origin = "absent"
        ox, oy, oz = offset
        for layer_index, layer in enumerate(layers):
            for row_index, row in enumerate(layer):
                for col, mark in enumerate(row):
                    if -ox - row_index == 0 and oy + layer_index == 0 and oz + col == 0:
                        origin = repr(mark)
        occupied = set()
        count = 0
        for layer_index, layer in enumerate(layers):
            for row_index, row in enumerate(layer):
                for col, mark in enumerate(row):
                    if mark == " ":
                        continue
                    coord = (-ox - row_index, oy + layer_index, oz + col)
                    if coord == (0, 0, 0):
                        continue
                    spec = dict(specs)[mark]
                    if spec == "controller":
                        continue
                    if coord in occupied:
                        raise SystemExit(f"{layout} duplicate cell {coord}")
                    occupied.add(coord)
                    count += 1
        if count == 0:
            raise SystemExit(f"{layout} is empty")
        print(f"{layout}: origin={origin!r} cells={count} offset={offset}")
        parsed.append((layout, path, offset, layers, specs))
    parts = {}
    for ident in sorted(used_ids):
        if ident in SHARED:
            continue
        title, material, hardness = registered[ident]
        parts[names[ident]] = (material, hardness, title)
    lines = []
    a = lines.append
    a("package com.lombinaxmasson.gt6ktfruaddon.generated;")
    a("")
    a("import java.util.List;")
    a("")
    a("import com.gregtech.gregtech.api.addon.CellMatch;")
    a("import com.gregtech.gregtech.api.addon.LayoutPart;")
    a("import com.gregtech.gregtech.api.addon.StructureLayout;")
    a("import com.gregtech.gregtech.api.addon.StructureRole;")
    a("import com.lombinaxmasson.gt6ktfruaddon.core.layout.Gt6KtfruLayerLayout;")
    a("")
    a("/**")
    a(" * Layouts transcribed from this repo's kTFRUAddon checkout.")
    a(" * An expandable layer is included once.")
    a(" * A compute cell accepts its node block or the source's wall fallback.")
    a(" */")
    a("public final class Gt6KtfruGeneratedLayouts {")
    a("    private Gt6KtfruGeneratedLayouts() {}")
    a("")
    a("    public static void addLayouts(List<StructureLayout> layouts) {")
    for layout, _, _, _, _ in parsed:
        a(f"        layouts.add({layout}());")
    a("    }")
    a("")
    a("    public static void addParts(List<LayoutPart> parts) {")
    for name in sorted(parts):
        material, hardness, _ = parts[name]
        a(f'        parts.add(LayoutPart.builder("{name}", "{material}").hardness({hardness:.1f}F).build());')
    a("    }")
    a("")
    for layout, path, offset, layers, specs in parsed:
        ox, oy, oz = offset
        a(f"    /** {path} offset {ox},{oy},{oz}. */")
        a(f"    public static StructureLayout {layout}() {{")
        a(f'        return Gt6KtfruLayerLayout.build("{layout}", {ox}, {oy}, {oz}, new String[][] {{')
        for layer in layers:
            rows = ", ".join(java_string(row) for row in layer)
            a(f"            new String[] {{{rows}}},")
        a("        },")
        rendered = []
        for mark, spec in specs:
            if spec == "controller":
                rendered.append(f"                Gt6KtfruLayerLayout.controller('{mark}')")
            elif spec == "air":
                rendered.append(f"                Gt6KtfruLayerLayout.air('{mark}')")
            else:
                kind, ident, role = spec
                if kind == "part":
                    rendered.append(f"                Gt6KtfruLayerLayout.part('{mark}', \"{ident}\", StructureRole.{role})")
                elif kind == "oneof":
                    members = ", ".join(
                        f'CellMatch.part("{value}")' if member == "part" else f'CellMatch.block("{value}")'
                        for member, value, _ in ident
                    )
                    rendered.append(f"                Gt6KtfruLayerLayout.oneOf('{mark}', StructureRole.{role}, {members})")
                else:
                    rendered.append(f"                Gt6KtfruLayerLayout.block('{mark}', \"{ident}\", StructureRole.{role})")
        a(",\n".join(rendered))
        a("        );")
        a("    }")
        a("")
    a("}")
    a("")
    OUT.write_text("\n".join(lines), encoding="utf-8")
    print(f"wrote {OUT} parts={len(parts)}")
    (ROOT / "tools" / "card20-parts.txt").write_text(
        "\n".join(f"{name}\t{title}" for name, (_, _, title) in sorted(parts.items())),
        encoding="utf-8")


if __name__ == "__main__":
    paths.require_inputs(upstream=True)
    main()
