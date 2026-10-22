# 22 自有前缀、产线配方与机器贴图

- 状态：closed
- 依赖：21
- 文件范围：
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruMaterials.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruItems.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruSourceRecipes.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruLaserFusion.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruAddon.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruMachines.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruLateMachines.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruResearchMachines.java`
  - `tools/extract_machine_recipes.py`
  - `tools/gen_card22_items.py`
  - `tools/port_block_textures.py`
  - `src/main/resources/assets/gt6ktfruaddon/`
- 平台：NeoForge 1.21.1

## 背景

`prefixList` 的 10 个前缀、`fluidPreInit` 的全部化工与石化流体、`SiliconeRubber` / `SBR` / `PEEK` 的板与箔、产线用到的 `ItemList` 物品，以及卡 08–20 机器与物品的贴图、全部 28 张配方图的源行，集中在这张卡里收口。

## 范围与设计

### 1. 前缀、板箔形态与流体

- `Gt6KtfruMaterials.prefixes()` 登记飞轮、大型涡轮叶片、三态燃气涡轮、三态蒸汽涡轮，以及商业纯、分析纯、绝对纯粉。贴图来自源 SHINY 套，放在 `textures/item/material_icons/`。
- `Gt6KtfruMaterials.forms()` 声明 `SiliconeRubber`、`SBR`、`PEEK` 的 `plate` 与 `foil`。
- `Gt6KtfruMaterials.materials()` 补齐 `fluidPreInit.java` 里所有自有材料的流体态（光刻胶、四氟乙烯、硅烷、一甲胺等）与独立 `flList` 流体（苯酚、冰醋酸、石脑油、裂解油气、铂族处理液、甲基吡咯烷酮、硫酸钡等）。

### 2. 物品与全配方图提取

- `ItemList` 普通产线物品共登记 504 个（含 `IntelligentCore`），其中 374 个直接从 `kTFRUAddon/src/main/resources/assets/ktfruaddon/textures/items/<group>/<meta>.png` 拷入原版贴图。
- `tools/extract_machine_recipes.py` 扩展到全部 28 张配方图和 18 个源配方文件（含 `CatalyticChemistry.java`、`QuantumObservation.java`、`listener/recipePrefixItems.java` 与 `tileEntityInit0.java`），支持：
  - `ST.tag(0..24)` 编程电路
  - 数量 0 的催化剂（写入 `keptInputs`）
  - 概率数组（按输出槽数截取并把 0 归一为 10000）
  - `.setSpecialNumber(...)` 特殊值（用于激光聚变脉冲与托卡马克）
  - `aMat` 变量与 `kRegistry0.getItem(...)` 机器/结构方块物品映射
  - `PurityRefining` 的商业纯、分析纯与 34 种绝对纯激光聚变展开，以及 `recipePrefixItems` 的蒸汽/燃气涡轮探伤展开
- 重新生成后共落账 **483 行**源配方：
  - 热混合 `HeatMixer`：150 行
  - 电路组装 `Assembler`：76 行
  - 激光惯性聚变 `LaserFusion` / `LaserFusionExperimental`：42 行
  - 三轴 `CNC`：35 行
  - 激光切割 `LaserCutter`：27 行
  - 掩膜对准 `MaskAligner`：24 行
  - 探伤仪 `FlawDetector`：24 行
  - 晶圆测试 `WaferTester`：24 行
  - 催化反应器 `CatalyticReactor`：17 行
  - 蒸馏塔 `DistillTower` / `SmallDistillTower` / `TinyDistillTower`：16 / 13 / 5 行（另含 `Gt6KtfruRecipes` 的 3 行海水蒸馏）
  - 晶圆涂胶 `WaferCoater`：4 行
  - 研究组装 `ResearchAssembler`：4 行
  - 托卡马克 `FusionTokamak` / `FusionTokamakExperimental`：4 行（另含 `Gt6KtfruRecipes` 的 1 行氘氚聚变）
  - 流体加热 `FluidHeating`：4 行
  - 超声混合 `UltrasonicMixer`：3 行
  - 微重力晶体生长 `MicrogravityCrystalGrowth`：3 行
  - 镜金合成 `MirrorGoldSynthesis`：2 行
  - 量子观测 `QuantumObservation`：2 行
  - 宇宙辐射 `CosmicRadiationResearch`：1 行
  - 量金合成 `QuantumGoldSynthesis`：1 行
  - 量子通讯 `QuantumCommunication`：1 行
  - 研究反应釜 `ResearchReactor`：1 行

### 3. 机器贴图与配方 GUI

- 卡 08–20 的全部 **196 台**机器和零件方块都换上对应的 kTFRU 或 GT6 复合染色贴图模型，移除了 `sun_heater_placeholder.json`，不再有任何机器使用 `white_concrete` 占位。
- 配方界面补齐 `fuelbattery.png`、`maskaligner.png`、`cnc.png`、`fueldeburner.png`、`fusiontokamak.png`、`researchassembler.png`、`mixer.png`、`fusion.png` 与 `gascompressor.png`。

## 步骤

- [x] 登记 10 个前缀并拷入 SHINY 贴图。
- [x] 补齐 `SiliconeRubber` / `SBR` / `PEEK` 板箔与 `fluidPreInit` 流体。
- [x] 登记 504 个 `ItemList` 产线物品并拷入 374 张物品贴图。
- [x] 扩展提取器到全部 28 张配方图与 18 个源文件，落账 483 行源配方。
- [x] 补齐卡 08–20 全部 196 台机器贴图与配方 GUI，清零白方块占位。

## 验收

- [x] 编译：`compileJava --offline` 通过。
- [ ] 开发客户端
- [ ] 玩法：纯度提纯、掩膜光刻、催化反应器、激光聚变脉冲与机器外观

## 日志

- 2026-10-22：登记 10 个前缀、6 项板箔请求、`fluidPreInit` 流体与 504 个自有物品（374 张原图）。提取器扩至全部 28 张配方图与 18 个源文件，共落账 483 行配方。卡 08–20 全部 196 台机器贴图对正，移除全部白混凝土占位。
- 2026-10-23：提取器补上 GT6CE 本体物品解析，483 行涨到 639 行。新增解析 `IL.*` 元件、`casingMachine` 四档、`wireGtNN`、`pipe*`、`wireFine`、GT6 数字电线/轴件 ID、细导线和局部变量。剩余 59 次调用，明细在卡 06 与 `build/source-recipe-report.txt`。物品贴图缺口（130 个纸模型）不在这次范围内。
