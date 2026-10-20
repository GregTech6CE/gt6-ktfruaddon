# 02 单方块配方机器

- 状态：active
- 依赖：01
- 文件范围：`Gt6KtfruMachines.java`，`Gt6KtfruRecipes.java`，`Gt6KtfruAddon.java` 的创造栏和启动检查，对应的方块状态、模型、语言和临时合成。占位拉晶炉在这张卡撤掉。氢燃烧器留给卡 03，它是燃料机，不是这张表里的配方机器。
- 平台：NeoForge 1.21.1

## 背景

表面清单 20000–27553 里，能接上 `RecipeMapDeclaration` 加 `MachineDeclaration.basic()` 的单方块在这里落地。材料名来自 gregapi `MT.DATA` 的 `Heat_T`、`Kinetic_T`、`Electric_T`，再写成 GT6CE 注册表里的名字。电压是源注册的 `NBT_INPUT`。槽数来自 `recipeMaps.java` 的构造参数：输入物品、输出物品、最少物品、输入流体、输出流体、最少流体、最少输入。`needsOutputs` 为真，流体输出也算输出。

## 机器

| 源 id | 方块 | 材料 | 每 tick | 并行 | 配方图槽 |
| --- | --- | --- | --- | --- | --- |
| 20000 | 流体加热器 ×4 | Steel、Invar、Titanium、TungstenCarbide | 32 / 128 / 512 / 2048 HU | 1 | `fluid_heating` 0,0,0, 1,1,1, 1 |
| 21000 | 光混合器 ×4 | Bronze、Steel、Titanium、Tungstensteel | 32 / 128 / 512 / 2048 RU | 4 / 8 / 16 / 32 | `light_mixer` 6,3,0, 6,6,0, 1 |
| 23000 | 电路组装机 ×5 | SteelGalvanized、Aluminium、StainlessSteel、Chromium、Titanium | 32 / 128 / 512 / 2048 / 8192 LU | 1 | `circuit_assembler` 9,3,1, 1,0,0, 1 |
| 23010 | 激光切割机 ×5 | 同上 | 同上 LU | 1 | `laser_cutter` 2,3,1, 1,0,0, 1 |
| 27000 | 热混合器 ×5 | 同上 | 同上 EU | 1 | `heat_mixer` 6,3,0, 6,3,0, 1 |
| 27030 | 电子设计器 ×5 | 同上 | 同上 EU | 1 | `electronics_designer` 6,3,1, 3,0,0, 1 |
| 27040 | 缺陷检测器 ×5 | 同上 | 同上 EU | 1 | `flaw_detector` 3,3,1, 0,0,0, 1 |
| 27050 | 电动光混合器 ×5 | 同上 | 同上 EU | 4 / 8 / 16 / 32 / 64 | 共用 `light_mixer`，机器类型 `lightmixerelectric` |
| 27500 | 晶圆测试器 ×5 | 同上 | 同上 EU | 1 | `wafer_tester` 3,3,0, 0,0,0, 1 |
| 27510 | 晶圆涂布器 ×5 | 同上 | 同上 EU | 1 | `wafer_coater` 3,3,0, 3,0,0, 1 |
| 27542 | 超声混合器 ×3 | StainlessSteel、Chromium、Titanium | 512 / 2048 / 8192 EU | 1 | `ultrasonic_mixer` 6,3,0, 6,2,0, 1 |

电路组装机和激光切割机的显示名跟源码一样用 T1–T5。其余电动机器用 LV–IV。接受能量的范围是标称值的一半到两倍。80 HU 的蒸馏水加热在 LV 加热器上跑不了（上限 64），在 MV 上可以。16 EU 的硫粉热混合落在 LV 热混合器的 16–64 里。

## 配方

源行能对上现有物品和流体的才加。缺的输出不拿别的材料顶。

