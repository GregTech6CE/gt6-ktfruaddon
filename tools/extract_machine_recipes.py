"""Classify kTFRUAddon 6404ab69 rows for the ten single-block recipe maps.

Writes Gt6KtfruSourceRecipes.java for rows whose inputs and outputs resolve
through AddonItems and AddonFluids. Prints the skip ledger used by card 06.
"""

from __future__ import annotations

import ast
import json
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
    "TinyDistillTower",
    "SmallDistillTower",
    "DistillTower",
    "MaskAligner",
    "CNC",
    "FusionTokamak",
    "FusionTokamakExperimental",
    "LaserFusion",
    "LaserFusionExperimental",
    "CatalyticReactor",
    "MirrorGoldSynthesis",
    "MirrorGoldQuantumization",
    "CosmicRadiationResearch",
    "MicrogravityCrystalGrowth",
    "QuantumGoldSynthesis",
    "QuantumObservation",
    "QuantumCommunication",
    "ResearchAssembler",
    "ResearchReactor",
)
MAP_FIELD = {
    "FluidHeating": "Gt6KtfruMachines.FLUID_HEATING",
    "LightMixer": "Gt6KtfruMachines.LIGHT_MIXER",
    "Assembler": "Gt6KtfruMachines.CIRCUIT_ASSEMBLER",
    "LaserCutter": "Gt6KtfruMachines.LASER_CUTTER",
    "HeatMixer": "Gt6KtfruMachines.HEAT_MIXER",
    "EDA": "Gt6KtfruMachines.ELECTRONICS_DESIGNER",
    "FlawDetector": "Gt6KtfruMachines.FLAW_DETECTOR",
    "WaferTester": "Gt6KtfruMachines.WAFER_TESTER",
    "WaferCoater": "Gt6KtfruMachines.WAFER_COATER",
    "UltrasonicMixer": "Gt6KtfruMachines.ULTRASONIC_MIXER",
    "TinyDistillTower": "Gt6KtfruMachines.TINY_DISTILL",
    "SmallDistillTower": "Gt6KtfruMachines.SMALL_DISTILL",
    "DistillTower": "Gt6KtfruMachines.DISTILL",
    "MaskAligner": "Gt6KtfruMachines.MASK_ALIGNER",
    "CNC": "Gt6KtfruMachines.CNC",
    "FusionTokamak": "Gt6KtfruMachines.FUSION_TOKAMAK",
    "FusionTokamakExperimental": "Gt6KtfruMachines.FUSION_TOKAMAK",
    "LaserFusion": "Gt6KtfruLateMachines.LASER",
    "LaserFusionExperimental": "Gt6KtfruLateMachines.LASER",
    "CatalyticReactor": "Gt6KtfruLateMachines.CATALYTIC",
    "MirrorGoldSynthesis": "Gt6KtfruLateMachines.MIRROR",
    "MirrorGoldQuantumization": "Gt6KtfruLateMachines.MIRROR_QUANTUM",
    "CosmicRadiationResearch": "Gt6KtfruLateMachines.COSMIC",
    "MicrogravityCrystalGrowth": "Gt6KtfruLateMachines.CRYSTAL",
    "QuantumGoldSynthesis": "Gt6KtfruLateMachines.QUANTUM_GOLD",
    "QuantumObservation": "Gt6KtfruLateMachines.OBSERVATION",
    "QuantumCommunication": "Gt6KtfruLateMachines.RELAY_MAP",
    "ResearchAssembler": "Gt6KtfruResearchMachines.ASSEMBLER_MAP",
    "ResearchReactor": "Gt6KtfruResearchMachines.REACTOR_MAP",
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
    "TinyDistillTower": (1, 3, 0, 1, 3, 0, 1),
    "SmallDistillTower": (1, 3, 0, 1, 6, 0, 1),
    "DistillTower": (1, 3, 0, 1, 9, 0, 1),
    "MaskAligner": (3, 3, 1, 3, 0, 0, 1),
    "CNC": (4, 3, 1, 3, 0, 1, 1),
    "FusionTokamak": (3, 3, 0, 6, 6, 1, 1),
    "FusionTokamakExperimental": (3, 3, 0, 6, 6, 1, 1),
    "LaserFusion": (2, 1, 0, 2, 2, 0, 1),
    "LaserFusionExperimental": (2, 1, 0, 2, 2, 0, 1),
    "CatalyticReactor": (6, 3, 0, 6, 3, 0, 1),
    "MirrorGoldSynthesis": (5, 5, 0, 2, 2, 0, 1),
    "MirrorGoldQuantumization": (5, 5, 0, 2, 2, 0, 1),
    "CosmicRadiationResearch": (1, 3, 0, 0, 0, 0, 1),
    "MicrogravityCrystalGrowth": (1, 1, 0, 2, 0, 0, 1),
    "QuantumGoldSynthesis": (4, 4, 0, 2, 2, 0, 1),
    "QuantumObservation": (4, 4, 0, 2, 2, 0, 1),
    "QuantumCommunication": (1, 1, 0, 0, 0, 0, 1),
    "ResearchAssembler": (9, 6, 0, 3, 3, 0, 1),
    "ResearchReactor": (9, 6, 0, 2, 1, 0, 1),
}
FILES = (
    "Chemistry.java",
    "Plastic.java",
    "Circuits.java",
    "CompactItem.java",
    "ComputerBuilding.java",
    "Fusion.java",
    "QuantumEntry.java",
    "QuantumObservation.java",
    "CatalyticChemistry.java",
    "ResearchRecipes.java",
    "RocketBuilding.java",
    "HeatMixer.java",
    "OilProcessing.java",
    "OreProcessing.java",
    "PlatinumGroupProcess.java",
    "PurityRefining.java",
    "listener/recipePrefixItems.java",
    "../../tile/tileEntityInit0.java",
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
# prefixList field to the registry name passed to PrefixDeclaration.
PREFIX_FIELDS = {
    "flywheel": "flywheel",
    "largeTurbineBlade": "bladeLargeTurbine",
    "turbineLargeGas": "turbineLargeGas",
    "turbineLargeGasChecked": "turbineLargeGasChecked",
    "turbineLargeGasDamaged": "turbineLargeGasDamaged",
    "turbineLargeSteam": "turbineLargeSteam",
    "turbineLargeSteamChecked": "turbineLargeSteamChecked",
    "turbineLargeSteamDamaged": "turbineLargeSteamDamaged",
    "CommercialPureDust": "dustCommercialPure",
    "AnalyticalPureDust": "dustAnalyticalPure",
    "AbsolutelyPureDust": "dustAbsolutelyPure",
}
OWN_PREFIXES = set(PREFIX_FIELDS.values())
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
    r"(" + "|".join(MAPS) + r")"
    r"\s*\.\s*addRecipe([012X])\s*\("
)

