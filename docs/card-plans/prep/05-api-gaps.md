# 05 表面里接口接不住的部分

- 状态：prep
- 依赖：无
- 文件范围：这张卡。不改机器，除非某条被接口打开后另开实施卡。
- 平台：NeoForge 1.21.1

## 背景

这些条目在表面清单的 A/B 档里，所以不是 C 档那种「不要移植」。C 档本身不在任何实施卡里。

只记 GT6CE 附属接口里没有的东西。源码对照的是 `addon/kubejs` 的 `9c67ceaa4`（2026-10-22）。`6eba3e1ed` 之后的 13 个接口提交里，11 个已进 `origin/main`（PR #89–#91），`fd5f38381` 和 `9c67ceaa4` 只在本地和 fork 分支。附属现在编译用的是 `libs/gt6ce/gt6ce-addon-sdk-1.21.1-neoforge-0.0.0.jar`，已在 2026-10-23 按 `9c67ceaa4` 重打并用 `javap` 和附属 `compileJava` 核对过；旧包留在同目录的 `gt6ce-addon-sdk-1.21.1-neoforge-0.0.0-6eba3e1ed.jar`。运行用的 `gt6ce-1.21.1-neoforge.jar` 也已在 2026-10-23 按 `9c67ceaa4` 重打，`build-commit.txt` 和 `.sha256` 已同步。下面「已有入口」记的是源码和这份新 SDK 包都有的状态。API 仍是 0，模组版本仍是 0.0.0。每一条都写清来源卡、源名字、试过的入口、缺的是什么，以及当前机器怎么落的。

## 已有入口，不列缺口

