# 接口缺口

当前缺口以[卡 05](docs/card-plans/prep/05-api-gaps.md)为准，对照 `addon/remaining-api-gaps` 的 `1d1f69fbb`（API 0，模组 0.0.0）。SDK 和运行包都已按这个提交重打：`libs/gt6ce/gt6ce-addon-sdk-1.21.1-neoforge-0.0.0.jar` 和 `gt6ce-1.21.1-neoforge.jar`。原仓库大约 412 个 Java 文件，主体是自己的方块实体。

## 这一刀用上的

- `AddonMaterialContributor`：按名字声明材料、已有形态、矿石和矿石副产物。`MaterialDeclaration.Builder.polymer()` / `wires()` 给附属自有材料打开 `plate`、`foil` 和 `wireFine`，`PrefixDeclaration.block` 可以新建方块前缀。
- `AddonMachineContributor`：`RecipeTable`、`RecipeMachine` 的单方块和大型配方机、精确能量包范围、大型机结构 IO、燃料机、外壳、能量零件、条件零件和功能零件。布局支持 `LayoutRepeat` 重复层、`CellMatch.anyFluid` / `anyOpaque`、`LayoutCell.withDesign` 外观号，以及 `PartTanks` / `gui`。
- `BehaviorMachine`：自定义 tick 行为、跨 tick 储能、纯发电、`io` 按面限制物品和流体、`auxiliaryEnergy`、`acceptsEnergy` 拒收注入、`emitTo` 结构外送能、`pushToPart` 内部通道、`gasValve`、坩埚内容读取与模具浇铸。
- `AddonItems` / `AddonRecipes`：`selectorTag`、`composition` 物品反查，`Line.keptInputs` / `keptFluidInputs` / `outputChances`，以及 1 mB + kept 的存在性流体催化剂。
- `fluidPipes()`、`itemPipes()`、`compressedGasTanks()` 和 `pushCompressedGas`。
- `onRecipesReady` 里的 `AddonItems`、`AddonRecipes` 和 `gt6ce:machine` 数据包。
- `1d1f69fbb` 新增：全部原零件名、`CellMatch.oneOf`、1024 份重复段、配方机 `auxiliaryEnergy` / `startupEnergy`、前缀 `MOLTEN` / `METAL` / `ALLOY` 与排除名单、`AddonFluids.acid` / `plasma` / `magic`、行为机结构端口、`EnergyInput.anyType()`。

## 仍不直接表达

- **核心材料不能通过 `forms()` 请求未开启的形态，也不能复用内置方块前缀。** 石墨板、核心材料的 `casingMachine` 仍没有入口。
- **0 mB 流体输入在 NeoForge 上不可用。** `FluidStack.getFluid()` 对数量 ≤ 0 返回 `Fluids.EMPTY`，`Line` 会拒绝；催化剂写 1 mB + `keptFluidInputs`。
- **大型压缩气罐阀门只到控制器面。** 阀门做成结构墙零件时仍没有接收方入口。
- **配方机的结构端口不转发第二种能量。** 托卡马克的 LU 只能从控制器自己声明的面进。
- **没有爆炸入口。** 液体电池的酸、魔流体和高温流体只能拒收并清空，不能像源那样爆炸。
- **GT6CE 没有公开 `powerconducting`。** 导电流体用 `plasma` 近似。
- **多选不能嵌套，重复段仍以 1024 份和世界高度为上限。**
- **动态能量类型只到行为机主输入。** `auxiliaryEnergy` 仍必须写死类型。

### 行为运行器的边界（WP4，2026-10-25）

- **行为配方付款没有公开的材料等价匹配器。** 固定 SDK `1d1f69fbb` 的 `AddonRecipes.Map.find` 内部使用 `RecipeInputs.consume`，可以按材料等价和部分组件规则命中；SDK 没有公开相同的匹配或付款规划入口。附属的 `RecipeRunner` 使用公开的严格物品及组件匹配，支持分格付款、重复输入和 kept 输入。仅靠材料等价命中的输入可能停机，但不会先扣能量或扣掉部分材料。没有借用 GT6CE 内部类，也没有用材料名称或成分猜测等价。
- **行为机没有内置的配方运行器与世界生命周期挂载点。** 本附属用一个公共运行器和 NeoForge 服务端事件管理临时登记；这不是新增 SDK 接口。
- **`emitTo` 只查本维度。** 量子无线仍用受生命周期管理的跨维度收件箱；余量先预订再付费，未能存进接收器的能量留在收件箱。

## 这条探针自己的限度

- 燃料多方块的声明没有结构提示键。语言文件里的 `gt6ktfruaddon.structure.burner.1` 还没有入口可挂。
- 130 个物品模型仍是纸：73 个研究数据、4 个电池电极、其余是无线计算节点、层间晶圆、激光靶和数据物品。
- 卡 02–04 已关闭，但 EU 配方机、钢壳轮机、蒸馏塔/矿处理系统的玩法验收仍未勾。
- 研究小游戏与任务树 UI 不搬；读相邻行为机的状态、沿 GT 电线判断连通也不做成 GT6CE 的通用接口。

TFC、Advanced Rocketry、Botania 以及整合包脚手架是 C 档，不进入本仓库的移植卡。
