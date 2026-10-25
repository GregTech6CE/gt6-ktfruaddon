# kTFRUAddon：只依赖 GT6 的表面

从 CrucibleCraft `docs/current/ktfruaddon-gt6-surface.md` 拷来。

对照 [kuzuanpa/kTFRUAddon](https://github.com/kuzuanpa/kTFRUAddon)
`6404ab6979de9fce832ce0c41171000decf00e69`。本仓库运行语义以固定 GT6CE `1d1f69fbb` 的公开 SDK 和附属实现为准；上游是行为与数据的对照来源。这份清单回答：addon 里哪些东西是 **GregTech 6 风格**、
运行时 **不绑 Advanced Rocketry / TerraFirmaCraft**（以及同类整合模组）。

这不是 unique-active，也不是移植队列。增减公共 16 前缀、开形态、写配方卡，
都不从本页自动开工。

## 怎么判

kTFRUAddon 的 README 写明：用来 **ADD**；MODIFY/DELETE 走 GT6-TFRU；希望
TFRU 包外也能跑一部分。`build.gradle` 仍编译一整包 TFRU 模组
（gregtech6-TFRU、AdvancedRocketry-TFRU、TerraFirmaCraft-TFRU、LibVulpes-TFRU、
Botania-TFRU、ForestryMC-TFRU、TFCTech-TFRU、Draconic-Evolution-TFRU，外加
AE2 / IC2 / Galacticraft / BuildCraft 等）。**编译依赖 ≠ 运行时逻辑依赖。**

运行时边界看 Java import 和 `Loader.isModLoaded` / `EnvironmentHelper` /
`MD.*`，不看 gradle：

| 档 | 含义 | 进不进本页「GT6 表面」 |
| --- | --- | --- |
| A | 只碰 `gregapi` / `gregtech` / 原版 / Forge / kTFRUAddon 自己 | 进 |
| B | 多方块还 import LibVulpes `ItemProjector` / `BlockMeta`（TFRU 全息投影仪） | 进（本仓库用 GT6CE 结构布局代替全息；机器 tick 仍是 GT） |
| C | 运行时 import 或硬门 TFC / AR 小行星 / Botania rune / AE 电路 | **不进** |
| D | 配方里偶发 `MD.AE` / `FL.Coolant_IC2` / TFRU 金砖 generify | 主体进，那几条配方单独标 |

`isGregtechTFRU` 只挡住 **TFRU 叉上的额外柴油机注册**，机器类仍是 GT6
`MultiTileEntityMotorLiquid`。那批算 A，但身份是 addon DESIGN_POLICY，不是
vanilla GT6 已有的 1300 段柴油。

## GT6 表面（A + B）

注册入口：`tile/tileEntityInit0.java`（`ktfru.multitileentity`）。配方入口：
`recipe/recipeInit.java`（TFC 那一支被 `Loader.isModLoaded("terrafirmacraft")`
挡住，见下节）。

### 连接件

- Teflon 流体管、PVC 物品管（registry 0 / 26 一带）：gregapi
  `MultiTileEntityPipeFluid` / `PipeItem` 的额外材料档。

### 单方块能源（10000–15050）

| ID | 内容 | 说明 |
| --- | --- | --- |
| 10000–10006 | Small Gas Turbine ×7 | `MultiTileEntityGasMotor`（4 行 MotorLiquid）；`FM.Gas` → RU；效率 3500。材料：青铜 / 钢 / 因瓦 / 钛 / 钨钢 / **铱** / 铬。**不是** GT6 大型燃气涡轮 17231–17234。本仓库已在卡 03 注册这 7 台单方块；CrucibleCraft 的不移植结论不适用于本仓库。 |
| 10010–10011 | Gas Battery | `FuelBattery` + `recipeMaps.FuelBattery`；EU；要质子交换膜等 addon 零件。 |
| 10021–10027 | Manual Motor | 手摇 RU / KU。 |
| 10050–10054 | Compressed Gas Drum | 钢 / 不锈钢 / 下界合金 / 钨钢为 GT 材料。Manasteel 那档用 `MT.Manasteel`（GT 表里的植物魔法材料名，无 Botania 时可能空）。 |
| 10100–10108 | Diesel Engine | `if (isGregtechTFRU)` 才注册；类是 GT6 `MotorLiquid` + `FM.Engine`；曲轴/气缸来自 addon `ItemList`。 |
| 10150–10160 | Turbocharging Diesel | 同上，另加涡轮零件。 |
| 10200–10255 | Transformer / Flywheel Gear / Light Transform 部件 | 多方块零件，EU / RU / LU。 |
| 15000–15055 | Flywheel Box 六档 × 机械/大/电/真空 | `FlywheelBox` / `FlywheelBoxElec`；RU 储能。前缀物品 `flywheel`。 |

9800 Water Mill 注释写「Early TFC Stage」，类本身只看原版水方块 + gregapi，
**没有** TFC import。主题是早期木结构，不是 TFC API。

### 单方块加工机（20000–27553）

GT6 `MultiTileEntityBasicMachine` / `BasicMachineElectric` 壳 + addon RecipeMap。

| 段 | 机器 | 能 |
| --- | --- | --- |
| 20000 | Fluid Heater ×4 | HU，`FluidHeating` |
| 21000 | Light Mixer ×4 | RU |
| 22000 | Gas Compressor ×5 | KU，写入压缩气罐 |
| 23000 | Circuit Assembler ×5 | LU，`Assembler` |
| 23010 | Laser Cutter ×5 | LU |
| 27000 | Heat Mixer ×5 | EU |
| 27030 | Electronics Designer ×5 | EU，`EDA` |
| 27040 | Flaw Detector ×5 | EU；Co-60 芯 |
| 27050 | Electric Light Mixer ×5 | EU |
| 27500 | Wafer Tester ×5 | EU |
| 27510 | Wafer Coater ×5 | EU |
| 27520 | Miner ×4 | EU/RF 范围矿机（不是 AR 小行星） |
| 27542 | Ultrasonic Mixer ×3 | EU |
| 27550 | Void Hopper ×4 | EU/RF |

CVD / Ultra Clean Bath 在源码里整段注释掉，不算 live。

### 多方块（30003+，零件 31000+）

多数控制器 import `zmaster587.libVulpes.items.ItemProjector`，只为结构全息
（档 B）。tick / 配方仍走 gregapi。

**半导体 / 精密加工**

- Mask Aligner UV / UV+ / DUV / EUV（30003, 30009–30011）+ Light Module / IO Manager / Platform
- 3-axle CNC（30012）+ CNC 平台 / 电机 / 刀头
- Research Assembler（30042，测试名）

**蒸馏 / 油 / 热**

- Tiny / Small / Distillation Tower（30004–30006）
- Oil Miner（30013）+ 钻头零件
- Sun Heater（30007）+ 镜 / 吸热器 / 热管
- Mantle Heater（30028）
- Fuel Deburn Factory（30027）

**大型涡轮 / 电机 / 锅炉（GT6 LargeTurbine 家族的 addon 档）**

vanilla GT6 已有大型蒸汽/燃气涡轮。kTFRUAddon 再加更高材料壳：

- Gas Turbine Housing：Magnalium / Trinitanium / Graphene / Vibramantium / Trinaquadalloy（30016–30019, 30052）；`FM.Gas`，TU→RU
- Steam Turbine Housing：同材料（30020–30023, 30053）
- Dynamo Housing：不锈钢 / 钛 / 钨钢 / 金刚 / Trinaquadalloy（30037–30040, 30054）；RU→EU
- Expanded Motor Controller ×6（30046–30051）；`FM.Engine`→RU
- TEST Separate-Excite Dynamo / Fluid Boiler / Large Sun Mirror（30055–30057）

大涡轮前缀：`turbineLargeGas` / `turbineLargeSteam` 及 checked/damaged。

**储能 / 变换 / 气罐**

- Liquid Battery ×3（30024–30026）
- Transformattery EU / RU / LU（30029–30031）
- Electromagnet Crucible Controller（30032）——坩埚控制是 GT 坩埚 API；TFC 热接口在 **另一个** `CrucibleModel` 类上，见划掉节
- Tidal-Wave Generator（30033）
- Ore Processing System（30041）
- Large Compressed Gas Tank 阀（30100–30101）
- 墙 / 线圈 / 导体等 CommonPart（31000+）

**核（9900 段）**

- Co-60 breeder / product rod：GT6 `MultiTileEntityReactorRodBreeder` / `Product`
- Neutron Liquid Breeder attachment
- 铀/锂/硅岩棒在源码里注释掉

**计算机集群 / 研究桌（1100、9810）**

- Electric / Wireless / Wired Controller、Network Cable、Test User
- Research monitor / tables / databases
- 计算节点零件

研究树 **解锁表** 会点到 AR 物品，见划掉节。控制器方块本身是 GT 多方块。

### 6404ab69 的后期系统与本仓库落点

旧版清单只列“研究桌/计算节点”，不能据此认为以下系统仍待分档。对照固定上游注册与卡 15–23，当前归类如下；源业务中指向外部模组的研究任务与 GUI 不随控制器一起搬入。

| 源内容 | 档与范围 | 当前落点 |
| --- | --- | --- |
| 研究监视器、显微镜、研究组装机/反应釜（9818、30042/30043、30077/30078） | 控制器 A/B；含外部物品的任务逐条排除 | research；卡 15、21，自有解锁表 |
| 计算集群、无线计算、时空/量子/生物计算节点（30058–30070、32010–32043） | A/B；GT 电线寻路和外部计算物品不搬 | network；卡 16、20、21，自有拓扑 |
| 微重力房间（30102/30103/30105） | A/B；局部业务索引，未声称改世界重力规则 | research；卡 21 |
| AI 中心（30106–30108） | A/B；算力分配，研究小游戏 UI 不搬 | research；卡 18、21 |
| 激光聚变（30071/30072） | A/B；脉冲池与冷却由附属实现 | fusion；卡 18、21 |
| 量子中继与量子枢纽（30074、30115） | A/B；自有配对/收件箱 | quantum、network；卡 17、21、23 |
| 镜金/量子金及对应产线 | A；已解析配方，未解析行单列账本 | 材料与生成配方；卡 22、23 |

当前内容边界与原行为差异见 [唯一账本](card-plans/prep/05-api-gaps.md)，玩法状态见 [未验收清单](unverified-gameplay.md)。

### 材料、前缀、配方图

`api/material/materialPreInit.java`：铬/铝中间体、醇、醛、油渣、环氧等
（约 22000+ 材料号）。`prefixList`：飞轮、大涡轮叶片、三级纯尘。

`api/recipe/recipeMaps.java` 里与 GT 线对应、且配方 Java **没有** AR/TFC import 的：

LightMixer、HeatMixer、UltrasonicMixer、Assembler、LaserCutter、MaskAligner、
EDA、FlawDetector、WaferCoater、WaferTester、Ionizer、三级 DistillTower、CVD、
FuelBattery、FluidHeating、CNC、FuelDeburner、RTG、OreProcessSystem、
NeutronAbsorption、FusionTokamak、QuantumPetrochem / MoleculeOperator。

`recipeInit` 里对应的配方类（HeatMixer、ParticleCollider、OreProcessing、
Chemistry、Circuits、ComputerBuilding、OilProcessing、Plastic、CompactItem
的发动机零件、Fusion、PlatinumGroupProcess、FakeRecipe）主体是 gregapi `RM.*`
+ 上面这些图。例外：

- `ComputerBuilding` 有几条 EDA 光罩用 `MD.AE` 电路（档 D）
- `Chemistry` 有 `FL.Coolant_IC2` 别名加热（档 D；GT6 流体名，不是 IC2 类 import）
- `CompactItem` 里 TFC 金 → GT 金的 `RM.generify`（档 C，两条）
- `RocketBuilding` 整文件 AR + IronChest（档 C）

## 明确划掉（C）

这些不是「GT6 表面」。不要从本页当成可移植 GT 内容。

**TFC**

- `TFCPresser`（9801）：`com.bioxx.tfc` 砧 / `AnvilReq`
- `TFCRecipe.init()`：仅 `terrafirmacraft` 加载时跑；粘土模具、窑、HeatRegistry
- `CrucibleModel`：`@Optional.Interface` TFC `IHeatAccepter` / `IHeater`
- `OreScanner` API：读 `TEOre` / `TFCBlocks`
- `CompactItem` TFC 金砖/金锭 generify

**Advanced Rocketry / LibVulpes 空间内容**（全息投影仪不算这里）

- `AsteroidFinder` / `AsteroidMiner`（30034–30035）：`advancedRocketry.util.AsteroidSmall` 等
- `RocketBuilder`（30036）+ `recipeMaps.RocketAssembler` + `RocketBuilding.java`（燃料罐、火箭电机、IronChest）
- `ResearchTrees.java`：import `AdvancedRocketryItems`
- Dyson Sphere monitor GUI：AR 恒星 / Dyson API
- `kTFRUAddonARProjectorCompact`：LibVulpes dummy multiblock 注册
- `IComputerItem` 默认表里的 `MD.GC_ADV_ROCKETRY` `circuitIC`

**Botania**

- Ore Scanner（27530–27533）：`if (isBotaniaTFRU)` + `LibOreDict.RUNE`
- `TileOreScanner`：`IManaReceiver`
- `FxRenderBlockOutline`：`vazkii.botania.common.Botania`

**包环境本身**

- `EnvironmentHelper`：探测 TFRU 叉、拉 Modrinth 更新、校验 config/docs/mods SHA1。
  这是整合包脚手架，不是 GT 机器。

## 和 vanilla GT6 的关系

| 东西 | 身份 |
| --- | --- |
| `FM.Gas` / `FM.Engine` 燃料行、MotorLiquid 排气丢第二槽 | GT6 源行为（SOURCE_BACKED） |
| 小型燃气涡轮 7 档、飞轮箱、半导体线、addon 蒸馏塔、扩展柴油/涡轮壳 | kTFRUAddon 自有内容；本仓库按 AGPL 在 GT6CE 适配 |
| GT6 大型燃气涡轮 17231–17234 | GT6 本体内容，与附属小型涡轮分开 |

本页是来源范围清单；本仓库实施状态以阶段卡和唯一账本为准。

## 源码入口

| 读 | 路径（相对于 kTFRUAddon 检出） |
| --- | --- |
| README 意图 | `README.md` |
| 注册 | `src/main/java/cn/kuzuanpa/ktfruaddon/tile/tileEntityInit0.java` |
| 配方门 | `src/main/java/cn/kuzuanpa/ktfruaddon/recipe/recipeInit.java` |
| RecipeMap | `src/main/java/cn/kuzuanpa/ktfruaddon/api/recipe/recipeMaps.java` |
| 材料 | `src/main/java/cn/kuzuanpa/ktfruaddon/api/material/materialPreInit.java` |
| 环境门 | `src/main/java/cn/kuzuanpa/ktfruaddon/EnvironmentHelper.java` |
