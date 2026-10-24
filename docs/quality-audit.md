# 质量审计与整改计划

- 状态：草案。§6 的最低质量线和 §8 的待决事项还没有确认。
- 日期：2026-10-24
- 对照提交：`2311f8c`。文中的行号都按这个提交，`git show 2311f8c:<路径>` 可以对上。
- 性质：不是实施卡。卡 23 仍是 `active`，整改轨道怎么登记见 §8 第 6 条。
- 用法：每个整改提交在 §9 勾掉对应项，提交信息里写问题编号（S4、D1 这类）。

## 1. 背景与目标

本仓库同时做两件事：把 kTFRUAddon 移植到 GT6CE（NeoForge 1.21.1），以及探测 GT6CE 附属接口能接住多少。卡 01–23 以探针为先：每张卡先证明接口接不接得住，代码走最短路径落地，缺口写进文档。结果是覆盖面很大（语言文件里约 414 个方块条目、504 个自有物品、689 行源配方），但代码结构不适合别人阅读和维护。

目标：整理到 §6 的最低质量线，让没参与过的人能构建、能读懂、能加一台机器，也能看出每样内容来自哪里。

不在范围内：新增机器和配方、玩法验收、C 档内容。整改期间碰到的接口缺口写进账本，不写成绕过代码。

## 2. 方法与局限

- 53 个 Java 文件里，21 个通读，4 个读了一部分（`SourceRecipes`、`GeneratedLayouts`、`Items`、`Materials`，都是生成文件），28 个只做了模式检索。构建配置和 CI 通读；6 个生成脚本读了开头和相关片段；`docs/` 里策略文档、卡 README、卡 23、`GAP.md`、`README.md` 通读，卡 05 和表面清单读了前半，已关闭的卡只做了检索；资源目录只做了统计（语言文件、模型、贴图）。
- 没通读的 28 个（省略 `Gt6Ktfru` 前缀）：`Turbines`、`Batteries`、`TurbineLayouts`、`BatteryLayouts`、`HeatLayouts`、`ResearchLayouts`、`CrucibleLayouts`、`ComputeMachines`、`ResearchMachines`、`WirelessMachines`、`LateMachines`、`CrucibleMachines`、`Crucible`、`TransformBattery`、`SteamTurbine`、`Dynamo`、`FluidBoiler`、`GasTank`、`GasCompressor`、`ManualMotor`、`WaterMill`、`Microscope`、`MantleHeater`、`SunHeater`、`SunMirror`、`TidalGenerator`、`TransformerPart`、`FuelDeburner`。整改动到它们时要补读。
- 没有编译，也没有运行：`libs/gt6ce/*.jar` 不在这个检出里，生成脚本在当前目录布局下也跑不了（G2）。
- 看不到 SDK jar，涉及 SDK 行为的判断都是从调用代码推断的，标 [推断]；需要核实的标 [待核实]。
- §4.4 的缺陷来自阅读，没有复现。
- 统计口径和命令见附录 A。

## 3. 总判断

对 SDK 的声明式用法（builder、布局、零件）基本可用，带源 id 的来源注释也有价值。问题在外面一圈：所有类挤在同一个包里互相拉扯；一台机器的信息散在六处以上；近一半 Java 是没有标注的生成代码，而生成脚本在这台机器上跑不了；行为层把同一套配方运行逻辑复制了好几份，还靠全局静态表通信；没有任何测试；文档里有已确认过期的事实，署名也没有跟上"这是上游内容的适配"这件事。探针阶段的做法（出错就抛、绕过去的办法记在卡里）留在了产品代码里。

值得保留的：

- 声明式的 SDK builder 用法，和带源 id 的来源注释。
- `Gt6KtfruLayerLayout`：字符网格加 `Spec` 的布局写法，其他手写布局应该迁到它上面。
- 配方提取器和它的跳过账本（689 行已解析，19 行跳过并按原因分类）。
- CI 把 GT6CE 钉在固定提交并缓存 SDK。
- 卡片账本抓出了真实的接口缺口，卡 05 的条目质量不错。

## 4. 发现

标签：[推断] 从调用代码推断，没看到 SDK；[待核实] 需要查证；[未测量] 没有性能数据。

### 4.1 结构（S）

**S1 所有类都在同一个包里。**

- 53 个类、14,806 行，全在 `com.lombinaxmasson.gt6ktfruaddon` 一个包里。`Gt6Ktfru` 前缀在 53 个类名上各出现一次；分类靠手工后缀（`*Machines` 7 个、`*Layouts` 6 个、`*Behavior` 1 个，其余 39 个没有后缀），相当于用文件名模拟包。IDE 里是 53 个同前缀的兄弟文件，生成文件和手写文件混在一起。
- 没有封装边界：类间引用共 1,170 处，其中 33 个包级可见成员被别的类直接使用（`Research.parsePos` 这类公用工具、`LateMachines` 的 10 个配方图常量、`FuelBattery.rate` 等）。这个数量不大，拆包时公开或包装即可。
- 依赖成环：类间依赖 82 条边，其中 20 个类构成一个强连通分量：`Addon`、`Machines`、`Items`、`Materials`、`Recipes`、`SourceRecipes`、`HeatMachines`、`HeatLayouts`、`Turbines`、`ResearchMachines`、`ComputeMachines`、`WirelessMachines`、`LateMachines`、`Compute`、`Wireless`、`QuantumRelay`、`LaserFusion`、`LateBehavior`、`FuelBattery`、`FuelDeburner`。成因是三个枢纽，不是功能本身耦合：
  1. 入口类里的 `MODID`：8 个类只为这一个常量依赖 `Gt6KtfruAddon`（`Compute`、`Items`、`MachineColors`、`Machines`、`Materials`、`SourceRecipes`、`StructureBuild`、`Wireless`），而入口类又依赖 `Machines`、`Items`、`Materials`、`Recipes`。
  2. `Machines` 里的 34 个常量（配方图名、机器类型名）：`SourceRecipes` 一个文件引用 588 次（`CIRCUIT_ASSEMBLER` 181 次、`HEAT_MIXER` 164 次……），同时 `Machines` 又调用各个子类。`Machines` 依赖 20 个类，被 12 个类引用。
  3. `Research.parsePos/formatPos/Key`：计算、AI 中心、研究组装机借用，形成研究和计算之间的环。
- 试算见附录 B：按层分（machine/layout/behavior/…）和按特性切片（power/heat/…）两种方案，在当前依赖下都会出现包级循环（分别涉及 6 个包和 7 个包）。所以顺序必须是先拆枢纽、再移动类，不是先建目录。
- 拆包会碰到：`META-INF/services` 的 3 个文件、生成脚本模板里写死的类名（`Gt6KtfruMachines.X`、`Gt6KtfruItems.stack` 等）；`@EventBusSubscriber` 类靠自动扫描，不受影响。移动类不改变注册 id，不影响存档。
- 整改：WP3。

**S2 一台机器没有单一登记点。**

- 新增或改动一台机器要同时改：声明、布局、零件、`machineTypes()`、`create()` 与 `owns()`（八个子类各有一套，七个另有 `behaviorTypes()`）、`Addon` 里的启动自检；再加方块状态、物品模型、两份语言文件。
- `create()` 是按字符串逐个比较的 if 链（`Machines.java:352–379`）。
- 启动自检是手写的 55 个 id（`Addon.java:84–164`），声明的方块有 408 个、物品 504 个，覆盖约 6%。漏补不会报错。
- 创造栏每次构建、两个客户端着色事件、首次使用建造杖，都会 `new Gt6KtfruMachines()` 把全部声明重建一遍（`Addon.java:49`、`MachineColors.java:24,29`、`StructureBuild.java:169`），`new Gt6KtfruMaterials()` 也是（`Addon.java:63`）。
- 整改：WP2（自检由登记表推导）、WP3（`Feature` 接口加缓存）。

**S3 `Machines` 是上帝类。**

- 1,107 行，混着：配方图声明、单方块配方机、蒸馏塔和矿处理系统、气体燃料机、动能行为机、变压零件、气体电池、掩膜对准机和 CNC 及其布局、压缩气罐、行为工厂分派。类注释（`:30–50`）按卡号写流水账（"Card 08 adds…"），这个文件 12 处，全仓库 22 处。
- `sides()`（`:489–549`）用 `id.startsWith("fluid_heater_")` 这类前缀匹配决定面配置；`basic()` 里 `id.startsWith("flaw_detector_")` 决定探伤仪标志（`:477`）。
- 平行数组表按下标对齐：`kineticBehaviors`（`:888–923`）、`transformerParts`（`:929–954`）、`heat()` 的 4 元素 `suffix` 配变长 `materials`（`:420–426`，第 5 个材料会越界）。
- `hopperInput = {64, 32, 128, 512}`（`:891`）不单调，`VoidHopper.INPUT` 里又抄了一份。是上游原值还是笔误，仓库里没有说明 [待核实]。
- 公有可变数组：`GAS_DRUMS`、`FLUID_PIPE_SIZES`、`ITEM_PIPE_SIZES`（`:129–140`）。
- 魔法数字：GUI 坐标、`1_000_000_000_000L`、掩膜对准机布局里的源 id（`31002`、`31110`、`31200`，`:627–644`）。
- 行为类里的常数和声明处重复，靠人工保持一致：`Miner.INPUT`、`VoidHopper.INPUT` 和 `Machines` 里的声明各写一份；而 `FuelBattery.rate()` 是行为类导出、声明处引用。两种做法并存。
- 三种布局写法并存：手写布局（`Machines`、`BatteryLayouts`、`TurbineLayouts`、`HeatLayouts` 等）直接调 `layout.cell(...)` 写循环；`LayerLayout.build` 只有生成文件在用；还有生成代码本身。
- 10 处声明引用同一张占位 GUI 贴图 `gascompressor.png`（9 个文件），每处各写一遍路径。
- `casing(...)` 辅助方法在 `Machines`、`HeatMachines`、`Turbines` 各一份。
- 整改：WP3。

