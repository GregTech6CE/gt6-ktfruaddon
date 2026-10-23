# 23 按 1d1f69fbb 补齐 API 缺口

- 状态：active
- 依赖：无
- 文件范围：
  - `libs/gt6ce/` 的两个包和基线文件
  - `Gt6KtfruBatteries.java`、`Gt6KtfruBatteryLayouts.java`
  - `Gt6KtfruCrucibleLayouts.java`
  - `Gt6KtfruHeatLayouts.java`
  - `Gt6KtfruLateMachines.java`
  - `Gt6KtfruLayerLayout.java`
  - `Gt6KtfruLiquidBattery.java`
  - `Gt6KtfruMaterials.java`
  - `Gt6KtfruRecipes.java`
  - `Gt6KtfruSourceRecipes.java`（重新生成）
  - `Gt6KtfruWirelessMachines.java`
  - `Gt6KtfruGeneratedLayouts.java`（重新生成）
  - `tools/extract_machine_recipes.py`、`tools/gen_card20_layouts.py`
  - 本卡和卡 05、`GAP.md`、卡表
- 平台：NeoForge 1.21.1

## 背景

GT6CE 在 `addon/remaining-api-gaps` 上新增 `1d1f69fbb feat: close the remaining addon API gaps`，正好覆盖卡 05 记下的十条缺口：原零件名解析、一格多选、更高的重复段、配方机第二能量和启动能量、0 mB 流体催化剂、熔融/金属/合金前缀条件和按名排除、酸/等离子/魔流体判断、行为机结构端口、按首包锁定能量类型。

本卡只做「SDK 和运行包已经带上入口，附属这边把它用起来」的部分。C 档、继承 GT 内部方块实体、研究小游戏和 GT 电线连通仍不做。

## 基线

- SDK：`libs/gt6ce/gt6ce-addon-sdk-1.21.1-neoforge-0.0.0.jar`，按 `1d1f69fbb` 重打，185,356 字节。
- 运行包：`libs/gt6ce/gt6ce-1.21.1-neoforge.jar`，按 `1d1f69fbb` 重打，sha256 `b82e423c0c47911b6c6b2233cd7e38828692e4e20c4ce538f54b7b9b2a263e93`。
- `build-commit.txt` 已改为 `1d1f69fbb267ad31548b1fcb1ec1168435b67bf3`。

## 变更

### 1. 原零件名（`idForName`）

`SharedLargeMachineParts.idForName` 现在先查 `OriginalMultiblockPartData.byPath`。布局直接用原名：

- 钨钢流体电池 30026 的壳：`tungstensteel_wall` → `implosion_compressor_wall`（18023）。
- EU 变换电池 30029 的线圈：`large_copper_coil` → `large_niobium_titanium_coil`（18041）。
- 感应无线能量塔 30114 的 `O` 格：`large_copper_coil` → `large_niobium_titanium_coil`（18041）。
- 电磁坩埚的 `C` 格：`large_copper_coil` → `large_niobium_titanium_coil`（18041）。

`tools/gen_card20_layouts.py` 的 `KNOWN[18041]` 同步改成正确的名字，避免重跑又写回铜线圈。

### 2. 一格多选（`CellMatch.oneOf`）

- 流体电池池壁：青铜、钢、塑料三种 `liquid_battery_wall_*` 写成一格三选一。
- 无线计算控制器、大型、巨型、量子计算中心：计算格 `N` 接受计算节点方块或 `tank_wall`（18002 的 `ComputePartPredicate(WALL)` 回退）。
- 托卡马克实验型和 T1：`H` 格接受铝制无线计算节点或 `tank_wall`（源 `ComputePartPredicate(18002)`）。

`Gt6KtfruLayerLayout.oneOf(...)` 是新增的便捷写法；生成器现在会把带回退的 `ComputePartPredicate` 写成 `oneOf`。

### 3. 更高的重复段

- `LayoutRepeat` 的上限从 256 提到 1024，运行时的无界重复以世界高度和 1024 中较小的数为限。
- 地幔加热在固定钻头格下面新增 `drill_rod` 重复段，向下（`step(-1)`、`max=-1`）一直铺到世界底部。源在旧世界是打到 y=1；现代世界由世界高度截断。
- 固定钻头格仍在 `(0,-1,2)`，重复从 `-2` 开始，避免和固定格重叠。

### 4. 配方机第二能量与启动能量

- 托卡马克 30014/30015 用 `auxiliaryEnergy` 声明 LU，`anyPacket()`，缓存 4,194,304 LU，控制器六面接收；`startupEnergy(true)`。
- 聚变配方重新生成后带上源特殊值：氘氚 1,048,576 LU，氘-氦三 4,194,304 LU；手写的 2048 EU/t 氘氚行也补上 1,048,576 LU。
- 启动能量从辅助缓存扣，扣满才推进进度；普通 EU 主缓存不变。

