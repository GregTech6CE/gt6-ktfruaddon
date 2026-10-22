# 20 卡 15–18 检出差异对比与补齐

- 状态：closed
- 依赖：排在卡 19 之后
- 文件范围：卡 15–18 漏掉与做反的机器、零件、配方图、结构布局、方块状态和语言
- 平台：NeoForge 1.21.1

## 背景

`e164df5`（卡 15）、`7c99b3b`（卡 16）、`2d0008e`（卡 17）、`6d6ce1d`（卡 18）实施时误读了旧检出（`75cebb71`），没有读本仓库根目录下的 `kTFRUAddon/`（`6404ab69`）。原卡 15–19 的卡片文本已从 `6a67f54` 恢复。这张卡逐条列出已落代码与 `kTFRUAddon/`（`6404ab69`）的差异，并给出补齐清单与源类结构字符串。

---

## 一、卡 15（研究系统）对比与补齐

### 1. 已落代码与 `6404ab69` 的差异

| 项目 | 当前代码（`e164df5`） | `kTFRUAddon/`（`6404ab69`） | 处理 |
| --- | --- | --- | --- |
| 9810–9815、1106 | 已注册 | 活注册（StainlessSteel，送出 512 EU） | 保留 |
| 9816 `ResearchTableCompute` | 漏注册 | 活注册（StainlessSteel，送出 512 EU） | 补注册 `research_table_compute` |
| 9817 `ResearchTableLinkGame` | 漏注册 | 活注册（StainlessSteel，送出 512 EU，电流控制） | 补注册 `research_table_current_control` |
| 31056 `GlassPart` | 漏注册 | 活注册（Aluminium，硬度 4.0，抗爆 12.0） | 补声明 `structure_glass` 外壳零件 |
| 30077 `OpticalMicroscope` | 漏注册 | 活注册（Aluminium，EU 128，范围 16–512，容量 16384，每件 256 EU） | 补注册多方块行为机 `optical_microscope` |
| 30078 `ElectronMicroscope` | 漏注册 | 活注册（StainlessSteel，EU 512，范围 16–4096，容量 131072，每件 4096 EU） | 补注册多方块行为机 `electron_microscope` |
| 30042 `ResearchAssembler` | 写成了收 LU 128（64–256）、查 `gt.recipe.assembler` | Aluminium，收 EU 131072（范围 64–524288），配方图 `ResearchAssembler`（槽位 9、6、0、3、3、0、1） | 改成 EU 131072（64–524288）并建专用配方图 |
| 30043 `ResearchReactor` | 漏注册 | StainlessSteel，收 EU 131072（范围 64–524288），配方图 `ResearchReactor`（槽位 9、6、0、6、6、0、1），继承 `ResearchAssembler` 结构 | 补注册 `research_reactor` 与专用配方图 |

### 2. 显微镜结构（来自 `OpticalMicroscope.java` / `ElectronMicroscope.java`）

- **30077 光学显微镜**：轴 Y，层规则 `ABC`，偏移 `0, 0, 0`。`C` 是 GT 18002（`tank_wall`，物品流体能量进），`G` 是 `minecraft:glass`（角色 `CASING`），空格是控制器：
  - `A`: `" C" / "CC"`
  - `B`: `"GG" / "CC"`
  - `C`: `"CC" / "CC"`
- **30078 电子显微镜**：轴 Y，层规则 `ABC`，偏移 `-2, -1, 0`。`C` 是 GT 18002（`tank_wall`），`S` 是 GT 18006（`titanium_wall`），`G` 是 `minecraft:glass`（`CASING`），`B` 层第 3 行首格空格是控制器：
  - `A`: `"CCC" / "CGC" / "CCC" / "CSC" / "CCC"`
  - `B`: `"SSS" / "SCS" / " CC" / " G " / "SSS"`
  - `C`: `" G " / "CGC" / "CCC" / " G " / "CGC"`

---

## 二、卡 16（计算机集群与无线/生物/量子/时空计算）对比与补齐

### 1. 已落代码与 `6404ab69` 的差异

