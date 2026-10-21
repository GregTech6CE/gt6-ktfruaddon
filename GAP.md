# 接口缺口

当前缺口以[卡 05](docs/card-plans/prep/05-api-gaps.md)为准，对照 `addon/behavior-recipe-catalyst` 的 `9688dada`（API 0，模组 0.0.0）。原仓库大约 412 个 Java 文件，主体是自己的方块实体。

## 这一刀用上的

- `AddonMaterialContributor`：按名字声明材料、已有形态、矿石和矿石副产物。
- `AddonMachineContributor`：配方表、`BASIC` / `LARGE_RECIPE`、精确能量包范围、大型机结构 IO、燃料表、燃料多方块、外壳和能量零件。
- `BehaviorMachineDeclaration`：自定义 tick 行为、跨 tick 储能、能量输出、范围破坏、物品收集，以及读取配方/燃料表。`recipeMaps` 还能把行为机列为 JEI/EMI 催化剂。
- `fluidPipes()`、`itemPipes()`、`compressedGasTanks()` 和 `pushCompressedGas`。
- `onRecipesReady` 里的 `AddonItems`、`AddonRecipes` 和 `gt6ce:machine` 数据包。

## 仍不直接表达

- **有自己 IO 或界面的结构零件。** `parts()` 只有被动外壳和能量零件；坩埚屏、需要单独接线的计算零件这类零件级逻辑没有入口。只靠位置和天空判断的部分（例如太阳炉镜子朝天）可以由控制器行为代查。
- **第二种输入能量。** 控制器只收一种能量，能量零件从不接收能量。掩膜对准机的灯模块收 LU、能量模块收 EU，脱燃工厂同时收 EU 和 HU，都接不住。脱燃工厂离开 HU 后，温度系数会让进度倒退。
- **研究、计算机集群、房间、加速器和自己的坩埚方块。** 没有对应的控制器、研究系统或坩埚外壳/龙头入口。
- **新的电缆和能量导体。** 管道入口不等于电缆入口。
- **修改 GT 内置机器行为。** 附属仍只能增加/删除公开配方表里的静态行，不能替换 GT 方块实体逻辑。
- **TFC 压板和 TFC 配方。** 留在 1.7.10 那边。

能量互转和带物品燃料机不再算“没有基础入口”：行为机可以声明不同的输出能源，也可以查燃料图、扣物品并 `generate` / `emit`。它们还没有在本附属里实施。缺陷检测器已用 `searchOutputTanks` 和 `exclusiveOutputs` 打开。两只大型气罐阀门已注册，容量拆成七只储罐，正面只灌得进压缩气罐。显微镜的玻璃格能查，研究任务仍没有入口。GT 零件上的外观号仍写不进去。

## 这条探针自己的限度

- 燃料多方块的声明没有结构提示键。语言文件里的 `gt6ktfruaddon.structure.burner.1` 还没有入口可挂。
- 拉晶炉和氢燃烧器的合成只用原版石英、铁、铜和玻璃，方便放下来。正式产线不该这样合成。
- 方块外观是原版方块贴图，GUI 是纯色占位。
- 还没有在开发客户端里把拉晶和燃烧跑通。

TFC、Advanced Rocketry、Botania 以及整合包脚手架是 C 档，不进入本仓库的移植卡。