- 第二种输入能量。行为机用 `auxiliaryEnergy`：自己的面、正容量，以及包范围或 `anyPacket()`。这份缓存用 `energy(type)` 和 `consume(type, amount)` 读写。大型配方机用条件零件，`energyInput` 加 `condition`。掩膜对准机的灯收 LU、能量模块收 EU，脱燃工厂收 EU 和 HU，都走这两条。卡 04、11、12。
- 功能零件和布局格。`LayoutPart` 可以写 `slots`、`tanks`、`gui`、`tick`、`use`、`field`，也可以和能量输出或条件输入写在一起。普通方块和方块标签可以写成 `CASING` 格。布局坐标不再限制在 ±16。灯模块、平台、CNC 电机和刀头、钻头、吸热器、热管、镜架、镜子在源里是 `CommonPart`，按外壳声明。卡 04、11、12。
- 探伤仪。`RecipeMachine` 有 `searchOutputTanks(true)` 和 `exclusiveOutputs(true)`，对应输出储罐回退和两项物品输出二选一。卡 02。
- 编程电路、保留物品和输出概率。编程电路是 `AddonItems.selectorTag(0..24)`。不消耗的物品输入按数量 1 写进 `Line.keptInputs`。物品输出概率在 `Line.outputChances`。流体的版本见下面「配方行没有 0 mB 的流体输入」。卡 06、09。
- 单方块压缩气罐和管子。气罐是 `compressedGasTanks()`，灌气是 `pushCompressedGas`。管子是 `fluidPipes()` 和 `itemPipes()`。卡 07。
- 研究、无线计算、无线能量、微重力房间、激光脉冲和量子中继。科技树、算力网、房间包围盒、无线配对坐标、激光脉冲池和量子中继配对都存在附属自己的方块状态或附属静态表里，GT 不负责保存或转发。坩埚温度和熔融物走下面的 `CrucibleContent` / `CrucibleMold` 入口。卡 15–21。
- 异种能量转换与双缓存。行为机主 `energy` 和 `auxiliaryEnergy` 都能写 `range(min, max)` / `anyPacket()`；需要收另一种能量类型（如量子中继收 EU 发 QU）时，用 `auxiliaryEnergy` 声明第二种类型并和主缓存分在不同面。卡 14、20、21。
- 纯发电行为机。`BehaviorMachine` 只写 `energyOutput`、不写 `energy` 就能建；`generate` 和 `emit` 用一份完整输出包作缓冲。研究方块、计算方块和两只计算零件不再需要凭空声明进电面。`0a910848b`。卡 08、09、15、16。
- 行为机的物品和流体面。`io(MachineIo)` 对行为机开放，没点名的通道是关的，不是六面缺省；`canInsert` 和 `canFill` 有带 `MachineFace` 的重载，管子先看面再问行为。自动面和 `IoTarget` 仍拒绝。`0278fc5b8`。卡 07、09、14。
- 行为机主输入的包规则。主 `energy` 可以写 `range(min, max)`、`anyPacket()`（要求正容量）和 `overload(false)`；`anyPacket` 不检查包大小，`overload(false)` 拒绝窗口外的包。`BehaviorEnergyRules.admitInput` 按绝对值判断大小。卡 07、08、10、14。
- 行为机拒收注入。`MachineBehavior.acceptsEnergy(context, type, face, size, amount)` 在面检查和缓存检查之后被 `doInject` 调用，主缓存和附加缓存都问；返回 false 就拒收，不进缓存也不过载。卡 10、14。
- 输出包下限与符号。`EnergyOutput.minSize` 缺省 1，`allowNegative()` 只给 KU 和 RU，`requireEmit` 按绝对值检查。燃料机用 `FuelGenerator.Builder.amperes(n)`，1–64。`77592cf59`。卡 08、13。
- 双向能量零件和内部通道。`LayoutPart` 同时写 `energyOutput` 和 `energyInput` 时，`energyInput` 是不带 `faces`、不带 `condition` 的控制器→零件内部通道；控制器用 `pushToPart(part, type, size, amount)` 或 `emit(part, size, amount)` 充能，零件在自己的 tick 从 `energyOutput.faces()` 送出。`37dfaa566`。卡 10、14。
- 结构外送能。`MachineBehaviorContext.emitTo(target, side, size, amount)` 把声明的输出类型和包上限送到任意已加载坐标的方块实体，不占用声明的面；目标不保留引用。`EnergyOutput.Builder.remote()` 允许输出不写 `faces`，这种输出不会自己发到相邻方块，只走 `emitTo`。`9c67ceaa4`。卡 12、13、17。
- 布局的重复层和方块谓词。`LayoutRepeat` 支持向上/向下和 `max=-1` 自动重复（上限 64 份）；`CellMatch.anyFluid()`、`anyFluid(fluidTag)`、`anyOpaque()`、`block` 和 `tag` 都能写。数据包也能写 `repeats`。`37dfaa566`。卡 12、13、14。
- GT 零件外观号。`LayoutCell.withDesign(int)`、`LayoutRepeat.Builder.cell(..., design)`，数据包和 KubeJS 的格子也能写 `design`；成形时写进 `gt.design`，拆开时还原。`654e797f6`。卡 04、12、13、14。
- 材料统计与物品反查。`GregTechMaterials.Material` 有 `mass()`、`toolQuality()`、`toolSpeed()`、`toolDurability()`；`AddonItems.composition(ItemStack)` 按 GT 自己的物品数据反查前缀和材料单位，没有登记的物品仍是空。`e87273731`。卡 08、14、19。
- 坩埚内容与浇铸。绑定零件用 `PartContext.crucible()` 读 `CrucibleContent`（材料、数量、温度），不取走；行为机实现 `MachineBehavior.getCrucibleMaterial` / `getCrucibleTemperature` / `getCrucibleContentAmount`，用 `fillMoldAtSide(context, mold, side, amount)` 把熔融物灌进 `CrucibleMold`。`fd5f38381`、`82694dacf`。卡 19。
- 附属材料的额外形态与方块前缀。`MaterialDeclaration.Builder.polymer()` 打开 `plate` 和 `foil`，`wires()` 打开 `wireFine`；`PrefixDeclaration.block` 新建实心方块前缀并带 `forge:<oreDictionary>/<material>` 标签。核心材料仍不能走这两条。`fb48b4d3e`。卡 01、06。
- 工具与点击顺序。`AddonTool` 有 `SPADE`、`UNIVERSAL_SPADE`、`MAGNIFYING_GLASS`、`SCREWDRIVER`、`SOFT_HAMMER`、`WRENCH`、`MONKEY_WRENCH`、`HARD_HAMMER`、`WIRE_CUTTER`、`SAW`；`onTool` 在扳手转向之前运行，返回 true 吃掉点击。`e1289c194`。卡 08、09、14、17。
- 辅助能量同面。和主输入同类型又共用一个面才失败；类型不同可以共面。卡 13 的分激电机把激励 EU 放在正面，和 RU 共用。卡 13。
- 压缩气体阀门。`BehaviorMachine.Builder.gasValve(MachineFace)` 让行为机在控制器的一个面上收 `pushCompressedGas`，也能从同一面送出；单方块气罐每一面都收。`654e797f6`。卡 03、07。

