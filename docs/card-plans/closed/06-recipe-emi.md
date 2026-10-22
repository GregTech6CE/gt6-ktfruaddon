# 06 单方块机器的源配方和 EMI

- 状态：closed
- 依赖：02
- 排在卡 03、卡 04 之前
- 文件范围：`Gt6KtfruRecipes.java`，`Gt6KtfruSourceRecipes.java`。不新开机器，不新开材料。
- 平台：NeoForge 1.21.1

## 背景

卡 02 把十张配方图和机器落了地。源配方在 `kTFRUAddon/` 的 `recipe/recipe/*.java`，按 `6404ab69` 点名这些图。去掉注释后的实际调用数：

| 图 | 调用 | 卡上原来写的 | 注册进游戏 |
| --- | --- | --- | --- |
| `FluidHeating` | 11 | 11 | 4 |
| `LightMixer` | 1 | 1 | 1，仍是卡 02 的那一行，没有再注册一遍 |
| `Assembler` | 121 | 121 | 0 |
| `LaserCutter` | 38 | 38 | 1 |
| `HeatMixer` | 146 | 104 | 45 |
| `EDA` | 24 | 24 | 0 |
| `FlawDetector` | 0 | 0 | 0 |
| `WaferTester` | 24 | 24 | 0 |
| `WaferCoater` | 4 | 4 | 0 |
| `UltrasonicMixer` | 3 | 3 | 0 |

热混合是 146 次，不是卡上原先的 104。多出来的调用在 `Plastic`、`OilProcessing`、`PlatinumGroupProcess` 里，注释里的两处没有算进去。

数量按 gregapi 的 `UT.Code.units`：气体和化学液体 1 单位 = 1000 mB，熔融金属 1 单位 = 144 mB，`FL.make(n)` 仍是 n mB。源里的舍入参数为真、而且余数不够 1 mB 时，结果是 1 mB。所以 `MT.He.gas(10, true)` 和 `MT.Ar.gas(1, true)` 是 1 mB，不是 10 mB。带优化标记的行按 GT 的方式约分。`MT.Polycarbonate` 在这份 GT6CE 里的注册名是 `HardPlastic`。

## 目标

十张图上、输入和输出都能对上现有物品或流体的源行已经注册。对不上的记在下面，没有拿别的材料顶。

- 卡 02 的临时行已撤：铁锭变铁粒、金锭变金粒、煤炭变木炭、燧石变沙砾、粘土球变红砖、沙子变玻璃、铁粉变铁锭。
- 缺陷检测器没有源行，临时煤炭行已删，没有另造配方。
- 光混合保持卡 02 的写法。
- 液态氯酸锂、熔融钠钾、热熔融金属、重水仍留着。
- D 档的 `Coolant_IC2` 加热留着，生成代码里标了 D-tier。
- C 档没有进。`TFCRecipe.java` 没扫。Advanced Rocketry 的 3 行组装跳过。

## 对不上的行

接口缺口，已补到卡 05：

- 编程电路 `ST.tag`：54 行。组装 38，热混合 8，激光 6，超声 2。`AddonItems` 没有这个物品。
- 数量为 0 的催化剂：46 行，全在热混合。`AddonRecipes.Line` 不能标不消耗的输入，数量 0 会变成空栈。
- 概率输出：激光 24 行。和缺陷检测器是同一条 `Line` 缺口。

命名缺口，照旧留着：

- 流体加热 7 行：熔融钠钾 1，热熔融钠、锡、氯化锂 3，重水、半重水、氚水 3。
- 热混合 1 行：液态氯酸锂。

其余对不上现有物品或流体，没有替换：