TILE_ITEMS = {
    10010: "gas_battery_aluminium",
    10011: "gas_battery_stainless",
    10200: "transformer_small_lv",
    10201: "transformer_small_mv",
    10202: "transformer_small_hv",
    10203: "transformer_small_ev",
    10204: "transformer_small_iv",
    10205: "transformer_small_luv",
    10210: "transformer_large_lv",
    10211: "transformer_large_mv",
    10212: "transformer_large_hv",
    10213: "transformer_large_ev",
    10214: "transformer_large_iv",
    10215: "transformer_large_luv",
    10220: "flywheel_gear_small_t1",
    10221: "flywheel_gear_small_t2",
    10222: "flywheel_gear_small_t3",
    10223: "flywheel_gear_small_t4",
    10224: "flywheel_gear_small_t5",
    10225: "flywheel_gear_small_t6",
    10230: "flywheel_gear_large_t1",
    10231: "flywheel_gear_large_t2",
    10232: "flywheel_gear_large_t3",
    10233: "flywheel_gear_large_t4",
    10234: "flywheel_gear_large_t5",
    10235: "flywheel_gear_large_t6",
    10240: "light_transformer_small_lv",
    10241: "light_transformer_small_mv",
    10242: "light_transformer_small_hv",
    10243: "light_transformer_small_ev",
    10244: "light_transformer_small_iv",
    10245: "light_transformer_small_luv",
    10250: "light_transformer_large_lv",
    10251: "light_transformer_large_mv",
    10252: "light_transformer_large_hv",
    10253: "light_transformer_large_ev",
    10254: "light_transformer_large_iv",
    10255: "light_transformer_large_luv",
    23000: "circuit_assembler_lv",
    23001: "circuit_assembler_mv",
    23002: "circuit_assembler_hv",
    23003: "circuit_assembler_ev",
    23004: "circuit_assembler_iv",
    30003: "mask_aligner_uv",
    30009: "mask_aligner_uv_plus",
    30010: "mask_aligner_duv",
    30011: "mask_aligner_euv",
    30012: "cnc_machine",
    30014: "fusion_tokamak_exp",
    30015: "fusion_tokamak_t1",
    30020: "large_steam_turbine_magnalium",
    30021: "large_steam_turbine_trinitanium",
    30022: "large_steam_turbine_graphene",
    30023: "large_steam_turbine_vibramantium",
    30024: "liquid_battery_stainless",
    30025: "liquid_battery_titanium",
    30026: "liquid_battery_tungstensteel",
    30058: "wireless_compute_controller",
    30059: "wireless_compute_controller_large",
    30060: "wireless_compute_controller_huge",
    30065: "quantum_compute_t1",
    30066: "quantum_compute_t2",
    30067: "quantum_compute_t3",
    30068: "quantum_compute_t4",
    30069: "spacetime_compute_t1",
    30070: "spacetime_compute_t2",
    30071: "laser_fusion_experimental",
    30072: "laser_fusion_industrial",
    30073: "mirror_gold_synthesis",
    30074: "quantum_communication_relay",
    30075: "mirror_gold_quantumization",
    30076: "quantum_gold_chamber",
    30077: "optical_microscope",
    30078: "electron_microscope",
    30079: "quantum_compute_center",
    30080: "quantum_observation_tower",
    30102: "centrifugal_microgravity",
    30103: "microgravity_crystal_growth",
    30104: "cosmic_radiation_chamber",
    30105: "microgravity_assembler",
    30106: "ai_flow_control",
    30107: "ai_fill_planning",
    30108: "ai_pattern_recognition",
    30114: "wireless_battery_inductive",
    30115: "wireless_battery_quantum",
    30120: "catalytic_reactor_ev",
    30121: "catalytic_reactor_iv",
    31000: "aluminium_wall",
    31002: "sunlight_absorber",
    31003: "heat_pipe",
    31004: "sun_heater_top_layer",
    31016: "tokamak_magnetic_conductor_exp",
    31017: "tokamak_magnetic_coil_exp",
    31018: "tokamak_magnetic_wall_exp",
    31019: "tokamak_plasma_pipe",
    31024: "tokamak_coolant_pump",
    31025: "tokamak_magnetic_conductor_t1",
    31026: "tokamak_magnetic_coil_t1",
    31027: "tokamak_magnetic_wall_t1",
    31028: "tokamak_neutron_heater",
    31029: "stainless_turbine_shell",
    31030: "titanium_turbine_shell",
    31031: "tungstensteel_turbine_shell",
    31032: "adamantium_turbine_shell",
    31037: "enhanced_frame",
    31038: "chemical_reactor_pipe",
    31039: "mantle_drill_rod",
    31040: "magnetic_conductor",
    31041: "battery_cabin",
    31042: "flywheel_cabin",
    31043: "transform_battery_gear_set",
    31044: "energy_crystal_block",
    31045: "tidal_blade",
    31048: "trinaquadalloy_wall",
    31049: "trinaquadalloy_turbine_shell",
    31056: "structure_glass",
    31057: "biology_culture_casing",
    31058: "biology_neural_scaffold",
    31059: "biology_cortex_matrix",
    31060: "laser_focusing_array",
    31061: "laser_target_injector",
    31062: "laser_pulse_storage",
    31063: "laser_product_collector",
    31064: "biology_synapse_router",
    31065: "quantum_coherence_casing",
    31066: "quantum_bit_lattice",
    31067: "quantum_cryogenic_manifold",
    31068: "quantum_readout_bus",
    31069: "quantum_phase_isolation_frame",
    31070: "stabilized_quantum_lattice_core",
    31071: "quantum_readout_spine",
    31072: "topological_constraint_frame",
    31073: "quantum_interweave_surface",
    31074: "entanglement_exchange_relay",
    31075: "quantum_interconnect_bridge",
    31076: "deep_coherence_containment_wall",
    31077: "quantum_interconnect_node",
    31078: "fault_tolerant_correction_plane",
    31079: "distributed_quantum_readout_relay",
    31080: "spacetime_constraint_frame",
    31081: "spacetime_phase_conduit",
    31082: "chronal_lattice",
    31083: "spacetime_simulation_core",
    31084: "phase_stabilizer_pylon",
    31085: "causality_horizon_frame",
    31086: "causality_ring_segment",
    31087: "zero_point_compute_core",
    31088: "gravitic_stabilizer_anchor",
    31089: "causality_observation_spire",
    31090: "chronal_coupling_relay",
    31091: "ai_compute_frame",
    31092: "ai_memory_bank",
    31093: "ai_routing_matrix",
    31094: "ai_packing_planner",
    31095: "ai_recognition_matrix",
    31111: "quantum_coherence_casing_iridium",
    31112: "quantum_stabilization_substrate",
    31113: "entanglement_channel_module",
    31114: "quantum_readout_coil",
    31115: "quantum_gold_drive_module",
    31116: "quantum_gold_lattice_stabilizer",
    31117: "wireless_receiver_inductive",
    31118: "wireless_receiver_quantum",
    31130: "biology_cortex_frame",
    31131: "biology_culture_vat",
    31132: "biology_perfusion_bus",
    31133: "biology_genomic_storage_array",
    31200: "catalytic_reactor_casing",
    31201: "catalytic_reactor_catalyst_bed",
    31202: "catalytic_reactor_energy_port",
    31203: "catalytic_reactor_io_port",
    31204: "high_shield_laminated_wall",
    31205: "counter_rotating_mass_ring",
    31206: "gyro_stabilization_anchor",
    31207: "sealed_access_hatch",
    31208: "room_energy_port",
    31500: "mask_aligner_energy_uv",
    31501: "mask_aligner_energy_uv_plus",
    31504: "electromagnet_crucible_screen",
    32005: "compute_cluster",
    32006: "compute_cluster_simple",
    32010: "wireless_compute_node_aluminium",
    32011: "wireless_compute_node_stainless",
    32012: "wireless_compute_node_chromium",
    32013: "wireless_compute_node_titanium",
    32040: "quantum_compute_node_naquadah",
    32041: "quantum_compute_node_naquadria",
    32042: "quantum_compute_node_enriched",
    32043: "quantum_compute_node_trinaquadalloy",
}


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
        if char in "([{":
            depth += 1
        elif char in ")]}":
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


