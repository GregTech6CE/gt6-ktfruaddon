"""Classify kTFRUAddon 6404ab69 rows for the ten single-block recipe maps.

Writes Gt6KtfruSourceRecipes.java for rows whose inputs and outputs resolve
through AddonItems and AddonFluids. Prints the skip ledger used by card 06.
"""

from __future__ import annotations

import ast
import re
from collections import Counter, defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "kTFRUAddon" / "src" / "main" / "java" / "cn" / "kuzuanpa" / "ktfruaddon" / "recipe" / "recipe"
GT = Path(r"D:\Baobao\projects\dev\java\GregTech6CommunityEdition\GregTech6")
OUT_JAVA = ROOT / "src" / "main" / "java" / "com" / "lombinaxmasson" / "gt6ktfruaddon" / "Gt6KtfruSourceRecipes.java"
REPORT = ROOT / "build" / "source-recipe-report.txt"

MAPS = (
    "FluidHeating",
    "LightMixer",
    "Assembler",
    "LaserCutter",
    "HeatMixer",
    "EDA",
    "FlawDetector",
    "WaferTester",
    "WaferCoater",
    "UltrasonicMixer",
)
MAP_FIELD = {
    "FluidHeating": "FLUID_HEATING",
    "LightMixer": "LIGHT_MIXER",
    "Assembler": "CIRCUIT_ASSEMBLER",
    "LaserCutter": "LASER_CUTTER",
    "HeatMixer": "HEAT_MIXER",
    "EDA": "ELECTRONICS_DESIGNER",
    "FlawDetector": "FLAW_DETECTOR",
    "WaferTester": "WAFER_TESTER",
    "WaferCoater": "WAFER_COATER",
    "UltrasonicMixer": "ULTRASONIC_MIXER",
}
# input items, output items, min items, input fluids, output fluids, min fluids, min inputs
SLOTS = {
    "FluidHeating": (0, 0, 0, 1, 1, 1, 1),
    "LightMixer": (6, 3, 0, 6, 6, 0, 1),
    "Assembler": (9, 3, 1, 1, 0, 0, 1),
    "LaserCutter": (2, 3, 1, 1, 0, 0, 1),
    "HeatMixer": (6, 3, 0, 6, 3, 0, 1),
    "EDA": (6, 3, 1, 3, 0, 0, 1),
    "FlawDetector": (3, 3, 1, 0, 0, 0, 1),
    "WaferTester": (3, 3, 0, 0, 0, 0, 1),
    "WaferCoater": (3, 3, 0, 3, 0, 0, 1),
    "UltrasonicMixer": (6, 3, 0, 6, 2, 0, 1),
}
FILES = (
    "Chemistry.java",
    "Plastic.java",
    "Circuits.java",
    "CompactItem.java",
    "ComputerBuilding.java",
    "Fusion.java",
    "QuantumEntry.java",
    "ResearchRecipes.java",
    "RocketBuilding.java",
    "HeatMixer.java",
    "OilProcessing.java",
    "OreProcessing.java",
    "PlatinumGroupProcess.java",
    "PurityRefining.java",
)
DATA_TIERS = {
    "Electric_T": [
        "TinAlloy", "SteelGalvanized", "Aluminium", "StainlessSteel", "Chromium",
        "Titanium", "Iridium", "OsmiumElemental", "Trinitanium", "Trinaquadalloy",
    ],
    "Heat_T": [None, "Steel", "Invar", "Titanium", "TungstenCarbide"],
    "Kinetic_T": [None, "Bronze", "Steel", "Titanium", "Tungstensteel"],
}
# gregapi CS.U. Fluid amounts are UT.Code.units(raw, U, mBPerUnit, roundUp).
U = 648_648_000
CORE_PREFIX_FLAGS = {
    "dust": ("DUSTS",), "dustSmall": ("DUSTS",), "dustTiny": ("DUSTS",), "dustDiv72": ("DUSTS",),
    "plate": ("PLATES", "MULTIPLATES"), "plateTiny": ("PLATES",), "plateCurved": ("PLATES",),
    "plateDouble": ("MULTIPLATES", "PLATES"), "plateTriple": ("MULTIPLATES", "PLATES"),
    "plateQuadruple": ("MULTIPLATES", "PLATES"), "plateQuintuple": ("MULTIPLATES", "PLATES"),
    "plateDense": ("DENSEPLATES",), "foil": ("FOILS",),
    "stick": ("STICKS",), "stickLong": ("STICKS",),
    "ingot": ("INGOTS",), "ingotHot": ("INGOTS_HOT", "INGOTS"),
    "ingotDouble": ("MULTIINGOTS", "INGOTS"), "ingotTriple": ("MULTIINGOTS", "INGOTS"),
    "ingotQuadruple": ("MULTIINGOTS", "INGOTS"), "ingotQuintuple": ("MULTIINGOTS", "INGOTS"),
    "nugget": ("INGOTS",), "gem": ("GEMS",),
    "gearGt": ("PARTS",), "gearGtSmall": ("PARTS",), "ring": ("PARTS",), "rotor": ("PARTS",),
    "bolt": ("PARTS",), "screw": ("PARTS",), "spring": ("PARTS",), "springSmall": ("PARTS",),
    "itemCasing": ("PARTS",), "round": ("PARTS",),
}
METAL_PREFIXES = {
    "ingot", "ingotDouble", "ingotTriple", "ingotQuadruple", "ingotQuintuple", "ingotHot",
    "nugget", "plate", "plateTiny", "plateDouble", "plateTriple", "plateQuadruple",
    "plateQuintuple", "plateDense", "plateCurved", "foil", "stick", "stickLong",
    "bolt", "screw", "ring", "rotor", "gearGt", "gearGtSmall", "wireFine",
    "round", "spring", "springSmall", "itemCasing",
}
MC_ITEMS = {
    "paper": "Items.PAPER", "redstone": "Items.REDSTONE", "diamond": "Items.DIAMOND",
    "emerald": "Items.EMERALD", "coal": "Items.COAL", "charcoal": "Items.CHARCOAL",
    "flint": "Items.FLINT", "clay_ball": "Items.CLAY_BALL", "brick": "Items.BRICK",
    "stick": "Items.STICK", "string": "Items.STRING", "leather": "Items.LEATHER",
    "slime_ball": "Items.SLIME_BALL", "ender_pearl": "Items.ENDER_PEARL",
    "blaze_powder": "Items.BLAZE_POWDER", "blaze_rod": "Items.BLAZE_ROD",
    "ghast_tear": "Items.GHAST_TEAR", "nether_star": "Items.NETHER_STAR",
    "quartz": "Items.QUARTZ", "gunpowder": "Items.GUNPOWDER", "sugar": "Items.SUGAR",
    "wheat": "Items.WHEAT", "bone": "Items.BONE", "iron_ingot": "Items.IRON_INGOT",
    "gold_ingot": "Items.GOLD_INGOT", "glowstone_dust": "Items.GLOWSTONE_DUST",
    "ender_eye": "Items.ENDER_EYE", "nether_wart": "Items.NETHER_WART",
    "glass_bottle": "Items.GLASS_BOTTLE", "bucket": "Items.BUCKET", "bowl": "Items.BOWL",
    "feather": "Items.FEATHER", "egg": "Items.EGG", "snowball": "Items.SNOWBALL",
    "apple": "Items.APPLE", "bread": "Items.BREAD", "reeds": "Items.SUGAR_CANE",
    "iron_nugget": "Items.IRON_NUGGET", "gold_nugget": "Items.GOLD_NUGGET",
}
MC_BLOCKS = {
    "glass": "Blocks.GLASS", "sand": "Blocks.SAND", "obsidian": "Blocks.OBSIDIAN",
    "stone": "Blocks.STONE", "cobblestone": "Blocks.COBBLESTONE", "dirt": "Blocks.DIRT",
    "gravel": "Blocks.GRAVEL", "clay": "Blocks.CLAY", "brick_block": "Blocks.BRICKS",
    "redstone_block": "Blocks.REDSTONE_BLOCK", "iron_block": "Blocks.IRON_BLOCK",
    "gold_block": "Blocks.GOLD_BLOCK", "diamond_block": "Blocks.DIAMOND_BLOCK",
    "emerald_block": "Blocks.EMERALD_BLOCK", "coal_block": "Blocks.COAL_BLOCK",
    "lapis_block": "Blocks.LAPIS_BLOCK", "netherrack": "Blocks.NETHERRACK",
    "soul_sand": "Blocks.SOUL_SAND", "end_stone": "Blocks.END_STONE",
    "bookshelf": "Blocks.BOOKSHELF", "chest": "Blocks.CHEST", "tnt": "Blocks.TNT",
    "torch": "Blocks.TORCH", "planks": "Blocks.OAK_PLANKS", "log": "Blocks.OAK_LOG",
    "glass_pane": "Blocks.GLASS_PANE", "hardened_clay": "Blocks.TERRACOTTA",
    "crafting_table": "Blocks.CRAFTING_TABLE", "furnace": "Blocks.FURNACE",
    "glowstone": "Blocks.GLOWSTONE", "ice": "Blocks.ICE", "snow": "Blocks.SNOW_BLOCK",
    "anvil": "Blocks.ANVIL", "piston": "Blocks.PISTON", "sticky_piston": "Blocks.STICKY_PISTON",
    "hopper": "Blocks.HOPPER", "noteblock": "Blocks.NOTE_BLOCK", "jukebox": "Blocks.JUKEBOX",
    "enchanting_table": "Blocks.ENCHANTING_TABLE",
    "mossy_cobblestone": "Blocks.MOSSY_COBBLESTONE", "stonebrick": "Blocks.STONE_BRICKS",
    "nether_brick": "Blocks.NETHER_BRICKS",
}
CALL_RE = re.compile(
    r"(?<![A-Za-z0-9_])(?:recipeMaps\s*\.\s*)?"
    r"(FluidHeating|LightMixer|Assembler|LaserCutter|HeatMixer|EDA|FlawDetector|WaferTester|WaferCoater|UltrasonicMixer)"
    r"\s*\.\s*addRecipe([012X])\s*\("
)


