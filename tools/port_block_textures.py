"""Copy kTFRU and GT6 machine art onto all card 08–20 blocks.

Does not replace the behavior-machine GUI sheet.
"""

from __future__ import annotations

import json
import shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "kTFRUAddon" / "src" / "main" / "resources" / "assets" / "gregtech" / "textures" / "blocks" / "machines"
GT_SRC = ROOT.parent / "GregTech6CommunityEdition" / "GregTech6" / "core" / "src" / "main" / "resources" / "assets" / "gt6ce" / "textures" / "block" / "machines"
GUI_SRC = ROOT / "kTFRUAddon" / "src" / "main" / "resources" / "assets" / "gregtech" / "textures" / "gui" / "machines"
GT_GUI_SRC = ROOT.parent / "GregTech6CommunityEdition" / "GregTech6" / "core" / "src" / "main" / "resources" / "assets" / "gt6ce" / "textures" / "gui" / "recipes"
ASSETS = ROOT / "src" / "main" / "resources" / "assets" / "gt6ktfruaddon"
DEST = ASSETS / "textures" / "block" / "machines"
MODELS = ASSETS / "models" / "block" / "machine" / "basic"
BLOCKSTATES = ASSETS / "blockstates"
ITEMS = ASSETS / "models" / "item"
RECIPES = ASSETS / "textures" / "gui" / "recipes"
REPORT = ROOT / "build" / "texture-port.txt"

FACES = ("down", "up", "north", "south", "west", "east")
SIX = {"down": "bottom", "up": "top", "north": "front", "south": "back", "west": "right", "east": "left"}
COPIED: dict[str, str] = {}
MISSING: list[str] = []


def use(rel: str) -> str | None:
    rel = rel.removesuffix(".png")
    src = SRC / f"{rel}.png"
    if not src.is_file():
        src = GT_SRC / f"{rel}.png"
    if not src.is_file():
        MISSING.append(rel)
        return None
    if rel not in COPIED:
        dest = DEST / f"{rel}.png"
        dest.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(src, dest)
        meta = src.with_name(src.name + ".mcmeta")
        if meta.is_file():
            shutil.copyfile(meta, dest.with_name(dest.name + ".mcmeta"))
        COPIED[rel] = f"gt6ktfruaddon:block/machines/{rel}"
    return COPIED[rel]


def all_faces(texture: str) -> dict[str, str]:
    return {face: texture for face in FACES}


def cube(textures: dict[str, str], tint: bool) -> dict:
    faces = {}
    for face in textures:
        entry = {"texture": f"#{face}", "cullface": face}
        if tint:
            entry["tintindex"] = 0
        faces[face] = entry
    return {"from": [0, 0, 0], "to": [16, 16, 16], "faces": faces}


def model(colored: dict[str, str], overlay: dict[str, str] | None, tint: bool) -> dict:
    particle = colored.get("down") or colored.get("north") or next(iter(colored.values()))
    children = {
        "layer0": {
            "parent": "minecraft:block/block",
            "textures": colored,
            "elements": [cube(colored, tint)],
            "render_type": "minecraft:solid" if tint else "minecraft:cutout",
        }
    }
    if overlay:
        children["layer1"] = {
            "parent": "minecraft:block/block",
            "textures": overlay,
            "elements": [cube(overlay, False)],
            "render_type": "minecraft:cutout",
        }
    return {
        "loader": "neoforge:composite",
        "children": children,
        "parent": "minecraft:block/block",
        "textures": {"particle": particle},
    }


def pack(colored, idle, active, running, tint=True):
    if colored is None:
        return None
    return [
        model(colored, idle, tint),
        model(colored, active, tint),
        model(colored, running if running is not None else active, tint),
    ]


def painted(idle: str, active: str):
    return [
        model(all_faces(idle), None, False),
        model(all_faces(active), None, False),
        model(all_faces(active), None, False),
    ]


