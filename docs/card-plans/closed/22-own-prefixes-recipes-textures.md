# 22 自有前缀、产线配方与机器贴图

- 状态：closed
- 依赖：21
- 文件范围：
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruMaterials.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruItems.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruSourceRecipes.java`
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

`prefixList` 的 10 个前缀、产线用到的 `ItemList` 物品，以及卡 08–20 里仍是白方块的机器贴图，都留在源码 `6404ab69` 里。十张单方块配方图里，编程电路、数量为 0 的催化剂、激光概率和纯度粉循环也可以落到现有的 `AddonRecipes.Line` 上。

## 范围与设计

### 1. 前缀

`Gt6KtfruMaterials.prefixes()` 登记飞轮、大型涡轮叶片、三态燃气涡轮、三态蒸汽涡轮，以及商业纯、分析纯、绝对纯粉。飞轮、叶片和六种涡轮沿用工具且可锻造的筛选。纯度粉列出提纯家族里的材料，条件集为空。涡轮源条件里的熔融、金属或合金、以及三种具名排除，记在卡 05。

贴图来自源 SHINY 套，放在 `textures/item/material_icons/`。GT 按前缀声明生成染色模型。

### 2. 物品与配方

`ItemList` 里普通产线物品登记为 496 个新物品，加上原有的 3 张膜和 4 根电池极，共 503 个。TFC、Advanced Rocketry、Applied Energistics 和 Twilight Forest 的条目不登记。源物品贴图按数字 meta 存放，模型暂时用纸张外观。

提取器现在认识：

- `ST.tag(0..24)`，消耗的编程电路
- `ItemList` 与 `OP` 数量 0，堆叠数保持 1，并写入 `keptInputs`
- `long[]` 概率，源里的 0 和 10000 视为必定产出；数组比物品输出更长的 5 行继续跳过
- `prefixList` 的 10 个字段
- `PurityRefining` 里两处带 `material` 变量的热混合：钨和钼的商业纯，以及阀金属、贵金属（含智金）、难熔金属、半导体的分析纯

重新生成后十张图的落账行数：流体加热 4，光混合仍在 `Gt6KtfruRecipes`，组装 59，激光切割 22，热混合 88，晶圆测试 24。电子设计、探伤、涂布和超声混合这四张图没有新的可落账行。合计 197 行。光混合、硫、水和石墨烯三处原有断言保持通过。

仍跳过的行包括 GT 元件、机器外壳、没有对应形态的导线、缺失流体、数量为 0 的流体、C 档，以及监听器 `recipePrefixItems` 里的飞轮和涡轮加工。那份监听器不在这十张图的扫描范围内。

### 3. 机器贴图与配方 GUI

130 台有源贴图的机器换成了 kTFRU 原图，包括飞轮、齿轮、变压器、掩膜、CNC、太阳炉、潮汐、油矿、地幔加热、脱燃、大型涡轮、扩展马达、流体锅炉、气体电池、压缩气罐、手摇马达、矿机、水车、研究监视器、研究组装、计算集群零件、无线接收器和托卡马克。白方块模型改名为 `sun_heater_placeholder`，没有源贴图的机器继续指向它：发电机、虚空漏斗、气体压缩机、激光聚变、除监视器以外的研究台、生物/量子/时空阵列、显微镜、AI 中心和无线算力控制器。卡 02–04 已经有自己模型的单方块机器没有改。

配方界面补上了燃料电池、掩膜、CNC、脱燃、托卡马克和研究组装。研究组装的文件名是 `researchassembler.png`，避免 EMI 把名为 assembler 的界面改到默认图。仍用气体压缩机界面的配方图，另有一份 `textures/gui/recipes/gascompressor.png`。行为机器的界面继续用 `textures/gui/machines/gascompressor.png`。

## 步骤

- [x] 登记 10 个前缀并拷入 SHINY 贴图。
- [x] 登记产线用到的 `ItemList` 物品，创造栏改走 `BY_PATH`。
- [x] 扩展配方提取器，重新生成 197 行，并保留硫、水、石墨烯断言。
- [x] 给有源文件的卡 08–20 机器换贴图，并补上缺失的配方 GUI。
- [x] 启动时检查铁飞轮、铁的商业纯粉和镍催化剂簇。

## 验收

- [x] 编译：`compileJava --offline` 通过。
- [ ] 开发客户端
- [ ] 玩法：纯度热混合、激光切割概率、催化剂保留，以及换过贴图的机器外观

## 日志

- 2026-10-22：前缀 10 个，自有物品 503 个，十张配方图落账 197 行（组装 59、激光切割 22、热混合 88、晶圆测试 24、流体加热 4）。130 台机器换上源贴图。概率数组长于物品输出的 5 行，以及监听器里的飞轮和涡轮加工，仍留在跳过清单。