class Skip(Exception):
    def __init__(self, reason: str):
        super().__init__(reason)
        self.reason = reason


def strip_comments(text: str) -> str:
    out = []
    i = 0
    n = len(text)
    while i < n:
        if text.startswith("//", i):
            while i < n and text[i] != "\n":
                out.append(" ")
                i += 1
        elif text.startswith("/*", i):
            out.append("  ")
            i += 2
            while i < n and not text.startswith("*/", i):
                out.append("\n" if text[i] == "\n" else " ")
                i += 1
            if i < n:
                out.append("  ")
                i += 2
        elif text[i] in "\"'":
            quote = text[i]
            out.append(quote)
            i += 1
            while i < n:
                out.append(text[i])
                if text[i] == "\\":
                    i += 1
                    if i < n:
                        out.append(text[i])
                    i += 1
                    continue
                if text[i] == quote:
                    i += 1
                    break
                i += 1
        else:
            out.append(text[i])
            i += 1
    return "".join(out)


def split_args(text: str) -> list[str]:
    args = []
    depth = 0
    start = 0
    i = 0
    while i < len(text):
        char = text[i]
        if char in "\"'":
            quote = char
            i += 1
            while i < len(text):
                if text[i] == "\\":
                    i += 2
                    continue
                if text[i] == quote:
                    i += 1
                    break
                i += 1
            continue
        if char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
        elif char == "," and depth == 0:
            args.append(text[start:i].strip())
            start = i + 1
        i += 1
    tail = text[start:].strip()
    if tail:
        args.append(tail)
    return args


def unwrap(text: str) -> str:
    open_at = text.index("(")
    if not text.endswith(")"):
        raise Skip(f"括号不配对 {text[:80]}")
    return text[open_at + 1:-1]


