"""Register ordinary kTFRU ItemList entries and their lang lines.

C-tier integration items stay out. Membranes and battery poles keep the ids
already used by the gas battery. Writes tools/own_items.json for the recipe
extractor.
"""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ENUM = ROOT / "kTFRUAddon" / "src" / "main" / "java" / "cn" / "kuzuanpa" / "ktfruaddon" / "api" / "item" / "ItemList.java"
ITEM_SRC = ROOT / "kTFRUAddon" / "src" / "main" / "java" / "cn" / "kuzuanpa" / "ktfruaddon" / "item"
JAVA = ROOT / "src" / "main" / "java" / "com" / "lombinaxmasson" / "gt6ktfruaddon" / "Gt6KtfruItems.java"
LANGS = (
    ROOT / "src" / "main" / "resources" / "assets" / "gt6ktfruaddon" / "lang" / "en_us.json",
    ROOT / "src" / "main" / "resources" / "assets" / "gt6ktfruaddon" / "lang" / "zh_cn.json",
)
MODELS = ROOT / "src" / "main" / "resources" / "assets" / "gt6ktfruaddon" / "models" / "item"
ICONS = ROOT / "src" / "main" / "resources" / "assets" / "gt6ktfruaddon" / "textures" / "item" / "material_icons"
SHINY = ROOT / "kTFRUAddon" / "src" / "main" / "resources" / "assets" / "ktfruaddon" / "textures" / "items" / "materialicons" / "SHINY"
MAP_JSON = ROOT / "tools" / "own_items.json"

FIXED = {
    "ProtonExchangeMembrane": "proton_exchange_membrane",
    "AlkalineIonExchangeMembrane": "alkaline_ion_exchange_membrane",
    "IrAlkalineIonExchangeMembrane": "ir_alkaline_ion_exchange_membrane",
    "BatteryPoleNickel": "ktfru.item.battery.pole.nickel",
    "BatteryPoleCaTiO3": "ktfru.item.battery.pole.catio3",
    "BatteryPolePlatinum": "ktfru.item.battery.pole.platinum",
    "BatteryPoleCarbon": "ktfru.item.battery.pole.carbon",
}
EXACT_SKIP = {
    "ArmorAirSealant", "SpaceSuitCloth",
    "RefinedStoragePart", "RefinedFluidStoragePart",
    "PreparedIronOre", "TwilightCore", "NaturalCore", "FlowerCluster",
    "ClayGlassBlockMold", "CeramicGlassBlockMold", "CeramicGlassBlockMoldFull",
    "CeramicGlassBlockMoldComplete", "ClayGlassBottleMold", "CeramicGlassBottleMold",
    "CeramicGlassBottleMoldFull", "CeramicGlassBottleMoldComplete",
}
PREFIX_TEXTURES = {
    "flywheel.png": "flywheel.png",
    "bladeLargeTurbine.png": "blade_large_turbine.png",
    "largeTurbine.png": "turbine_large.png",
    "dustCommercialPure.png": "dust_commercial_pure.png",
    "dustAnalyticalPure.png": "dust_analytical_pure.png",
    "dustAbsolutelyPure.png": "dust_absolutely_pure.png",
}
MODEL = """{
  "parent": "minecraft:item/generated",
  "textures": { "layer0": "minecraft:item/paper" }
}
"""


def snake(name: str) -> str:
    text = re.sub(r"(?<=[a-z0-9])(?=[A-Z])", "_", name)
    text = re.sub(r"(?<=[A-Z])(?=[A-Z][a-z])", "_", text)
    text = re.sub(r"(?<=[0-9])(?=[A-Za-z])", "_", text)
    return text.lower()


def skipped(name: str) -> bool:
    if name in EXACT_SKIP:
        return True
    return name.startswith("AsteroidMinerRocket") or name.startswith("DeprecatedAsteroid") or name.startswith("TFRUCoin")


def enum_names() -> list[str]:
    text = ENUM.read_text(encoding="utf-8")
    text = re.sub(r"//.*?$", "", text, flags=re.M)
    text = re.sub(r"/\*.*?\*/", "", text, flags=re.S)
    body = text.split("public enum ItemList", 1)[1]
    body = body.split("{", 1)[1].split("private ItemStack", 1)[0]
    body = body.rsplit(";", 1)[0]
    names = re.findall(r"[A-Za-z_][A-Za-z0-9_]*", body)
    seen = []
    for name in names:
        if name not in seen:
            seen.append(name)
    return seen


