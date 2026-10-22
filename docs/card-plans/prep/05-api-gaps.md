# 05 表面里接口接不住的部分

- 状态：prep
- 依赖：无
- 文件范围：这张卡。不改机器，除非某条被接口打开后另开实施卡。
- 平台：NeoForge 1.21.1

## 背景

这些条目在表面清单的 A/B 档里，所以不是 C 档那种「不要移植」。C 档本身不在任何实施卡里。

只记 GT6CE 附属接口里没有的东西。对照的是 `addon/functional-parts` 的 `6eba3e1ed`。API 仍是 0，模组版本仍是 0.0.0。每一条都写清来源卡、源名字、试过的入口、缺的是什么，以及当前机器怎么落的。

## 已有入口，不列缺口

- 第二种输入能量。行为机用 `auxiliaryEnergy`：自己的面、正容量，以及包范围或 `anyPacket()`。这份缓存用 `energy(type)` 和 `consume(type, amount)` 读写。大型配方机用条件零件，`energyInput` 加 `condition`。掩膜对准机的灯收 LU、能量模块收 EU，脱燃工厂收 EU 和 HU，都走这两条。卡 04、11、12。
- 功能零件和布局格。`LayoutPart` 可以写 `slots`、`tick`、`use`、`field`，也可以和能量输出或条件输入写在一起。普通方块和方块标签可以写成 `CASING` 格。布局坐标不再限制在 ±16。灯模块、平台、CNC 电机和刀头、钻头、吸热器、热管、镜架、镜子在源里是 `CommonPart`，按外壳声明。卡 04、11、12。
- 探伤仪。`RecipeMachine` 有 `searchOutputTanks(true)` 和 `exclusiveOutputs(true)`，对应输出储罐回退和两项物品输出二选一。卡 02。
- 编程电路、保留物品和输出概率。编程电路是 `AddonItems.selectorTag(0..24)`。不消耗的物品输入按数量 1 写进 `Line.keptInputs`。物品输出概率在 `Line.outputChances`。卡 06、09。
- 单方块压缩气罐和管子。气罐是 `compressedGasTanks()`，灌气是 `pushCompressedGas`。管子是 `fluidPipes()` 和 `itemPipes()`。卡 07。
- 研究、无线计算、无线能量和坩埚温度。科技树、无线配对坐标、坩埚温度和熔融物都存在附属自己的方块上，GT 不负责保存或转发。卡 15–19。

## 接口缺口

### 行为机的能量与侧面

