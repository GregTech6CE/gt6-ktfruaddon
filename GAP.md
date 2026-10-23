# 接口缺口

当前缺口以[卡 05](docs/card-plans/prep/05-api-gaps.md)为准，对照 `addon/kubejs` 的 `9c67ceaa4`（API 0，模组 0.0.0）。SDK 和运行包都已按这个提交重打：`libs/gt6ce/gt6ce-addon-sdk-1.21.1-neoforge-0.0.0.jar` 和 `gt6ce-1.21.1-neoforge.jar`。原仓库大约 412 个 Java 文件，主体是自己的方块实体。

## 这一刀用上的

- `AddonMaterialContributor`：按名字声明材料、已有形态、矿石和矿石副产物。`MaterialDeclaration.Builder.polymer()` / `wires()` 给附属自有材料打开 `plate`、`foil` 和 `wireFine`，`PrefixDeclaration.block` 可以新建方块前缀。
- `AddonMachineContributor`：`RecipeTable`、`RecipeMachine` 的单方块和大型配方机、精确能量包范围、大型机结构 IO、燃料机、外壳、能量零件、条件零件和功能零件。布局支持 `LayoutRepeat` 重复层、`CellMatch.anyFluid` / `anyOpaque`、`LayoutCell.withDesign` 外观号，以及 `PartTanks` / `gui`。
- `BehaviorMachine`：自定义 tick 行为、跨 tick 储能、纯发电、`io` 按面限制物品和流体、`auxiliaryEnergy`、`acceptsEnergy` 拒收注入、`emitTo` 结构外送能、`pushToPart` 内部通道、`gasValve`、坩埚内容读取与模具浇铸。
- `AddonItems` / `AddonRecipes`：`selectorTag`、`composition` 物品反查，`Line.keptInputs` / `keptFluidInputs` / `outputChances`。
- `fluidPipes()`、`itemPipes()`、`compressedGasTanks()` 和 `pushCompressedGas`。
- `onRecipesReady` 里的 `AddonItems`、`AddonRecipes` 和 `gt6ce:machine` 数据包。

## 仍不直接表达

- **`SharedLargeMachineParts.idForName` 认不全已有 GT 零件。** 18023 的 `implosion_compressor_wall`、18041 的 `large_niobium_titanium_coil` 等仍解析成 -1，布局里写不出来；现在用最近的已有零件代替。
- **一格只能写一个匹配。** `CellMatch` 没有「A 或 B」；绑定零件、`part` 和 `block` 混写、以及「计算零件或储罐墙」这种组合仍表达不了。
- **重复段最多 64 份。** `LayoutRepeat` 的 `max=-1` 在 64 层处停，地幔加热那种一直打到基岩的钻杆柱仍表达不了。
- **配方机没有第二种能量和启动能量。** `RecipeMachine.Builder` 只有 `energy`；托卡马克的 LU 和启动充能仍没有入口。
- **前缀条件没有熔融、金属或合金，也不能按材料名排除。** 大型涡轮叶片和六种涡轮仍按飞轮的工具属性加可锻造生成。
- **核心材料不能通过 `forms()` 请求未开启的形态，也不能复用内置方块前缀。** 石墨板、核心材料的 `casingMachine` 仍没有入口。
- **0 mB 流体输入仍被拒绝。** `keptFluidInputs` 已经能用，但要写正数量（例如 1 mB）；热混合 11 行数量 0 的流体催化剂仍未落成配方行。
- **流体分类只有气体。** `AddonFluids` 能按名字拿 still/flowing，但只提供 `gas()` 分类；液体电池需要的酸、魔流体和导电流体判断仍自己写。
- **行为机多方块没有 GT 结构端口。** `BehaviorMachineBlockEntity` 不实现 `MultiblockPortOwner`，物品、流体和能量仍走控制器；功能零件可以作为附属自己的端口。
- **行为机的能量类型在注册时固定。** 量子枢纽那种按首包动态锁定能量类型仍没有入口，现在固定为 EU。

## 这条探针自己的限度

- 燃料多方块的声明没有结构提示键。语言文件里的 `gt6ktfruaddon.structure.burner.1` 还没有入口可挂。
- 130 个物品模型仍是纸：73 个研究数据、4 个电池电极、其余是无线计算节点、层间晶圆、激光靶和数据物品。
- 卡 02–04 已关闭，但 EU 配方机、钢壳轮机、蒸馏塔/矿处理系统的玩法验收仍未勾。
- 研究小游戏与任务树 UI 不搬；读相邻行为机的状态、沿 GT 电线判断连通也不做成 GT6CE 的通用接口。

TFC、Advanced Rocketry、Botania 以及整合包脚手架是 C 档，不进入本仓库的移植卡。