def display_names() -> dict[str, tuple[str, int, str]]:
    found = {}
    group_re = re.compile(r'super\(\s*MOD_ID\s*,\s*"([^"]+)"\s*\)')
    pattern = re.compile(r'ItemList\.(\w+)\s*\.set\(\s*addItem\(\s*(-?\d+)\s*,\s*"([^"]*)"')
    for path in ITEM_SRC.rglob("*.java"):
        text = path.read_text(encoding="utf-8")
        group_match = group_re.search(text)
        group = group_match.group(1) if group_match else ""
        for name, meta, label in pattern.findall(text):
            found[name] = (group, int(meta), label)
    return found


def spaced(name: str) -> str:
    return snake(name).replace("_", " ").title()


def upsert_lang(path: Path, pairs: list[tuple[str, str]]) -> None:
    text = path.read_text(encoding="utf-8")
    data = json.loads(text)
    missing = []
    for key, value in pairs:
        if key in data:
            old = json.dumps(data[key], ensure_ascii=False)
            new = json.dumps(value, ensure_ascii=False)
            if old == new:
                continue
            needle = json.dumps(key, ensure_ascii=False) + ": " + old
            if needle not in text:
                raise SystemExit(f"cannot replace {key} in {path.name}")
            text = text.replace(needle, json.dumps(key, ensure_ascii=False) + ": " + new, 1)
            continue
        missing.append((key, value))
    if missing:
        body = text.rstrip()
        if not body.endswith("}"):
            raise SystemExit(f"lang shape {path}")
        body = body[:-1].rstrip()
        if not body.endswith(","):
            body += ","
        lines = [body]
        for key, value in missing:
            lines.append("  " + json.dumps(key, ensure_ascii=False) + ": " + json.dumps(value, ensure_ascii=False) + ",")
        lines[-1] = lines[-1][:-1]
        lines.append("}")
        text = "\n".join(lines) + "\n"
    path.write_text(text, encoding="utf-8")
    json.loads(path.read_text(encoding="utf-8"))


def write_java(extra: list[str]) -> None:
    rows = ",\n".join(f'            "{path}"' for path in extra)
    JAVA.write_text(f"""package com.lombinaxmasson.gt6ktfruaddon;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Ordinary kTFRU items. Membranes and battery poles keep the ids the gas battery
 * already checks. The rest are ItemList entries used by the chemistry, circuit
 * and engine lines. TFC, Advanced Rocketry, Applied Energistics and Twilight
 * Forest entries stay unregistered.
 */
public final class Gt6KtfruItems {{
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.ITEM, Gt6KtfruAddon.MODID);

    public static final DeferredHolder<Item, Item> PROTON_EXCHANGE_MEMBRANE = item("proton_exchange_membrane");
    public static final DeferredHolder<Item, Item> ALKALINE_ION_EXCHANGE_MEMBRANE =
            item("alkaline_ion_exchange_membrane");
    public static final DeferredHolder<Item, Item> IR_ALKALINE_ION_EXCHANGE_MEMBRANE =
            item("ir_alkaline_ion_exchange_membrane");
    public static final DeferredHolder<Item, Item> BATTERY_POLE_NICKEL = item("ktfru.item.battery.pole.nickel");
    public static final DeferredHolder<Item, Item> BATTERY_POLE_CATIO3 = item("ktfru.item.battery.pole.catio3");
    public static final DeferredHolder<Item, Item> BATTERY_POLE_PLATINUM = item("ktfru.item.battery.pole.platinum");
    public static final DeferredHolder<Item, Item> BATTERY_POLE_CARBON = item("ktfru.item.battery.pole.carbon");

    public static final List<DeferredHolder<Item, Item>> ALL = List.of(
            PROTON_EXCHANGE_MEMBRANE, ALKALINE_ION_EXCHANGE_MEMBRANE, IR_ALKALINE_ION_EXCHANGE_MEMBRANE,
            BATTERY_POLE_NICKEL, BATTERY_POLE_CATIO3, BATTERY_POLE_PLATINUM, BATTERY_POLE_CARBON);

    public static final List<String> EXTRA = List.of(
{rows});

    public static final Map<String, DeferredHolder<Item, Item>> BY_PATH;

    static {{
        Map<String, DeferredHolder<Item, Item>> map = new LinkedHashMap<>();
        map.put("proton_exchange_membrane", PROTON_EXCHANGE_MEMBRANE);
        map.put("alkaline_ion_exchange_membrane", ALKALINE_ION_EXCHANGE_MEMBRANE);
        map.put("ir_alkaline_ion_exchange_membrane", IR_ALKALINE_ION_EXCHANGE_MEMBRANE);
        map.put("ktfru.item.battery.pole.nickel", BATTERY_POLE_NICKEL);
        map.put("ktfru.item.battery.pole.catio3", BATTERY_POLE_CATIO3);
        map.put("ktfru.item.battery.pole.platinum", BATTERY_POLE_PLATINUM);
        map.put("ktfru.item.battery.pole.carbon", BATTERY_POLE_CARBON);
        for (String path : EXTRA) map.put(path, item(path));
        BY_PATH = Map.copyOf(map);
    }}

    private Gt6KtfruItems() {{}}

    public static ItemStack stack(String path, int count) {{
        DeferredHolder<Item, Item> holder = BY_PATH.get(path);
        if (holder == null) return ItemStack.EMPTY;
        return new ItemStack(holder.get(), count);
    }}

    private static DeferredHolder<Item, Item> item(String path) {{
        return ITEMS.register(path, () -> new Item(new Item.Properties()));
    }}
}}
""", encoding="utf-8")