def eval_raw(expr: str) -> int:
    """Java long arithmetic. ``U`` is one material unit; ``U10`` is ``U/10``; a bare ``10`` is ten raw units."""
    expr = expr.strip().replace("CS.", "")
    expr = re.sub(r"\bU(\d+)\b", lambda match: str(U // int(match.group(1))), expr)
    expr = re.sub(r"\bU\b", str(U), expr)
    if not re.fullmatch(r"[0-9+\-*/().\s]+", expr):
        raise Skip(f"数量表达式 {expr}")
    value = _eval_long(ast.parse(expr, mode="eval").body)
    if value <= 0:
        raise Skip("催化剂数量 0")
    return value


def _eval_long(node):
    if isinstance(node, ast.Constant) and isinstance(node.value, int):
        return node.value
    if isinstance(node, ast.UnaryOp) and isinstance(node.op, ast.USub):
        return -_eval_long(node.operand)
    if isinstance(node, ast.BinOp) and isinstance(node.op, (ast.Add, ast.Sub, ast.Mult, ast.Div)):
        left = _eval_long(node.left)
        right = _eval_long(node.right)
        if isinstance(node.op, ast.Add):
            return left + right
        if isinstance(node.op, ast.Sub):
            return left - right
        if isinstance(node.op, ast.Mult):
            return left * right
        if right == 0:
            raise Skip("数量表达式")
        return int(left / right)
    raise Skip("数量表达式")


def eval_int(expr: str) -> int:
    value = eval_raw(expr)
    if value > 2_000_000_000:
        raise Skip(f"数量过大 {expr}")
    return value


def units_mb(raw: int, per_unit: int, round_up: bool) -> int:
    """gregapi ``UT.Code.units(raw, U, perUnit, roundUp)`` with ``U`` divisible by ``perUnit``."""
    divisor = U // per_unit
    amount, remainder = divmod(raw, divisor)
    if round_up and remainder:
        amount += 1
    if amount <= 0:
        raise Skip("流体不足 1 mB")
    if amount > 2_000_000_000:
        raise Skip("流体数量过大")
    return amount


def round_up(parts: list[str]) -> bool:
    if len(parts) < 2:
        return False
    token = parts[1].strip().replace("CS.", "")
    if token in ("T", "true"):
        return True
    if token in ("F", "false"):
        return False
    raise Skip(f"流体舍入 {token}")


def dust_from_raw(raw: int) -> tuple[str, int]:
    """gregapi ``OM.dust``. A block of dust is not an addon item, so that size is left unresolved."""
    if raw < U // 72:
        raise Skip("尘量不足")
    if raw >= U * 72:
        raise Skip("块粉")
    if raw >= U and (raw >= U * 16 or raw % U == 0):
        count = raw // U
        prefix = "dust"
    elif raw >= U // 4 and (raw >= U * 8 or raw % (U // 4) <= raw % (U // 9)):
        count = (raw * 4) // U
        prefix = "dustSmall"
    elif raw >= U // 9 and (raw >= U or raw % (U // 9) <= raw % (U // 72)):
        count = (raw * 9) // U
        prefix = "dustTiny"
    else:
        count = (raw * 72) // U
        prefix = "dustDiv72"
    if count <= 0:
        raise Skip("尘量不足")
    if count > 64:
        raise Skip("堆叠超过 64")
    return prefix, count


def sanitize(name: str) -> str:
    return name.replace(" ", "").replace("-", "").replace("'", "")


def load_materials():
    names = {}
    kinds = {}
    ids = {}
    generated = GT / "core" / "src" / "main" / "java" / "com" / "gregtech" / "gregtech" / "content" / "material" / "generated"
    factory = re.compile(
        r"public static final GTMaterial\s+(\w+)\s*=\s*(\w+)\(\s*(\d+)\s*,\s*\"([^\"]+)\""
    )
    for path in generated.glob("*.java"):
        for match in factory.finditer(path.read_text(encoding="utf-8")):
            field, kind, material_id, raw_name = match.groups()
            registry = sanitize(raw_name)
            names[field] = registry
            kinds[registry] = kind
            ids[registry] = material_id
    symbols = dict(names)
    gt6 = (GT / "core" / "src" / "main" / "java" / "com" / "gregtech" / "gregtech" / "data" / "generated" / "GT6Materials.java").read_text(encoding="utf-8")
    for match in re.finditer(
        r"public static final GTMaterial\s+(\w+)\s*=\s*[\w.]+\.(\w+)\s*;", gt6
    ):
        symbol, field = match.groups()
        if field in names:
            symbols[symbol] = names[field]
    forms = {}
    forms_text = (GT / "core" / "src" / "main" / "java" / "com" / "gregtech" / "gregtech" / "data" / "generated" / "MaterialForms.java").read_text(encoding="utf-8")
    for match in re.finditer(r"\"(\d+)\|([A-Z0-9_,]+)\"", forms_text):
        forms[match.group(1)] = set(match.group(2).split(","))
    props = {}
    for registry, kind in kinds.items():
        flags = forms.get(ids.get(registry, ""), set())
        props[registry] = {
            "gas": kind == "gas" or "GASES" in flags,
            "liquid": kind == "liquid" or "LIQUID" in flags,
            "molten": kind == "metal" or "MOLTEN" in flags or "METAL" in flags,
            "flags": flags,
        }
    return symbols, props


def load_fluids():
    text = (GT / "core" / "src" / "main" / "java" / "com" / "gregtech" / "gregtech" / "data" / "FluidCatalog.java").read_text(encoding="utf-8")
    fields = {}
    by_registry = {}
    for match in re.finditer(r"fluid\(\s*\"([^\"]+)\"\s*,\s*\"([^\"]+)\"", text):
        field, registry = match.groups()
        fields[field] = registry
        by_registry.setdefault(registry.lower(), field)
    return fields, by_registry


def load_prefixes():
    text = (GT / "core" / "src" / "main" / "java" / "com" / "gregtech" / "gregtech" / "data" / "MaterialPrefixes.java").read_text(encoding="utf-8")
    items = set(re.findall(r'register\("([A-Za-z0-9_]+)"\)', text))
    blocks = set(re.findall(r'registerBlock\(block\("([A-Za-z0-9_]+)"\)', text))
    return items, blocks


def load_addon():
    text = (ROOT / "src" / "main" / "java" / "com" / "lombinaxmasson" / "gt6ktfruaddon" / "Gt6KtfruMaterials.java").read_text(encoding="utf-8")
    addon = {}
    for match in re.finditer(r'builder\("([^"]+)"\)(.*?)\.build\(\)', text):
        name, body = match.groups()
        flags = set()
        for flag in ("dust", "gas", "liquid", "metal"):
            if re.search(rf"\.{flag}\(\)", body):
                flags.add(flag)
        addon[name] = flags
    return addon


def fluid_field(material: str, kind: str, fluids: dict, by_registry: dict) -> str:
    dedicated = by_registry.get(material.lower())
    if dedicated and not dedicated.startswith("GenMolten_") and kind != "molten":
        return dedicated
    if material in fluids and kind != "molten":
        return material
    if kind == "gas":
        return "GenGas_" + material
    if kind == "molten":
        return "GenMolten_" + material
    return "GenLiquid_" + material


class World:
    def __init__(self):
        self.symbols, self.props = load_materials()
        self.fluids, self.by_registry = load_fluids()
        self.item_prefixes, self.block_prefixes = load_prefixes()
        self.addon = load_addon()

    def material_name(self, expr: str) -> str:
        expr = expr.strip().replace("CS.", "")
        tier = re.fullmatch(r"MT\.DATA\.(\w+)\[(\d+)\]", expr)
        if tier:
            table, index = tier.group(1), int(tier.group(2))
            if table not in DATA_TIERS or index >= len(DATA_TIERS[table]) or DATA_TIERS[table][index] is None:
                raise Skip(f"材料档 {expr}")
            return DATA_TIERS[table][index]
        nested = re.fullmatch(r"MT\.(?:OREMATS|STONES|WOODS)\.(\w+)", expr)
        if nested:
            symbol = nested.group(1)
            if symbol not in self.symbols:
                raise Skip(f"未知材料 {expr}")
            return self.symbols[symbol]
        plain = re.fullmatch(r"MT\.(\w+)", expr)
        if plain:
            symbol = plain.group(1)
            if symbol not in self.symbols:
                raise Skip(f"未知材料 {expr}")
            return self.symbols[symbol]
        listed = re.fullmatch(r"matList\.(\w+)\.(?:mat|get\(\))", expr)
        if listed:
            name = listed.group(1)
            if name not in self.addon:
                raise Skip(f"未移植材料 {name}")
            return name
        raise Skip(f"材料 {expr}")

    def form_allowed(self, prefix: str, material: str) -> None:
        if prefix in self.block_prefixes or prefix not in self.item_prefixes:
            raise Skip(f"前缀没有物品 {prefix}")
        if material not in self.addon:
            flags = self.props.get(material, {}).get("flags", set())
            if not flags:
                return
            needed = CORE_PREFIX_FLAGS.get(prefix)
            if prefix.startswith("wire") or prefix.startswith("cable"):
                needed = ("WIRES",)
            elif prefix.startswith("pipe"):
                needed = ("PIPES",)
            if needed and not any(flag in flags for flag in needed):
                raise Skip(f"材料没有{prefix} {material}")
            return
        flags = self.addon[material]
        if prefix in ("dust", "dustSmall", "dustTiny", "dustDiv72"):
            if "dust" in flags or "metal" in flags:
                return
            raise Skip(f"材料没有粉 {material}")
        if prefix in METAL_PREFIXES or prefix.startswith("wire") or prefix.startswith("cable") or prefix.startswith("pipe"):
            if "metal" in flags:
                return
            raise Skip(f"材料没有{prefix} {material}")
        raise Skip(f"形态 {prefix} {material}")

    def item_form(self, prefix: str, material_expr: str, count: int):
        if count <= 0:
            raise Skip("催化剂数量 0")
        if count > 99:
            raise Skip(f"堆叠超过 99 {prefix}")
        material = self.material_name(material_expr)
        self.form_allowed(prefix, material)
        return ("item", prefix, material, count)

    def vanilla(self, expr: str, count: int, meta: str | None):
        if count <= 0:
            raise Skip("催化剂数量 0")
        if count > 99:
            raise Skip("堆叠超过 99")
        if meta is not None and eval_int(meta) != 0:
            raise Skip(f"原版元数据 {expr}")
        expr = expr.strip()
        if expr.startswith("Items."):
            mapped = MC_ITEMS.get(expr.split(".", 1)[1])
            if not mapped:
                raise Skip(f"原版物品 {expr}")
            return ("vanilla", mapped, count)
        if expr.startswith("Blocks."):
            mapped = MC_BLOCKS.get(expr.split(".", 1)[1])
            if not mapped:
                raise Skip(f"原版方块 {expr}")
            return ("vanilla", mapped, count)
        raise Skip(f"原版物品 {expr}")

    def mc_named(self, name: str, count: int, meta: str | None):
        if meta is not None and eval_int(meta) != 0:
            raise Skip(f"原版元数据 {name}")
        if name in MC_ITEMS and MC_ITEMS[name]:
            return ("vanilla", MC_ITEMS[name], count)
        if name in MC_BLOCKS and MC_BLOCKS[name]:
            return ("vanilla", MC_BLOCKS[name], count)
        raise Skip(f"原版物品 {name}")

    def fluid_state(self, material: str, method: str) -> tuple[str, int]:
        props = self.props.get(material, {"gas": False, "liquid": False, "molten": False})
        addon = self.addon.get(material, set())
        gas = props["gas"] or "gas" in addon
        liquid = props["liquid"] or "liquid" in addon
        molten = props["molten"] or "metal" in addon
        if method == "gas":
            if not gas:
                raise Skip(f"材料没有气体 {material}")
            return "gas", 1000
        # GT keeps one chemical fluid for the gas and liquid states, both at 1000 mB
        # per unit. Molten metal is the 144 mB fluid, and only when the material has
        # no chemical liquid to take the liquid() call.
        if method == "liquid":
            if molten and not liquid:
                return "molten", 144
            if liquid:
                return "liquid", 1000
            if gas:
                return "gas", 1000
            raise Skip(f"材料没有流体 {material}")
        if method == "fluid":
            if gas and not liquid and not molten:
                return "gas", 1000
            if liquid:
                return "liquid", 1000
            if molten and not gas:
                return "molten", 144
            if gas:
                return "gas", 1000
            raise Skip(f"材料没有流体 {material}")

    def fluid_amount_mb(self, material: str, method: str, raw: int, round_up_flag: bool) -> tuple[str, int]:
        kind, per_unit = self.fluid_state(material, method)
        return fluid_field(material, kind, self.fluids, self.by_registry), units_mb(raw, per_unit, round_up_flag)

    def parse_item(self, expr: str):
        expr = expr.strip().replace("CS.", "")
        if expr in ("ZL_IS", "NI"):
            return None
        if expr.startswith("ST.tag(") or expr.startswith("tag("):
            raise Skip("编程电路")
        if expr.startswith("ItemList.") or ".ItemList." in expr:
            name = re.search(r"ItemList\.(\w+)", expr)
            raise Skip("附属物品 " + (name.group(1) if name else expr))
        if re.match(r"IL\.", expr) or ".IL." in expr:
            name = re.search(r"IL\.(\w+)", expr)
            raise Skip("GT元件 " + (name.group(1) if name else expr))
        if "prefixList." in expr:
            raise Skip("自有前缀")
        if "getItem(" in expr or "GTTileEntityRegistry" in expr:
            raise Skip("机器物品")
        if expr.startswith("ANY.") or "ANY." in expr:
            raise Skip("矿辞通配 " + expr)
        if expr.startswith("MD.") or "MD." in expr:
            if "MD.AE" in expr:
                raise Skip("D档 AE")
            if any(token in expr for token in ("MD.GC", "MD.BOT", "TFC", "Botania")):
                raise Skip("C档")
            raise Skip("其他模组物品 " + expr.split(",")[0])
        made = re.fullmatch(r"ST\.make\((.*)\)", expr)
        if made:
            parts = split_args(made.group(1))
            if parts and parts[0].strip() in ("MD.MC", "MD.MC"):
                name = parts[1].strip().strip('"')
                meta = parts[3] if len(parts) > 3 else None
                return self.mc_named(name, eval_int(parts[2]), meta)
            if len(parts) >= 2:
                meta = parts[2] if len(parts) > 2 else None
                return self.vanilla(parts[0], eval_int(parts[1]), meta)
        prefixed = re.fullmatch(r"OP\.(\w+)\.mat\((.*)\)", expr)
        if prefixed:
            prefix, inner = prefixed.groups()
            parts = split_args(inner)
            if len(parts) < 2:
                raise Skip(f"前缀参数 {expr}")
            return self.item_form(prefix, parts[0], eval_int(parts[1]))
        dusted = re.fullmatch(r"matList\.(\w+)\.get(Dust|DustTiny|DustSmall|DustDiv72)\((.*)\)", expr)
        if dusted:
            material, kind, count = dusted.groups()
            prefix = {"Dust": "dust", "DustTiny": "dustTiny", "DustSmall": "dustSmall", "DustDiv72": "dustDiv72"}[kind]
            return self.item_form(prefix, f"matList.{material}.mat", eval_int(count))
        om = re.fullmatch(r"OM\.dust\((.*)\)", expr)
        if om:
            parts = split_args(om.group(1))
            prefix, count = dust_from_raw(eval_raw(parts[1]))
            return self.item_form(prefix, parts[0], count)
        raise Skip(f"物品 {expr[:120]}")

    def parse_fluid(self, expr: str):
        expr = expr.strip().replace("CS.", "")
        if expr in ("ZL_FS", "NF"):
            return None
        listed = re.fullmatch(r"flList\.(\w+)\.make\((.*)\)", expr)
        if listed:
            name, inner = listed.groups()
            amount = split_args(inner)[0]
            if "U" in amount:
                raise Skip(f"流体数量 {expr}")
            mb = eval_int(amount)
            if name in self.addon:
                flags = self.addon[name]
                if "liquid" in flags:
                    field = "GenLiquid_" + name
                elif "gas" in flags:
                    field = "GenGas_" + name
                else:
                    raise Skip(f"材料没有流体 {name}")
            elif name in self.fluids:
                field = name
            else:
                raise Skip(f"流体 {name}")
            return ("fluid", field, mb)
        named = re.fullmatch(r"FL\.(\w+)\.make\((.*)\)", expr)
        if named:
            field, inner = named.groups()
            amount = split_args(inner)[0]
            if "U" in amount:
                raise Skip(f"流体数量 {expr}")
            if field not in self.fluids and not field.startswith("Gen"):
                raise Skip(f"流体 {field}")
            return ("fluid", field, eval_int(amount))
        made = re.fullmatch(r"FL\.make\(\s*\"([^\"]+)\"\s*,(.*)\)", expr)
        if made:
            field, amount = made.groups()
            if field not in self.fluids:
                raise Skip(f"流体 {field}")
            return ("fluid", field, eval_int(amount))
        material = re.fullmatch(
            r"(MT\.(?:(?:OREMATS|STONES|WOODS)\.\w+|\w+))\.(gas|liquid|fluid)\((.*)\)", expr
        )
        if material:
            material_expr, method, inner = material.groups()
            name = self.material_name(material_expr)
            parts = split_args(inner)
            field, mb = self.fluid_amount_mb(name, method, eval_raw(parts[0]), round_up(parts))
            return ("fluid", field, mb)
        raise Skip(f"流体 {expr[:120]}")

    def parse_many(self, kind: str, exprs: list[str]):
        values = []
        for expr in exprs:
            parsed = self.parse_item(expr) if kind == "items" else self.parse_fluid(expr)
            if parsed is not None:
                values.append(parsed)
        return values


def classify_arg(expr: str):
    expr = expr.strip()
    bare = expr.replace("CS.", "")
    if bare in ("T", "F", "true", "false"):
        return ("bool", bare in ("T", "true"))
    if bare in ("ZL_IS", "NI"):
        return ("items", [])
    if bare in ("ZL_FS", "NF"):
        return ("fluids", [])
    if bare.startswith("FL.array("):
        return ("fluids", split_args(unwrap(bare)))
    if bare.startswith("ST.array("):
        return ("items", split_args(unwrap(bare)))
    if bare.startswith("new long[") or bare.startswith("long["):
        return ("chances", bare)
    if re.fullmatch(r"[0-9+\-*/().\s]+", bare):
        return ("num", eval_int(bare))
    if any(token in bare for token in (".gas(", ".liquid(", ".fluid(", ".make(", "FL.", "flList.")):
        return ("fluids", [bare])
    return ("items", [bare])


def optimize(duration, items_in, items_out, fluids_in, fluids_out):
    ins = [list(item) for item in items_in]
    outs = [list(item) for item in items_out]
    fins = [list(fluid) for fluid in fluids_in]
    fouts = [list(fluid) for fluid in fluids_out]
    limit = max(1, duration // 16)
    for incoming in ins:
        for outgoing in outs:
            if incoming[1:3] == outgoing[1:3] and incoming[3] and outgoing[3]:
                if incoming[3] >= outgoing[3]:
                    incoming[3] -= outgoing[3]
                    outgoing[3] = 0
                else:
                    outgoing[3] -= incoming[3]
                    incoming[3] = 0
    counts = [item[3] for item in ins + outs if item[3]] + [fluid[2] for fluid in fins + fouts if fluid[2]]
    for count in counts:
        limit = min(limit, count)
    for divisor in range(int(limit), 1, -1):
        if all(count % divisor == 0 for count in counts):
            duration //= divisor
            for item in ins + outs:
                item[3] //= divisor
            for fluid in fins + fouts:
                fluid[2] //= divisor
            break
    ins = [tuple(item) for item in ins if item[3] > 0]
    outs = [tuple(item) for item in outs if item[3] > 0]
    fins = [tuple(fluid) for fluid in fins if fluid[2] > 0]
    fouts = [tuple(fluid) for fluid in fouts if fluid[2] > 0]
    if not ins and not fins:
        raise Skip("优化后没有输入")
    if not outs and not fouts:
        raise Skip("优化后没有输出")
    return duration, ins, outs, fins, fouts


def reserved(text: str) -> str | None:
    checks = (
        ("LiClO3", "保留缺口 液态氯酸锂"),
        ("MoltenNaK", "保留缺口 熔融钠钾"),
        ("HotMoltenNaK", "保留缺口 熔融钠钾"),
        ("Hot_Molten_", "保留缺口 热熔融金属"),
        ("MT.D2O", "保留缺口 重水"),
        ("MT.HDO", "保留缺口 重水"),
        ("MT.T2O", "保留缺口 重水"),
        ("Hot_Heavy_Water", "保留缺口 重水"),
        ("Hot_Semi_Heavy_Water", "保留缺口 重水"),
        ("Hot_Tritiated_Water", "保留缺口 重水"),
    )
    for token, reason in checks:
        if token in text:
            return reason
    return None


def c_tier(text: str) -> bool:
    return any(token in text for token in (
        "MD.GC", "AdvancedRocketry", "zmaster587", "libVulpes", "LibVulpes",
        "Botania", "MD.BOT", "terrafirmacraft", "MD.TFC", "TFCRecipe",
    ))


def parse_call(world: World, file_name: str, line: int, map_name: str, arity: str, args: list[str], raw: str):
    if map_name == "LightMixer":
        raise Skip("保持卡 02 的光混合行")
    held = reserved(raw)
    if held:
        raise Skip(held)
    if c_tier(raw):
        raise Skip("C档")
    if "MD.AE" in raw:
        raise Skip("D档 AE")
    classified = [classify_arg(arg) for arg in args]
    if len(classified) < 3 or classified[0][0] != "bool":
        raise Skip("参数形状")
    optimize_flag = classified[0][1]
    index = 1
    if classified[1][0] == "bool":
        index = 6
    if classified[index][0] != "num" or classified[index + 1][0] != "num":
        raise Skip("电压或时间不是常数")
    eu = classified[index][1]
    duration = classified[index + 1][1]
    rest = classified[index + 2:]
    if rest and rest[0][0] == "num":
        chance = rest[0][1]
        if chance not in (0, 10000):
            raise Skip("概率输出")
        rest = rest[1:]
    if rest and rest[0][0] == "chances":
        if not re.fullmatch(r"new long\[\]\{\s*(?:0|10000)\s*(?:,\s*(?:0|10000)\s*)*\}", rest[0][1].replace(" ", "")):
            raise Skip("概率输出")
        rest = rest[1:]
    needed = {"0": 0, "1": 1, "2": 2, "X": 1}[arity]
    if arity == "X":
        if not rest or rest[0][0] != "items":
            raise Skip("组装输入不是数组")
        item_exprs = rest[0][1]
        rest = rest[1:]
    else:
        item_exprs = []
        for _ in range(needed):
            if not rest or rest[0][0] != "items":
                raise Skip("物品参数")
            item_exprs.extend(rest[0][1])
            rest = rest[1:]
    items_in = world.parse_many("items", item_exprs)
    if rest and rest[0][0] == "items":
        fluids_in, fluids_out = [], []
        item_out_exprs = []
        for kind, value in rest:
            if kind != "items":
                raise Skip("流体和物品穿插")
            item_out_exprs.extend(value)
    elif not rest:
        fluids_in, fluids_out, item_out_exprs = [], [], []
    else:
        if rest[0][0] != "fluids":
            raise Skip("流体参数")
        fluids_in = world.parse_many("fluids", rest[0][1])
        rest = rest[1:]
        if any(kind == "items" for kind, _ in rest):
            if not rest or rest[0][0] != "fluids":
                raise Skip("流体输出")
            fluids_out = world.parse_many("fluids", rest[0][1])
            item_out_exprs = []
            for kind, value in rest[1:]:
                if kind != "items":
                    raise Skip("输出物品")
                item_out_exprs.extend(value)
        else:
            fluids_out = []
            for kind, value in rest:
                if kind != "fluids":
                    raise Skip("流体输出")
                fluids_out.extend(world.parse_many("fluids", value))
            item_out_exprs = []
    items_out = world.parse_many("items", item_out_exprs)
    if optimize_flag:
        duration, items_in, items_out, fluids_in, fluids_out = optimize(
            duration, items_in, items_out, fluids_in, fluids_out
        )
    slots = SLOTS[map_name]
    if len(items_in) > slots[0] or len(fluids_in) > slots[3]:
        raise Skip("输入超过配方图槽位")
    if len(items_in) < slots[2] or len(fluids_in) < slots[5] or len(items_in) + len(fluids_in) < slots[6]:
        raise Skip("输入少于配方图下限")
    if len(items_out) > slots[1] or len(fluids_out) > slots[4]:
        raise Skip("输出超过配方图槽位")
    if not items_out and not fluids_out:
        raise Skip("没有输出")
    for stack in items_in + items_out:
        if stack[0] == "item" and stack[3] > 99:
            raise Skip("堆叠超过 99")
        if stack[0] == "vanilla" and stack[2] > 99:
            raise Skip("堆叠超过 99")
    return {
        "file": file_name,
        "line": line,
        "map": map_name,
        "eu": eu,
        "duration": max(1, duration),
        "items_in": items_in,
        "items_out": items_out,
        "fluids_in": fluids_in,
        "fluids_out": fluids_out,
        "d_tier": "Coolant_IC2" in raw,
    }


def matching_paren(text: str, open_at: int) -> int:
    depth = 0
    i = open_at
    while i < len(text):
        if text[i] in "\"'":
            quote = text[i]
            i += 1
            while i < len(text):
                if text[i] == "\\":
                    i += 2
                    continue
                if text[i] == quote:
                    break
                i += 1
        elif text[i] == "(":
            depth += 1
        elif text[i] == ")":
            depth -= 1
            if depth == 0:
                return i
        i += 1
    raise ValueError("unclosed call")


def line_of(text: str, index: int) -> int:
    return text.count("\n", 0, index) + 1


def java_stack(stack) -> str:
    if stack[0] == "item":
        return f'form("{stack[1]}", "{stack[2]}", {stack[3]})'
    if stack[0] == "vanilla":
        return f"vanilla({stack[1]}, {stack[2]})"
    if stack[0] == "fluid":
        return f'field("{stack[1]}", {stack[2]})'
    raise AssertionError(stack)


def java_array(kind: str, stacks) -> str:
    if not stacks:
        return "NO_ITEMS" if kind == "item" else "NO_FLUIDS"
    rendered = ", ".join(java_stack(stack) for stack in stacks)
    return f"new ItemStack[] {{{rendered}}}" if kind == "item" else f"new FluidStack[] {{{rendered}}}"


def emit(rows: list[dict]) -> str:
    by_map = defaultdict(list)
    for row in rows:
        by_map[row["map"]].append(row)
    methods = []
    calls = []
    for map_name in MAPS:
        group = by_map.get(map_name, [])
        if not group:
            continue
        method = map_name[0].lower() + map_name[1:]
        calls.append(f"        {method}();")
        body = [f"    private static void {method}() {{"]
        for row in group:
            note = " D-tier Coolant_IC2." if row["d_tier"] else ""
            body.append(f"        // {row['file']}:{row['line']}{note}")
            body.append(
                "        add(\"{file}:{line}\", Gt6KtfruMachines.{field}, {eu}L, {duration}L,\n"
                "                {items_in},\n"
                "                {items_out},\n"
                "                {fluids_in},\n"
                "                {fluids_out});".format(
                    file=row["file"],
                    line=row["line"],
                    field=MAP_FIELD[map_name],
                    eu=row["eu"],
                    duration=row["duration"],
                    items_in=java_array("item", row["items_in"]),
                    items_out=java_array("item", row["items_out"]),
                    fluids_in=java_array("fluid", row["fluids_in"]),
                    fluids_out=java_array("fluid", row["fluids_out"]),
                )
            )
        body.append("    }")
        methods.append("\n".join(body))
    return """package com.lombinaxmasson.gt6ktfruaddon;

import java.util.LinkedHashSet;
import java.util.Set;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Source rows from kTFRUAddon {{@code 6404ab69}} whose inputs and outputs resolve
 * to items and fluids already in this build. Amounts follow gregapi
 * {{@code UT.Code.units}}: one material unit of gas or chemical liquid is 1000 mB,
 * one unit of molten metal is 144 mB, and {{@code FL.make}} stays millibuckets.
 * A leftover that does not fill one millibucket rounds up only when the source
 * call asks for it. Rows with the source optimize flag are divided the way
 * GregTech divides them. The light mixer row stays in {{@link Gt6KtfruRecipes}}.
 */
public final class Gt6KtfruSourceRecipes {{
    private static final ItemStack[] NO_ITEMS = new ItemStack[0];
    private static final FluidStack[] NO_FLUIDS = new FluidStack[0];
    private static final Set<String> PROBLEMS = new LinkedHashSet<>();

    private Gt6KtfruSourceRecipes() {{}}

    public static void register() {{
        PROBLEMS.clear();
{calls}
        if (!PROBLEMS.isEmpty()) {{
            throw new IllegalStateException(PROBLEMS.size() + " source stacks did not resolve: "
                    + String.join("; ", PROBLEMS));
        }}
    }}

{methods}

    private static void add(String where, String map, long euPerTick, long duration,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut) {{
        if (broken(itemsIn) || broken(fluidsIn) || broken(itemsOut) || broken(fluidsOut)) {{
            return;
        }}
        if (AddonRecipes.map(map).add(new AddonRecipes.Line(
                itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, 0)) == null) {{
            PROBLEMS.add(where + " was rejected");
        }}
    }}

    private static boolean broken(ItemStack[] stacks) {{
        for (ItemStack stack : stacks) {{
            if (stack == null || stack.isEmpty()) return true;
        }}
        return false;
    }}

    private static boolean broken(FluidStack[] stacks) {{
        for (FluidStack stack : stacks) {{
            if (stack == null || stack.isEmpty()) return true;
        }}
        return false;
    }}

    private static ItemStack form(String prefix, String material, int count) {{
        ItemStack stack = AddonItems.stack(prefix, material, count);
        if (stack == null || stack.isEmpty()) {{
            PROBLEMS.add(prefix + " " + material);
            return ItemStack.EMPTY;
        }}
        return stack;
    }}

    private static ItemStack vanilla(ItemLike item, int count) {{
        return new ItemStack(item, count);
    }}

    private static FluidStack field(String name, int amount) {{
        FluidStack stack = AddonFluids.still(name, amount);
        if (stack == null || stack.isEmpty()) {{
            PROBLEMS.add("fluid " + name);
            return FluidStack.EMPTY;
        }}
        return stack;
    }}
}}
""".format(calls="\n".join(calls), methods="\n\n".join(methods))


def recipe_text(row: dict) -> str:
    def show(stack):
        if stack[0] == "fluid":
            return f"{stack[1]} {stack[2]}mB"
        if stack[0] == "item":
            return f"{stack[3]} {stack[1]} {stack[2]}"
        return f"{stack[2]} {stack[1]}"

    left = ", ".join(show(stack) for stack in row["items_in"] + row["fluids_in"])
    right = ", ".join(show(stack) for stack in row["items_out"] + row["fluids_out"])
    return f"{row['file']}:{row['line']} {row['map']} {row['eu']} EU {row['duration']}t {left} -> {right}"


def main():
    world = World()
    rows = []
    skips = []
    counts = Counter()
    for file_name in FILES:
        original = (SOURCE / file_name).read_text(encoding="utf-8")
        text = strip_comments(original)
        for match in CALL_RE.finditer(text):
            map_name, arity = match.group(1), match.group(2)
            counts[map_name] += 1
            open_at = match.end() - 1
            close_at = matching_paren(text, open_at)
            raw = text[match.start():close_at + 1]
            line = line_of(text, match.start())
            try:
                rows.append(parse_call(world, file_name, line, map_name, arity, split_args(text[open_at + 1:close_at]), raw))
            except Skip as skip:
                skips.append((map_name, file_name, line, skip.reason, raw.replace("\n", " ")[:180]))
    expected = {
        "FluidHeating": 11, "LightMixer": 1, "Assembler": 121, "LaserCutter": 38,
        "HeatMixer": 104, "EDA": 24, "FlawDetector": 0, "WaferTester": 24,
        "WaferCoater": 4, "UltrasonicMixer": 3,
    }
    lines = ["call sites"]
    for map_name in MAPS:
        lines.append(f"  {map_name}: {counts[map_name]} (card {expected[map_name]})")
    lines.append(f"resolved {len(rows)}")
    for map_name in MAPS:
        lines.append(f"  {map_name}: {sum(1 for row in rows if row['map'] == map_name)}")
    lines.append("resolved rows")
    for row in rows:
        lines.append("  " + recipe_text(row))
    reason_counts = Counter(item[3] for item in skips)
    lines.append("skips")
    for reason, count in reason_counts.most_common():
        lines.append(f"  {count} {reason}")
    grouped = Counter((item[0], item[3].split()[0]) for item in skips)
    lines.append("skip groups")
    for (map_name, bucket), count in sorted(grouped.items()):
        lines.append(f"  {map_name} {bucket} {count}")
    REPORT.parent.mkdir(parents=True, exist_ok=True)
    REPORT.write_text("\n".join(lines), encoding="utf-8")
    print("\n".join(lines[:80]))
    print(f"... report {REPORT}")
    sulfur = [row for row in rows if row["file"] == "HeatMixer.java" and row["line"] == 28]
    if len(sulfur) != 1:
        raise SystemExit("sulfur row missing")
    row = sulfur[0]
    if row["items_in"] != [("item", "dust", "Sulfur", 1)] or row["fluids_in"] != [("fluid", "Hydrogen", 2000)]:
        raise SystemExit(f"sulfur inputs {row}")
    if row["fluids_out"] != [("fluid", "GenGas_HydrosulfuricAcid", 3000)] or row["duration"] != 48 or row["eu"] != 16:
        raise SystemExit(f"sulfur outputs {row}")
    water = [row for row in rows if row["file"] == "HeatMixer.java" and row["line"] == 30]
    if not water or water[0]["fluids_out"] != [("fluid", "Water", 2000)] or water[0]["items_out"] != [("item", "dust", "Sulfur", 1)]:
        raise SystemExit(f"water row {water}")
    graphene = [row for row in rows if row["file"] == "Chemistry.java" and any(stack[2] == "Graphene" for stack in row["items_in"] if stack[0] == "item")]
    if not graphene:
        raise SystemExit("graphene row missing")
    OUT_JAVA.write_text(emit(rows), encoding="utf-8")
    print(f"wrote {OUT_JAVA}")


if __name__ == "__main__":
    main()