### 5. 0 mB 流体催化剂

`tools/extract_machine_recipes.py`：

- 数量 0 的流体输入不再整行跳过，写成 `field(name, 1)` 并记进 `keptFluidInputs`。GT6CE 允许 0 mB，但 NeoForge 的 `FluidStack.getFluid()` 在数量 ≤ 0 时返回 `Fluids.EMPTY`，`AddonRecipes.Line` 会把它当成空栈拒绝；1 mB + kept 是当前能工作的写法。
- 生成器接受 `1024L * 1024L` 这类 Java long 后缀，`setSpecialNumber` 不再丢成 0。
- 输入校验只要求流体类型存在；输出仍拒绝空栈。

重新生成后：

- 热混合补回 11 行源催化剂（`OreProcessing.java:106/107`、`Plastic.java:66/87/101/117/131/139/141/155`、`PlatinumGroupProcess.java:41`）。
- 气体电池 16 行补回 1 mB 电解液催化剂（镍/CaTiO3 用氧化钇锆，碳/铂用硫酸），行为里的电极对检查保留，配方行本身也能区分两种电解液。
- 源配方从 639 行涨到 652 行。

### 6. 自有材料形态

`SiliconeRubber`、`SBR` 和 `PEEK` 声明了 `plate` / `foil` 的 `MaterialFormRequest`，但材料本身没有 `polymer()`，启动时报 `form 'plate / SiliconeRubber' is not valid for that material`。三个声明都补上 `.polymer()`。

### 7. 前缀条件

大型涡轮叶片和六种涡轮从飞轮的工具属性+可锻造条件改为源的 `conditionLargeTurbine`：

- `HAS_TOOL_STATS`、`MOLTEN`、`METAL`（GT 的合金都同时带 `METAL`，所以等价于源的「金属或合金」）。
- 按名排除 `AnnealedCopper`、`WroughtIron`、`Alumite`。

飞轮仍用工具属性+可锻造；纯度粉仍用显式名单，因为源条件是纯负条件，`PrefixDeclaration` 的空材料列表不接受。

### 8. 酸、等离子和魔流体

- `Gt6KtfruLiquidBattery` 把危险流体分成两类：气体或等离子直接倒掉；酸、魔流体或温度高于 573 的流体拒收并倒掉。
- `AddonFluids.plasma` 作为源 `FL.powerconducting` 的近似入口；GT6CE 没有公开 `powerconducting`。
- 行为接口没有爆炸，源里的「酸/魔/高温就爆炸」暂时退化成拒收和清空，不破坏周围方块。

### 9. 行为机结构端口

`BehaviorMachineBlockEntity` 现在实现 `MultiblockPortOwner`，结构墙上带 `ITEM_*`、`FLUID_*`、`ENERGY_*` 角色的零件会把物品、流体和能量转发到控制器的槽和罐。附属这边已有的角色不需要重写：

- 研究组装机/反应釜的 `steel_wall` 是 `ITEM_FLUID_ENERGY`。
- 激光聚变的 `P`/`I`/`L`/`C` 已是能量、物品和流体角色。
- 微重力晶体生长的 `I`/`O`/`E` 已是物品和能量角色。
- 脱燃厂的 `W`/`F`/`G` 已是输入/输出角色。

### 10. 行为机 GUI 重叠

SDK 的 `BehaviorMachine.Builder` 会检查能量条、进度条、槽、罐和开关的矩形不能重叠。激光聚变的能量条在默认 `(8,8)`，和 `item(8,26)`、`fluid(...,46,8)` 重叠；研究组装机/反应釜的能量条和 `fluid(...,16,60)` 擦边。激光聚变把能量条移到 `(84,8)`、进度条移到 `(84,64)`；研究组装机/反应釜把能量条移到 `(6,8)`。

### 11. 布局名

`StructureLayout` 的布局名只接受 `[a-z0-9_]+`。脱燃厂的布局原来用配方图常量 `gt6ktfruaddon.recipe.fuel_deburner`，启动时报 `Invalid layout name`；改成机器类型 `fuel_deburn_factory`。

### 12. 机器类型的能量签名

`AddonMachines` 要求同一机器类型的所有方块定义一致，签名里包含主能量类型。矿机的 RF 档和其余 EU 档原来共用 `miner`，四台手动电机把 RU 和 KU 混在 `manual_motor` 下。拆成 `miner` / `miner_rf` 和 `manual_motor` / `manual_kinetic_motor`，行为工厂两边都映射到同一个行为。

### 13. 前缀物品与机器方块重名

`flywheel` 前缀会给每个工具材料生成 `gt6ktfruaddon:flywheel_<material>` 物品，而卡 08 的六台小型飞轮机器方块已经占用了 `flywheel_wood`、`flywheel_bronze`、`flywheel_steel`、`flywheel_titanium`、`flywheel_tungstensteel`、`flywheel_adamantium`。启动时物品注册抛 `duplicate key`。前缀用 `excludedMaterials` 排除这六个材料；铁等其余材料仍生成前缀物品，`AddonItems.stack("flywheel", "Iron", 1)` 的启动检查不受影响。