**S4 行为层：复制粘贴加全局状态。**

- 同一套"找配方、付费、放输出、推进度"的逻辑，在 `Rooms`、`QuantumRelay`、`ResearchAssembler`、`LaserFusion` 里各写了一份（`FuelDeburner`、`FuelBattery` 有变体）：`kept()` 四份（`Rooms.java:220`、`QuantumRelay.java:175`、`ResearchAssembler.java:164`、`LaserFusion.java:251`）；进度循环至少三份（`Rooms.java:95–108`、`QuantumRelay.java:72–85`、`ResearchAssembler.java:57–70`）；辅助能量搬进主缓冲三份（`Wireless.java:83–87`、`Rooms.java:78–82`、`ResearchAssembler.java:40–44`）；"每 200 tick 掉一次电"至少两份（`Flywheel.java:70–79`、`LiquidBattery.java:118–127`）。
- 几份拷贝已经漂移：输出能否放下的检查，`Rooms` 检查每一个输出，`ResearchAssembler`、`QuantumRelay`、`LaserFusion` 只检查第一个物品输出；四份都不处理 `Line` 的 `outputChances` [待核实：这些图里有没有带概率的行]。
- 全局状态：5 个类里共有 13 个静态 `HashMap`（`Compute` 4、`QuantumRelay` 3、`Wireless` 3、`Research` 2、`Rooms` 1），以（维度，坐标）为键。全仓库没有任何服务器停止、世界卸载、玩家退出的处理。单人游戏里退出再进另一个世界，这些表还留着上一个世界的内容；已破坏的接收器、已退出玩家的待配对记录永不清除；`Wireless` 没有 `beforeBreak`。
- 同步策略不一致：`Compute`、`Research`、`QuantumRelay`、`Rooms` 用 `synchronized`，`Wireless` 的三张表不用。
- "每 tick 写 `lastTick`、读时判 20 tick 过期"的活性登记写了四遍，`EXPIRY_TICKS = 20L` 四份；`parsePos/formatPos` 两份（`Research.java:175–188`、`QuantumRelay.java:182–195`）；链接和坐标的序列化有三种格式、四份实现（`Wireless` 的 `维度,x,y,z` 用 `|` 分隔，`Compute` 的 `x,y,z` 用 `;` 分隔，`Research` 和 `QuantumRelay` 各一份单坐标）。
- 持久化状态要么是静态表里的内存副本（`Research` 的监视器、`QuantumRelay` 的握手），要么是每 tick 重新解析的 CSV 字符串。
- 4 个类按枚举或布尔开关分叉成两种行为（`Compute`、`LateBehavior`、`Research`、`Wireless`），类名也不表意。
- 30 个行为类里 23 个的 `canFill` 就是 `return false;`，各写一遍。
- 整改：WP4。

**S5 绕过接口的做法没有进账本。**

- 靠 GT6CE 内部命名约定的字符串匹配：`StructureBuild` 用路径含 `builder_wand` 认建造杖、用 `gt6ce:<零件名>` 当方块 id（`:147,157–160`）；`Addon.fluidItem` 假定 `gt6ce:fluid_item_<流体>`（`:179`）；`OilMiner` 解析 `fluid_spring_*` 方块路径推出流体（`:79–110`）；`"gt6ce"` 字面量 7 处、5 个文件；附属还在 `assets/gt6ce/lang/` 下发 139 条 `material.gt6ce.*` 翻译键，也就是别的模组的命名空间 [待核实：`MaterialDeclaration.displayName` 为什么不够]。卡 README 写明"不要改用 GT 内部类绕过去"，字符串约定同样是内部耦合，只是更隐蔽。
- 附属自己的识别方式也脆：`FuelBattery` 用 `getDescriptionId().contains("ktfru.item.battery.pole…")` 认电极（`:50,84–95`），`Flywheel` 用路径含 `flywheel`（`:103–106`）。
- 探针发现只存在于代码里，没进账本：(a) 行为机没有内置的配方运行能力，所以有 4–5 份拷贝 [推断]；(b) 状态只有 String/Int/Long 读写，链接表只能存成分隔字符串 [推断]；(c) 没有跟世界或服务器生命周期绑定的挂载点，所以是 13 张静态表 [推断]；(d) 大型配方控制器不吃建造杖，`StructureBuild` 自己监听右键（类注释自述）；(e) 没有"泉"的查询入口（`OilMiner`）；(f) 行为机必须声明一个能量输入，哪怕源里没有（`HeatMachines.java:77–84` 的 1 HU 假输入，类注释自述）。
- 整改：WP7 写账本；(a)(b)(c) 同时是 WP4 的设计输入。

**S6 实践与书面政策不一致。**

- `porting-strategy.md` 写的是"缺一格就保持缺口，不用原版零件或其他材料顶上"。生成产物里有：Naquadria 外壳换成 Trinaquadalloy 和 Tungstensteel 的同形态（`SourceRecipes.java:5253–5257`）、`plate Paper`、`plate Glass`、`paneGlass Black` 换成原版物品（`:5200–5203`）、涡轮 CNC 的 boule 输入用锭代替（卡 23 §16）。卡 23 记了，`GAP.md` 一条没列，这些替换会改变进度平衡。
- 飞轮：36 台飞轮箱（6 种乘 6 种材料）容量为 0，不存能量；`Flywheel.applyLoss` 对着永远为 0 的缓冲跑，`TransformBattery` 的 RU 容量也是 0。理由是"材料接口没有质量、工具属性，飞轮前缀没注册"（卡 08 第 56 行、卡 14 第 44 行、`Flywheel.java:13–17`）。但卡 05 现在写着材料已有 `mass()`、`toolQuality()`、`toolSpeed()`，卡 23 §13 写着飞轮前缀已注册。够不够实现储能没有核实，也没有人回头查过；`GAP.md` 里没有这一项。
- 整改：WP7（账本）、WP6（飞轮复查）。

**S7 命名与 id。**

- 4 个物品保留 `ktfru.item.battery.pole.*` 点号 id，贴图目录有 7 个带点（`ktfru.item.it` 等），和其余 500 个 snake_case 物品不一致。`gen_card22_items.py` 的 `FIXED` 表把这 4 个钉死，因为 `FuelBattery` 靠描述 id 匹配（S5）。
- 两套 id 风格：配方图名 `gt6ktfruaddon.recipe.xxx`，机器类型裸名 `sun_heater`；源数字 id 还渗进了名字（`quantum_coherence_chamber_wall_31111`，`SourceRecipes.java:5235`）。
- 按"加入时间"命名：`LateMachines`、`LateBehavior`；按卡号命名：`gen_card20_*`、`gen_card22_items.py`，类注释里的 "Card 12 controllers"。
- 整改：WP3（重命名）、WP5（脚本改名）、WP6（电极识别）。

**S8 失败策略是探针式的。**

- `Addon.onRecipesReady` 里有 25 处 `throw`；`SourceRecipes.register()` 末尾把所有未解析的栈汇总后抛出（`:64–67`）；`Recipes.register()` 还断言"临时行已不存在"（`:40–47`），那是卡 02 的历史。任何一个栈解析不了，异常就从 `onRecipesReady` 抛出去。GT6CE 在 `FMLCommonSetupEvent` 里调用它（`GregTechNeoForge.java:157`，核对于 `1d1f69fbb`），不吞异常：`GregTechAddons.dispatchRecipesReady` 把阶段置为 `FAILED`，包装成 `IllegalStateException("GT addon recipes failed: gt6ktfruaddon")` 重新抛出。也就是说，一个解析不了的栈会让游戏在模组加载阶段就崩，不是进世界以后。
- 探针需要这样响。发行版应该记录并跳过那一行，由开关决定严不严格（§8 第 4 条）。
- 整改：WP2。

### 4.2 生成代码与工具（G）

**G1 生成文件没有标注。** `SourceRecipes`、`GeneratedLayouts`、`Materials`、`Items` 四个文件共 6,943 行、567,118 字节，占 Java 的 46.9% 和 60.8%。只有 `GeneratedLayouts` 的名字提示了它是生成的；类注释写的是内容，没写由哪个脚本、哪个提交生成。2026-10-24 在 kTFRUAddon `6404ab69`、GT6CE `1d1f69fbb` 上重新生成核对：`Items`、`GeneratedLayouts`、`SourceRecipes` 与已提交的逐字节一致（`SourceRecipes` 的核对还用了原版 GregTech 6 源码和重打的运行包 jar）；`Materials` 不一致（G5）。手改的内容会在下次生成时丢掉，读的人无从知道。