- 组装 121 行全空。编程电路 38，附属 `ItemList` 30，材料没有对应电线 19，自有前缀 9，`casingMachine` 方块前缀 8，GT 电路元件 7，PEEK 没有板 4，C 档 3，机器物品、未注册流体、局部变量 `empty` 各 1。
- 电子设计 24 行：GT 元件 14，附属物品 10。
- 晶圆测试 24 行、晶圆涂布 4 行：全是附属晶圆或光刻胶。
- 超声 3 行：编程电路 2，缺流体 1。
- 激光剩下 13 行：编程电路 6，附属物品 6，石墨没有板 1。
- 热混合剩下 101 行：催化剂 46，材料没有对应流体 19，`flList` 里还没注册的流体 20，编程电路 8，附属物品 3，未知材料 `CrO2` 1，未移植的 `Zincoxide` 1，纯度流程里的变量 `material` 1，电压或时间不是常数 1，液态氯酸锂 1。

空图不会进 EMI。组装、电子设计、缺陷检测、晶圆测试、晶圆涂布、超声混合这次都没有分类。有行的四张图进了：流体加热、光混合、激光切割、热混合。

## 接口缺口

附属图在 `RECIPE_MAP_LIST` 里，`mViewerAllowed` 没有把它们藏掉。开发客户端的 `debug.log` 在 EMI 重载时写了：

- `EMI registering gt6ktfruaddon.recipe.fluid_heating`
- `EMI registering gt6ktfruaddon.recipe.light_mixer`
- `EMI registering gt6ktfruaddon.recipe.laser_cutter`
- `EMI registering gt6ktfruaddon.recipe.heat_mixer`

工作站来自 `RecipeMachines.collect()`。它遍历 `BasicMachineRegistries.all()`，不按命名空间过滤，附属 `BASIC` 机器注册时用的就是 `gt6ktfruaddon`。EMI 插件再对每台机器 `addWorkstation`。分类在，机器也在工作站列表里，所以没有另写 EMI 插件。

## 不做

- 新机器、新材料、新前缀。
- 卡 03 的燃料，卡 04 的多方块配方。
- 缺陷检测器的概率输出。
- 研究树解锁和无线研究配方。
- 临时原版行。

## 步骤

- [x] 按上面十个图把源行分成「能解析」和「缺物品或流体」，数字写进这张卡。
- [x] 能解析的源行全部注册，撤掉临时行。
- [x] 开发客户端里 EMI 载入了热混合和光混合。工作站走 GT 已有的机器注册，没有另写插件。

## 验收

- [x] 编译
- [x] 开发客户端里，EMI 载入了热混合源行和光混合那一条
- [x] 临时行不再出现

## 日志

- 2026-10-20：`6404ab69` 去掉注释后是 372 次调用，热混合 146 而不是 104。注册 50 行源配方，加上卡 02 原样留下的光混合。`compileJava` 通过。开发客户端进了 `New World`，启动检查在 `gt6ktfruaddon: ready on neoforge 1.21.1 (GT6CE addon API 8)` 之前要求硫粉加热、蒸馏水加热和光混合都在，并拒绝铁锭组装和煤炭缺陷检测。EMI 重载后 `debug.log` 载入上面四张有行的图。没有写 EMI 插件。
- 2026-10-23：提取脚本补上 GT6CE 本体物品解析，28 张图的源配方从 483 行涨到 639 行。`IL.*` 电路、元件、马达、传感器、发射器、激光气瓶和铅酸电芯按 `gt6ce:` 注册名拿；`casingMachine` 四档、`wireGtNN` 粗细电线、`pipe*` 管道、`wireFine`、GT6 数字电线和轴件 ID，以及局部变量 `empty` 都接上。剩余 59 次调用：流体催化剂 11（API 0 的 `Line` 没有 `keptFluidInputs`）、机器物品 11（EDA 的 6007 八处、气体电池的到自有物品 id 2 两处、CNC 的 18002 一处）、C 档 9、热熔融金属和重水等流体 12、手写重复 4、非常数电压/时间 3、形态或材料缺失 6、堆叠超过 99 一处、其余流体 3。`compileJava` 通过，开发客户端到标题画面，启动检查没有报告源物品解析失败。上面按 10 张图统计的旧账以这次日志为准，完整明细在 `build/source-recipe-report.txt` 的 skip ledger。