def six(folder: str):
    def layer(sub: str) -> dict[str, str] | None:
        found = {}
        for face, name in SIX.items():
            texture = use(f"{folder}/{sub}/{name}")
            if texture:
                found[face] = texture
            elif sub == "colored":
                return None
        return found or None

    colored = layer("colored")
    if colored is None:
        return None
    idle = layer("overlay") or {}
    active = dict(idle)
    active.update(layer("overlay_active") or {})
    running = dict(active)
    running.update(layer("overlay_running") or {})
    return pack(colored, idle or None, active or None, running or None)


def axis(folder: str, north_overlay=None):
    side = use(f"{folder}/colored/side")
    axle = use(f"{folder}/colored/axis")
    if not side or not axle:
        return None
    colored = {face: side if face == "north" else axle for face in FACES}
    if north_overlay is None:
        return pack(colored, None, None, None)

    def overlay(name: str):
        front = use(f"{folder}/overlay/{name}")
        ring = use(f"{folder}/overlay/axis")
        if not front or not ring:
            return None
        return {face: front if face == "north" else ring for face in FACES}

    return pack(colored, overlay(north_overlay[0]), overlay(north_overlay[1]), overlay(north_overlay[2]))


def common(folder: str, base_name="common", front_name="front"):
    base = use(f"{folder}/{base_name}")
    front = use(f"{folder}/{front_name}")
    if not base or not front:
        return None
    return pack(all_faces(base), {"north": front}, None, None)


def base_front(folder: str):
    return common(folder, "base", "front")


def turbine_faces(folder: str):
    parts = {}
    for key, rel in {
        "down": f"{folder}/colored/bottom",
        "up": f"{folder}/colored/top",
        "north": f"{folder}/colored_front/side",
        "side": f"{folder}/colored/side",
        "odown": f"{folder}/overlay/bottom",
        "oup": f"{folder}/overlay/top",
        "onorth": f"{folder}/overlay_front/side",
        "oside": f"{folder}/overlay/side",
    }.items():
        texture = use(rel)
        if texture is None:
            return None
        parts[key] = texture
    colored = {
        "down": parts["down"], "up": parts["up"], "north": parts["north"],
        "south": parts["side"], "west": parts["side"], "east": parts["side"],
    }
    overlay = {
        "down": parts["odown"], "up": parts["oup"], "north": parts["onorth"],
        "south": parts["oside"], "west": parts["oside"], "east": parts["oside"],
    }
    return pack(colored, overlay, None, None)


def hopper():
    parts = {}
    for key, rel in {
        "down": "hopper/colored/bottom",
        "up": "hopper/colored/top",
        "side": "hopper/colored/side",
        "odown": "hopper/overlay/bottom",
        "oup": "hopper/overlay/top",
        "oside": "hopper/overlay/side",
    }.items():
        texture = use(rel)
        if texture is None:
            return None
        parts[key] = texture
    colored = {
        "down": parts["down"], "up": parts["up"], "north": parts["side"],
        "south": parts["side"], "west": parts["side"], "east": parts["side"],
    }
    overlay = {
        "down": parts["odown"], "up": parts["oup"], "north": parts["oside"],
        "south": parts["oside"], "west": parts["oside"], "east": parts["oside"],
    }
    return pack(colored, overlay, None, None)


def tokamak():
    base = use("fusion/tokamak/exp/base")
    stop = use("fusion/tokamak/exp/overlay_stop")
    charging = use("fusion/tokamak/exp/overlay_charging")
    running = use("fusion/tokamak/exp/overlay_running")
    if not all((base, stop, charging, running)):
        return None
    return pack(all_faces(base), all_faces(stop), all_faces(charging), all_faces(running))


def mask_zero():
    sides = use("maskaligner/0/single/sides")
    front = use("maskaligner/0/overlay/front")
    active = use("maskaligner/0/overlay/front_active")
    running = use("maskaligner/0/overlay/front_running_glow")
    if not all((sides, front, active, running)):
        return None
    return pack(all_faces(sides), {"north": front}, {"north": active}, {"north": running})