**G2 脚本不可复现。**

- 4 个脚本里有 5 处写死的 `D:\Baobao\projects\dev\java\…`（`extract_machine_recipes.py:18`、`gen_card20_assets.py:6`、`gen_card20_layouts.py:7`、`gen_materials.py:8,239`）；`port_block_textures.py` 用 `ROOT.parent / "GregTech6CommunityEdition"`。现在的 GT6 源码树是 `D:\Projects\java\GregTech6`，目录名和层级都对不上。
- 输入不在仓库里：`kTFRUAddon/` 上游检出被 `.gitignore` 忽略，当前检出里没有；GT6 源码树在仓库外。
- 流水线顺序没有文档：`gen_card22_items.py` 写 `tools/own_items.json`，`extract_machine_recipes.py` 读它；`gen_card20_layouts.py` 写不入库的中间文件 `tools/card20-parts.txt`，`gen_card20_assets.py` 读它。WP0 已把顺序写进 `tools/README.md`。
- `port_block_textures.py` 和 `gen_card20_assets.py` 都写 blockstate 和模型：先跑 assets、后跑 textures 才得到已提交的结果；单独重跑 `gen_card20_assets.py` 会改动 94 个文件（47 个 blockstate 和 47 个物品模型退回通用模板）。
- 脚本按卡号命名；`gen_materials.py` 自称 one-shot，却仍能覆盖 `Materials.java`（G5）；它还带着一个调试用的 `probe()`，每次运行都扫描整个 GT6 源码树找 "Methanol"，WP0 已删。
- `extract_machine_recipes.py` 还读原版 GregTech 6（1.7.10）源码里的一个文件（`work/gt6-ref/gregtech6/.../Loader_MultiTileEntities.java`，用来还原电线和电缆的数字 id）。这份副本不在仓库里，GT6CE 仓库的文档也没提到它。核对时本机没有；之后你把它放到了 `D:\Projects\java\gt6_source_code`，`paths.py` 现在默认去上级目录的 `gt6_source_code` 找。有了它，这个脚本重新生成的 `SourceRecipes`（689 行）与已提交的逐字节一致。
- 没有任何检查比较"已提交的产物"和"重新生成的产物"。2026-10-24 手工核对的结果见 G1。

**G3 映射表藏在 Python 字符串里。** `block()` 的别名表、`gt()` 的 Naquadria 替代、`form()` 的 Paper/Glass 替代，都在 `extract_machine_recipes.py:1852–1906` 的模板文本里。改一条映射要改 Python 再重新生成 309 KB 的 Java。

**G4 资源 JSON 大量重复。** 639 个方块模型只有 125 种内容，782 个物品模型 588 种（其中 130 个完全相同，是占位），278 个 blockstate 213 种，935 张 PNG 535 种。可以用共享父模型或 datagen 收敛，不是门槛。

**G5 `Materials` 是一次性生成后手改过的文件。** 重新生成会丢掉 `prefixes()` 声明、`SiliconeRubber`、`SBR`、`PEEK` 的 `.polymer()`（卡 23 §6：缺了它启动时报形态校验失败）、`Bittern` 液体和对应的 `assets/gt6ce/lang` 条目，以及十几个材料的 `.gas()`、`.liquid()` 标志。`gen_materials.py` 现在要加 `--force` 才会运行（WP0）。这个文件应当按手写文件对待，不进 `generated/`。

### 4.3 构建与验证（B）

**B1 没有测试。** 没有 `src/test`，`build.gradle` 里没有测试配置，`build` 只做编译、资源处理和打包。启动是唯一的验证手段：卡 23 记录的十来处启动失败（缺 `.polymer()`、GUI 矩形重叠、布局名带点、同一机器类型混用能量、前缀物品和方块重名、实验与普通配方共图……）都是起服务端才发现的。其中至少一部分是 SDK 构建器的校验，纯 JVM 测试调用一遍各 Contributor 就能发现：SDK 核心 API 的 54 个类没有任何 Minecraft 或 NeoForge 导入（核对于 `1d1f69fbb` 的 `core/src/api`），构建器在 `build()` 里校验（机器类型和 GUI 路径的正则、控件重叠等）。

**B2 本地不可构建。**

- `libs/gt6ce/*.jar` 被忽略，仓库里只有 `build-commit.txt` 和 `.sha256`；产出这两个 jar 的步骤只存在于 `build.yml` 里。
- `update-gt.ps1:21` 会清空 `libs/gt6ce/`，再装入 0.8.0 的产物；README 第 11 行自己说这些产物装不上。WP0 已删除这个脚本。
- 构建 GT6CE 需要：Gradle 用 JDK 17、工具链提供 JDK 17 和 21（`GregTech6/gradle.properties` 的 `JAVA17_HOME`、`JAVA21_HOME`），以及 Python（`prepareNativeTools` 调 `tools/native_tool.py`）。GT6CE 的 `core` 要求 17 工具链（`core/build.gradle:6`），所以 21 单独不够。这台机器最初只有 JDK 21 和 25；后来另一个会话的 Gradle 构建把 Temurin 17.0.20.1 下载进了 `~/.gradle/jdks`，脚本会在那里找到它。
- GT6 源码树 `D:\Projects\java\GregTech6` 在 `main@8059bd00b`，工作区干净；固定提交 `1d1f69fbb` 在它的 `main` 历史里，可以直接克隆出来，不用动这棵树。`build.yml` 和 `update-gt.ps1` 用的 `SaltNya/GregTech6` 现在重定向到 `GregTech6CE/GregTech6`（这棵树的 `origin`）；WP0 把 `build.yml` 指向后者。

**B3 CI 只编译。** `build.yml` 最后一步是 `gradlew build`；GT6CE jar 与记录的校验和不一致只发警告（`:104–109`）；缓存未命中时现场构建 GT6CE，超时 45 分钟。

**B4 提交粒度。** 36 个提交（10-19 到 10-23），其中 4 个各改动 460–1,343 个文件，生成物、手写代码和文档混在一起；提交按卡号组织，不按变更意图。事后 bisect 和 review 都很难。

**B5 构建模板残留。** `build.gradle:15–19` 的 `src/generated/resources` 和 `.bbmodel` 排除、`:111–122` 的 `publishing`（发布到 `file://repo`）都没有对应的东西。

### 4.4 缺陷（D，来自阅读）

| 编号 | 位置 | 现象 | 后果 |
| --- | --- | --- | --- |
| D1 | `Wireless.java:99–105`、`:214–236` | `Inbox.give()` 不扣减 `ROOM`；两个塔链接同一个接收器时，各自按同一份剩余空间发送；接收器所在区块已加载但不 tick 时，`ROOM` 保持旧值。塔先付费（`:104`）再 `give`（`:105`）。 | 塔付出接收器收不下的能量，能量被销毁（取决于 `generate` 是否截断 [推断]）；`WAITING` 里在途的能量在停服时丢失。 |
| D2 | `QuantumRelay.java:43–47`、`:107–133` | 握手只持久化了一半：伙伴坐标写进 B 的状态，A 通过内存里的 `NEW_PARTNERS` 在下一次 tick 才知道。 | 远端中继的区块没加载（远距离配对的常态）时停服，只剩单向链接，`isPaired` 永远为假；`NEW_PARTNERS` 里的残留项还会接到之后放在同一坐标的另一台中继上。 |
| D3 | `Rooms.java:98–100`、`QuantumRelay.java:75–77`、`ResearchAssembler.java:60–62` | `progress >= duration` 分支不更新 `working`。 | 输出放不下时机器一直显示"工作中"。 |
| D4 | `MachineColors.java:53–57` | 每次着色查询都调用 `GregTechMaterials.find(material)`。 | 区块网格重建的热路径上做查询 [未测量]。 |
| D5 | `Wireless.java:159–179`、`Research.java:106–128`、`QuantumRelay.java:51`；`Rooms.java:88`、`ResearchAssembler.java:50`、`QuantumRelay.java:62`、`LaserFusion.java:125` | 每 tick 重新解析 CSV；每 tick 做配方 `find()`，机器空闲时也做。`Miner`、`LateBehavior`、`Flywheel` 每 tick 写状态。 | 每台机器每 tick 的分配和查找 [未测量]；`state().setX` 是否每次都标脏并同步，待核实 SDK [待核实]。 |
| D6 | `FuelBattery.java:50`、`:84–95` | 用 `getDescriptionId().contains(...)` 认电极和电解液配对。 | 靠翻译键判断物品身份；是 S7 里点号 id 的根因。 |
| D7 | `SourceRecipes.java:5156`、`Recipes.java:174` | 同名 `add()`，两个相邻 `long` 的顺序相反：`(euPerTick, duration)` 对 `(duration, euPerTick)`；`deburn(circuit, eu, duration, special)` 又是第三种写法。 | 手写或生成时互换而不报错。 |
| D8 | `Machines.java:420–426`；`Miner.java:163–167`、`VoidHopper.java:123–127`、`Flywheel.java:108–112` | `heat()` 变长材料配定长后缀；三处 `index()` 把越界档位静默钳到 0。 | 配置错误被掩盖，或者数组越界。 |
| D9 | `Machines.java:891`、`VoidHopper.java:26`；`Materials.java:35–36`、`:65`、`:68` | 虚空漏斗功率表不单调；`Propanediol` 的化学式和 `Propanol` 相同，`Butadiene` 写成 `CH₂=CHCH=CH2₂`，`EpoxyResin` 用 `.metal()`，显示名空格不统一。 | 看起来像笔误，也可能是上游原样；仓库里没有"逐字来自上游"的标记 [待核实]。 |

