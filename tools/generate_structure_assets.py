# Writes structure language entries and fallback models not owned by the texture porter.

import json
from pathlib import Path

import paths
import port_block_textures

ROOT = paths.ROOT
ASSETS = ROOT / "src" / "main" / "resources" / "assets" / "gt6ktfruaddon"
TEMPLATE = (ASSETS / "blockstates" / "research_monitor.json").read_text(encoding="utf-8")
ITEM = '{ "parent": "gt6ktfruaddon:block/machine/basic/sun_heater" }\n'

MACHINES = {
    "research_table_compute": ("Research Table Compute", "研究计算台"),
    "research_table_current_control": ("Research Table Current Control", "研究电流控制台"),
    "optical_microscope": ("Optical Microscope", "光学显微镜"),
    "electron_microscope": ("Electron Microscope", "电子显微镜"),
    "research_reactor": ("Research Reactor", "研究反应釜"),
    "wireless_compute_controller": ("Wireless Compute Controller", "无线计算控制器"),
    "wireless_compute_controller_large": ("Large Wireless Compute Controller", "大型无线计算控制器"),
    "wireless_compute_controller_huge": ("Huge Wireless Compute Controller", "巨型无线计算控制器"),
    "biology_compute_t1": ("Biology Compute Array (T1)", "生物计算阵列（T1）"),
    "biology_compute_t2": ("Biology Compute Array (T2)", "生物计算阵列（T2）"),
    "biology_compute_t3": ("Biology Compute Array (T3)", "生物计算阵列（T3）"),
    "biology_compute_t4": ("Biology Compute Array (T4)", "生物计算阵列（T4）"),
    "quantum_compute_t1": ("Quantum Compute Array (T1)", "量子计算阵列（T1）"),
    "quantum_compute_t2": ("Quantum Compute Array (T2)", "量子计算阵列（T2）"),
    "quantum_compute_t3": ("Quantum Compute Array (T3)", "量子计算阵列（T3）"),
    "quantum_compute_t4": ("Quantum Compute Array (T4)", "量子计算阵列（T4）"),
    "spacetime_compute_t1": ("Spacetime Compute Array (T1)", "时空计算阵列（T1）"),
    "spacetime_compute_t2": ("Spacetime Compute Array (T2)", "时空计算阵列（T2）"),
    "quantum_compute_center": ("Quantum Compute Center", "量子计算中心"),
    "wireless_compute_node_aluminium": ("Wireless Compute Node (T1)", "无线计算节点（T1）"),
    "wireless_compute_node_stainless": ("Wireless Compute Node (T2)", "无线计算节点（T2）"),
    "wireless_compute_node_chromium": ("Wireless Compute Node (T3)", "无线计算节点（T3）"),
    "wireless_compute_node_titanium": ("Wireless Compute Node (T4)", "无线计算节点（T4）"),
    "quantum_compute_node_naquadah": ("Local Quantum Compute Part (T1)", "本地量子计算件（T1）"),
    "quantum_compute_node_naquadria": ("Local Quantum Compute Part (T2)", "本地量子计算件（T2）"),
    "quantum_compute_node_enriched": ("Local Quantum Compute Part (T3)", "本地量子计算件（T3）"),
    "quantum_compute_node_trinaquadalloy": ("Local Quantum Compute Part (T4)", "本地量子计算件（T4）"),
    "wireless_battery_inductive": ("Inductive Wireless Energy Tower", "感应无线能量塔"),
    "wireless_battery_quantum": ("Quantum Wireless Energy Hub", "量子无线能量枢纽"),
    "wireless_receiver_inductive": ("Inductive Wireless Energy Receiver", "感应无线能量接收器"),
    "wireless_receiver_quantum": ("Quantum Wireless Energy Receiver", "量子无线能量接收器"),
    "laser_fusion_experimental": ("Laser Inertial Fusion Target Chamber (Experimental)", "激光惯性聚变靶室（实验）"),
    "laser_fusion_industrial": ("Laser Inertial Fusion Target Chamber (Industrial)", "激光惯性聚变靶室（工业）"),
    "catalytic_reactor_ev": ("Catalytic Reactor (EV)", "催化反应釜（EV）"),
    "catalytic_reactor_iv": ("Catalytic Reactor (IV)", "催化反应釜（IV）"),
    "mirror_gold_synthesis": ("Mirror Gold Synthesis Chamber", "镜金合成室"),
    "mirror_gold_quantumization": ("Mirror Gold Quantumization Furnace", "镜金量子化炉"),
    "cosmic_radiation_chamber": ("Cosmic Radiation Analysis Chamber", "宇宙辐射分析室"),
    "microgravity_assembler": ("Microgravity Assembler", "微重力组装机"),
    "quantum_gold_chamber": ("Quantum Gold Synthesis Chamber", "量子金合成室"),
    "quantum_observation_tower": ("Quantum Observation Tower", "量子观测塔"),
    "microgravity_crystal_growth": ("Microgravity Crystal Growth Machine", "微重力晶体生长机"),
    "quantum_communication_relay": ("Quantum Communication Relay", "量子通信中继"),
    "centrifugal_microgravity": ("Centrifugal High-Shield Microgravity Controller", "离心高屏蔽微重力控制器"),
    "ai_flow_control": ("AI Flow Control Center", "AI 流控中心"),
    "ai_fill_planning": ("AI Fill Planning Center", "AI 装填规划中心"),
    "ai_pattern_recognition": ("AI Pattern Recognition Center", "AI 模式识别中心"),
}