def mask_one():
    common_tex = use("maskaligner/1/common")
    front = use("maskaligner/1/overlay/front")
    active = use("maskaligner/1/overlay/front_active")
    running = use("maskaligner/1/overlay/front_running")
    if not all((common_tex, front, active, running)):
        return None
    return pack(all_faces(common_tex), {"north": front}, {"north": active}, {"north": running})


def miner():
    body = use("miner/colored")
    front = use("miner/front")
    active = use("miner/front_active")
    finished = use("miner/front_finished")
    side_in = use("miner/side_in")
    if not all((body, front, active, finished, side_in)):
        return None
    colored = all_faces(body)
    return pack(
        colored,
        {"north": front, "south": side_in},
        {"north": active, "south": side_in},
        {"north": finished, "south": side_in},
    )


def water_mill():
    face = use("generators/watermill/face")
    side = use("generators/watermill/side")
    if not face or not side:
        return None
    colored = all_faces(side)
    colored["north"] = face
    return pack(colored, None, None, None)


def manual(kind: str):
    folder = f"generators/motor_manual/{kind}"
    front = use(f"{folder}/colored/front")
    back = use(f"{folder}/colored/back")
    sides = use(f"{folder}/colored/sides")
    overlay_front = use(f"{folder}/overlay/front")
    overlay_back = use(f"{folder}/overlay/back")
    overlay_sides = use(f"{folder}/overlay/sides")
    active_front = use(f"{folder}/overlay_active_front/medium")
    active_back = use(f"{folder}/overlay_active/back")
    active_sides = use(f"{folder}/overlay_active_sides/medium")
    if not all((front, back, sides, overlay_front, overlay_back, overlay_sides, active_front, active_back, active_sides)):
        return None
    colored = {"north": front, "south": back, "east": sides, "west": sides, "up": sides, "down": sides}
    idle = {"north": overlay_front, "south": overlay_back, "east": overlay_sides, "west": overlay_sides}
    active = {"north": active_front, "south": active_back, "east": active_sides, "west": active_sides}
    return pack(colored, idle, active, active)


def fuel_battery():
    color = use("generators/fuel_battery/color")
    front = use("generators/fuel_battery/overlay/front")
    front_active = use("generators/fuel_battery/overlay/front_active")
    sides = use("generators/fuel_battery/overlay/sides")
    sides_active = use("generators/fuel_battery/overlay/sides_active")
    top = use("generators/fuel_battery/overlay/top")
    if not all((color, front, front_active, sides, sides_active, top)):
        return None
    idle = {"north": front, "south": sides, "east": sides, "west": sides, "up": top, "down": sides}
    active = {"north": front_active, "south": sides_active, "east": sides_active, "west": sides_active, "up": top, "down": sides}
    return pack(all_faces(color), idle, active, active)


def gas_tank():
    body = use("tankgascompressedinputer/material")
    front = use("tankgascompressedinputer/front")
    active = use("tankgascompressedinputer/front_active")
    running = use("tankgascompressedinputer/front_running")
    back = use("tankgascompressedinputer/back")
    if not all((body, front, active, running, back)):
        return None
    return pack(
        all_faces(body),
        {"north": front, "south": back},
        {"north": active, "south": back},
        {"north": running, "south": back},
    )


def compute(folder: str):
    background = use(f"{folder}/background")
    normal_front = use(f"{folder}/normal/front")
    normal_sides = use(f"{folder}/normal/sides")
    active_front = use(f"{folder}/active/front")
    running_front = use(f"{folder}/running/front")
    running_sides = use(f"{folder}/running/sides")
    if not all((background, normal_front, normal_sides, active_front, running_front, running_sides)):
        return None
    idle = {"north": normal_front, "south": normal_sides, "east": normal_sides, "west": normal_sides}
    active = {"north": active_front, "south": running_sides, "east": running_sides, "west": running_sides}
    running = {"north": running_front, "south": running_sides, "east": running_sides, "west": running_sides}
    return pack(all_faces(background), idle, active, running)