### 4.5 文档、账本与署名（R）

**R1 与事实不符的条目（已逐条核对）。**

- `README.md:19` 说 `build-commit.txt` 是 `9c67ceaa4`；实际是 `1d1f69fbb267ad31548b1fcb1ec1168435b67bf3`。`docs/porting-strategy.md:14` 同样。
- `README.md:5` 只写到卡 03，实际已到卡 23；`neoforge.mods.toml` 的描述也只写了卡 02–03 的范围。
- `docs/porting-strategy.md:5` 说"计划卡先不开工""卡 01 的验收保持未勾"；卡 01–22 已关闭。
- `docs/porting-strategy.md:13–14,102` 写的是 `D:\Baobao\projects\dev\java\…` 的绝对路径。
- `docs/ktfruaddon-gt6-surface.md:5–6` 对照的是 `75cebb71`，策略文档要求刷新到 `6404ab69`；`:50` 保留着从 CrucibleCraft 拷来的"CC 不移植这 7 台单方块；曾经落地的注册已移除"，本仓库恰恰注册了这 7 台。
- `Gt6KtfruFlywheel` 的类注释、卡 08 的"材料接口没有质量"，已经和卡 05、卡 23 对不上（S6）。
- 没能核对：`README.md` 的 `2311f8c` 版第 19 行提到 `addon/behavior-energy` 上的 `0f77a888`，这个提交在 GT6CE 仓库里查不到（`git cat-file` 找不到对象）[待核实]。

**R2 账本有两份。** `GAP.md:3` 自己写着"以卡 05 为准"，两边各列一遍缺口，卡 23 末尾又列一遍。

**R3 文档体量与形态。** Markdown 190,808 字节，约为 Java 的 20%，大部分是带日期的卡片日志，和提交历史重复。缺少的是：架构概览、"怎么新增一台机器"、"怎么重新生成"、"怎么构建"。Java 注释里也夹带过程信息（"Card 08 adds…"，22 处）。

**R4 署名与定位。**

- `src/main` 下的 java、json、toml 文件里没有 `kuzuanpa`、版权或 SPDX 字样；仓库根没有 `NOTICE` 或 `CREDITS`；`authors` 只有 `lombinaxmasson`。
- README 第 3 行以"不是 kTFRUAddon 1.7.10 源码的移植"的口径链接上游；而仓库里有：689 行从上游转写的配方、504 个物品条目、布局字符串、语言条目，以及 `port_block_textures.py` 从 kTFRU 和 GT6 资源复制的贴图。
- AGPL-3.0 对保留版权和变更声明有要求，具体义务请自行核实，这不是法律意见。不管许可证怎么写，说清楚这些内容来自哪里是应有之义。

**R5 "closed" 的含义。** 卡 02–04 由你确认关闭，玩法验收仍未勾；卡 23 还有两项玩法验收未勾。"closed"在这个仓库里是"代码落地"，不等于"在游戏里验证过"；卡 README 没写明，也没有"未验收清单"。

**R6 玩家可见的缺口。**

- 只有 12 条普通合成表（各机器族的最低一档，"铁块围一圈，中心各不相同"），其余机器靠创造栏，或者 689 行机器配方里的产物；没有"哪些方块能在生存里得到"的清单。
- `zh_cn.json` 的 1,067 条里有 487 条和英文一字不差，其中 418 条是物品（物品共 504 条）。
- 130 个物品模型是占位（`GAP.md` 已记录）。

## 5. 基线指标（整改前）

| 指标 | 现值 | 目标 |
| --- | --- | --- |
| 根包 Java 文件数 | 53 | ≤ 2 |
| 最大手写类行数 | 1,107（`Machines`）；另有 `HeatLayouts` 529 | ≤ 400 |
| 类级强连通分量 | 20 个类 | 无 |
| 带头部标注的生成文件 | 0 / 3（`Materials` 已是手改文件，不算） | 3 / 3 |
| 全局可变静态 Map | 13 | 0，或由带生命周期的服务持有 |
| `kept()` 副本 | 4 | 1（`RecipeRunner` 内） |
| 手写启动自检 id | 55 | 0 |
| 全量声明重建 | 4 处 `new Gt6KtfruMachines()`、1 处 `new Gt6KtfruMaterials()` | 0（登记表构建一次） |
| 生成脚本里的硬编码绝对路径 | 5 处，4 个脚本 | 0 |
| 测试 | 0 | 契约测试，CI 里运行 |
| 文档里已确认与事实不符的条目 | R1 共 9 条 | 0 |
| 署名文件 | 无 `NOTICE`；`src/main` 里无任何署名 | README、`NOTICE`、生成文件头 |
| `zh_cn` 与 `en_us` 文本相同 | 487 / 1,067 | 逐步消化（非门槛） |

## 6. 最低质量线（提议，待确认）

- [ ] **M1 克隆即可构建。** 按文档里的一组步骤，从干净克隆得到能 `gradlew build` 的状态；`update-gt.ps1` 要么修好，要么删除。WP0 已按这个做到，在没有 jar 的工作区里验证过；还要用全新克隆验证一次，才打勾。
- [ ] **M2 包结构。** 根包不超过 2 个类；其余按职责分包；包级无循环；跨包只用公开 API；没有超过 400 行的手写类（例外在类注释里说明）。
- [ ] **M3 单一登记点。** 新增一台机器只改一个特性包加资源；启动自检由登记表推导，没有手写 id 清单。
- [ ] **M4 行为层。** 配方运行、辅助能量中转、链接表和活性登记各一份实现；没有不受管理的全局静态状态（要么没有，要么由带生命周期的服务持有，服务器停止时清空）。
- [ ] **M5 生成代码。** `SourceRecipes`、`GeneratedLayouts`、`Items` 在 `generated` 包；文件头写明来源提交、生成命令、"勿手改"；脚本在干净克隆里按文档能跑（路径来自配置，缺输入时明确报错）；替代项清单随生成文件输出。
- [ ] **M6 测试。** CI 跑不依赖游戏窗口的契约测试；校验和不一致要么失败，要么写明为什么只警告。
- [ ] **M7 已知缺陷。** D1–D8 修掉，或在账本里写明为什么不修；D9 核对完。
- [ ] **M8 文档与事实一致。** R1 全部修正；`GAP.md` 与卡 05 合成一份；S5、S6 的条目写进账本；列出 `zh_cn` 未翻译清单。
- [ ] **M9 署名与来源。** README 如实说明这是 kTFRUAddon 的 GT6CE 适配，附上游链接、许可证和对照提交；`NOTICE`（或 `CREDITS.md`）列出上游代码、数据、贴图的来源；生成文件头写明来源。

不是门槛但建议做：G4 资源去重、点号旧 id 迁移、`zh_cn` 翻译、配方数据化。

## 7. 整改工作包

大小：S 小、M 中、L 大，不估工期。

### 做法约束

1. 每个工作包拆成小提交。纯移动（`git mv`，只改 `package` 和 `import`）、重构、行为修复分开提交；生成物和手写代码分开提交。
2. 每个提交都能 `gradlew build`；WP2 之后还要契约测试通过。
3. 生成文件只通过脚本改；要改映射表，就改 `tools/` 里的输入，再重新生成。
4. 整改期间不新增机器和配方。碰到接口缺口，写进账本，不写绕过代码。
5. 每个提交在 §9 勾掉对应项，提交信息写问题编号。
6. 不改写已有历史。

### WP0 本地可构建、可复现（M）

依赖：无。

1. 写 `tools/build-gt6ce.ps1`，复刻 `build.yml` 的 SDK 构建：从 `libs/gt6ce/build-commit.txt` 读提交；用 `git worktree` 在临时目录检出，不碰 `D:\Projects\java\GregTech6` 的当前分支；`prepareNativeTools` 之后跑 `:multiversion:1.21.1-neoforge:addonSdkJar` 和 `:jar`；拷到 `libs/gt6ce/`；校验 `.sha256`。
2. 先弄清 GT6CE 构建的 JDK 要求（B2）：本机有 JDK 21 和 25，没有 17。装 Temurin 17，或者验证 21 能不能单独构建 NeoForge 节点（§8 第 8 条）。
3. 删除 `update-gt.ps1`，或者改成调用上面的脚本；README"开发"一节改成真实步骤。
4. 生成脚本的路径集中到一处配置（环境变量或 `tools/paths.py`），缺输入时报明确的错；去掉 5 处 `D:\Baobao\…`。
5. `build.gradle` 去掉模板残留（B5）。

完成标准：干净克隆，按文档的一组步骤后 `gradlew build` 通过（要在你的机器上验证）。