## 接口缺口

### 结构零件与布局

- **`SharedLargeMachineParts.idForName` 认不全已有 GT 零件。** 来源：卡 14（流体电池池壁 31034–31036 三选一，变换电池 `T` 是卡 10 的 `TransformerPart` 或能量墙；钨钢壳 18023、EU 线圈 18041）、卡 17（感应塔的 `mCoil` 18041）。`OriginalMultiblockPartData` 已经有 `implosion_compressor_wall`（18023）、`large_niobium_titanium_coil`（18041）等 45 个零件，但 `SharedLargeMachineParts.idForName` 只查自己的 `DEFINITIONS` 和六个固定名字，查不到就返回 -1，`AddonMachines` 随即抛「uses unknown part」。所以布局里仍然写不出 18023 和 18041。现在池壁只用青铜墙，钢墙和塑料墙单独注册。钨钢流体电池的壳用 `tungstensteel_wall`。EU 变换电池的线圈用 `large_copper_coil`。感应塔的 18041 也仍是 `large_copper_coil`。
- **一格只能写一个匹配。** 来源：卡 14（流体电池池壁三选一）、卡 16（`ComputePartPredicate`）、卡 18（托卡马克 H 格）。`CellMatch` 是 sealed interface，一格一个值。普通方块的多选可以用方块标签（`tag`）绕开，但需要绑定的 GT 零件、`part` 和 `block` 混写、以及「计算零件或储罐墙」这种组合仍然写不出来。托卡马克的 H 格固定成铝制无线节点。量子阵列、时空阵列和量子计算中心的计算格固定成 `quantum_compute_node_naquadah`。现在可扩展层只收了一层，计算格各写一种方块。
- **重复段最多 64 份。** 来源：卡 12（地幔加热 `MantleHeater` 30028 从钻杆一直打到基岩）。`LayoutRepeat` 的 `max=-1` 会在 `LargeMachineLayouts.MAX_REPEATS`（64）处停，超过 64 层的结构表达不了。现在地幔加热的布局停在地上那四层，不包含往下的钻杆柱。

### 配方机器

- **配方机没有第二种能量，也没有启动能量。** 来源：卡 18（托卡马克 `FusionReactorTokamakExp` 30014、`FusionReactorTokamakT1` 30015 的第二种能量是 LU，并且 `NBT_SPECIAL_IS_START_ENERGY`）。`RecipeMachine.Builder` 只有 `energy`；行为机有 `auxiliaryEnergy`，配方机没有。`AddonRecipes.Line.special` 只是数值透传，没有启动能量语义。现在两台机器的结构已经收成布局，只收 EU，功率不恒定。启动充能、磁场和算力检查没有落。实验型的燃料图并进同一张托卡马克配方图。

### 材料与前缀

- **前缀条件没有熔融、金属或合金，也不能按材料名排除。** 来源：卡 22（`prefixList` 的大型涡轮叶片和六种涡轮）。源条件是工具属性、可熔、金属或合金，再排除退火铜、熟铁、铝铁。`PrefixCondition` 只有 `HAS_TOOL_STATS`、`SMITHABLE`、`NOT_WOOD`、`NOT_COAL`、`NOT_STONE`、`NOT_FOOD`、`NOT_MEAT`。现在叶片和涡轮按飞轮的工具属性加可锻造生成。纯度粉改用精炼家族的显式名单。
- **核心材料不能通过 `forms()` 请求未开启的形态，也不能复用内置方块前缀。** 来源：卡 01、卡 06（石墨板，组装机里的 `casingMachine` 和缺失电线）。`AddonMaterials.explicitPlateOrFoil` 对核心材料直接返回 false；`polymer()` 和 `wires()` 只对附属自有材料生效。`PrefixDeclaration.block` 只能新建名字，撞上内置 `casingMachine` 会启动失败。现在附属自有材料的 `plate`、`foil`、`wireFine` 已能通过 `forms()`；核心材料仍走 `MaterialPrefix.isValidFor`。石墨板仍没有 `casingMachine` 方块。