def turbine_icon():
    idle = use("multiblockmains/turbine")
    active = use("multiblockmains/turbine_active")
    if not idle or not active:
        return None
    return painted(idle, active)


def spec_for(stem: str):
    if stem == "cnc_machine":
        return six("basicmachines/cncmachine3")
    if stem in ("catalytic_reactor_ev", "catalytic_reactor_iv"):
        return six("basicmachines/mixer")
    if stem in ("microgravity_crystal_growth", "cosmic_radiation_chamber", "microgravity_assembler"):
        return six("basicmachines/heatmixer")
    if stem in ("laser_fusion_experimental", "laser_fusion_industrial"):
        return six("basicmachines/fusionreactor")
    if stem.startswith("flywheel_gear_small_"):
        return common("multiblockparts/transformer/rot_small")
    if stem.startswith("flywheel_gear_large_"):
        return common("multiblockparts/transformer/rot_large")
    if stem.startswith("electric_flywheel_") or stem.startswith("large_electric_flywheel_"):
        return axis("energystorages/flywheel_box_elec", ("side", "side_has", "side_full"))
    if stem.startswith((
            "flywheel_", "large_flywheel_", "vacuum_flywheel_", "large_vacuum_flywheel_", "liquid_battery_")):
        return axis("energystorages/flywheel_box")
    if stem.startswith("transformer_small_"):
        return common("multiblockparts/transformer/elec_small")
    if stem.startswith("transformer_large_"):
        return common("multiblockparts/transformer/elec_large")
    if stem.startswith("light_transformer_small_"):
        return common("multiblockparts/transformer/light_small")
    if stem.startswith("light_transformer_large_"):
        return common("multiblockparts/transformer/light_large")
    if stem in ("wireless_receiver_inductive", "wireless_receiver_quantum"):
        return common("multiblockparts/transformer/elec_large")
    if stem == "sun_heater_mirror_large" or stem.startswith(("wireless_battery_", "transform_battery_")):
        return common("multiblockmains/transformer")
    bases = {
        "sun_heater": "multiblockmains/sunHeater",
        "mantle_heater": "multiblockmains/mantleHeater",
        "fuel_deburn_factory": "multiblockmains/fuelDeburner",
        "tidal_wave_generator": "multiblockmains/tidalWaveGenerator",
        "fluid_boiler": "multiblockmains/fluidBoiler",
        "research_monitor": "research/monitor",
        "research_database_item": "research/monitor",
        "research_database_online": "research/monitor",
    }
    if stem in bases:
        return base_front(bases[stem])
    if stem.startswith("research_table_"):
        return base_front("research/table/fill")
    if stem == "oil_miner":
        return turbine_faces("multiblockmains/oilminer")
    if stem == "electromagnet_crucible" or stem.startswith("wireless_compute_controller"):
        return turbine_faces("multiblockmains/dummycrucible")
    if stem in ("steel_compressed_gas_tank", "stainless_compressed_gas_tank"):
        return turbine_faces("multiblockmains/tankmetal")
    if stem.startswith("dynamo_") or stem == "separate_excite_dynamo":
        return turbine_faces("multiblockmains/largedynamo")
    if stem.startswith(("large_gas_turbine_", "large_steam_turbine_", "expanded_motor_")):
        return turbine_icon()
    if stem in ("fusion_tokamak_exp", "fusion_tokamak_t1"):
        return tokamak()
    if stem in ("mask_aligner_uv", "mask_aligner_duv", "mask_aligner_euv", "research_assembler", "research_reactor", "optical_microscope"):
        return mask_zero()
    if stem == "mask_aligner_uv_plus":
        return mask_one()
    if stem.startswith("miner_"):
        return miner()
    if stem == "water_mill":
        return water_mill()
    if stem.startswith("manual_motor_"):
        return manual("RU")
    if stem.startswith("manual_kinetic_motor_"):
        return manual("KU")
    if stem in ("gas_battery_aluminium", "gas_battery_stainless"):
        return fuel_battery()
    if stem.startswith("gas_compressor_"):
        return gas_tank()
    if stem.startswith("void_hopper_"):
        return hopper()
    if stem == "compute_cluster_simple":
        return compute("multiblockparts/computeclustersimple")
    if stem in (
        "compute_cluster",
        "centrifugal_microgravity",
        "electron_microscope",
        "mirror_gold_synthesis",
        "mirror_gold_quantumization",
        "quantum_gold_chamber",
        "quantum_observation_tower",
        "quantum_communication_relay",
        "quantum_compute_center",
        "compute_user",
        "dream_brain",
    ) or stem.startswith((
        "biology_compute_",
        "quantum_compute_",
        "spacetime_compute_",
        "wireless_compute_node_",
        "ai_",
    )):
        return compute("multiblockparts/computecluster")
    return None