STRUCTURES = {
    "research_reactor": (
        "Same two-layer shell as the research assembler. EU 131072, packets 64-524288.",
        "和研究组装机相同的两层外壳。EU 131072，包大小 64–524288。",
    ),
    "optical_microscope": (
        "Three layers. C is a tank wall. G is glass. The controller is the bottom gap. 256 EU per item.",
        "三层。C 是储罐墙。G 是玻璃。控制器在底层空位。每件物品 256 EU。",
    ),
    "electron_microscope": (
        "Three layers, offset -2,-1,0. C is a tank wall, S is a titanium wall, G is glass. 4096 EU per item.",
        "三层，偏移 -2,-1,0。C 是储罐墙，S 是钛墙，G 是玻璃。每件物品 4096 EU。",
    ),
    "wireless_compute_controller": (
        "3x3x3. One middle layer. N is an aluminium wireless compute node. Reach is 512 blocks.",
        "3×3×3。中间一层。N 是铝制无线计算节点。距离 512 格。",
    ),
    "wireless_battery_inductive": (
        "Seven 4x4 layers, AABBBCC. Same dimension, 64 blocks, 8 links, distance loss.",
        "七层 4×4，AABBBCC。同维度，64 格，8 条链路，距离有损耗。",
    ),
    "wireless_battery_quantum": (
        "5x5 layers ABCCCDDDD. Cross-dimension, 32 links, no distance loss, quantum receivers only.",
        "5×5 层，ABCCCDDDD。可跨维度，32 条链路，无距离损耗，只连量子接收器。",
    ),
    "fusion_tokamak_exp": (
        "Layer rule 0123456, offset -8,0,0. EU 2048. The LU start charge is not applied.",
        "层规则 0123456，偏移 -8,0,0。EU 2048。不收取 LU 启动能量。",
    ),
    "fusion_tokamak_t1": (
        "Layer rule ABCDEFGGFEKLMM, offset -12,-1,0. EU 16384. The LU start charge is not applied.",
        "层规则 ABCDEFGGFEKLMM，偏移 -12,-1,0。EU 16384。不收取 LU 启动能量。",
    ),
    "laser_fusion_experimental": (
        "7x7x7. LU packets 1-131072. No pulse buffer and no cooldown.",
        "7×7×7。LU 包 1–131072。没有脉冲缓存，也没有冷却。",
    ),
    "laser_fusion_industrial": (
        "11x11x11. LU packets 1-2097152. No pulse buffer and no cooldown.",
        "11×11×11。LU 包 1–2097152。没有脉冲缓存，也没有冷却。",
    ),
    "catalytic_reactor_ev": (
        "5x5x5 ABCDE, offset -2,-1,0. EU 2048, packets 256-4096, cheap overclock.",
        "5×5×5 ABCDE，偏移 -2,-1,0。EU 2048，包 256–4096，廉价超频。",
    ),
    "catalytic_reactor_iv": (
        "Same shell as the EV reactor. EU 8192, packets 1024-16384, cheap overclock.",
        "和 EV 反应釜相同的外壳。EU 8192，包 1024–16384，廉价超频。",
    ),
    "centrifugal_microgravity": (
        "9x9x9 shell. Pays 2048 EU per tick and counts 600 ticks. World rules stay unchanged.",
        "9×9×9 外壳。每刻支付 2048 EU，计数 600 刻。不改变世界规则。",
    ),
}