- **纯发电行为机仍要声明进电面。** 来源：卡 08（水车 `WaterMill` 9800、手摇 `ManualGenerator` 10021/10022/10026/10027）、卡 09（气体电池 `FuelBattery` 10010–10011）、卡 15（研究方块 9810–9815 和 1106，注册只写了送出 512 EU）、卡 16（计算方块 1100–1105 同样只写了送出 512 EU；计算零件 32005、32006 没有能量注册）。`BehaviorMachine.build` 要求 `energy` 非空，并且 `faces` 非空，否则抛 `Missing energy type` 或 `accepts energy on no face`。源机器只送电、不收电。现在水车把输入面放在顶面，手摇和气体电池放在底面。研究方块和计算方块的底面收 256–1024 EU，行为不凭空产生那 512 EU。两只计算零件的底面收 1 EU。
- **行为机不能按面限制物品和流体。** 来源：卡 07（压缩机 `tankGasCompressedInputer` 只从顶面进气）、卡 09（`FuelBattery` 正面和背面不收流体）、卡 14（流体电池 `LiquidBattery` 只从底面排液）。`BehaviorMachine.build` 拒绝 `MachineIo`（`Behavior machines cannot declare item and fluid faces`）。`BehaviorMachineBlockEntity` 注册物品和流体能力时忽略方向，共用同一个 `PipeItems` / `PipeFluids`；`canInsert` 和 `canFill` 也没有面参数。现在各面都能灌抽。流体电池的储罐角色是 `ANY`。
- **行为机主输入不能声明包范围或忽略包大小，过载也不能关闭。** 来源：卡 07（压缩机）、卡 08（飞轮箱 `FlywheelBox` / `FlywheelBoxElec` 15000–15055）、卡 10（变换零件 `TransformerPart` 10200–10255）、卡 14（流体电池和变换电池的输入都从 64 拉到注册上限，跨度超过四倍）。`BehaviorMachine.build` 对主 `energy` 拒绝 `range` 和 `anyPacket()`。它们只在 `auxiliaryEnergy` 上可用。主输入固定按标称值的一半到两倍收包，超过上限就走 `MachineOverload`。源飞轮和电动飞轮的钳制、满箱爆炸，以及变换零件从内部面收任意包，都对不上这条主输入规则。卡 14 把 64 到注册上限放在辅助输入上，底面 1 HU 只用来满足主输入。
- **行为机不能从回调里拒绝外部注入。** 来源：卡 10（`TransformerPart` 的 `mStopped`）、卡 14（变换电池 `TransformBattery` 未封存时不收电）。`doInject` 只看声明的面和缓存空余，不回调 `MachineBehavior`。停机开关只能停掉 `emit`，挡不住背面继续收电。
- **能量输出没有最小包和负包。** 来源：卡 08（飞轮输出下限 `VMIN[j]`、水车负扭矩、手摇 KU 正负交替）。`EnergyOutput` 只有 `maxSize` 和 `maxAmount`。`BehaviorEnergyRules.requireEmit` 要求 `size >= 1` 且 `amount >= 1`。

### 多方块零件

- **多方块没有「内部面」，结构零件也不能同时进电和出电。** 来源：卡 10（`TransformerPart`）、卡 14（`TransformBattery`）、卡 17（`WirelessEnergyReceiver` 31117–31118）。源类用 `SIDE_INSIDE` 表示控制器向零件内部注入，再从零件正面送出。`MachineFace` 只有上下前后左右六个朝向，`LayoutPart.build` 又拒绝同时设置 `energyOutput` 和 `energyInput`（`cannot both accept and emit`）。所以卡 10 的 36 件变换零件做成单方块行为机，用背面代替内部面。卡 14 同样没有内部面，回调里也推不进相邻的变换零件，见「不能把能量推进另一台机器」。
- **GT 零件上的外观号（`NBT_DESIGN`）。** 来源：卡 04（矿处理系统 30041 的输出墙外观 7）、卡 12（脱燃工厂出口框架 G 的外观 7、油矿钻头 31014 的外观 7）、卡 13（涡轮壳和流体锅炉的外观 7）、卡 14（流体电池控制器外观）。这是写在 GT 零件上的贴图序号。`StructureLayout` 的格子只认方块和角色，不能给 GT 墙指定这套贴图；另声明一块 `layoutParts()` 又会变成别的方块。输出角色已经按格子生效，外观保持原墙。
- **结构布局是一份固定格子，不能按高度重复，也不能写成「任意液体」或「任意不透明方块」。** 来源：卡 12（太阳炉 `SunHeater` 30007 的吸收层 1–16 层、地幔加热 `MantleHeater` 30028 从钻杆一直打到基岩、潮汐 `TidalWaveGenerater` 30033 的 `LiquidPredicate` / `OpaqueCubePredicate`）、卡 13（流体锅炉 `FluidBoiler` 30056 的中间层最多加到 8）、卡 14（流体电池 `LiquidBattery` 30024–30026 的池子，层数 16/32/64，边长 16/32/48）。`StructureLayout` 只收确定的 `LayoutCell`。`CellMatch` 有零件、空气、方块 id 和方块标签，没有「任何液体」或「任何不透明方块」。太阳炉现在固定成 1 层吸收层再加顶盖。地幔加热的布局停在地上那四层，不包含往下的钻杆柱。潮汐的液体格是 `minecraft:water`，不透明格是 `minecraft:stone`，角色都是 `CASING`。流体锅炉的中间层固定成 1 层。流体电池的池子固定成背后 1 格空气。
- **能量只能从控制器声明的面送出，不能打到结构外的任意坐标。** 来源：卡 12（太阳炉库存超过 9 倍速率后，从结构顶上 3×3 向下送 HU；地幔加热从顶上五个位置送 HU）、卡 13（电机 EU 从背面 3 格外的能量口送出，蒸汽涡轮从背面能量口送 RU）。`MachineBehaviorContext.emit` 只走 `EnergyOutput` 声明的 `MachineFace`。太阳炉和地幔加热把热量留在缓存或方块状态里。电机的 EU 从控制器背面送出，蒸汽涡轮的 RU 从控制器正面送出。结构里的 `ENERGY_OUTPUT` 格子不转发这一包。燃气涡轮是燃料多方块，`FuelOutput.behind(4)` 能打到壳外面。
- **功能零件没有流体储罐，也没有独立 GUI。** 来源：卡 04、卡 19（电磁坩埚屏 `DummyCrucibleScreen` 31504）。`LayoutPart` 有物品槽 `PartSlots`、服务端 `tick`、`use` 和 `field`，没有流体槽和菜单入口。放大镜文本走 `PartBehavior.describe`。
- **大型压缩气罐阀门收不到 `pushCompressedGas`。** 来源：卡 03、卡 07（大型阀门 `steel_compressed_gas_tank` / `stainless_compressed_gas_tank`）。`MachineBehaviorContext.pushCompressedGas` 只往 `compressedGasTanks()` 注册的单罐里写。大型气罐是带 3×3×3 结构的行为机，压缩机推不过去，阀门往外推也只认那种单罐。