**2026-10-24 进展：**

- 步骤 1：脚本实际用本地克隆（`git clone --no-checkout`，源仓库只读），不用 `git worktree`，因为 worktree 会往源仓库的 `.git` 里写元数据。另有 `-NoBuild`、`-DryRun`、`-UpdateChecksum`、`-AutoDownloadJdk`。
- 步骤 2：GT6CE 的 `core` 要求 17 工具链，21 单独不够（§8 第 8 条已定）。
- 步骤 3：`update-gt.ps1` 已删除；README"开发"一节已改，去掉了过期的 `9c67ceaa4`。
- 步骤 4：`tools/paths.py` 集中全部输入位置，缺输入或提交不对时脚本先停下，不写文件；`tools/README.md` 记录了输入、运行顺序和复现性核对结果。`gen_materials.py` 加了 `--force` 保护，并删掉调试用的 `probe()`。
- 步骤 5：`build.gradle` 的模板残留已删；SDK jar 改按通配符找，缺 jar 时 `checkGt6ceJars` 报出构建说明（配置缓存下成功和失败两条路径都试过）。
- 另外：`build.yml` 改指向 `GregTech6CE/GregTech6`；`.gitignore` 加了 `.gt6ce-src` 和 `tools/card20-parts.txt`。
- §2 说的"没有编译，也没有运行"是审计时的状态；WP0 之后本机可以构建，也可以重新生成。

验证结果（本机，从没有 jar 的状态开始）：

| 项 | 结果 |
| --- | --- |
| `tools\build-gt6ce.ps1` | 成功。`prepareNativeTools` 6 分 10 秒，两个 jar 21 分 1 秒（同时有另一个 Gradle 构建在跑，脚本以低优先级运行） |
| GT6CE 运行包 jar | sha256 与记录值一致（`b82e423c…`）；SDK jar 185,356 字节，与卡 23 的记录一致 |
| `gradlew build`（插件） | 成功，5 分 1 秒，`checkGt6ceJars` 先于 `compileJava` 运行；第二次 2 秒，复用配置缓存 |
| 生成脚本 | `Items`、`GeneratedLayouts`、`SourceRecipes`、assets 加 textures 与已提交的文件逐字节一致；`Materials` 不一致（G5） |

没覆盖到的：真正的全新克隆（改动没提交，只能在没有 jar 的工作区里验证）；`-AutoDownloadJdk`（本机已有 JDK 17，没走这条路径）；`runClient` 和服务端启动（留给 WP2 的冒烟脚本）。

### WP1 署名与定位（S）

依赖：无，可以马上做。

- README 首段改写：这是 kTFRUAddon 的 GT6CE 适配，同时是附属 API 的探针；链接上游，说明许可证和对照提交 `6404ab69`，列出搬过来的内容类别（配方行、物品、贴图、语言、布局）。
- 新增 `NOTICE` 或 `CREDITS.md`。
- `neoforge.mods.toml` 的描述同步；`authors` 要不要写上游作者，见 §8 第 5 条。

完成标准：任何打开仓库的人，一分钟内能看出哪些内容来自哪里。

**2026-10-24 进展：**

- README 首段：改成"kTFRUAddon 的 GT6CE 适配，同时是附属接口的探针"，链接上游，写明协议、对照提交 `6404ab69`，列出搬过来的内容（配方行、物品条目、材料表、多方块布局、物品名和材料中文名、贴图），并指向 `NOTICE`。原来那句"不是移植"改成"机器行为不是 1.7.10 源码的逐类移植"。README 其余过期的条目（R1）留给 WP7。
- `NOTICE`（新）：上游的作者、仓库、协议和对照提交；每一类搬来的内容在哪里、有多少、用哪个脚本进来的；没有逐项记录来源的部分；GT6CE 的协议和资源说明；原版 GregTech 6 只读一个文件。数字都核对过：689 行（`tools/README.md` 的复现记录）、504 个物品、230 个材料声明、31 个布局，上游 412 个 Java 文件里 359 个带"This class was created by <kuzuanpa>"文件头。
- 贴图按字节哈希比对：974 个文件里，940 个与上游检出相同（其中 384 个同时与 GT6CE core 相同），28 个只与 GT6CE core 相同，6 个两边都没有。比对脚本是 `tools/texture_provenance.py`（只读），数字可以复算。
- `neoforge.mods.toml`：`authors` 写 `kuzuanpa, lombinaxmasson`（§8 第 5 条，你定的）；描述改成如实的版本，原来还写着"单方块配方机器和流体燃料机"。生成的 toml 用 `tomllib` 解析过；`gradlew build` 通过，冒烟 79 秒通过。
- 顺带发现：`displayURL` 和 `issueTrackerURL` 指向 `lombinaxmasson/gt6-ktfruaddon`，GitHub 重定向到 `GregTech6CE/gt6-ktfruaddon`（仓库已转移），能用，没改。GitHub 上仓库的简介还写着"a silicon crystal puller and a hydrogen burner"，是过期的；改它是公开动作，等你定。

没做的：

- 生成文件头写来源（M9 的最后一项），在 WP5。
- `NOTICE` 不随 jar 发布，jar 里只有 AGPL 全文；要不要放进去，等你定。

### WP2 安全网：契约测试和登记表推导的自检（M）

依赖：WP0（要有 SDK jar 才能编译测试）。

1. 加 `src/test`（JUnit 5）。声明部分（`materials()`、`recipeTables()`、`addonMachines()`、`structureLayouts()`、`layoutParts()`）用纯 JVM 测试，不需要 Minecraft（B1 已核对）。`create(type)` 会加载行为类，行为类引用 Minecraft 类，测试类路径上要有它们：先试 ModDevGradle 给主源集的类路径，不行再用它的 `unitTest` [待核实]。配方注册（`onRecipesReady`）要游戏注册表，纯 JVM 测不了，仍靠起服务端到 `Done` 的冒烟脚本（也放进 WP2）。
2. 契约测试：对每个 Contributor 调用 `materials()`、`recipeTables()`、`addonMachines()`、`structureLayouts()`、`layoutParts()`、`machineTypes()`；断言 id 唯一、布局名匹配 `[a-z0-9_]+`、`create(type)` 对每个 `machineTypes()` 返回非空、布局引用的零件名都在 `layoutParts()` 或已知 GT 零件里、配方行引用的配方图都存在。卡 23 的几类启动失败（缺 `.polymer()`、GUI 矩形重叠、布局名带点、同一类型混用能量）应该先在这里变红。
3. 文件头检查：`generated/` 下每个文件都有头部。
4. `Addon.onRecipesReady` 里 55 个 id 的自检换成遍历登记表；失败策略加开关（§8 第 4 条）。
5. CI 跑 `gradlew test`；校验和不一致改为失败，或者在 `build.yml` 里写明为什么只警告。

完成标准：故意漏一个 `create()` 分支，或者写错一个布局名，测试变红；去掉手写 id 清单后，自检覆盖全部机器。

**2026-10-24 进展：**

- 步骤 1：测试在 `src/test/java/com/lombinaxmasson/gt6ktfruaddon/`，共 22 个 JUnit 5 测试，`gradlew test` 约 4 秒。`gradlew build` 会跑它们，所以 CI 里的 `./gradlew build` 不用改。测试类路径直接用主源集的编译类路径（Minecraft、NeoForge、SDK 都在），不需要 `unitTest`，`create(type)` 在纯 JVM 里能实例化行为类。物品注册表在纯 JVM 里起不来（`Bootstrap.bootStrap()` 要 FML 的 `LoadingModList`），所以物品只测资源，注册靠冒烟。
- 步骤 2：
  - `MachineContractTest`（11 项）：声明都能构建；方块 id 在机器、零件、气瓶、管道之间唯一；同一机器类型的方块主能量类型一致；`machineTypes()`、`create()` 和行为机器三者一致，未知类型被拒；配方表名唯一；机器只用已声明的或 GT6CE 内置的配方图；大型配方机器有以类型命名的布局；布局名唯一且都有机器类型在用；布局只引用已声明的零件或 GT6CE 的内置零件（名单取自运行包 jar 里的物品模型）。
  - `ResourceContractTest`（4 项）：中英文语言键一致；每台机器有 blockstate、物品模型和两种语言的名称；零件和气瓶有名称；结构提示行的键两种语言都在。
  - `ServiceRegistrationTest`（3 项）：三个 `META-INF/services` 都能加载到提供者。`MaterialContractTest`（2 项）：材料名和前缀名唯一。`ItemResourceTest`（2 项）：物品模型都有名称，名称都有模型。
  - 卡 23 点名的失败：同一类型混用能量有单独的测试；GUI 矩形重叠和各类名称格式是 SDK 构建器在 `build()` 里拒绝的，构建声明就会失败；缺 `.polymer()` 的构建器不检查，是 GT6CE 加载材料时才报，只有冒烟抓得到（下表）。
  - 测试第一次运行就找到一个真缺陷：`quantum_gold_chamber` 和 `quantum_observation_tower` 的结构提示第 1 行，两种语言里都缺。已各补两条；文案是新写的，待核对。