def eval_item_count(expr: str) -> tuple[int, bool]:
    """A source count of zero is a catalyst. The addon stack stays at one and is kept."""
    try:
        return eval_int(expr), False
    except Skip as skip:
        if skip.reason == "催化剂数量 0":
            return 1, True
        raise


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
    for match in re.finditer(r'new MaterialFormRequest\(\s*"([^"]+)"\s*,\s*"([^"]+)"\s*\)', text):
        mat, form = match.groups()
        addon.setdefault(mat, set()).add(form)
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
        self.own_items = json.loads((ROOT / "tools" / "own_items.json").read_text(encoding="utf-8"))

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
            name = {"Ij": "intellite"}.get(listed.group(1), listed.group(1))
            if name not in self.addon:
                raise Skip(f"未移植材料 {name}")
            return name
        raise Skip(f"材料 {expr}")

    def form_allowed(self, prefix: str, material: str) -> None:
        if prefix in OWN_PREFIXES:
            return
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
        if prefix in flags:
            return
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

    def parse_item(self, expr: str, output: bool = False):
        expr = expr.strip().replace("CS.", "")
        if expr in ("ZL_IS", "NI"):
            return None
        tagged = re.fullmatch(r"(?:ST\s*\.\s*)?tag\(\s*(\d+)\s*\)", expr)
        if tagged or expr.startswith("ST.tag(") or expr.startswith("tag("):
            if not tagged:
                raise Skip("编程电路")
            index = int(tagged.group(1))
            if index < 0 or index > 24:
                raise Skip(f"编程电路 {index}")
            return ("circuit", index, 1), False
        if expr.startswith("ItemList.") or ".ItemList." in expr:
            found = re.search(r"ItemList\.\s*(\w+)", expr)
            name = found.group(1) if found else expr
            getter = re.search(r"\.get\(([^)]*)\)\s*$", expr)
            path = self.own_items.get(name)
            if getter is None or not path:
                raise Skip("附属物品 " + name)
            count, kept = self.item_amount(getter.group(1), output)
            return ("own", path, count), kept
        if re.match(r"IL\.", expr) or ".IL." in expr:
            name = re.search(r"IL\.(\w+)", expr)
            raise Skip("GT元件 " + (name.group(1) if name else expr))
        own_prefix = re.fullmatch(r"prefixList\.(\w+)\.mat\((.*)\)", expr)
        if own_prefix:
            field, inner = own_prefix.groups()
            registry = PREFIX_FIELDS.get(field)
            if registry is None:
                raise Skip("自有前缀 " + field)
            parts = split_args(inner)
            if len(parts) < 2:
                raise Skip(f"前缀参数 {expr}")
            if re.search(r"\bmaterial\b", parts[0]):
                raise Skip("纯度循环")
            count, kept = self.item_amount(parts[1], output)
            return self.item_form(registry, parts[0], count), kept
        if "prefixList." in expr:
            raise Skip("自有前缀")
        if "getItem(" in expr or "GTTileEntityRegistry" in expr:
            tile_match = re.fullmatch(
                r"(?:GTTileEntityRegistry\.ktfruaddon|kRegistry0|kRegistry|ktfru|registry)\.getItem\(\s*(\d+)(?:\s*,\s*([^)]+))?\s*\)",
                expr,
            )
            if tile_match:
                tile_id = int(tile_match.group(1))
                block_id = TILE_ITEMS.get(tile_id)
                if block_id:
                    count_expr = tile_match.group(2) or "1"
                    count, kept = self.item_amount(count_expr, output)
                    return ("block", block_id, count), kept
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
                count, kept = self.item_amount(parts[2], output)
                return self.mc_named(name, count, meta), kept
            if len(parts) >= 2:
                meta = parts[2] if len(parts) > 2 else None
                count, kept = self.item_amount(parts[1], output)
                return self.vanilla(parts[0], count, meta), kept
        prefixed = re.fullmatch(r"(?:OP\s*\.\s*)?(\w+)\s*\.\s*mat\((.*)\)", expr)
        if prefixed and (prefixed.group(1) in self.item_prefixes or prefixed.group(1) in self.block_prefixes):
            prefix, inner = prefixed.groups()
            parts = split_args(inner)
            if len(parts) < 2:
                raise Skip(f"前缀参数 {expr}")
            count, kept = self.item_amount(parts[1], output)
            return self.item_form(prefix, parts[0], count), kept
        dusted = re.fullmatch(r"matList\.(\w+)\.get(Dust|DustTiny|DustSmall|DustDiv72)\((.*)\)", expr)
        if dusted:
            material, kind, count_expr = dusted.groups()
            prefix = {"Dust": "dust", "DustTiny": "dustTiny", "DustSmall": "dustSmall", "DustDiv72": "dustDiv72"}[kind]
            count, kept = self.item_amount(count_expr, output)
            return self.item_form(prefix, f"matList.{material}.mat", count), kept
        om = re.fullmatch(r"OM\.dust\((.*)\)", expr)
        if om:
            parts = split_args(om.group(1))
            try:
                raw = eval_raw(parts[1])
            except Skip as skip:
                if skip.reason != "催化剂数量 0" or output:
                    raise
                raise Skip("催化剂数量 0")
            prefix, count = dust_from_raw(raw)
            return self.item_form(prefix, parts[0], count), False
        raise Skip(f"物品 {expr[:120]}")

    def item_amount(self, expr: str, output: bool) -> tuple[int, bool]:
        count, kept = eval_item_count(expr)
        if output and kept:
            raise Skip("催化剂数量 0")
        return count, kept

    def parse_fluid(self, expr: str):
        expr = expr.strip().replace("CS.", "")
        if expr in ("ZL_FS", "NF"):
            return None
        listed = re.fullmatch(r"flList\s*\.\s*(\w+)\s*\.\s*make\((.*)\)", expr)
        if listed:
            name, inner = listed.groups()
            name = {
                "HexaFluoroPropylene": "HPF",
                "Perfluorocyclobutane": "perfluorocyclobutane",
            }.get(name, name)
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
            elif name in self.props and self.props[name]["gas"]:
                field = "GenGas_" + name
            elif name in self.props and self.props[name]["liquid"]:
                field = "GenLiquid_" + name
            elif name in self.props and self.props[name]["molten"]:
                field = "GenMolten_" + name
            else:
                raise Skip(f"流体 {name}")
            return ("fluid", field, mb)
        named = re.fullmatch(r"FL\s*\.\s*(\w+)\s*\.\s*make\((.*)\)", expr)
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

    def parse_items(self, exprs: list[str], output: bool = False):
        stacks = []
        kept = []
        for expr in exprs:
            parsed = self.parse_item(expr, output)
            if parsed is None:
                continue
            stack, keep = parsed
            stacks.append(stack)
            kept.append(keep)
        return stacks, kept

    def parse_many(self, kind: str, exprs: list[str]):
        if kind == "items":
            stacks, _kept = self.parse_items(exprs)
            return stacks
        values = []
        for expr in exprs:
            parsed = self.parse_fluid(expr)
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