| 项目 | 当前代码（`7c99b3b`） | `kTFRUAddon/`（`6404ab69`） | 处理 |
| --- | --- | --- | --- |
| 1100 `electric_controller`、1102 `wired_controller`、1104 `network_cable` | 已注册 | `tileEntityInit0` 已注释，卡 16 明确「不恢复」 | 移除 |
| 1101 `wireless_controller` | 单方块 | 已迁为 30058–30060 三台多方块无线控制器 | 替换为 30058–30060 |
| 1103 `compute_user`、1105 `dream_brain`、32005、32006 | 已注册 | 活注册 | 保留 |
| 30058–30060 无线控制器（小/大/巨） | 漏注册 | 活注册（StainlessSteel，送出 512 EU，入簇距离 512） | 补注册三台多方块控制器 |
| 30061–30064 生物计算阵列 T1–T4 | 漏注册 | 活注册 | 补注册控制器及外壳 31057–31059、31064、31130–31133 |
| 30065–30068 量子计算阵列 T1–T4 | 漏注册 | 活注册 | 补注册控制器及外壳 31065–31079 |
| 30069–30070 时空计算阵列 T1–T2 | 漏注册 | 活注册 | 补注册控制器及外壳 31080–31090 |
| 30079 量子计算中心 | 漏注册 | 活注册（Trinaquadalloy，7×7×3 `ABA`） | 补注册控制器 |
| 32010–32013 无线计算节点 T1–T4 | 漏注册 | 活注册（Normal 算力 2000 / 20000 / 200000 / 2000000，损耗 0.5 / 0.3 / 0.2 / 0.1） | 补注册 |
| 32040–32043 本地量子计算零件 T1–T4 | 漏注册 | 活注册（Quantum 算力 65536 / 262144 / 1048576 / 16777216，损耗 1023 / 1023 / 511 / 63） | 补注册 |

### 2. 无线、生物、量子、时空控制器参数与结构

| 源 id | 类 | 材料 | 输入算力需求 | 节点数 | 产出算力 | 结构规模与偏移 |
| --- | --- | --- | --- | --- | --- | --- |
| 30058 | `ControllerWireless` | StainlessSteel | — | 2（单层 B） | 汇总无线/本地 | `ABA` 3×3×3，偏移 `-1, 0, 0`，墙 18002 |
| 30059 | `ControllerWirelessLarge` | StainlessSteel | — | 8（单层 B） | 汇总无线/本地 | `ABA` 5×3×5，偏移 `-2, 0, 0`，墙 18002，玻璃 31056 |
| 30060 | `ControllerWirelessHuge` | StainlessSteel | — | 18（单层 N） | 汇总无线/本地 | `ANA` 7×3×7，偏移 `-3, 0, 0`，墙 18002，玻璃 31056 |
| 30061 | `BiologyComputeControllerT1` | StainlessSteel | Normal 262144 | 1 | Biology 65536 | `ABCD` 5×4×3，偏移 `-2, 0, 0`，W=31057，N=31131 |
| 30062 | `BiologyComputeControllerT2` | Chromium | Normal 2097152 | 2 | Biology 524288 | `ABCDEF` 7×6×5，偏移 `-3, 0, 0`，W=31058，N=31132 |
| 30063 | `BiologyComputeControllerT3` | Graphene | Normal 16777216 | 4 | Biology 4194304 | `ABCDEFG` 9×7×5，偏移 `-2, 0, 0`，W=31059，N=31133 |
| 30064 | `BiologyComputeControllerT4` | Trinaquadalloy | Normal 67108864 | 8 | Biology 16777216 | `ABCDEFGH` 11×8×5，偏移 `-2, 0, 0`，W=31130，N=31064 |
| 30065 | `QuantumComputeControllerT1` | Naquadah | Normal 16777216 | 1 | Quantum 65536 | `ABCDE` 7×5×7，偏移 `-3, 0, 0`，31065–31068 |
| 30066 | `QuantumComputeControllerT2` | Nq_522 | Normal 134217728 | 2 | Quantum 262144 | `ABCDEFG` 9×7×9，偏移 `-4, 0, 0`，31069–31071 |
| 30067 | `QuantumComputeControllerT3` | Nq_522 | Normal 1073741824 | 3 | Quantum 1048576 | `ABCDEFGHI` 11×9×11，偏移 `-5, 0, 0`，31072–31075 |
| 30068 | `QuantumComputeControllerT4` | Trinaquadalloy | Normal 8589934592 | 4 | Quantum 16777216 | `ABCDEFGHIJK` 15×11×13，偏移 `-6, 0, 0`，31076–31079 |
| 30069 | `SpacetimeComputeControllerT1` | Nq_522 | Quantum 2097152 | 4 | Spacetime 2000 | `AAABBBCCCDDDEFFFGGGHHHIIIJJJKKLLM` 33×33×33，偏移 `-16, 0, 0`，31080–31084 |
| 30070 | `SpacetimeComputeControllerT2` | Trinaquadalloy | Quantum 67108864 | 12 | Spacetime 200000 | 33×65×33，偏移 `-16, 0, 0`，31085–31090 |
| 30079 | `QuantumComputeCenterController` | Trinaquadalloy | 本地 Quantum | 16 | 汇总 Quantum | `ABA` 7×3×7，偏移 `-3, 0, 0`，墙 18002，玻璃 31056 |

