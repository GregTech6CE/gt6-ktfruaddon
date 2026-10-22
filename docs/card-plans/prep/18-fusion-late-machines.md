# 18 聚变、催化反应器和后期配方机

- 状态：prep
- 依赖：排在卡 17 之后
- 文件范围：这些控制器的配方机或行为机、自有外壳、语言
- 平台：NeoForge 1.21.1

## 背景

`MultiTileEntityBasicMachine` 的那几台可以走 `RecipeMachine`。槽位顺序是输入物品、输出物品、最少物品、输入流体、输出流体、最少流体、最少输入。托卡马克的启动能量是第二种能量 LU。微重力组装机的源注册抄了宇宙辐射的配方图，注册时保持这个指向。

## 配方机

| 源 id | 类 | 材料 | 能量 | 配方图 | 槽位 |
| --- | --- | --- | --- | --- | --- |
| 30073 | `MultiTileEntityBasicMachine` | Iridium | EU 8192 | `MirrorGoldSynthesis` | 5、5、0、2、2、0、1 |
| 30075 | `MultiTileEntityBasicMachine` | Nq_522 | EU 16384 | `MirrorGoldQuantumization` | 5、5、0、2、2、0、1 |
| 30104 | `MultiTileEntityBasicMachine` | Tungstensteel | EU 8192，2048–16384 | `CosmicRadiationResearch` | 1、3、0、0、0、0、1 |
| 30105 | `MultiTileEntityBasicMachine` | Tungstensteel | EU 8192，2048–16384 | 同一张宇宙辐射图 | 同上 |
| 30120 | `CatalyticReactor` | StainlessSteel | EU 2048，256–4096，廉价超频 | `CatalyticReactor` | 6、3、0、6、3、0、1 |
| 30121 | `CatalyticReactor` | Tungstensteel | EU 8192，1024–16384，廉价超频 | 同一张 | 同上 |
| 30014 | `FusionReactorTokamakExp` | StainlessSteel | EU 2048，第二种 LU，启动能量，非常量功率。没有 min/max | `FusionTokamak`，燃料图键是 `FusionTokamakExperimental` 的内部名 | 3、3、0、6、6、1、1 |
| 30015 | `FusionReactorTokamakT1` | StainlessSteel | EU 16384，第二种 LU，启动能量，非常量功率。没有 min/max | `FusionTokamak` | 同上 |
| 30103 | `MicrogravityCrystalGrowthMachine` | Titanium | EU 8192，1024–16384 | `MicrogravityCrystalGrowth` | 1、1、0、2、0、0、1 |
| 30076 | `QuantumGoldSynthesisChamber` | Nq_522 | EU 65536 | `QuantumGoldSynthesis` | 4、4、0、2、2、0、1 |
| 30080 | `QuantumObservationTower` | Trinaquadalloy | EU 16384 | `QuantumObservation` | 4、4、0、2、2、0、1 |
| 30074 | `QuantumCommunicationRelay` | Iridium | 注册写了 8192，没有能量类型 | `QuantumCommunication` | 1、1、0、0、0、0、1 |

实验托卡马克的零件：真空泵 31015 锇，导体 31016 钛，线圈 31017 铜，墙 31018 不锈钢，等离子体管 31019 铬，冷却泵 31024 锇。T1：导体 31025 钛，线圈 31026 铜，墙 31027 不锈钢，中子加热 31028 镍。

激光聚变没有在控制器上写 EU。30071 是钨钢，配方图 `LaserFusion`，燃料图键是 `LaserFusionExperimental` 的内部名。30072 是锇，只有 `LaserFusion`。槽位 3、3、0、3、3、0、1，特殊值的后缀是 ` LU`。零件：聚焦阵列 31060 钨钢，靶注入 31061 钛，脉冲储存 31062 锇，LU 32768、上限 131072，中子产物收集 31063 铅。

催化反应器外壳 31200–31203。托卡马克、激光聚变、催化反应器和三台 AI 中心的结构字符串还没读。

30076 是三层 3×3。底层 `QEQ / Q D / QQQ`，中层 `QGQ / GLG / QGQ`，顶层 `SSS / SRS / SSS`。空格是控制器。Q 是相干墙 31111，D 是铝墙 31000 的物品流体口，E 是 31000 的能量输入，G 是驱动 31115，L 是晶格稳定 31116，S 是衬底 31112，R 是纠缠通道 31113。

30080 是 3×3×5，层规则 `ABCDE`。底和顶是 `WWW / WEW / WWW`。第二层和第四层是 `WIW / BSB / WIW`。中层是 `RCR / DKS / RCR`，K 是控制器。W 31111，B 31112，C 31113，R 31114，D 31115，S 31116。E 是 31000 能量输入，I 是 31000 物品流体。

30103 是 3×3×3，层规则 `ABC`。底 `WCW / WWW / WWW`，中 `WIW / W W / WOW`，顶 `WWW / WEW / WWW`。W 是房间屏蔽墙 31204，C 是这台控制器，I 和 O 是检修口 31207 的进出，E 是能量口 31208。中层中心是空气。

30102 `CentrifugalMicrogravityController` 是钨钢，EU 2048、上限 8192，没有下限。房间是 9×9×9，层规则 `ABBBBBBBC`，控制器在底层中心，中间六层是空腔。零件：31204 铅屏蔽墙，31205 不锈钢质量环，31206 钨钢陀螺锚，31207 钛检修口，31208 不锈钢能量口，EU 2048、上限 8192。房间怎么改世界规则要自己写。

30106–30108 是钨钢，注册没有能量。AI 零件也是钨钢：31091 框架、31092 内存、31093 路由、31094 装箱、31095 识别。

30074 是单方块。两台用自己的坐标 NBT 配对。QU 缓存 32768000，每 tick 送出 128 QU，一条配方要 65536 QU。

## 不做

- 钴-60 增殖棒 9980 和产物棒 9990。类是 GT 的 `MultiTileEntityReactorRodBreeder` / `Product`，附属不注册 GT 方块实体。
- 9970 `NuclearAttachmentNeutronLiquidBreeder`。类继承 GT 的 `MultiTileEntityReactorCore`，同样不继承。它把未慢化中子累进 `NeutronAbsorption` 的流体配方，这张图留在源里。
- 小行星寻找机、小行星矿机、火箭组装机。
- 把 30105 的配方图改成别的图。
- 调试发电机 32766、代码工具机 32760、锈蚀机壳 32761–32762。

## 步骤

- [x] 配方机的材料、能量和槽位已写入上表。9970 因继承 GT 反应堆核心，不注册。
- [ ] 先注册一台 `MultiTileEntityBasicMachine`，并给一条不依赖 C 档物品的配方。
- [ ] 托卡马克、激光聚变、催化反应器和 AI 中心读完结构再注册。量子金、观测塔、微重力房间和晶体机的结构已经写在上面。
- [ ] 开发客户端里把那台配方机跑完一条配方。

## 验收

- [ ] 编译
- [ ] 开发客户端
- [ ] 玩法：上表里一台配方机加工完一条配方

## 日志

- 2026-10-22：从卡 04 右列拆出。30105 保持源配方图。9980 和 9990 不进实施。
- 2026-10-22：配方机的材料和槽位、激光聚变的 LU 零件、量子中继的 QU、微重力房间的能量写入这张卡。9970 改到「不做」。