### 14. 配方图拆分与源物品映射

启动时配方注册还暴露了三类旧问题：

- **实验型配方和普通配方共用一个图。** 源里 `FusionTokamak` / `FusionTokamakExperimental` 和 `LaserFusion` / `LaserFusionExperimental` 是四个图；附属原来把它们并成两张。相同输入的实验行被 `RecipeMap` 当成碰撞拒收。现在拆成 `FUSION_TOKAMAK` / `FUSION_TOKAMAK_EXP` 和 `LASER` / `LASER_EXP_MAP`，实验型托卡马克和实验型激光聚变各用自己的图。
- **源方块名和附属方块 id 不一致。** 激光聚变零件、AI 内存、房间能量口、量子相干外壳、涡轮壳和太阳炉顶层用的源名字和实际注册名不同。`Gt6KtfruSourceRecipes` 的 `block()` 增加别名表；Naquadria 的 `casingMachine` 用 `Trinaquadalloy` 和 `Tungstensteel` 的同形态代替。
- **GT6CE 没有 Paper / Glass 板和黑色玻璃板。** `form()` 把 `plate Paper`、`plate Glass` 和 `paneGlass Black` 映射到对应的原版物品。
- **子集碰撞。** 研究组装机的 528 行输入是 529 行的子集，先注册子集会让 529 被拒。生成器现在按输入数从多到少稳定排序，具体配方先注册。
- **手写行和生成行重复。** `Gt6KtfruRecipes` 手写的氧化钇锆热混合行和生成的 `Chemistry.java:58` 完全相同；生成行先注册后，手写行被拒。删掉手写行，保留 `find` 检查。

### 15. 按首包锁定能量类型

量子枢纽 30115 的主输入改成 `EnergyInput.anyType()`；空缓存时接受任意 GT 能量类型，首包锁定后只收同类型。辅助 EU 输入不变。

## 步骤

- [x] 按 `1d1f69fbb` 重打 SDK 和运行包，更新基线和 sha256。
- [x] 原零件名、`oneOf`、重复段落地。
- [x] 托卡马克第二能量和启动能量落地。
- [x] 提取器支持 0 mB 催化剂和 long 后缀，重新生成源配方。
- [x] 三个自有聚合物材料补上 `.polymer()`，修掉启动时的形态校验失败。
- [x] 前缀条件、流体危险判定、动态能量类型落地。
- [x] `compileJava` 和 `build` 通过。
- [x] 开发服务端启动到 `Done`，确认注册和配方没有报错。
- [ ] 界面里搭一台托卡马克，确认 LU 充能后进度才开始。

## 验收

- [x] `compileJava` 通过
- [x] `build` 通过
- [x] 启动日志没有 `source stacks did not resolve`、`uses unknown part` 或配方被拒；服务端到达 `Done (9.589s)`
- [ ] 玩法：托卡马克启动能量、流体电池危险流体、计算格二选一各跑一次

## 接口缺口（本卡之后仍存在的）

- 核心材料仍不能通过 `forms()` 请求未开启的形态，也不能复用内置方块前缀（石墨板 `casingMachine`）。
- 大型压缩气罐的阀门如果做成结构墙零件，仍没有接收方入口；当前只能放在控制器面。
- 配方机的结构端口只转发主能量类型，不转发 `auxiliaryEnergy`；托卡马克的 LU 只能从控制器面进。
- 没有爆炸入口；液体电池的酸/魔/高温只能拒收和清空。
- GT6CE 没有公开 `powerconducting`，用 `plasma` 近似。
- `CellMatch.oneOf` 不接受嵌套组；需要「多选里再套多选」时仍要拆格。
- `LayoutRepeat` 仍以 1024 份和世界高度为上限，超过世界高度的结构表达不了。

## 日志

- 2026-10-23：建立本卡。GT6CE `1d1f69fbb` 的接口提交正好覆盖卡 05 的十条缺口；本卡把 SDK/运行包切到这个提交，并把附属能用的入口逐条落地。

- 2026-10-23：开发服务端冒烟。依次修掉旧问题：聚合物材料缺 `.polymer()`、行为机 GUI 矩形重叠、布局名带点、同一机器类型下混用能量类型、前缀物品和机器方块重名、实验/普通配方共图、源方块名映射、子集配方碰撞、手写与生成行重复。最终服务端到达 `Done (9.589s)`，没有 `source stacks did not resolve`、`uses unknown part` 或配方被拒。GT6CE 的 0 mB 输入在 NeoForge 上实际不可用，催化剂改为 1 mB + `keptFluidInputs`。