MESSAGES = {
    "gt6ktfruaddon.compute.node": ("Noted %s %s", "已记下 %s %s"),
    "gt6ktfruaddon.compute.links": ("Linked nodes: %s", "已连接节点：%s"),
    "gt6ktfruaddon.compute.array": ("Formed %s. Requires %s, provides %s %s. No cluster schedule.", "成型 %s。需要 %s，提供 %s %s。不调度集群。"),
    "gt6ktfruaddon.compute.plain": ("Emits %s EU", "送出 %s EU"),
    "gt6ktfruaddon.ai.points": ("Would grant %s points per tick. Formed %s. The minigame is not run.", "成型后每刻记 %s 分。成型 %s。不跑小游戏。"),
}

OBSOLETE = [
    "electric_controller",
    "wireless_controller",
    "wired_controller",
    "network_cable",
    "wireless_battery",
    "wireless_receiver",
]


def write_block(name):
    if port_block_textures.owns(name):
        return
    (ASSETS / "blockstates" / f"{name}.json").write_text(TEMPLATE, encoding="utf-8", newline="\n")
    (ASSETS / "models" / "item" / f"{name}.json").write_text(ITEM, encoding="utf-8", newline="\n")


def append(path, pairs):
    text = path.read_text(encoding="utf-8")
    data = json.loads(text)
    added = []
    for key, value in pairs:
        if key not in data:
            added.append(f'  "{key}": "{value}"')
    if not added:
        return
    if not text.rstrip().endswith("}"):
        raise SystemExit(f"unexpected ending in {path}")
    body = text.rstrip()
    body = body[:body.rfind("}")]
    body = body.rstrip()
    if body.endswith(","):
        body = body[:-1]
    extra = ",\n" + ",\n".join(added) + "\n}\n"
    path.write_text(body + extra, encoding="utf-8", newline="\n")


def main():
    for name in OBSOLETE:
        for folder in ("blockstates", "models/item"):
            path = ASSETS / folder / f"{name}.json"
            if path.exists():
                path.unlink()
    for name in MACHINES:
        write_block(name)
    en = []
    zh = []
    for name, (english, chinese) in MACHINES.items():
        en.append((f"block.gt6ktfruaddon.{name}", english))
        zh.append((f"block.gt6ktfruaddon.{name}", chinese))
    for name, (english, chinese) in STRUCTURES.items():
        en.append((f"gt6ktfruaddon.structure.{name}.1", english))
        zh.append((f"gt6ktfruaddon.structure.{name}.1", chinese))
    parts = (ROOT / "build" / "generated" / "structure-parts.txt").read_text(encoding="utf-8").splitlines()
    for line in parts:
        name, title = line.split("\t", 1)
        en.append((f"block.gt6ktfruaddon.{name}", title))
        zh.append((f"block.gt6ktfruaddon.{name}", title))
    for key, (english, chinese) in MESSAGES.items():
        en.append((key, english))
        zh.append((key, chinese))
    append(ASSETS / "lang" / "en_us.json", en)
    append(ASSETS / "lang" / "zh_cn.json", zh)
    print(f"assets {len(MACHINES)} lang {len(en)}")


if __name__ == "__main__":
    paths.require_inputs(upstream=True, gt6=True)
    main()
