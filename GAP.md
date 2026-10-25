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

### WP6 复查与保留行为（2026-10-25）

- **飞轮储能接口已够用，容量为 0 的旧说明失效。** 固定 SDK `1d1f69fbb` 的 `GregTechMaterials.Material` 已公开 `mass()`、`toolQuality()`、`toolSpeed()`；`AddonItems.composition` 返回 `flywheel` 前缀及材料，`MachineBehaviorContext.setCapacity` 可动态设置容量。附属用这些公开 API 恢复飞轮箱与 RU 变压电池容量，不调用内部类。公式来自 kTFRUAddon `6404ab69` 的 `item/items/itemFlywheel.java:33–39`、`tile/energy/storage/FlywheelBox.java:100–115` 与 `tile/multiblock/energy/storage/TransformBattery.java:196–198`。飞轮箱按最慢飞轮转速与机器上限计算，变压电池按每件独立容量累加；取出后的超额能量被清掉，空箱拒收。现有发包策略增加飞轮 RPM 上限，未声称完整还原上游转速曲线、超载损毁和结构能量零件外送；超载损毁仍受缺少公开爆炸入口限制，变压电池的结构外送仍未实现。六种与机器 id 冲突的材料仍在现有飞轮前缀排除表里，本次不迁移 id。
- **材料疑点按固定上游保留（D9）。** `materialPreInit.java:49` 的 Propanediol 与 `:46` 的 Propanol 公式相同；`:147` 的 Butadiene 末尾确实写成 `CH2` 再加下标 2。附属逐字保留并在声明旁标注来源，未自行作化学更正。`:138–139` 的 EpoxyResin 明确请求 `INGOT_BASED`、粉、板和箔；附属 `.metal()` 是打开这些生成形态的 SDK 开关，不是另作物性判断。
- **名称空格不是统一改写任务（D9）。** 138 个材料注册名/显示名直接匹配上游材料声明，包含 ZincOxide、Propanediol 等不拆词的名称。其余来自流体的声明中，有 13 个现有显示名做过可读化：AllylAlcohol、AllylAcetate、MethacrylicAcid、GlacialAceticAcid、InitalBottomOil、MoltenTeflon、MoltenBakelite、MoltenPolycarbonate、ImpureTitaniumAcylate、CrudeTitaniumAcylate、PurifyTitaniumAcylate、DistilledPlatinumGroupSolutionDown、Graphite_Methylpyrrolidone。这些差异包括拆词/修正拼写、Molten 三项补状态、Distilled…Down 修正上游误写 Up。此次核对不再改变显示名、注册 id 或材料形态。
- **虚空漏斗木头 64、青铜 32 是上游数值（D9）。** kTFRUAddon `tileEntityInit0` 中源 id 21010–21013 明确是 64、32、128、512；保持不单调，并在声明表/行为表旁标注来源。
- **命名约定仍是兼容边界（S5）。** `core.Gt6ce` 集中记录 `gt6ce` 命名空间、布局零件名、`fluid_item_`、`builder_wand`、`fluid_spring_` 等假设；它们仍不是 SDK 的专门查询接口。集中管理方便上游命名变化时定位，不代表这些耦合已消失。