def main() -> None:
    labels = display_names()
    mapping = {}
    extra = []
    for name in enum_names():
        if skipped(name):
            continue
        path = FIXED.get(name, snake(name))
        if not re.fullmatch(r"[a-z0-9._-]+", path):
            raise SystemExit(f"bad path {path} from {name}")
        previous = [key for key, value in mapping.items() if value == path]
        if previous:
            raise SystemExit(f"duplicate path {path}: {previous} {name}")
        mapping[name] = path
        if name not in FIXED:
            extra.append(path)
    if len(extra) != len(set(extra)):
        raise SystemExit("duplicate extra path")
    MAP_JSON.write_text(json.dumps(mapping, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    write_java(extra)
    en_pairs = []
    zh_pairs = []
    for name, path in mapping.items():
        if name in FIXED:
            continue
        info = labels.get(name)
        label = info[2] if info else ""
        english = label if label and not re.search(r"[\u4e00-\u9fff]", label) else spaced(name)
        chinese = label if re.search(r"[\u4e00-\u9fff]", label) else english
        key = "item.gt6ktfruaddon." + path
        en_pairs.append((key, english))
        zh_pairs.append((key, chinese))
    upsert_lang(LANGS[0], en_pairs)
    upsert_lang(LANGS[1], zh_pairs)
    MODELS.mkdir(parents=True, exist_ok=True)
    item_tex_src = ROOT / "kTFRUAddon" / "src" / "main" / "resources" / "assets" / "ktfruaddon" / "textures" / "items"
    item_tex_dest = ROOT / "src" / "main" / "resources" / "assets" / "gt6ktfruaddon" / "textures" / "item"
    textured = 0
    for name, path in mapping.items():
        info = labels.get(name)
        layer0 = "minecraft:item/paper"
        if info and info[0]:
            group, meta, _ = info
            group_dir = group.lower()
            src_png = item_tex_src / group / f"{meta}.png"
            if src_png.is_file():
                dest_png = item_tex_dest / group_dir / f"{meta}.png"
                dest_png.parent.mkdir(parents=True, exist_ok=True)
                dest_png.write_bytes(src_png.read_bytes())
                src_meta = src_png.with_name(src_png.name + ".mcmeta")
                if src_meta.is_file():
                    dest_png.with_name(dest_png.name + ".mcmeta").write_bytes(src_meta.read_bytes())
                layer0 = f"gt6ktfruaddon:item/{group_dir}/{meta}"
                textured += 1
        model = MODELS / f"{path}.json"
        model.write_text(
            f'{{\n  "parent": "minecraft:item/generated",\n  "textures": {{ "layer0": "{layer0}" }}\n}}\n',
            encoding="utf-8",
        )
    ICONS.mkdir(parents=True, exist_ok=True)
    for source_name, dest_name in PREFIX_TEXTURES.items():
        source = SHINY / source_name
        if not source.is_file():
            raise SystemExit(f"missing prefix texture {source}")
        (ICONS / dest_name).write_bytes(source.read_bytes())
    print(f"items {len(mapping)} extra {len(extra)} textured {textured}")


if __name__ == "__main__":
    main()