- 步骤 4（自检部分）：`Gt6KtfruAddon.requireDeclaredIdsRegistered()` 取代了手写的 55 个 id（53 个方块、2 个物品）。它从 `Gt6KtfruMachines` 的声明（机器外壳、布局零件、气瓶、两类管道的尺寸）和 `Gt6KtfruItems.BY_PATH` 推出要查的 id，在 `Gt6KtfruRecipes.register()` 之后检查，缺的一次报全。新增机器不用再改它。现在覆盖 912 个 id：408 个方块和 504 个物品，旧清单的 55 个都在里面（逐个核对过）。
- 更正：审计里原来写的"44 个 id"是按字符串字面量数的，漏了用常量给出的 id；按 `stack(` 调用数是 55。正文、基线表和附录的复核命令都已改正。
- 顺带发现：`en_us` 里有 6 个 `block.` 名字没有对应的方块（`electric_controller`、`network_cable`、`wired_controller`、`wireless_battery`、`wireless_controller`、`wireless_receiver`），代码里找不到声明它们的地方。是不是上游遗留的无用条目没有核对，没动。
- 冒烟：`tools/smoke-boot.ps1` 跑 `gradlew runGameTestServer`（`build.gradle` 新增的运行配置）。判据是日志标记，不看退出码，原因和用法在 `tools/README.md`。`runData` 试过，不能用：数据生成运行不触发通用初始化事件，GT6CE 没派发附属也会"通过"。GameTestServer 不要 EULA；附属没有 GameTest，结尾的 `No test functions were given!` 是预期的。
- §2 B1 说的"没有测试"是审计时的状态。

验证结果（本机）：

| 项 | 结果 |
| --- | --- |
| `gradlew build` | 成功；22 个测试，0 失败 |
| `smoke-boot.ps1`（没做故意破坏时） | 通过，约 80 秒 |
| 契约测试的故意破坏（每次一处，改完还原） | 6 处都有测试变红：删掉 `create()` 的 `gas_compressor` 分支；布局里写错零件名；激光切割机改用热混合机的 id；删掉 `zh_cn` 的一条结构提示；材料服务文件指向不存在的类；藏起 `sun_heater` 的 blockstate |
| 冒烟的故意破坏（同上） | 3 处都变红：布局里写错零件名（`uses unknown part`）；多声明一个 GT6CE 不会造的管道尺寸（自检点名 `pipe_gigantic_teflon`）；去掉 `SiliconeRubber` 的 `.polymer()`（GT6CE 报 `form 'plate / SiliconeRubber' is not valid for that material`） |

没做的：

- 步骤 3 的文件头检查：现在没有 `generated/` 目录，生成文件也没有头部，等 WP3、WP5 之后再加。
- 步骤 4 的失败策略开关（§8 第 4 条）。
- 步骤 5：冒烟还没进 CI。`smoke-boot.ps1` 是 Windows PowerShell 脚本，`build.yml` 在 Ubuntu 上跑，需要一份可移植的版本，本机没法验证。校验和不一致仍然只是警告：可复现性只在本机测过，要等 Linux CI 的实测再决定改不改成失败。
- 没覆盖到：`runClient`；物品注册只有冒烟盖到，没有单元测试；`create()` 返回的行为类只测了能实例化，没跑生命周期（WP4）。

### WP3 包结构和登记表（L）

依赖：WP2。

顺序不能换，每步都保持构建通过：

1. **拆枢纽。** `MODID` 移到叶子类 `Ktfru`；34 个配方图和机器类型常量从 `Machines` 移到叶子类 `recipe.RecipeMaps`，同步改生成脚本模板，重新生成，或者对已生成文件做同样的替换；`parsePos/formatPos/Key` 移到 `core`。完成后重跑依赖分析，类级强连通分量应该消失。
2. **登记表。** 引入 `Feature` 接口：每个特性提供 `recipeTables`、`machines`、`layouts`、`parts`，以及 `Map<String, Supplier<MachineBehavior>> behaviors()`；`machineTypes()` 和 `create()` 都由这张 Map 得出；登记表只构建一次并缓存。八个子类依次改成 `Feature`，删掉 `owns`、`create`、`behaviorTypes`。
3. **切 `Machines`。** 按特性切出去（`processing`、`gas`、`kinetic`、`semiconductor`，气体燃料机和变压零件并入 `power`），一个特性一个提交；`sides()` 改成数据表（`SideProfile` record），平行数组改成 record 表；`LateMachines` 按领域拆开 [待核实：这个类没通读，拆法按类注释和常量名推断]。
4. **纯移动。** `git mv` 加改 `package`、`import`，同步 3 个 `META-INF/services` 文件和脚本模板里的类名；行为不变。
5. **去前缀。** 单独一个提交去掉 `Gt6Ktfru` 前缀（§8 第 2 条）。
6. **依赖方向检查。** ArchUnit 或 `tools/` 里的脚本（附录 B 的试算脚本可以作为原型）：`core` 不依赖任何特性；特性之间只允许 `research` 依赖 `network`；生成代码只依赖 `core`、`recipe`；顶层接线（`KtfruAddon`、登记表、`client`、`event`）不被依赖。

目标结构（提议，开工前按依赖分析微调；类到包的映射见附录 B）：

```
com.lombinaxmasson.gt6ktfruaddon
├─ KtfruAddon            @Mod 入口，只做接线（原 Gt6KtfruAddon）
├─ Ktfru                 MODID 等常量，叶子类，不依赖任何东西
├─ core/                 与具体机器无关的公共件
│  ├─ feature/           Feature 接口、Behaviors（行为表）
│  ├─ layout/            LayerLayout
│  ├─ behavior/          RecipeRunner、EnergyBridge、LossTimer、TtlRegistry、PosCodec
│  └─ Gt6ce              GT6CE 命名约定的唯一出处（"gt6ce" 命名空间、fluid_item_、builder_wand、fluid_spring_）
├─ registry/             Features（登记表，认得所有特性）、机器的 ServiceLoader 入口
├─ recipe/               Recipes（手写行）、RecipeMaps（配方图名常量）
├─ generated/            SourceRecipes、GeneratedLayouts、Items
├─ content/              Materials（一次性生成后手改过，按手写文件处理）
├─ processing/           单方块配方机、蒸馏塔、矿处理系统、催化反应器
├─ gas/                  压缩气罐、压缩机、鼓、管道
├─ kinetic/              虚空漏斗、矿机、水车、手摇、飞轮
├─ power/                燃料机、涡轮、发电机、锅炉、电池、变压零件
├─ heat/                 太阳炉、潮汐、油矿、地幔、脱燃、坩埚
├─ semiconductor/        掩膜对准机、CNC
├─ research/             研究监视器、显微镜、研究组装、微重力房间、AI 中心
├─ network/              计算集群、无线计算、无线能量、量子中继
├─ fusion/               激光聚变、托卡马克
├─ client/               MachineColors
└─ event/                StructureBuild
```

完成标准：M2、M3。

风险与注意：

- 生成脚本模板和类名要同步，用"重新生成再 diff"验证，需要上游检出可用。
- 33 个包级可见成员需要公开或包装，数量不大。
- 移动类不改变注册 id，存档不受影响；任何 id 重命名（S7 的点号 id）才需要别名。
- 历史卡片里的文件名引用不改。

**2026-10-24 进展：**

决定（§8 第 1、2 条）：阈值你交给我定，我沿用 §6 的数字：根包不超过 2 个类，包级无环（第 6 步用 ArchUnit 在测试里卡死），手写类不超过 400 行。生成文件不算，例外在类注释里写明理由，不放宽数字：现在最大的手写类是 `Machines` 1,107 行和 `HeatLayouts` 529 行，按特性切完预计都能压到 400 行以内。去前缀也定了，做第 5 步。

第 1 步（拆枢纽）：

- 三处叶子类：`Ktfru`（`MODID`）；`recipe.RecipeMaps`（34 个名字：本仓库声明的 32 张配方图，加两张 GT 内置燃料图 `GAS_FUEL`、`ENGINE_FUEL`；原来的 `Machines.map(...)` 改名 `RecipeMaps.table(...)` 放在这里）；`core.behavior.DimPos` 和 `PosCodec`（`Research` 里的 `Key`、`formatPos`、`parsePos`，`Compute` 里重复的 `Key` 并进来）。
- `LateMachines`、`ResearchMachines` 里的配方图常量也搬进 `RecipeMaps`，其中 12 个换了更清楚的名字（`LASER` 变 `LASER_FUSION`，`RELAY_MAP` 变 `QUANTUM_COMMUNICATION`，`FUSION_TOKAMAK_EXP` 变 `FUSION_TOKAMAK_EXPERIMENTAL` 等）。字符串值一个没变，脚本改之前逐个比对过。
- 原计划把"机器类型常量"也放进 `RecipeMaps`，实际没放：机器类型不是配方图。六个热机类型（`SUN_HEATER` 等）搬进了 `HeatMachines`，`machineTypes()` 也改成像别的特性一样调 `HeatMachines.behaviorTypes()`。
- 生成脚本同步：`extract_machine_recipes.py` 的 `MAP_FIELD` 和模板，`gen_card22_items.py`、`gen_materials.py` 的 `MODID`。`Items` 和 `SourceRecipes` 用脚本重新生成，与替换后的文件逐字节一致（`SourceRecipes` 里 689 处配方图引用，另有 10 处 `MODID` 引用）。

