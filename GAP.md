# 接口缺口

当前缺口以[卡 05](docs/card-plans/prep/05-api-gaps.md)为准，对照 `addon/functional-parts` 的 `6eba3e1ed`（API 0，模组 0.0.0）。原仓库大约 412 个 Java 文件，主体是自己的方块实体。

## 这一刀用上的

- `AddonMaterialContributor`：按名字声明材料、已有形态、矿石和矿石副产物。
- `AddonMachineContributor`：`RecipeTable`、`RecipeMachine` 的单方块和大型配方机、精确能量包范围、大型机结构 IO、燃料机、外壳、能量零件、条件零件和功能零件。
- `BehaviorMachine`：自定义 tick 行为、跨 tick 储能、能量输出、第二种输入、范围破坏、物品收集，以及读取配方/燃料表。`recipeMaps` 还能把行为机列为 JEI/EMI 催化剂。
- `fluidPipes()`、`itemPipes()`、`compressedGasTanks()` 和 `pushCompressedGas`。
- `onRecipesReady` 里的 `AddonItems`、`AddonRecipes` 和 `gt6ce:machine` 数据包。

## 仍不直接表达

- **功能零件的流体储罐和自己的 GUI，以及从任意物品反查材料组成。** 物品槽、tick、右键、命名字段、条件输入和能量输出已经有了。放大镜读 `describe()`。材料熔点可以读。电磁坩埚的熔融物存在它自己身上。
- **房间和加速器自己的世界规则。** 零件可以 tick、存物品和记字段。计算机集群和无线能量的链路是 kTFRU 自己的坐标表，附属自己写。房间怎么改世界规则，仍要自己写。
- **新的电缆和能量导体。** 管道入口不等于电缆入口。
- **不消耗的流体输入。** API 0 的 `AddonRecipes.Line` 只有 `keptInputs`，没有 `keptFluidInputs`。热混合 11 行数量 0 的流体催化剂因此没有落地；上游源码已有这个字段，当前运行包还没带。
- **修改 GT 内置机器行为。** 附属仍只能增加/删除公开配方表里的静态行，不能替换 GT 方块实体逻辑。
- **TFC 压板和 TFC 配方。** 留在 1.7.10 那边。

能量互转、第二种输入和带物品燃料机不再算“没有基础入口”。行为机可以声明不同的输出能源，可以用 `auxiliaryEnergy` 再收一种，也可以查燃料图、扣物品并 `generate` / `emit`。条件零件可以挡住大型配方机开工。这些还没有在本附属里做成掩膜对准机或脱燃工厂。缺陷检测器已用 `searchOutputTanks` 和 `exclusiveOutputs` 打开。两只大型气罐阀门已注册，容量拆成七只储罐，正面只灌得进压缩气罐。五档气体压缩机和五只单方块压缩气罐、特氟龙流体管、PVC 物品管在卡 07。GT 零件上的外观号仍写不进去。

## 这条探针自己的限度

- 燃料多方块的声明没有结构提示键。语言文件里的 `gt6ktfruaddon.structure.burner.1` 还没有入口可挂。
- 拉晶炉和氢燃烧器的合成只用原版石英、铁、铜和玻璃，方便放下来。正式产线不该这样合成。
- 方块外观是原版方块贴图，GUI 是纯色占位。
- 还没有在开发客户端里把拉晶和燃烧跑通。

TFC、Advanced Rocketry、Botania 以及整合包脚手架是 C 档，不进入本仓库的移植卡。