### 材料、前缀与物品反查

- **材料读取接口没有质量、工具品质和工具速度。** 来源：卡 08（飞轮箱容量和损耗依赖飞轮材料的这三项）、卡 14（变换电池 RU 的飞轮物品，容量是最大库存乘堆叠数）。`GregTechMaterials.Material` 只有名字、id、颜色、熔点、沸点、密度、化学式、翻译键、组成和副产物。飞轮箱和这台变换电池的飞轮容量因此留在 0。
- **无锭材料不能通过 `forms()` 请求板、箔或电线，也没有 `casingMachine` 这类方块前缀。** 来源：卡 01、卡 06（硅橡胶、丁苯橡胶、PEEK 的板和箔，石墨板，组装机里的 `casingMachine` 和缺失电线）。`MaterialFormRequest` 只对已有生成规则前缀放行；材料没有 `metal()` 时请求 plate 或 foil 会在启动时被拒绝。新物品前缀走 `PrefixDeclaration`，方块前缀没有入口。
- **不能从任意物品反查材料和前缀组成。** 来源：卡 19（电磁坩埚 `ElectromagnetCrucible` 30032）。源里用 `OM.anydata_` 把投入的物品折算成材料和单位数。附属接口能按名字查 `GregTechMaterials.find`，不能从任意 `ItemStack` 反查它含什么材料。卡 19 不写猜 NBT 的替代查询。坩埚只对一份已知的铁加热，温度跨过 `Iron` 的熔点就记成熔化。材料质量不在接口里，总质量保持自身的 3200。

### 配方与工具

