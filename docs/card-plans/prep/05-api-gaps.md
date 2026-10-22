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

- **纯发电行为机仍要声明进电面。** 来源：卡 08（水车 `WaterMill` 9800、手摇 `ManualGenerator` 10021/10022/10026/10027）、卡 09（气体电池 `FuelBattery` 10010–10011）。`BehaviorMachine.build` 要求 `energy` 非空，并且 `faces` 非空，否则抛 `Missing energy type` 或 `accepts energy on no face`。源机器只送电、不收电。现在水车把输入面放在顶面，手摇和气体电池放在底面。
- **行为机不能按面限制物品和流体。** 来源：卡 07（压缩机 `tankGasCompressedInputer` 只从顶面进气）、卡 09（`FuelBattery` 正面和背面不收流体）。`BehaviorMachine.build` 拒绝 `MachineIo`（`Behavior machines cannot declare item and fluid faces`）。`BehaviorMachineBlockEntity` 注册物品和流体能力时忽略方向，共用同一个 `PipeItems` / `PipeFluids`；`canInsert` 和 `canFill` 也没有面参数。现在各面都能灌抽。
- **行为机主输入不能声明包范围或忽略包大小，过载也不能关闭。** 来源：卡 07（压缩机）、卡 08（飞轮箱 `FlywheelBox` / `FlywheelBoxElec` 15000–15055）、卡 10（变换零件 `TransformerPart` 10200–10255）。`BehaviorMachine.build` 对主 `energy` 拒绝 `range` 和 `anyPacket()`。它们只在 `auxiliaryEnergy` 上可用。主输入固定按标称值的一半到两倍收包，超过上限就走 `MachineOverload`。源飞轮和电动飞轮的钳制、满箱爆炸，以及变换零件从内部面收任意包，都对不上这条主输入规则。
- **行为机不能从回调里拒绝外部注入。** 来源：卡 10（`TransformerPart` 的 `mStopped`）、卡 14（变换电池 `TransformBattery` 未封存时不收电）。`doInject` 只看声明的面和缓存空余，不回调 `MachineBehavior`。停机开关只能停掉 `emit`，挡不住背面继续收电。
- **能量输出没有最小包和负包。** 来源：卡 08（飞轮输出下限 `VMIN[j]`、水车负扭矩、手摇 KU 正负交替）。`EnergyOutput` 只有 `maxSize` 和 `maxAmount`。`BehaviorEnergyRules.requireEmit` 要求 `size >= 1` 且 `amount >= 1`。

### 多方块零件

- **多方块没有「内部面」，结构零件也不能同时进电和出电。** 来源：卡 10（`TransformerPart`）、卡 14（`TransformBattery`）、卡 17（`WirelessEnergyReceiver` 31117–31118）。源类用 `SIDE_INSIDE` 表示控制器向零件内部注入，再从零件正面送出。`MachineFace` 只有上下前后左右六个朝向，`LayoutPart.build` 又拒绝同时设置 `energyOutput` 和 `energyInput`（`cannot both accept and emit`）。所以卡 10 的 36 件变换零件做成单方块行为机，用背面代替内部面；卡 14 推电时也要按背面接，不能按 `SIDE_INSIDE` 找。
- **GT 零件上的外观号（`NBT_DESIGN`）。** 来源：卡 04（矿处理系统 30041 的输出墙外观 7）、卡 13（涡轮壳和流体锅炉的外观 7）、卡 14（流体电池控制器外观）。这是写在 GT 零件上的贴图序号。`StructureLayout` 的格子只认方块和角色，不能给 GT 墙指定这套贴图；另声明一块 `layoutParts()` 又会变成别的方块。输出角色已经按格子生效，外观保持原墙。
- **功能零件没有流体储罐，也没有独立 GUI。** 来源：卡 04、卡 19（电磁坩埚屏 `DummyCrucibleScreen` 31504）。`LayoutPart` 有物品槽 `PartSlots`、服务端 `tick`、`use` 和 `field`，没有流体槽和菜单入口。放大镜文本走 `PartBehavior.describe`。
- **大型压缩气罐阀门收不到 `pushCompressedGas`。** 来源：卡 03、卡 07（大型阀门 `steel_compressed_gas_tank` / `stainless_compressed_gas_tank`）。`MachineBehaviorContext.pushCompressedGas` 只往 `compressedGasTanks()` 注册的单罐里写。大型气罐是带 3×3×3 结构的行为机，压缩机推不过去，阀门往外推也只认那种单罐。

### 材料、前缀与物品反查

- **材料读取接口没有质量、工具品质和工具速度。** 来源：卡 08（飞轮箱容量和损耗依赖飞轮材料的这三项）。`GregTechMaterials.Material` 只有名字、id、颜色、熔点、沸点、密度、化学式、翻译键、组成和副产物。飞轮箱现在的容量因此留在 0。
- **无锭材料不能通过 `forms()` 请求板、箔或电线，也没有 `casingMachine` 这类方块前缀。** 来源：卡 01、卡 06（硅橡胶、丁苯橡胶、PEEK 的板和箔，石墨板，组装机里的 `casingMachine` 和缺失电线）。`MaterialFormRequest` 只对已有生成规则前缀放行；材料没有 `metal()` 时请求 plate 或 foil 会在启动时被拒绝。新物品前缀走 `PrefixDeclaration`，方块前缀没有入口。
- **不能从任意物品反查材料和前缀组成。** 来源：卡 19（电磁坩埚 `ElectromagnetCrucible` 30032）。源里用 `OM.anydata_` 把投入的物品折算成材料和单位数。附属接口能按名字查 `GregTechMaterials.find`，不能从任意 `ItemStack` 反查它含什么材料。卡 19 不写猜 NBT 的替代查询。

### 配方与工具

- **配方行没有「不消耗的流体输入」。** 来源：卡 09（气体电池 `Chemistry.java` 61–79 的 16 行，第三种流体是 0 mB 的氧化钇锆或硫酸）。`AddonRecipes.Line` 的 `copyFluids` 会把数量为 0 的流体变成空栈，随后 `RecipeMap.copyFluids` 把空栈删掉；`keptInputs` 只标记物品下标，`RecipeInputs.consume` 会扣掉所有非空流体输入。卡 09 把 0 mB 电解液从配方输入里拿掉，改在 `Gt6KtfruFuelBattery` 里按电极对检查电解液罐，并且不扣它。
- **工具枚举只有三种，且工具点击在 `onUse` 之前不会交给行为机。** 来源：卡 08（虚空漏斗的螺丝刀和软锤、矿机的软锤）、卡 09（气体电池的猴扳手）、卡 14 和卡 17（变换电池与无线塔的活动扳手封存）。`AddonTool` 只有 `SPADE`、`UNIVERSAL_SPADE`、`MAGNIFYING_GLASS`。而且 `BehaviorMachineBlock` 对手持 GT 工具但 `onUse` 返回 false 的点击不会打开界面，扳手又在 `onUse` 之前先改朝向。所以这些切换都改成了界面里的 `toggle` 开关。

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