def blockstate(stem: str) -> dict:
    variants = {}
    for facing, turn in (("north", None), ("east", 90), ("south", 180), ("west", 270)):
        for lit in (False, True):
            for running in (False, True):
                suffix = ""
                if running and not lit:
                    suffix = "_running"
                elif lit:
                    suffix = "_active"
                entry = {"model": f"gt6ktfruaddon:block/machine/basic/{stem}{suffix}"}
                if turn:
                    entry["y"] = turn
                variants[f"facing={facing},lit={str(lit).lower()},running={str(running).lower()}"] = entry
    return {"variants": variants}


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")


def retarget_item(stem: str, parent: str) -> None:
    path = ITEMS / f"{stem}.json"
    if not path.is_file():
        return
    data = json.loads(path.read_text(encoding="utf-8-sig"))
    current = data.get("parent", "")
    if current.startswith("minecraft:item"):
        return
    if current == parent:
        return
    data["parent"] = parent
    write_json(path, data)


def copy_gui() -> None:
    RECIPES.mkdir(parents=True, exist_ok=True)
    mapping = {
        "FuelBattery.png": "fuelbattery.png",
        "MaskAligner.png": "maskaligner.png",
        "CNC.png": "cnc.png",
        "FuelDeburner.png": "fueldeburner.png",
        "FusionTokamak.png": "fusiontokamak.png",
        "Assembler.png": "researchassembler.png",
    }
    for source_name, dest_name in mapping.items():
        shutil.copyfile(GUI_SRC / source_name, RECIPES / dest_name)
    for gt_name in ("mixer.png", "fusion.png"):
        shutil.copyfile(GT_GUI_SRC / gt_name, RECIPES / gt_name)
    shutil.copyfile(ASSETS / "textures" / "gui" / "machines" / "gascompressor.png", RECIPES / "gascompressor.png")


def main() -> None:
    placeholder_file = MODELS / "sun_heater_placeholder.json"
    if placeholder_file.is_file():
        placeholder_file.unlink()
    mapped = []
    skipped = []
    for state in sorted(BLOCKSTATES.glob("*.json")):
        stem = state.stem
        before = len(MISSING)
        states = spec_for(stem)
        if states is None:
            if len(MISSING) != before:
                del MISSING[before:]
            skipped.append(stem)
            continue
        if any(item is None for item in states):
            skipped.append(stem + " (texture missing)")
            continue
        for suffix, data in (("", states[0]), ("_active", states[1]), ("_running", states[2])):
            write_json(MODELS / f"{stem}{suffix}.json", data)
        write_json(state, blockstate(stem))
        retarget_item(stem, f"gt6ktfruaddon:block/machine/basic/{stem}")
        mapped.append(stem)
    copy_gui()
    lines = [f"mapped {len(mapped)}", *("  " + stem for stem in mapped), f"left {len(skipped)}", *("  " + stem for stem in skipped)]
    lines.append(f"textures {len(COPIED)}")
    REPORT.parent.mkdir(parents=True, exist_ok=True)
    REPORT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print("\n".join(lines[:40]))
    print(f"... report {REPORT}")


if __name__ == "__main__":
    main()