- **配方行没有「不消耗的流体输入」。** 来源：卡 09（气体电池 `Chemistry.java` 61–79 的 16 行，第三种流体是 0 mB 的氧化钇锆或硫酸）。`AddonRecipes.Line` 的 `copyFluids` 会把数量为 0 的流体变成空栈，随后 `RecipeMap.copyFluids` 把空栈删掉；`keptInputs` 只标记物品下标，`RecipeInputs.consume` 会扣掉所有非空流体输入。卡 09 把 0 mB 电解液从配方输入里拿掉，改在 `Gt6KtfruFuelBattery` 里按电极对检查电解液罐，并且不扣它。
- **工具枚举只有三种，且工具点击在 `onUse` 之前不会交给行为机。** 来源：卡 08（虚空漏斗的螺丝刀和软锤、矿机的软锤）、卡 09（气体电池的猴扳手）、卡 14 和卡 17（变换电池与无线塔的活动扳手封存）。`AddonTool` 只有 `SPADE`、`UNIVERSAL_SPADE`、`MAGNIFYING_GLASS`。而且 `BehaviorMachineBlock` 对手持 GT 工具但 `onUse` 返回 false 的点击不会打开界面，扳手又在 `onUse` 之前先改朝向。所以这些切换都改成了界面里的 `toggle` 开关。
- **燃料发电机只送出 1 安培。** 来源：卡 13（扩展马达 `ExpandedMotor` 30046–30051 的 `MAX_AMPERE` 4）。`FuelGeneratorBlockEntity` 送出时把安培写成 1。扩展马达的注册速率仍是这一包 RU。
- **第二种能量不能和主输入用同一面。** 来源：卡 13（分激电机 `MultiSeparateExciteDynamo` 30055，RU 和激励 EU 都从正面进）。`BehaviorMachine.build` 拒绝辅助能量和主输入共用一面。激励 EU 放在顶面。那一面贴着结构，包进不去。
- **一格只能写一个零件名，附属布局也认不全 GT 零件名。** 来源：卡 14（流体电池池壁 31034–31036 三选一，变换电池 `T` 是卡 10 的 `TransformerPart` 或能量墙；钨钢壳 18023、EU 线圈 18041）。`CellMatch` 一格一个名字。`SharedLargeMachineParts.idForName` 不认识 `implosion_compressor_wall` 和 `large_niobium_titanium_coil`，写进布局会在加载时抛未知零件。池壁布局只用青铜墙，钢墙和塑料墙仍单独注册。钨钢流体电池的壳用 `tungstensteel_wall`。EU 变换电池的线圈用 `large_copper_coil`。`T` 用这一档的能量墙。
- **行为机不能把能量推进另一台机器。** 来源：卡 14（`TransformBattery` 封存后对每个 `TransformerPart` 调用 `doInject`，面是 `SIDE_INSIDE`）、卡 17（`WirelessBatteryBase` 对 `WirelessEnergyReceiver` 调用 `doInject`，面同样是 `SIDE_INSIDE`）。`MachineBehaviorContext` 没有按坐标注入。`EnergyPart.charge` 只对这份布局绑上的能量零件。封存后的变换电池把能量留在控制器里。未封存仍会收电，见上面「不能拒绝注入」。封存只让 `canInsert` 拒绝放入；`SlotRole.ANY` 的取出不看这个回调，物品仍能拿出来。卡 17 的塔把数量交给附属自己的投递表，接收器在自己的 tick 里用 `generate` 入账，再从正面 `emit`。
- **配方机没有第二种能量，也没有启动能量。** 来源：卡 18（托卡马克 `FusionReactorTokamakExp` 30014、`FusionReactorTokamakT1` 30015 的第二种能量是 LU，并且 `NBT_SPECIAL_IS_START_ENERGY`）。`RecipeMachine` 只有一个 `energy`。两台机器的结构已经收成布局，只收 EU，功率不恒定。启动充能、磁场和算力检查没有落。实验型的燃料图并进同一张托卡马克配方图。
- **行为机读不到旁边那台行为机的状态。** 来源：卡 15（`ResearchAssembler` 30042 看背后两格的 `IResearchDatabase`，研究台看相邻的 `ResearchTreeMonitor`）。`MachineBehaviorContext` 只有本机的 `state()`。`level()` 和 `pos()` 能拿到方块，另一台行为机的 `BehaviorState` 没有公开读取入口。研究组装机和研究反应釜改成了配方机，EU 窗口按源注册落地，研究树门槛不再执行。监视器仍是行为机，不再替组装机保存那份原版小树。
- **不能沿 GT 电线把两台控制器判成连通。** 来源：卡 16（`ControllerElectric` 1100 顺着 `MultiTileEntityWireElectric` 走到另一台控制器）。行为机只能看 `level()` 里的方块。GT 电线没有附属能认的方块标签。1100、1102、1104 在源里是注释，没有恢复。无线控制器改认 512 格内的无线计算节点。
- **结构里的计算格只能认一种方块。** 来源：卡 16（`ComputePartPredicate`）、卡 18（托卡马克 H 格）。`CellMatch` 一格一个零件或一个方块。无线控制器的 N 格固定为 `wireless_compute_node_aluminium`。量子阵列、时空阵列和量子计算中心的计算格固定为 `quantum_compute_node_naquadah`。托卡马克的 H 格同样固定成铝制无线节点，写不出「计算零件或储罐墙」。可扩展层只收了一层。阵列只在放大镜上报源里的需求算力和产出算力，不调度集群。
- **激光聚变没有脉冲缓存和冷却。** 来源：卡 18（`LaserFusionExperimental` 30071、`LaserFusionIndustrial` 30072）。两台是收 LU 的大型配方机，窗口是源里的单包上限。`RecipeMachine` 不保存脉冲，也不走冷却。源配方依赖 kTFRU 物品，没有搬进行。
- **量子枢纽只存 EU。** 来源：卡 17（`WirelessBatteryQuantum` 30115）。源可以锁定任意能量类型。这台行为机的缓存是 EU，窗口 1–32768。
- **量子中继不送 QU。** 来源：卡 18（`QuantumCommunicationRelay` 30074）。它是 EU 8192 的单方块配方机。配方机不能再声明 QU 输出，也没有 32768000 的 QU 缓存和每刻 128 QU 的配对发送。
- **微重力房间不改世界规则。** 来源：卡 18（`CentrifugalMicrogravityController` 30102）。成型后每刻消耗 2048 EU，计数到 600。接口没有房间。
- **AI 中心不跑研究小游戏。** 来源：卡 18（30106–30108）。放大镜报每刻点数。不消耗 Normal 算力，也不给研究任务加分。
- **显微镜不推进研究任务。** 来源：卡 15（`OpticalMicroscope` 30077、`ElectronMicroscope` 30078）。成型后按每件 EU 消耗槽里的物品。
- **线圈 18041 仍用 `large_copper_coil`。** 来源：卡 17（感应塔的 `mCoil`）。共享零件表没有 18041，最近的线圈名是 18040。
- **流体查询只有气体。** 来源：卡 14（`LiquidBattery` 倒掉气体和导电流体，酸、魔流体或温度高于 573 就爆炸）。`AddonFluids` 只有 `gas`。温度用流体类型自己的 `getTemperature`。气体和温度高于 573 的流体被倒掉。酸、魔流体和导电流体留在罐里。没有爆炸。

## 不做

- 在附属里继承 GT 内部方块实体（例如 9970、9980、9990、涡轮、坩埚）来绕过接口。
- 把 C 档内容（TFC、Advanced Rocketry、Botania、小行星、火箭、符文扫描、戴森球、TFC 坩埚模型）当成接口缺口。
- 为卡 19 写一个扫物品名或 NBT 的假材料反查。

## 步骤

- [ ] 卡 12–19 每碰到一条用不上 GT6CE 接口的，把来源卡、源名字、试过的入口、缺什么和当前落法补到上面。
- [ ] 某条有了公开入口、并且附属用的 SDK 已经带上它之后，另开实施卡，不把实施写在这张卡里。

## 验收

- [ ] 列表和表面清单的 A/B 档对得上，没有把 C 档混进来

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