---

## 三、卡 17（无线能量塔）对比与补齐

| 项目 | 当前代码（`2d0008e`） | `kTFRUAddon/`（`6404ab69`） | 处理 |
| --- | --- | --- | --- |
| 30114 `WirelessBatteryInductive` | 只有基类 `wireless_battery`，无距离损耗、无 64 格限制 | Tungstensteel，EU，容量 `367001600000`，输入 2048–8192（16 A），最多 8 链接，同维度距离 `<= 64`，损耗 `clamp(0.05 + 0.25 * min(1, d / 64), 0, 0.95)`，扣库存 `ceil(包 / (1 - 损耗))`，只认非量子接收器 | 改为 `wireless_battery_inductive` 并补齐 64 格距离与损耗公式 |
| 30115 `WirelessBatteryQuantum` | 漏注册 | `Nq_522`，容量 `1468006400000`，输入 1–32768（64 A），最多 32 链接，不限距离、0 损耗、可跨维度，只认量子接收器。结构 5×5×9 `ABCCCDDDD`（墙 18006、通道模块 31113、读出线圈 31114） | 补注册 `wireless_battery_quantum` 及零件 31113、31114 |
| 31117 感应接收器 | `wireless_receiver`（8192 EU × 4 A） | Tungstensteel，8192 EU × 4 A，非量子 | 对应 `wireless_receiver_inductive`，只接受 30114 配对 |
| 31118 量子接收器 | 漏注册 | `Nq_522`，32768 × 16 A，量子接收器 | 补注册 `wireless_receiver_quantum`，只接受 30115 配对 |

---

## 四、卡 18（聚变、催化反应器和后期配方机）对比与补齐

### 1. 已落代码与 `6404ab69` 的差异

| 源 id | 类 | 当前代码（`6d6ce1d`） | `kTFRUAddon/`（`6404ab69`）与补齐处理 |
| --- | --- | --- | --- |
| 30073 | `MirrorGoldSynthesis` | 漏注册 | 补单方块 `RecipeMachine`（Iridium，EU 8192，槽位 5、5、0、2、2、0、1） |
| 30075 | `MirrorGoldQuantumization` | 漏注册 | 补单方块 `RecipeMachine`（`Nq_522`，EU 16384，槽位 5、5、0、2、2、0、1） |
| 30104 | `CosmicRadiationResearch` | 漏注册 | 补单方块 `RecipeMachine`（Tungstensteel，EU 8192，窗口 2048–16384，槽位 1、3、0、0、0、0、1） |
| 30105 | 微重力组装机 | 漏注册 | 补单方块 `RecipeMachine`（Tungstensteel，EU 8192，窗口 2048–16384，保持指向宇宙辐射配方图） |
| 30120 / 30121 | `CatalyticReactor` EV / IV | 漏注册 | 补多方块 `RecipeMachine`（EV: StainlessSteel 2048 EU 256–4096；IV: Tungstensteel 8192 EU 1024–16384，廉价超频，槽位 6、3、0、6、3、0、1）及零件 31200–31203 |
| 30076 | `QuantumGoldSynthesisChamber` | 漏注册 | 补 3×3×3 多方块 `RecipeMachine`（`Nq_522`，EU 65536，槽位 4、4、0、2、2、0、1）及零件 31111–31116 |
| 30080 | `QuantumObservationTower` | 漏注册 | 补 3×3×5 多方块 `RecipeMachine`（Trinaquadalloy，EU 16384，槽位 4、4、0、2、2、0、1） |
| 30103 | `MicrogravityCrystalGrowthMachine` | 漏注册 | 补 3×3×3 多方块 `RecipeMachine`（Titanium，EU 8192，窗口 1024–16384，槽位 1、1、0、2、0、0、1）及零件 31204、31207、31208 |
| 30074 | `QuantumCommunicationRelay` | 漏注册 | 补单方块行为机（Iridium，EU 8192，QU 缓存 32768000，每 tick 送 128 QU，配对传输，配方图 `QuantumCommunication` 1、1、0、0、0、0、1 每条 65536 QU） |
| 30071 / 30072 | `LaserFusionExperimental` / `Industrial` | 漏注册 | 补多方块（30071 钨钢 7×7×7 `0123456` 偏移 `-3,0,-3`；30072 锇 11×11×11 `0123456789A` 偏移 `-5,0,-5`；配方图 `LaserFusion` 3、3、0、3、3、0、1）及零件 31060–31063 |
| 30014 / 30015 | `FusionReactorTokamakExp` / `T1` | 仅单方块配方机，无零件和布局 | 补零件 31015–31019、31024–31028 及两台托卡马克的多方块结构布局 |
| 30102 | `CentrifugalMicrogravityController` | 漏注册 | 补 9×9×9 多方块行为机（Tungstensteel，EU 2048，上限 8192，容量 65536，偏移 `-4,0,-4`，零件 31204–31208，满电 600 tick 激活房间） |
| 30106–30108 | 三台 AI 中心 | 漏注册 | 补 5×5×5 多方块行为机（Tungstensteel，偏移 `-2,0,0`，零件 31091–31095） |