验证（本机）：

| 项 | 结果 |
| --- | --- |
| 声明快照，改前改后 | 逐字节相同（SHA-256 一致）。做法：临时测试把三个 Contributor 返回的全部声明（278 台机器、80 个布局、112 个零件、32 张配方表、230 个材料等）和工厂对 99 种机器类型造出的行为类（含字段值）转成文本，977 行；临时测试不入库 |
| `gradlew build` | 成功；22 个测试，0 失败 |
| `smoke-boot.ps1` | 通过，83 秒 |
| 类级依赖分析 | 依赖边 82 条变 61 条；强连通分量从 20 个类变成 `Wireless` 和 `WirelessMachines` 一对（同一特性，WP4 拆 `Wireless` 时消除）；引用 `Machines` 的类从 12 个变 3 个（`Addon`、`MachineColors`、`StructureBuild`，都是 `new Gt6KtfruMachines()`，第 2 步的登记表解决）；跨类用到的包级成员从 33 个变 22 个 |

附录 B 的分包试算要等第 3 步切完 `Machines` 之后再按最终划分重跑，现在 `Machines` 里还混着五个特性的声明。

第 2 步（登记表）分三个提交：

- 每个特性类把"机器类型对行为"写成一张 `Behaviors` 表，`behaviorTypes`、`owns`、`create` 都读这张表。原来 `create` 里 switch 的默认分支（计算发射器、研究台、模式识别中心、感应接收器）现在逐个写出类型；`Machines` 自己的 22 个类型也一样。
- `Feature` 接口（`core.feature`）和登记表 `registry.Features`：特性只写自己有的（配方表、机器、布局、零件、气体鼓、管道、行为），登记表合并，每张列表只构建一次。八个特性类和原来 `Machines` 的内容（现在叫 `Gt6KtfruBaseMachines`，第 3 步切开）都成了 `Feature`；生成的布局由很薄的 `Gt6KtfruGeneratedStructures` 接进来。ServiceLoader 入口 `Gt6KtfruMachines` 只剩转发。`Addon` 的创造栏和自检、`MachineColors`、`StructureBuild` 都问登记表，`new Gt6KtfruMachines()` 一个不剩。
- 材料声明只构建一次（`Gt6KtfruMaterials` 里缓存）。

和计划不同的地方：

- 登记表放在 `registry`，不在 `core.feature`：它要认得所有特性，放进 `core` 就违反"`core` 不依赖任何特性"。`Feature` 接口留在 `core.feature`。
- `Feature` 比计划多了气体鼓、两种管道和 `derivedBlockIds()`（管道的块 id，自检和创造栏用）。这些声明现在在基础特性里，第 3 步会跟着 `gas` 特性走；自检因此不再需要知道管道尺寸。
- 顺序：登记表里特性的顺序就是每张列表的顺序。GT6CE 按位置给结构零件编号（`AddonMachines` 里从 1,000,000 起顺序分配），所以零件、机器、配方表的顺序必须不变，实际上也没变，第 3 步切开基础特性时同样要保持。只有布局的顺序变了一处：坩埚的布局现在排在生成布局之前（原来在之后）。GT6CE 按名字登记布局（`SharedLargeMachineLayouts` 是按名字的 `LinkedHashMap`），没有依赖顺序的地方。

验证（本机）：

| 项 | 结果 |
| --- | --- |
| 声明快照 | 21 段里 20 段逐字节相同；`structureLayouts` 一段内容相同，只是第 49 到 80 条（31 个生成布局加坩埚）换了位置。机器 278 台、零件 112 个（顺序不变）、配方表 32 张、99 种机器类型和各自造出的行为类及字段都在那 20 段里 |
| `gradlew build` | 33 个测试，0 失败（原来 22 个，加登记表 10 个、材料 1 个） |
| 故意破坏 | 3 处都变红：坩埚特性没进登记表；一个机器类型有两个行为；一台行为机没有行为。`FeaturesTest` 还会在写了 `Feature` 类却没登记时变红，以前没有东西能发现这种遗漏 |
| `smoke-boot.ps1` | 通过，104 秒 |

### WP4 行为层公共件和生命周期（L）

依赖：WP3 的 `core`。

1. `RecipeRunner`：统一"找配方、付费、放输出、推进度"；校验全部输出；重复物品输入分别扣；kept 输入和流体；`working` 一致更新；核对要不要支持 `outputChances`；测试里需要 `MachineBehaviorContext` 的替身 [待核实：SDK 里它是不是接口]。
2. `EnergyBridge`（辅助缓冲到主缓冲）、`LossTimer`（用 `level.getGameTime() % 200` 替代持久化计数）。
3. `TtlRegistry`（键含世界；服务器停止或世界卸载时清空；玩家退出时清待配对表）、`PosCodec`、统一的链接和坐标编码。
4. 4 个按枚举或布尔分叉的类拆成单一职责的类；`MachineBehavior` 的空实现放进基类（或接口默认方法，如果 SDK 允许）。
5. 修 D1、D2、D3；D5 能一并解决的一并解决（缓存解析结果，配方查找只在输入变化时做）。

完成标准：M4；4 份运行器加 2 个变体合并成 1 份；13 张静态表变 0 张（或全部在带生命周期的服务里）。

### WP5 生成代码卫生（M）

依赖：WP0；和 WP3 的类名变更同步。

- 3 个生成文件（`SourceRecipes`、`GeneratedLayouts`、`Items`）进 `generated/`，写文件头（来源提交、生成命令、勿手改）。`Materials` 按手写文件处理，进 `content/`；`gen_materials.py` 退役，或者只留作历史参考（G5）。
- 脚本去掉卡号命名，写 `tools/README.md`：流水线顺序、输入、输出、谁拥有哪些资源文件。`port_block_textures.py` 和 `gen_card20_assets.py` 的产物划清。
- `block()` 别名、`gt()` 替代、`form()` 的原版替代移到 `tools/mappings.json`，生成文件头部同时输出"替代项清单"，供账本引用。
- CI 在输入可得时校验"重新生成无差异"，输入不可得时只校验文件头。
- 评估项，不是门槛：689 行配方改成随 jar 发布的数据文件、运行时读取（`AddonRecipes.Line` 本来就是构造器），代价是运行时解析和错误处理；资源 JSON 去重（共享父模型或 datagen）。

完成标准：M5。

### WP6 缺陷和细节（M）

依赖：WP4（D4 等独立项可以更早）。

- D4、D6（改用注册 id 或标签；点号 id 迁移推迟）、D7（用 record 或 builder）、D8、D9。
- `Machines` 的公有数组改 `List.of`；GUI 坐标和魔法数字常量化。
- 飞轮容量复查：SDK 现在够不够（S6）。
- `"gt6ce"` 等 GT6CE 命名约定集中到 `core` 的一个类里，并写明依赖了哪些约定。

完成标准：M7。

### WP7 文档与账本（M）

依赖：WP3 之后写结构说明；其余可以更早。

- R1 全部修正；`GAP.md` 与卡 05 合并为一份账本（`GAP.md` 只留入口）；S5、S6 的条目补进账本。
- README 或 `docs/building.md` 的构建一节；"怎么新增一台机器""怎么重新生成"两页；README 增加指向本文的链接。
- 列出 `zh_cn` 未翻译的 487 条。
- 做一张"生存可得性"清单：生成脚本统计每个方块 id 是否出现在某条配方产物或 `data/…/recipe` 里。
- 卡 23 收尾；卡 README 写明"closed"的含义。

完成标准：M8、M9。

### WP8 展示前自检（S）

按 §6 逐条自检；冷克隆构建一次；`runServer` 跑到 `Done`；可选：把 §6 清单拿给上游作者看，问她哪几条是必须的（§8 第 5 条）。

## 8. 待决事项

| # | 问题 | 选项 | 建议 | 决定 |
| --- | --- | --- | --- | --- |
| 1 | 最低线的阈值合不合适（手写类 ≤ 400 行、根包 ≤ 2 个类、包级无环） | 调数字；增删条目 | 先按 §6，做完 WP3 再看实际 | 已定（2026-10-24，你交给我判断）：沿用 §6 的数字；生成文件不算，例外在类注释里写明，不放宽数字 |
| 2 | 分包后是否去掉 `Gt6Ktfru` 前缀 | 去掉；保留 | 去掉：包名已经承担命名空间；单独一个提交，历史卡片里的文件名不改 | 已定（2026-10-24）：去掉，WP3 第 5 步 |
| 3 | 689 行配方继续放 Java，还是改成数据文件 | 保持；数据化 | 先保持，WP5 评估后再定，不阻塞最低线 | 待定 |
| 4 | 失败策略 | 一律抛；一律跳过；开关 | 开关：开发和 CI 严格，发行版记录并跳过 | 待定 |
| 5 | 署名措辞；`authors` 要不要写上游作者；展示前要不要让上游作者看 §6 | — | README 和 `NOTICE` 先到位；`authors` 等她的意见；§6 清单在 WP8 时给她看 | `authors` 已定（2026-10-24）：写 kuzuanpa；README 和 `NOTICE` 已按这个写好；§6 给不给她看留到 WP8 |
| 6 | 实施卡位："同时只开一张实施卡"，卡 23 还是 `active` | 先关卡 23；整改独立于卡 | 整改独立于卡：它不是表面条目的移植，不占卡位；卡 23 的两项玩法验收记入"未验收清单" | 待定 |
| 7 | 这份文档是否进版本库；仓库若公开，要不要等首批修复落地后再提交 | — | 你定 | 待定 |
| 8 | GT6CE 构建用的 JDK | 装 Temurin 17；验证 21 能否单独构建 NeoForge 节点 | 已定（2026-10-24）：GT6CE `core` 要求 17 工具链，21 单独不够；脚本会找已有的 17，缺时加 `-AutoDownloadJdk` | 已定 |
| 9 | 飞轮容量 | 当缺陷修；回账本 | 先核实 SDK 现在够不够，够就当缺陷 | 待定 |
| 10 | 点号旧 id 要不要迁移成规范 id | 迁移（要别名，影响已存世界）；保留 | 保留；先把 `getDescriptionId()` 匹配换成注册 id 或标签 | 待定 |