### 配方与流体

- **配方行没有 0 mB 的流体输入。** 来源：卡 06（热混合 11 行数量 0 的流体催化剂）、卡 09（气体电池 `Chemistry.java` 61–79 的 16 行，第三种流体是 0 mB 的氧化钇锆或硫酸）。`Line.keptFluidInputs` 已经进了 `9c67ceaa4` 的源码和 2026-10-23 重打的 API 0 SDK 包（`98e28d39d`），可以用正数量做「检查而不扣」的流体输入。0 mB 本身仍被 `RecipeKeptInputs.requirePositiveFluid` 拒绝，`copyFluids` 也仍会把 0 数量变成空栈。现在 0 mB 电解液从配方输入里拿掉，改在 `Gt6KtfruFuelBattery` 里按电极对检查电解液罐，并且不扣它；要改成配方行，得把数量写成正数（例如 1 mB）。
- **流体分类只有气体。** 来源：卡 14（`LiquidBattery` 倒掉气体和导电流体，酸、魔流体或温度高于 573 就爆炸）。`AddonFluids` 可以按名字拿 `still` / `flowing`，但只提供 `gas()` 分类；酸、魔流体和导电流体的判断没有入口。温度仍从流体类型自己的 `getTemperature` 读。现在气体和温度高于 573 的流体被倒掉。酸、魔流体和导电流体留在罐里。没有爆炸。
- **大型压缩气罐阀门只到控制器面。** 来源：卡 03、卡 07（大型阀门 `steel_compressed_gas_tank` / `stainless_compressed_gas_tank`）。`BehaviorMachine.Builder.gasValve(MachineFace)` 现在让行为机在控制器自己的一个面上收/发压缩气体，`pushCompressedGas` 也能到达它。但阀门如果做成结构墙上的零件，仍然没有接收方入口。现在大型气罐是行为机时，把阀面放在控制器上就能用；阀门零件还没有接法。

### 行为机多方块

- **行为机多方块没有 GT 结构端口。** 来源：卡 12（脱燃厂）、卡 21（研究组装机 30042、研究反应釜 30043、激光聚变 30071–30072、微重力晶体生长 30103）。`MultiblockPortBlockEntity` 只对 `MultiblockPortOwner`（`LargeRecipeMachineBlockEntity` 与坩埚）转发结构墙上的物品、流体和能量；`BehaviorMachineBlockEntity` 不实现它，物品、流体和能量都走控制器方块本身。现在功能零件的 `PartSlots`/`PartTanks` 可以放在结构里当附属自己的端口，但不是 GT 的输入总线/仓。
- **行为机的能量类型在注册时固定。** 来源：卡 17（`WirelessBatteryQuantum` 30115）。源可以按首包动态锁定任意能量类型。行为机的主输入和 `auxiliaryEnergy` 类型都是声明时写死的，没有运行时锁定入口。现在量子枢纽的缓存定为 EU，窗口 1–32768。

## 不做

- 在附属里继承 GT 内部方块实体（例如 9970、9980、9990、涡轮、坩埚）来绕过接口。
- 把 C 档内容（TFC、Advanced Rocketry、Botania、小行星、火箭、符文扫描、戴森球、TFC 坩埚模型）当成接口缺口。
- 为卡 19 写一个扫物品名或 NBT 的假材料反查。
- 读相邻行为机的 `BehaviorState`。卡 15、16、18、21 的跨机器通信已经在卡 21 里用 `(维度, 坐标)` 静态表和 `BehaviorMachine` 内部状态机落地；不把它做成 GT6CE 的通用业务接口。
- 沿 GT 电线把两台控制器判成连通。卡 16 的 `ControllerElectric` 1100 顺着 `MultiTileEntityWireElectric` 走；行为机用 `level()` 自己检查方块就够，GT 电线没有附属能认的方块标签。
- 研究小游戏与任务树 UI。卡 15、卡 18 的研究台连线/装箱/识别 GUI 不搬。

## 步骤