### 2. 卡 18 待读结构补录（来自 `kTFRUAddon/`）

- **30120 / 30121 催化反应器**（`CatalyticReactor.java`）：5×5×5，层规则 `ABCDE`，偏移 `-2, -1, 0`。`C`=31200 外壳，`B`=31201 催化剂床，`E`=31202 能量口（能量进），`I`=31203 进出料口（物品流体），控制器在 B 层正前中央（即 `0, 0, 0` 对应的空位）：
  - `A`: `"CCCCC" / "CCECC" / "CCCCC" / "CCCCC" / "CCCCC"`
  - `B`、`C`、`D`: `"CCCCC" / "C   C" / "C B C" / "C   C" / "CCCCC"`
  - `E`: `"CCCCC" / "C   C" / "C I C" / "C   C" / "CCCCC"`
- **30071 激光聚变实验型**（`LaserFusionExperimental.java`）：7×7×7，层规则 `0123456`，偏移 `-3, 0, -3`。`P`=GT 18002（能量进），`S`=31062 脉冲储存，`L`=31060 聚焦阵列（能量进），`I`=31061 靶注入（物品进），`C`=31063 产物收集（物品流体出），`A`=空气。
- **30072 激光聚变工业型**（`LaserFusionIndustrial.java`）：11×11×11，层规则 `0123456789A`，偏移 `-5, 0, -5`。零件映射同 30071（`P`=GT 18002 外壳）。
- **30102 离心微重力控制器**（`CentrifugalMicrogravityController.java`）：9×9×9，层规则 `ABBBBBBBC`，偏移 `-4, 0, -4`。`W`=31204 屏蔽墙，`R`=31205 质量环，`A`=31206 陀螺锚，`H`=31207 检修口，`P`=31208 能量口，`C`=控制器（底层中心）：
  - `A`: `"AWRWPWRWA" / "WWWWWWWWW" ×3 / "WWWWCWWWW" / "WWWWWWWWW" ×3 / "AWRWHWRWA"`
  - `B`: `"WWWWWWWWW" / "W       W" ×7 / "WWWWWWWWW"`
  - `C`: `"AWRWWWRWA" / "WWWWWWWWW" ×7 / "AWRWWWRWA"`
- **30106–30108 三台 AI 中心**：5×5×5，层规则 `ABCDE`，偏移 `-2, 0, 0`。`W`=31091 框架，`M`=31092 内存，`R` 分别为 31093（30106 流控）/ 31094（30107 装箱）/ 31095（30108 识别）。底层 `A` 为 `"WWWWW" / "WWWWW" / " WWWW" / "WWWWW" / "WWWWW"`（控制器在空位），顶层 `E` 全是 `W`。

---

## 步骤

- [x] 恢复卡 15–19 的原卡文本，对照本仓库 `kTFRUAddon/`（`6404ab69`）列出全部差异与结构。
- [x] 修补卡 15：补 9816、9817、31056、30077、30078、30043，修正 30042 的能量与配方图。
- [x] 修补卡 16：移除已注释的 1100/1102/1104 与旧 1101，补齐 30058–30070、30079、32010–32043 及对应外壳。
- [x] 修补卡 17：拆出 30114/30115 与 31117/31118，补齐 31113/31114、距离损耗和量子枢纽结构。
- [x] 修补卡 18：补齐 30071–30076、30080、30102–30108、30120–30121、托卡马克零件与多方块布局。

## 验收

- [x] 编译：`compileJava --offline` 通过。
- [ ] 开发客户端
- [ ] 玩法：卡 15–18 补齐后的机器各自按原卡验收项跑通

## 日志

- 2026-10-22：在已有提交上补齐。研究组装机和研究反应釜是大型配方机，EU 131072，包 64–524288。9816 和 9817 只送出 512 EU。显微镜按每件 EU 消耗物品。1100、1102、1104 和旧的单方块无线控制器已去掉。无线、生物、量子、时空控制器和节点已注册，可扩展层固定为一层。感应塔有 64 格距离损耗，量子枢纽可跨维度。托卡马克、激光聚变、催化反应釜、量子金、观测塔、晶体生长、微重力房间和三台 AI 中心都有结构。30074 落成 EU 配方机。接不住的部分写在卡 05。没有回退 `e164df5` 到 `59d50a4`。