## 9. 进度跟踪

- [x] WP0 本地可构建、可复现（B2、B5；G2 的路径部分）。2026-10-24 做完；全新克隆和 `-AutoDownloadJdk` 还没验证
- [x] WP1 署名与定位（R4）。2026-10-24 做完：README 首段、`NOTICE`、`mods.toml` 的作者和描述；剩生成文件头写来源（WP5）、README 其余过期的条目（WP7）
- [ ] WP2 契约测试与自检（B1、B3、S8；S2 的自检部分）。2026-10-24 做了测试、自检推导和冒烟脚本；剩文件头检查（等 WP5）、失败策略开关、CI 里的冒烟
- [ ] WP3 包结构与登记表（S1、S2、S3、S7）。2026-10-24 做了第 1、2 步（拆枢纽、登记表）；第 3 至 6 步还没做
- [ ] WP4 行为层公共件与生命周期（S4、D1、D2、D3、D5）
- [ ] WP5 生成代码卫生（G1、G2、G3；G4 可选）
- [ ] WP6 缺陷与细节（D4、D6、D7、D8、D9；S6 的飞轮）
- [ ] WP7 文档与账本（R1、R2、R3、R5、R6；S5、S6 的账本部分）
- [ ] WP8 展示前自检（§6 全部）

B4（提交粒度）由 §7 的做法约束覆盖。

## 附录 A 度量方法与命令

在仓库根目录用 bash 运行，`P` 是源码目录。

```bash
P=src/main/java/com/lombinaxmasson/gt6ktfruaddon

# 文件数、行数、最大类
git ls-files src/main/java | wc -l
git ls-files src/main/java | xargs wc -l | sort -rn | head -12

# 生成文件占比
wc -lc $P/Gt6KtfruSourceRecipes.java $P/Gt6KtfruGeneratedLayouts.java $P/Gt6KtfruMaterials.java $P/Gt6KtfruItems.java

# 静态可变集合；生命周期钩子（后一条应当有输出）
grep -nE "static (final )?(Map|Set|List|HashMap)<[^>]*>[^;(]*=" $P/*.java | grep -v "List.of\|Set.of\|Map.of\|copyOf"
grep -nE "ServerStopp|LevelEvent\.Unload|PlayerLoggedOut" $P/*.java

# 复制的辅助方法、全量声明重建、GT6CE 字面量
grep -n "static boolean kept(" $P/*.java
grep -n "new Gt6KtfruMachines()\|new Gt6KtfruMaterials()" $P/*.java
grep -c '"gt6ce"' $P/*.java | grep -v ':0'

# 启动自检里手写的 id 数（`Gt6KtfruRecipes.register()` 到氢那行之间的 `stack(` 调用，含常量给出的 id）
awk '/Gt6KtfruRecipes.register\(\);/,/FluidStack hydrogen/' $P/Gt6KtfruAddon.java | grep -oE '(^|[^.[:alnum:]_])stack\(' | wc -l

# 资源重复（内容种数）
R=src/main/resources/assets/gt6ktfruaddon
md5sum $R/models/block/machine/basic/* | awk '{print $1}' | sort -u | wc -l
md5sum $R/models/item/* | awk '{print $1}' | sort -u | wc -l
```

```bash
# zh_cn 与 en_us 文本相同的条目数
python -I - <<'EOF'
import json
R = "src/main/resources/assets/gt6ktfruaddon/lang/"
en = json.load(open(R + "en_us.json", encoding="utf-8"))
zh = json.load(open(R + "zh_cn.json", encoding="utf-8"))
print(len(en), sum(en[k] == zh[k] for k in en))
EOF
```

耦合分析的口径：去掉注释后，按 `Gt6Ktfru\w+` 匹配建类级有向图，用 Tarjan 求强连通分量；成员可见性按 4 空格缩进的声明行解析；包级方案试算是把每个类映射到包，再统计跨包引用、需要公开的包级成员和包级强连通分量。脚本目前只在会话临时目录里，WP3 第 6 步会把它放进 `tools/`。

## 附录 B 分包试算与目标映射

### 试算结果

| 方案 | 包数（含根） | 跨包引用 / 总引用 | 需公开的包级成员 | 包级循环 |
| --- | --- | --- | --- | --- |
| 按层：machine、layout、behavior、recipe、content、client、build | 8 | 863 / 1,170 | 28 | 有，涉及 6 个包 |
| 按特性切片：power、heat、logistics、science、compute，加 core、recipe、content、generated、client、build | 12 | 1,039 / 1,170 | 20 | 有，涉及 7 个包 |

两个方案的跨包引用里，约 707 处主要来自 `SourceRecipes`（少量来自 `Recipes`）对 `Machines`、`ResearchMachines`、`LateMachines` 常量的引用；拆掉枢纽（WP3 第 1 步）后，这些变成对叶子类 `RecipeMaps` 的引用。选按特性切片：包数少、需公开的成员少，一台机器相关的类大多落在同一个包里。但在拆枢纽之前，两种方案都不是无环的。

试算里的特性包是 power、heat、logistics、science、compute；§7 的目标结构在此基础上按领域重新划分了名字和边界（processing、gas、kinetic、power、heat、semiconductor、research、network、fusion），试算没有对最终划分重跑，WP3 第 1 步之后要重跑一次。

### 类到包的映射（提议）

| 目标包 | 类（省略 `Gt6Ktfru` 前缀） | 备注 |
| --- | --- | --- |
| 根 | `Addon` 变 `KtfruAddon`；新增 `Ktfru`（`MODID`） | 创造栏和自检拆出去 |
| `core.layout` | `LayerLayout` | |
| `core.feature`、`core.behavior`、`core` | 新增：`Feature`、登记表、`RecipeRunner`、`EnergyBridge`、`LossTimer`、`TtlRegistry`、`PosCodec`、`Gt6ce` | WP3、WP4 |
| `recipe` | `Recipes`；新增 `RecipeMaps` | 34 个常量来自 `Machines` |
| `generated` | `SourceRecipes`、`GeneratedLayouts`、`Items` | 带文件头 |
| `content` | `Materials` | 手改过的材料声明；`META-INF/services` 同步 |
| `processing` | `Machines` 中的单方块配方机、蒸馏塔、矿处理系统及其布局；`LateMachines` 中的催化反应器 | 面配置改数据表 |
| `gas` | `GasTank`、`GasCompressor`；`Machines` 中的压缩气罐、鼓、管道声明 | |
| `kinetic` | `VoidHopper`、`Miner`、`WaterMill`、`ManualMotor`、`Flywheel`；`Machines.kineticBehaviors` 的声明 | |
| `power` | `Turbines`、`TurbineLayouts`、`Dynamo`、`SteamTurbine`、`FluidBoiler`、`Batteries`、`BatteryLayouts`、`LiquidBattery`、`TransformBattery`、`FuelBattery`、`TransformerPart`；`Machines` 中的气体燃料机、柴油机、变压零件、气体电池声明 | |
| `heat` | `HeatMachines`、`HeatLayouts`、`SunHeater`、`SunMirror`、`MantleHeater`、`OilMiner`、`FuelDeburner`、`TidalGenerator`、`Crucible`、`CrucibleMachines`、`CrucibleLayouts` | 潮汐发电机和声明同包 |
| `semiconductor` | `Machines` 中的掩膜对准机、CNC 及其布局和零件 | |
| `research` | `Research`、`ResearchAssembler`、`ResearchMachines`、`ResearchLayouts`、`Microscope`、`Rooms`、`LateBehavior`（拆成微重力房间控制器和 AI 中心两个类）；`LateMachines` 中的微重力和 AI 中心 | 依赖 `network` |
| `network` | `Compute`、`ComputeMachines`、`Wireless`、`WirelessMachines`、`QuantumRelay`；`LateMachines` 中的量子中继 | |
| `fusion` | `LaserFusion`；`LateMachines` 中的激光聚变和托卡马克 | |
| `client` | `MachineColors` | |
| `event` | `StructureBuild` | |