- 流体加热：蒸馏水 10 mB → 蒸汽 1600 mB，80 HU，20 tick。D 档：`Coolant_IC2` 10 mB → `Coolant_IC2_Hot` 10 mB，20 HU，10 tick。另有二氧化碳和氦气各 10 mB 加热成对应热气体。熔融钠钾、热熔融金属和重水那几行的流体还没接上，留着。
- 光混合：甲烷 300 mB + 氯气 600 mB → 一氯甲烷、二氯甲烷、氯仿各 100 mB + 氯化氢 600 mB，32 RU，600 tick。这是源里唯一的 LightMixer 行，写法没改。
- 激光切割：石墨烯板 → 石墨烯杆 ×2，500 LU，200 tick。HV 及以上能跑。其余源行要么是概率输出，要么缺电路或晶圆。
- 热混合：硫粉 + 氢 2000 mB → 硫化氢 3000 mB；硫化氢 2000 mB + 二氧化硫 1000 mB → 水 2000 mB + 硫粉。都是 16 EU，48 tick。氢氧化锂那行要液态氯酸锂，这份 GT6CE 没有这种液体，整行不加。能对上的其余源行在 `Gt6KtfruSourceRecipes`。
- 组装、电子设计、缺陷检测、晶圆测试、晶圆涂布、超声混合没有能对上的源行。卡 02 的临时原版行已由卡 06 撤掉，这些图现在是空的。

`6404ab69` 的这些配方文件里没有 `MD.AE`。AE 电路出现在研究树解锁，不在这张卡的配方里。

## 接口缺口

机器能用 `BASIC` 壳表达的已经注册。下面这些行为没有入口，已补到卡 05。

- 气体压缩机 `tankGasCompressedInputer`。它把气体推进 `ICompressGasTank`，没有配方图。五档都不注册。
- 缺陷检测器把输出槽当输入搜。概率输出在当前 GT6CE 里已有 `outputChances`，这一半没有。

## 临时合成

每种机器只给最低一档一个合成，方便放下来。铁块围一圈，中心各不相同：烈焰粉、萤石粉、红石、紫水晶碎片、煤炭、青金石、绿宝石、海晶砂粒、下界石英、金锭、海晶碎片。更高档先从创造栏拿。源配方和 EMI 在卡 06。

外观沿用源贴图。kTFRU 把 PNG 放在 `assets/gregtech/textures/blocks/machines/basicmachines/<名字>/`，这里拷到本模组的 `textures/block/machines/basicmachines/`。每个类型有浅灰机壳，以及待机、运行、发光三套覆盖层。电路组装机的目录名是 `laserwelder`。机壳的 tint index 是 0，客户端用 `GregTechMaterials.Material.color()` 按材料上色。源 GUI 贴图拷到 `textures/gui/recipes/`。组装机的文件名用 `circuitassembler`，因为路径最后一段正好是 `assembler` 时会被改成 `default`。

## 不做

- 矿机 27520、虚空漏斗 27550，以及 21010 的虚空漏斗。卡 05。
- 源码里整段注释掉的 CVD 和超净洗浴。
- 多方块。卡 04。
- 把 1.7.10 的方块实体类原样放进这个工程。
- 用 GT 内部类绕过公开接口。

## 步骤

- [x] 核对每台的材料档、电压和配方图槽数，写进这张卡。
- [x] 声明配方图和 `BASIC` 机器，撤掉占位拉晶炉。
- [x] 每个配方图至少一条不依赖档 C 物品的配方。
- [ ] 开发客户端里放下一台热混合器并跑完一条配方。

## 验收

- [x] 编译
- [x] 开发客户端到达标题画面，启动检查通过
- [ ] 玩法：一台 EU 配方机器吃进物品、吐出物品

## 日志

- 2026-10-20：51 台 `BASIC` 机器、10 张配方图。气体压缩机未注册。拉晶炉的注册、方块状态、模型和合成已删。氢燃烧器保留。`compileJava` 通过。开发客户端到标题画面，日志是 `gt6ktfruaddon: ready on neoforge 1.21.1 (GT6CE addon API 8)`。这条日志写在配方注册和 LV 热混合器物品检查之后，所以铁粉那行、蒸馏水加热行和热混合器物品都在。还没在世界里把热混合器跑完。
- 2026-10-20：创造栏里的原版方块贴图换成 GT6 单方块机器模型。电路组装机用 `laserwelder`，其余借最接近的现成正面。待机、运行、发光分开。机壳未着材料色。
- 2026-10-20：11 种机器改用 kTFRU 自带的 `basicmachines` 贴图。机壳按 `GregTechMaterials.Material.color()` 上色。
- 2026-10-20：晶圆测试器运行和发光正面原来是 16×318，高度不是帧高的整数倍，正面加载失败。补成 16×320。侧面提示是 GT 默认面罩，声明上没有入口。
- 2026-10-20：卡 06 撤掉临时原版行。能对上的源行进 `Gt6KtfruSourceRecipes`，光混合仍留在这里。组装、电子设计、缺陷检测、晶圆测试、晶圆涂布、超声混合现在没有配方。
- 2026-10-20：卡 03 撤掉了氢燃烧器。