- [ ] 卡 12–19 每碰到一条仍然用不上 GT6CE 接口的，把来源卡、源名字、试过的入口、缺什么和当前落法补到上面。
- [ ] 某条有了公开入口、并且附属用的 SDK 已经带上它之后，另开实施卡，不把实施写在这张卡里。
- [ ] 先给 `idForName` 补上 `OriginalMultiblockPartData` 里已有的零件名，另开实施卡。
- [x] 按 `9c67ceaa4` 重打 API 0 SDK 包，再逐条核对「已有入口」在包里是否真的能用。
- [x] 重打运行 jar（`gt6ce-1.21.1-neoforge.jar`）到 `9c67ceaa4`，否则开发运行仍缺新入口。

## 验收

- [ ] 列表和表面清单的 A/B 档对得上，没有把 C 档混进来
- [x] 每条都按源码基线 `9c67ceaa4` 核过，已经能用接口的条目不在缺口里
- [x] 「源码有、当前 SDK 包没带」的条目写清楚，不把它当成永久缺口

## 日志

- 2026-10-20：初版记下编程电路、零数量催化剂、输出槽当输入、压缩气罐、新前缀和气体电池。
- 2026-10-21：`5bc167303` 和 `9688dada` 打开管子、物品前缀、矿石副产物、编程电路、`keptInputs`、压缩气罐、`pushCompressedGas`、大型机能量范围、结构外 IO、行为机 `recipeMaps`、探伤仪的 `searchOutputTanks` 和 `exclusiveOutputs`。
- 2026-10-22：`6eba3e1ed` 换成 `RecipeMachine`、`BehaviorMachine`、`FuelGenerator`、`StructureLayout`、`RecipeTable`。`auxiliaryEnergy`、条件零件、功能零件和普通方块 `CASING` 格打开。研究、无线链路和坩埚温度归回附属自己的方块状态。
- 2026-10-22：按卡 07–11 的实际落地重写这张卡。补入行为机强制进电面、行为机无流体/物品面限制、主输入不能设范围或忽略包大小、停机挡不住注入、输出无最小包和负包、`SIDE_INSIDE` 与双向能量零件、飞轮材料缺质量与工具属性、0 mB 流体催化剂，以及 `AddonTool` 缺螺丝刀/软锤/猴扳手。
- 2026-10-22：卡 12 补入固定结构格子、任意液体/不透明方块、以及只能从声明的面送能。脱燃出口框架和油矿钻头的外观号并进原来的 `NBT_DESIGN` 那条。
- 2026-10-22：卡 13 补入燃料发电机只有 1 安培、辅助能量不能和主输入同一面。锅炉中间层并进固定格子。电机和蒸汽涡轮的送出面并进「只能从声明的面送能」。
- 2026-10-22：卡 14 补入一格一个零件名、认不全的 GT 零件、不能把能量推进另一台机器，以及流体查询只有气体。流体电池的池子并进固定格子。底面排液并进「不能按面限制流体」。
- 2026-10-22：卡 15 补入读不到相邻行为机的状态。研究方块的 512 EU 输出并进「纯发电行为机仍要声明进电面」。
- 2026-10-22：卡 16 补入不能沿 GT 电线判断连通。1100–1105 的 512 EU 输出和两只计算零件并进「纯发电行为机仍要声明进电面」。
- 2026-10-22：卡 17 的无线塔并进「不能把能量推进另一台机器」。接收器改由自己的 tick 入账。
- 2026-10-22：卡 18 补入配方机没有第二种能量和启动能量。托卡马克结构没有收成布局。
- 2026-10-22：卡 19 的坩埚不读物品组成。已知铁的熔点和升温按 `CrucibleSpec.KG_PER_ENERGY` 100 落地。超过 1024 EU 的包进不了主输入窗口，没有爆炸。
- 2026-10-22：卡 20 把托卡马克收成布局，并补上研究组装机不再执行研究树、计算格只能认一种方块、激光聚变没有脉冲缓存、量子枢纽只存 EU、量子中继不送 QU、微重力房间不改世界规则、AI 中心不跑小游戏、显微镜不推进研究任务，以及线圈 18041 仍用 `large_copper_coil`。
- 2026-10-22：卡 21 把不需要改 GT6CE 的附属自有子系统收进 `BehaviorMachine` 与附属静态表：微重力房间索引（30102/30103/30105）、研究监视器解锁表与研究组装/反应釜/显微镜联动（9818/30042/30043/30077/30078）、激光聚变脉冲蓄能与强制冷却（30071/30072）、量子通讯中继器双机配对与 128 QU/t 发射（30074）、以及算力网静态拓扑与 AI 中心算力分配（30058–30070/32010–32043/30106–30108）。
- 2026-10-22：卡 22 补入涡轮前缀表达不了熔融、金属或合金和三个材料名排除。纯度粉用显式名单落地。
- 2026-10-23：源配方重新提取后，GT6CE 本体物品不再走 `AddonItems.stack(前缀, 材料)`，而是按 `gt6ce:` 注册名查 `BuiltInRegistries`；`IL.*` 元件、`casingMachine`、`wireGtNN`、`pipe*`、细导线、GT6 数字电线/轴件 ID 都因此落地。剩余 59 次调用中，只有 11 次流体催化剂是接口本身缺口（上面的 `keptFluidInputs`）；其余是材料/流体没移植、物品形态没开、C 档、手写重复、非常数参数，或 `kTFRU` 自有物品/GT6 旧机器 ID 没有对照。明细见卡 06 和 `build/source-recipe-report.txt`。
- 2026-10-23：按 `9c67ceaa4` 重核并重写这张卡。`6eba3e1ed` 之后的接口提交把纯发电行为机、按面限制物品和流体、主输入 `range`/`anyPacket`/`overload(false)`、`acceptsEnergy`、输出 `minSize`/`allowNegative`、双向能量零件与内部通道、`design` 外观号、`LayoutRepeat` 与 `anyFluid`/`anyOpaque`、`emitTo`、功能零件 `tanks`/`gui`、材料 `mass`/`toolQuality`/`toolSpeed`/`toolDurability`、`AddonItems.composition`、`onTool` 与扩大的 `AddonTool`、燃料机 `amperes`、辅助能量同面、`gasValve` 移入「已有入口」。缺口只留 `idForName` 认不全 GT 零件、一格一个匹配、重复段 64 份上限、配方机第二能量和启动能量、前缀条件、核心材料形态、API 0 包的 `keptFluidInputs`、`AddonFluids` 只有气体、结构端口、动态能量类型。邻居状态、GT 电线连通和研究 UI 移入「不做」。当时 SDK 包仍是 `6eba3e1ed` 打的；随后已按 `9c67ceaa4` 重打，见下一条。
- 2026-10-23：按 `9c67ceaa4` 重打 NeoForge API 0 SDK 包。`gt6ce-addon-sdk-1.21.1-neoforge-0.0.0.jar` 从 120 个类涨到 131 个类，sha256 `c1d03a2dd8c3c6b2a3ec2e39854075947f0ec6abd1a38604bb734427bd154455`；旧包备份为 `libs/gt6ce/gt6ce-addon-sdk-1.21.1-neoforge-0.0.0-6eba3e1ed.jar`。`addonApiClosureCheck` 通过（131 个 API 类），`javap` 核对 107 项入口全部命中，附属 `compileJava` 用新包通过。`keptFluidInputs` 已进包，缺口缩为 0 mB。当时运行 jar 仍是 `6eba3e1ed`；随后已重打，见下一条。
- 2026-10-23：按 `9c67ceaa4` 重打 NeoForge 运行 jar。`gt6ce-neoforge-1.21.1-0.0.0.jar` 复制为 `libs/gt6ce/gt6ce-1.21.1-neoforge.jar`，sha256 `d6dc2e2307a3616fb59ed164eef5b40ed0d56354ce0395e97cf5aa3f2e884625`；旧包备份为 `gt6ce-1.21.1-neoforge-6eba3e1ed.jar`。`build-commit.txt` 更新为 `9c67ceaa405ab5675306e074be2ae505c31701ab`，`.sha256` 同步。运行 jar 里确认有 `emitTo`、`pushToPart`、`keptFluidInputs` 和 `LayoutRepeat`。SDK 和运行包现在都来自 `9c67ceaa4`。
- 2026-10-23：复核新 SDK 后补入坩埚入口。绑定零件可读 `PartContext.crucible()` 的 `CrucibleContent`，行为机可实现 `getCrucibleMaterial` / `getCrucibleTemperature` / `getCrucibleContentAmount` 和 `fillMoldAtSide`（`fd5f38381`、`82694dacf`）。`GAP.md` 同步到 `9c67ceaa4` 并删掉已不成立的「零件没有流体储罐/GUI、读不到物品组成、外观号写不进去」等旧话。剩余缺口仍是上面十条，没有新增永久缺口。
