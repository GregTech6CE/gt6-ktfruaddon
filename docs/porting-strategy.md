# 移植策略

落地仓库就是本仓库（AGPL-3.0-only，NeoForge 1.21.1，模组 id `gt6ktfruaddon`）。上游阅读副本在仓库根的 `kTFRUAddon/`，已写入 `.gitignore`。那份检出的分支名叫 `1.7.10`，因为 [kuzuanpa/kTFRUAddon](https://github.com/kuzuanpa/kTFRUAddon) 的默认分支就叫这个名字。本仓库的分支仍是 `main`。

计划卡先不开工。`docs/card-plans/` 里的 01–05 对着的是 CrucibleCraft 钉住的 `75cebb71`。`kTFRUAddon/` 比那个提交新 46 个提交，停在 `6404ab6979de9fce832ce0c41171000decf00e69`（2026-10-07，rename pure dusts; unify burn&fuel output to exhaust gas）。材料名、纯尘前缀和燃料排气都可能已经变了。表面清单刷新之前，卡 01 的验收保持未勾。

## 三份源各自的职责

| 位置 | 职责 |
| --- | --- |
| `kTFRUAddon/`，分支 `1.7.10`，提交 `6404ab69` | 行为与数据的阅读权威。AGPL-3.0。不进本仓库。上游 `dev` 停在 2026-02-13 的 `3e5b60ec`，比 `1.7.10` 旧，不跟。 |
| 本仓库 `main` | 附属本体。计划卡、语言、配方声明、改编后的代码都写在这里。编译只用 GT6CE 附属 SDK。 |
| `D:\Baobao\projects\dev\java\cruciblecraft-template-1.21.1` | 分档、证据层、关卡口径的参考。它的表面清单是旧对照，而且 CrucibleCraft 自己不收这些机器。 |
| `D:\Baobao\projects\dev\java\GregTech6CommunityEdition` | 接口形状的本地参考。附属实际编译对着 `libs/gt6ce/build-commit.txt` 里的 `32203391638dbcf5bd6524b21c85f6ea446dcb10`（探针文档写模组 0.8.0）。本机工作树的 `mod_version` 是 0.7.0，`API_VERSION` 是 7，和那只 jar 可能不一致。卡开工时以 SDK 里真实存在的类型为准。 |

上游检出有 412 个 Java 文件。主体在 `tile`（148）和 `api`（119），配方在 `recipe`（24）。`kTFRUAddon/进度.csv` 是上游作者自己的研究树进度，不拿来当移植队列。

## 许可

本仓库与上游都是 AGPL-3.0-only。`LICENSE` 与 `src/main/resources/META-INF/licenses/AGPL-3.0.txt` 是同一份 GNU AGPL 第 3 版原文。

可以从 `kTFRUAddon/` 改编代码、数据和资源，改编结果留在本仓库，协议保持 AGPL-3.0-only。1.7.10 的方块实体类不能原样丢进 NeoForge 1.21 工程。能接上 GT6CE 公开接口的行为，按接口重写；接不上的记进卡 05。

## 范围

沿用 CrucibleCraft 的四档，分母换成 `6404ab69`。判断看 Java import 和 `Loader.isModLoaded`，不看 `build.gradle`。上游 `build.gradle` 会拉一整包 TFRU、AE2、IC2、Galacticraft、BuildCraft，编译依赖不等于运行时依赖。

| 档 | 含义 | 处理 |
| --- | --- | --- |
| A | 只碰 `gregapi` / `gregtech` / 原版 / Forge / 附属自己 | 候选 |
| B | 多方块为了全息投影仪 import LibVulpes | 候选。结构改用 GT 的结构检查，不搬投影仪 |
| C | 运行时绑 TFC、Advanced Rocketry、Botania，或整合包脚手架（`EnvironmentHelper` 那类） | 留在 1.7.10 检出里 |
| D | 主体是 A，个别配方点到 `MD.AE` 或 IC2 冷却液别名 | 机器进候选，那几条配方单独标出 |

`75cebb71` 之后新出现的系统先全部标成待分档，默认留在缺口账：研究锁与研究配方、无线研究 / 计算 / 能量命令、微重力房间、镜金与量子金、激光聚变、时空 / 量子 / 生物计算机、AI 研究员。刷新表面时再逐个看 import。

阅读入口都相对于 `kTFRUAddon/`：

| 读 | 路径 |
| --- | --- |
| 作者意图 | `README.md` |
| 注册 | `src/main/java/cn/kuzuanpa/ktfruaddon/tile/tileEntityInit0.java` |
| 配方门 | `src/main/java/cn/kuzuanpa/ktfruaddon/recipe/recipeInit.java` |
| 配方图 | `src/main/java/cn/kuzuanpa/ktfruaddon/api/recipe/recipeMaps.java` |
| 材料 | `src/main/java/cn/kuzuanpa/ktfruaddon/api/material/materialPreInit.java` |
| 自有前缀 | `src/main/java/cn/kuzuanpa/ktfruaddon/api/material/prefix/prefixList.java` |

## 公开接口能表达的部分

对着本机 GT6CE 的 `com.gregtech.gregtech.api.addon`。SDK jar 若缺某个类型，以 jar 为准，把这条记进卡 05。

材料走 `AddonMaterialContributor`。身份是名字。`MaterialDeclaration.builder(int, String)` 已废弃，数字 id 在启动时被忽略，0.9 会删。源表从 22000 起的 id 不保留。能声明的形态是 `dust()`、`liquid()`、`gas()`、`metal()`。`metal()` 会连带锭、杆、齿轮和粉。`link()` 留给后面的成分和合金，不塞进第一张材料卡。

已有前缀可以用 `forms()` 的 `MaterialFormRequest` 按材料另要。GT6CE 里 `plate`、`foil`、`ore`、`crystal` 已经注册。卡 01 把硅橡胶的板和箔、智金和二氧化钼的矿写成「没有入口」。开工时先试 `forms()`，生成失败再写入卡 05。

新前缀没有入口。`prefixList` 里这些留在缺口账：`flywheel`、`bladeLargeTurbine`、`turbineLargeGas` 及其 checked/damaged、`turbineLargeSteam` 及其 checked/damaged、`dustCommercialPure`、`dustAnalyticalPure`、`dustAbsolutelyPure`。纯度若要做成物品，用不同的材料名，并在卡上写明这是替代。

机器走 `AddonMachineContributor`：

- `RecipeMapDeclaration` 加 `MachineDeclaration` 的 `BASIC` 或 `LARGE_RECIPE`。能源短名沿用源注册（EU、RU、HU、KU、LU），SDK 不认的短名记缺口。
- `FuelMapDeclaration` 加 `FuelGeneratorDeclaration`。效率是万分比，每 tick 一个包，储罐有上限，输出只在正面、背面，或正面背后 1 到 8 格。燃料图和配方图不能混用。
- `BehaviorMachineDeclaration` 只覆盖单方块。GUI 尺寸和槽位精灵位置是固定的。行为机器每 tick 清掉没用完的能量，不能拿来做飞轮或水槽。
- `MachineLayout` 的格子只能填已经存在的 GT 零件名、`air`，或结构壳。角色用 `StructureRole`。自有外壳、灯模块、刀头、镜子、计算零件不注册成新零件来充数。

配方行在 `GregTechAddon.onRecipesReady` 里用 `AddonRecipes` 添加。缺一格就保持缺口，不用原版零件或其他材料顶上。

## 公开接口表达不了的部分

这些留在卡 05，有公开入口之后另开实施卡。

- 新的线缆和管道，包括特氟龙流体管和 PVC 物品管。
- 跨 tick 的 RU 存储：飞轮箱、水库、无线电池。
- 手摇马达，以及看邻水的水车。
- 变压器、飞轮齿轮、能量种类互转。
- 矿机和虚空漏斗。
- 研究树、计算机集群、房间、加速器、戴森球界面。
- 修改 GT 本体机器的方块实体。数据包只能改已经存在的配方图上的静态行。
- TFC 压板、TFC 配方、TFC 受热坩埚接口。

## 交付口径

从 CrucibleCraft 的能力交付拿这几条，不拿它的机器清单：

- 计划卡只管理正在做的工作。关卡看机制能不能跑，玩家能不能从生存配方拿到是另一栏。
- 同时只开一条实施卡。注册表、配方图、主机拆开记。有一个方块 id 不等于这台机器完成。
- 每张实施卡的验收是：对着钉住的 SDK 编译通过，开发客户端里把声明的那一台跑完一条不依赖 C 档物品的配方或燃料。
- 临时合成必须写在卡上。正式产线跟源配方的输入输出，D 档那几条单独标。
- 人跑客户端发现的问题另开卡。

## 开工顺序

1. 刷新表面。对 `75cebb71..6404ab69` 看材料表、`prefixList`、`tileEntityInit0` 的新增注册、燃料排气。更新 `docs/ktfruaddon-gt6-surface.md` 的对照提交。新系统先分档，再决定进不进现有卡。
2. 材料卡。范围仍是粉、液体、气体，外加 `forms()` 能要到的已有前缀。名字、显示名、颜色、化学式、开尔文温度跟源表。摄氏条目沿用源码 `registerC` 的 +273。源表把乙烷标成液体，声明照这个标。
3. 单方块配方机器。流体加热器、光混合器、气体压缩机、电路组装机、激光切割机、热混合器、电子设计器、缺陷检测器、电动光混合器、晶圆测试器、晶圆涂布器、超声混合器。矿机和虚空漏斗进缺口账。源码里整段注释掉的机器不恢复。
4. 这十张图上能对上的源配方，并在 EMI 里能查到。卡 06，排在燃料机和多方块之前。对不上的行保持缺口。
5. 单方块流体燃料机。小型燃气轮机七档、柴油机、涡轮增压柴油。源码用 `isGregtechTFRU` 挡住柴油机注册，这里作为附属内容注册。排气以 `6404ab69` 的统一排气为准。气体电池先核对是不是「流体进、能量包出」；对不上就进缺口账。占位拉晶炉和氢燃烧器在对应的卡里撤掉或收成该卡的最小一台。
6. 多方块。先读控制器的结构零件名，分成「全部是现有 GT 零件」和「要自有零件」两列。只做前一列。最小候选是蒸馏塔：现有零件、一张蒸馏配方图、一条配方。
7. 缺口账从第 1 步起就追加。某条有了公开入口之后另开实施卡。

## 检出的用法

日常对照时让 `kTFRUAddon/` 停在 `6404ab69`。需要更新时在那个目录里拉取 `1.7.10`，并先改本页的提交号和「待分档」列表，然后才改计划卡。不要把那个目录 `git add` 进本仓库。