def item_count(stack) -> int:
    return stack[3] if stack[0] == "item" else stack[2]


def set_item_count(stack, value: int) -> None:
    if stack[0] == "item":
        stack[3] = value
    else:
        stack[2] = value


def same_item(left, right) -> bool:
    if left[0] != right[0]:
        return False
    if left[0] == "item":
        return left[1:3] == right[1:3]
    return left[1] == right[1]


def optimize(duration, items_in, kept_in, items_out, kept_out, fluids_in, fluids_out):
    ins = [list(item) for item in items_in]
    outs = [list(item) for item in items_out]
    kept_in = list(kept_in)
    kept_out = list(kept_out)
    fins = [list(fluid) for fluid in fluids_in]
    fouts = [list(fluid) for fluid in fluids_out]
    limit = max(1, duration // 16)
    for index, incoming in enumerate(ins):
        if kept_in[index]:
            continue
        for other, outgoing in enumerate(outs):
            if kept_out[other] or not same_item(incoming, outgoing):
                continue
            left = item_count(incoming)
            right = item_count(outgoing)
            if not left or not right:
                continue
            if left >= right:
                set_item_count(incoming, left - right)
                set_item_count(outgoing, 0)
            else:
                set_item_count(outgoing, right - left)
                set_item_count(incoming, 0)
    counts = [item_count(item) for item, kept in zip(ins + outs, kept_in + kept_out) if item_count(item) and not kept]
    counts += [fluid[2] for fluid in fins + fouts if fluid[2]]
    for count in counts:
        limit = min(limit, count)
    if counts:
        for divisor in range(int(limit), 1, -1):
            if all(count % divisor == 0 for count in counts):
                duration //= divisor
                for item, kept in zip(ins + outs, kept_in + kept_out):
                    if not kept:
                        set_item_count(item, item_count(item) // divisor)
                for fluid in fins + fouts:
                    fluid[2] //= divisor
                break
    kept_ins = []
    kept_outs = []
    next_in = []
    next_out = []
    for item, kept in zip(ins, kept_in):
        if item_count(item) > 0:
            next_in.append(tuple(item))
            kept_ins.append(kept)
    for item, kept in zip(outs, kept_out):
        if item_count(item) > 0:
            next_out.append(tuple(item))
            kept_outs.append(kept)
    fins = [tuple(fluid) for fluid in fins if fluid[2] > 0]
    fouts = [tuple(fluid) for fluid in fouts if fluid[2] > 0]
    if not next_in and not fins:
        raise Skip("优化后没有输入")
    if not next_out and not fouts:
        raise Skip("优化后没有输出")
    return duration, next_in, kept_ins, next_out, kept_outs, fins, fouts


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


def parse_chance_list(token: str) -> list[int]:
    compact = re.sub(r"\s+", "", token)
    match = re.fullmatch(r"(?:new)?long\[\]\{([0-9,]*)\}", compact)
    if not match:
        raise Skip("概率输出")
    body = match.group(1)
    values = [int(part) for part in body.split(",") if part] if body else []
    if any(value < 0 or value > 10000 for value in values):
        raise Skip("概率输出")
    return values


def fit_chances(raw: list[int] | None, outputs: int):
    """gregapi uses 0 and 10000 for a certain output. The addon chance array rejects 0."""
    if raw is None:
        return None
    fitted = []
    for index in range(outputs):
        value = raw[index] if index < len(raw) else 10000
        if value <= 0 or value >= 10000:
            value = 10000
        fitted.append(value)
    if all(value == 10000 for value in fitted):
        return None
    return fitted


def purity_row(world: World, line: int, material: str, prefix_in: str, prefix_out: str, reagent: str, eu: int, duration: int):
    fluid_in = world.parse_fluid(reagent)
    fluid_out = world.parse_fluid("FL.Water.make(1000)")
    return {
        "file": "PurityRefining.java",
        "line": line,
        "map": "HeatMixer",
        "eu": eu,
        "duration": duration,
        "special": 0,
        "items_in": [world.item_form(prefix_in, material, 4)],
        "items_out": [world.item_form(prefix_out, material, 3)],
        "fluids_in": [fluid_in],
        "fluids_out": [fluid_out],
        "chances": None,
        "kept": None,
        "d_tier": False,
    }


def purity_rows(world: World, file_name: str, line: int, raw: str):
    """PurityRefining and recipePrefixItems keep the material in a variable."""
    compact = re.sub(r"\s+", "", raw)
    if file_name.endswith("recipePrefixItems.java"):
        turbine_mats = (
            "MT.Magnalium", "MT.Trinitanium", "MT.Graphene", "MT.Vibramantium", "MT.Trinaquadalloy",
            "MT.Steel", "MT.StainlessSteel", "MT.Ti", "MT.TungstenSteel", "MT.Ad", "MT.Ir", "MT.Os",
        )
        if "turbineLargeSteam.mat(" in compact and "FlawDetector" in compact:
            return [
                {
                    "file": "recipePrefixItems.java",
                    "line": line,
                    "map": "FlawDetector",
                    "eu": 16,
                    "duration": 12000,
                    "special": 0,
                    "items_in": [world.item_form("turbineLargeSteam", mat, 1)],
                    "items_out": [
                        world.item_form("turbineLargeSteamChecked", mat, 1),
                        world.item_form("turbineLargeSteamDamaged", mat, 1),
                    ],
                    "fluids_in": [],
                    "fluids_out": [],
                    "chances": [6000, 4000],
                    "kept": None,
                    "d_tier": False,
                }
                for mat in turbine_mats
            ]
        if "turbineLargeGas.mat(" in compact and "FlawDetector" in compact:
            return [
                {
                    "file": "recipePrefixItems.java",
                    "line": line,
                    "map": "FlawDetector",
                    "eu": 16,
                    "duration": 18000,
                    "special": 0,
                    "items_in": [world.item_form("turbineLargeGas", mat, 1)],
                    "items_out": [
                        world.item_form("turbineLargeGasChecked", mat, 1),
                        world.item_form("turbineLargeGasDamaged", mat, 1),
                    ],
                    "fluids_in": [],
                    "fluids_out": [],
                    "chances": [6000, 4000],
                    "kept": None,
                    "d_tier": False,
                }
                for mat in turbine_mats
            ]
        return None
    if file_name != "PurityRefining.java":
        return None
    if "OP.dust.mat(material,4)" in compact and "CommercialPureDust.mat(material,3)" in compact:
        return [
            purity_row(world, line, material, "dust", "dustCommercialPure", "MT.H.gas(U4,T)", 1024, 1200)
            for material in ("MT.W", "MT.Mo")
        ]
    if "AnalyticalPureDust.mat(material,4)" in compact and "AbsolutelyPureDust.mat(material,1)" in compact:
        materials = (
            "MT.Li", "MT.Fe", "MT.Ni", "MT.Co", "MT.Cu", "MT.Zn", "MT.Ag", "MT.Sn", "MT.Pb", "MT.Bi",
            "MT.Al", "MT.Ti", "MT.Zr", "MT.Nb", "MT.Ta", "MT.Cr",
            "MT.Au", "MT.Pt", "MT.Pd", "MT.Rh", "MT.Ir", "MT.Os", "matList.Ij.mat",
            "MT.W",
            "MT.C", "MT.Si", "MT.Ge", "MT.Ga", "MT.In",
            "MT.D", "MT.T", "MT.U_238", "MT.Th", "MT.Nq_522",
        )
        xe = world.parse_fluid("MT.Xe.gas(U4,T)")
        return [
            {
                "file": "PurityRefining.java",
                "line": line,
                "map": "LaserFusion",
                "eu": 32768,
                "duration": 1200,
                "special": 10_000_000,
                "items_in": [world.item_form("dustAnalyticalPure", material, 4), ("own", "quantum_control_element", 1)],
                "items_out": [world.item_form("dustAbsolutelyPure", material, 1)],
                "fluids_in": [xe],
                "fluids_out": [],
                "chances": None,
                "kept": None,
                "d_tier": False,
            }
            for material in materials
        ]
    if "CommercialPureDust.mat(material,4)" not in compact or "AnalyticalPureDust.mat(material,3)" not in compact:
        return None
    groups = (
        (("MT.Al", "MT.Ti", "MT.Zr", "MT.Nb", "MT.Ta", "MT.Cr"), "MT.Cl.gas(U4,T)", 1024, 1200),
        (("MT.Au", "MT.Pt", "MT.Pd", "MT.Rh", "MT.Ir", "MT.Os", "matList.Ij.mat"), "MT.H.gas(U4,T)", 2048, 1600),
        (("MT.W", "MT.Mo"), "MT.Ar.gas(U4,T)", 4096, 2400),
        (("MT.C", "MT.Si", "MT.Ge", "MT.Ga", "MT.In"), "MT.Ar.gas(U4,T)", 2048, 1600),
    )
    rows = []
    for materials, reagent, eu, duration in groups:
        for material in materials:
            rows.append(purity_row(
                world, line, material, "dustCommercialPure", "dustAnalyticalPure", reagent, eu, duration
            ))
    return rows


def parse_call(world: World, file_name: str, line: int, map_name: str, arity: str, args: list[str], raw: str, special: int = 0):
    if map_name == "LightMixer":
        raise Skip("保持卡 02 的光混合行")
    if file_name == "Chemistry.java" and line in (107, 108, 109):
        raise Skip("保持卡 04 的海水蒸馏行")
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
    raw_chances = None
    if rest and rest[0][0] == "num":
        raw_chances = [rest[0][1]]
        rest = rest[1:]
    if rest and rest[0][0] == "chances":
        if raw_chances is not None:
            raise Skip("概率输出")
        raw_chances = parse_chance_list(rest[0][1])
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
    items_in, kept_in = world.parse_items(item_exprs)
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
    items_out, kept_out = world.parse_items(item_out_exprs, output=True)
    if optimize_flag:
        duration, items_in, kept_in, items_out, kept_out, fluids_in, fluids_out = optimize(
            duration, items_in, kept_in, items_out, kept_out, fluids_in, fluids_out
        )
    chances = fit_chances(raw_chances, len(items_out))
    kept = [index for index, flag in enumerate(kept_in) if flag] or None
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
        amount = stack[3] if stack[0] == "item" else stack[2]
        if amount > 99:
            raise Skip("堆叠超过 99")
    return {
        "file": Path(file_name).name,
        "line": line,
        "map": map_name,
        "eu": eu,
        "duration": max(1, duration),
        "special": special,
        "items_in": items_in,
        "items_out": items_out,
        "fluids_in": fluids_in,
        "fluids_out": fluids_out,
        "chances": chances,
        "kept": kept,
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
    if stack[0] == "own":
        return f'own("{stack[1]}", {stack[2]})'
    if stack[0] == "block":
        return f'block("{stack[1]}", {stack[2]})'
    if stack[0] == "circuit":
        return f"circuit({stack[1]}, {stack[2]})"
    if stack[0] == "fluid":
        return f'field("{stack[1]}", {stack[2]})'
    raise AssertionError(stack)


def java_ints(values) -> str:
    if not values:
        return "null"
    return "new int[] {" + ", ".join(str(value) for value in values) + "}"


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
            tail = ""
            if row.get("special") or row["chances"] or row["kept"]:
                tail = ",\n                {special}L,\n                {chances},\n                {kept}".format(
                    special=row.get("special", 0),
                    chances=java_ints(row["chances"]),
                    kept=java_ints(row["kept"]),
                )
            body.append(
                "        add(\"{file}:{line}\", {field}, {eu}L, {duration}L,\n"
                "                {items_in},\n"
                "                {items_out},\n"
                "                {fluids_in},\n"
                "                {fluids_out}{tail});".format(
                    file=row["file"],
                    line=row["line"],
                    field=MAP_FIELD[map_name],
                    eu=row["eu"],
                    duration=row["duration"],
                    items_in=java_array("item", row["items_in"]),
                    items_out=java_array("item", row["items_out"]),
                    fluids_in=java_array("fluid", row["fluids_in"]),
                    fluids_out=java_array("fluid", row["fluids_out"]),
                    tail=tail,
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

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        add(where, map, euPerTick, duration, itemsIn, itemsOut, fluidsIn, fluidsOut, 0L, null, null);
    }}

    private static void add(String where, String map, long euPerTick, long duration,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut,
            long special, int[] chances, int[] kept) {{
        if (broken(itemsIn) || broken(fluidsIn) || broken(itemsOut) || broken(fluidsOut)) {{
            return;
        }}
        AddonRecipes.Line line = chances == null && kept == null
                ? new AddonRecipes.Line(itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, special)
                : new AddonRecipes.Line(itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, special, chances, kept);
        if (AddonRecipes.map(map).add(line) == null) {{
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

    private static ItemStack own(String path, int count) {{
        ItemStack stack = Gt6KtfruItems.stack(path, count);
        if (stack == null || stack.isEmpty()) {{
            PROBLEMS.add("item " + path);
            return ItemStack.EMPTY;
        }}
        return stack;
    }}

    private static ItemStack block(String path, int count) {{
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(Gt6KtfruAddon.MODID, path));
        if (item == Items.AIR) {{
            PROBLEMS.add("block " + path);
            return ItemStack.EMPTY;
        }}
        return new ItemStack(item, count);
    }}

    private static ItemStack circuit(int index, int count) {{
        ItemStack stack = AddonItems.selectorTag(index);
        if (stack == null || stack.isEmpty()) {{
            PROBLEMS.add("selector " + index);
            return ItemStack.EMPTY;
        }}
        return stack.copyWithCount(count);
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
        if stack[0] == "own":
            return f"{stack[2]} {stack[1]}"
        if stack[0] == "circuit":
            return f"circuit {stack[1]}"
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
            call_body = text[open_at + 1:close_at]
            prefix_text = text[:match.start()]
            amat_matches = list(re.finditer(r"\baMat\s*=\s*([^;]+);", prefix_text))
            if amat_matches:
                amat_val = amat_matches[-1].group(1).strip().replace("ANY.Steel", "MT.Steel")
                call_body = re.sub(r"\baMat\b", amat_val, call_body)
                raw = re.sub(r"\baMat\b", amat_val, raw)
            if "kRegistry0.getItem()" in call_body:
                line_start = text.rfind("\n", 0, match.start()) + 1
                same_line = text[line_start:match.start()]
                add_match = re.search(r'kRegistry0\.add\(\s*"[^"]*"\s*,\s*"[^"]*"\s*,\s*(\d+)\s*,', same_line)
                if add_match:
                    tid = add_match.group(1)
                    call_body = call_body.replace("kRegistry0.getItem()", f"kRegistry0.getItem({tid})")
                    raw = raw.replace("kRegistry0.getItem()", f"kRegistry0.getItem({tid})")
            special_val = 0
            after_call = text[close_at + 1:close_at + 80]
            spec_match = re.match(r"\s*\.\s*setSpecialNumber\(([^)]+)\)", after_call)
            if spec_match:
                try:
                    special_val = eval_raw(spec_match.group(1))
                except Skip:
                    special_val = 0
            args = split_args(call_body)
            try:
                expanded = purity_rows(world, file_name, line, raw)
                if expanded is not None:
                    rows.extend(expanded)
                    continue
                rows.append(parse_call(world, file_name, line, map_name, arity, args, raw, special_val))
            except Skip as skip:
                skips.append((map_name, Path(file_name).name, line, skip.reason, raw.replace("\n", " ")[:180]))
    lines = ["call sites"]
    for map_name in MAPS:
        lines.append(f"  {map_name}: {counts[map_name]}")
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
