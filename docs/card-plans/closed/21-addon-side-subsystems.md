# 21 附属自有子系统：微重力、研究锁、激光脉冲、量子中继与算力网

- 状态：closed
- 依赖：20
- 文件范围：
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruRooms.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruLateBehavior.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruLateMachines.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruLaserFusion.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruQuantumRelay.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruResearch.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruResearchAssembler.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruResearchMachines.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruMicroscope.java`
  - `src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruCompute.java`
  - `src/main/resources/assets/gt6ktfruaddon/lang/en_us.json`
  - `src/main/resources/assets/gt6ktfruaddon/lang/zh_cn.json`
- 平台：NeoForge 1.21.1

## 背景

`kTFRUAddon`（`6404ab69`）里的微重力房间、研究树解锁、激光聚变脉冲蓄能、量子通讯中继器配对和算力网调度，属于附属自己的跨机器子系统。这些机制不需要往 GT6CE 核心里加专用接口，直接在附属内部用 `BehaviorMachine`、`AddonRecipes.map(...).find(...)` 和按 `(维度, BlockPos)` 索引的静态表就能闭环。

## 范围与设计

### 1. 微重力房间（30102、30103、30105）

- `centrifugal_microgravity`（30102）：成型且每 tick 消耗 2048 EU 满 600 tick 后，将本机 9×9×9 外壳的内部 7×7×7 空间（`[x - 3..x + 3, y + 1..y + 7, z - 3..z + 3]`）登记进 `Gt6KtfruRooms`，结构断开、断电或拆毁时移除。
- `microgravity_crystal_growth`（30103，多方块）与 `microgravity_assembler`（30105，单方块）：改为带 `.recipeMaps(...)` 的 `BehaviorMachine`。在 `tick` 中先检查本机坐标是否处于激活的微重力房间内（30103 同时检查结构成型），满足时才执行 `CRYSTAL` / `COSMIC` 配方；放大镜右键显示微重力环境状态。

### 2. 研究监视器、显微镜与研究组装/反应釜（9818、30042、30043、30077、30078）

- `research_monitor`（9818）：在 `tick` 中把本机 `(维度, 坐标)` 的已解锁物品集合同步到 `Gt6KtfruResearch` 静态表，拆毁时移除。
- `optical_microscope`（30077，256 EU/件）与 `electron_microscope`（30078，4096 EU/件）：增加输出槽 1。支持用放大镜先点监视器记录坐标、再点显微镜绑定（或自动查找正交相邻的监视器）。成型且消耗样本后，把该物品 ID 加入目标监视器的解锁表。
- `research_assembler`（30042）与 `research_reactor`（30043）：改为带 `.recipeMaps(...)` 的 `BehaviorMachine`（EU 131072，包窗口 64–524288）。在 `tick` 中检查控制器背后两格 `pos.relative(facing.getOpposite(), 2)`（或已绑定）的监视器，仅当配方的第一件产物已在监视器解锁时才推进进度。

### 3. 激光聚变脉冲池与强制冷却（30071、30072）

- `laser_fusion_experimental`（30071）与 `laser_fusion_industrial`（30072）：改为带 `.recipeMaps(LASER)` 的 `BehaviorMachine`（2 物品槽 + 4 流体槽 8000 mB），按 `LaserFusionControllerBase` 运行 `IDLE / CHARGING / COOLING / ERROR` 状态机：
  - 30071：脉冲容量 `16,777,216` LU（2 × 8M），峰值 `29,360,128` LU（14 × 2M），单包上限 `131,072` LU，冷却 `240` tick。
  - 30072：脉冲容量 `1,073,741,824` LU（8 × 128M），峰值 `3,019,898,880` LU（72 × 40M），单包上限 `2,097,152` LU，冷却 `80` tick。
  - 冷却期间不跑配方；充能攒够配方脉冲（`Math.max(1, line.special())`）且峰值达标、输出槽放得下时，单 tick 消耗输入与脉冲能量，写入产物并进入冷却。放大镜显示状态、当前脉冲、峰值与冷却。

### 4. 量子通讯中继器（30074）

- `quantum_communication_relay`（30074）：改为带 `.recipeMaps(RELAY_MAP)` 的 `BehaviorMachine`。使用 `auxiliaryEnergy` 接收 8192 EU（范围 2048–32768，容量 32,768,000），主缓存容量设为 `32,768,000` QU，`energyOutput` 从顶面（`TOP`）每 tick 发射 `128 QU × 1 A`。
- 支持同维度两台中继器用放大镜互相配对；配对状态下完成一条 `RELAY_MAP` 配方时向主缓存充入 `65536 QU`，每 tick 从顶面送出 `128 QU`。

### 5. 算力网拓扑与 AI 中心（30058–30070、30079、32010–32043、30106–30108）

- 算力节点（32010–32043）在 `tick` 中向 `Gt6KtfruCompute` 静态表登记本机坐标与算力产出；
- 无线算力控制器（30058–30060）汇总 512 格内已绑定无线节点的 Normal 算力并登记到静态表；生物/量子/时空阵列（30061–30070）支持用放大镜绑定算力控制器，成型且上游算力充足时登记转换后的算力产出；
- 三台 AI 中心（30106–30108）支持用放大镜绑定算力控制器和研究监视器（或自动识别正交相邻的监视器），成型且能从绑定的算力控制器分配到 `67,108,864` Normal 算力/tick 时进入 `RUNNING` 状态并向监视器累计研究点数（8 / 4 / 2 pt/t）。

## 步骤

- [x] 更新卡 05，把附属内部可闭环的子系统与真正的 GT6CE 接口边界分开记清。
- [x] 实现 `Gt6KtfruRooms` 及 30102、30103、30105 的微重力房间联动。
- [x] 实现 9818 监视器静态表、30077/30078 显微镜解锁样本、30042/30043 研究树门槛检查。
- [x] 实现 `Gt6KtfruLaserFusion` 脉冲蓄能与冷却状态机（30071、30072）。
- [x] 实现 `Gt6KtfruQuantumRelay` 双机配对、配方产 QU 与顶面 128 QU/t 发射（30074）。
- [x] 实现 `Gt6KtfruCompute` 静态算力汇总与 30106–30108 AI 中心的算力分配及监视器点数累计。

## 验收

- [x] 编译：`compileJava --offline` 通过，所有机器与布局构建器在 `6eba3e1ed` SDK 上校验通过。
- [ ] 开发客户端
- [ ] 玩法：微重力、研究解锁、激光脉冲、量子中继和算力网各自跑通

## 日志

- 2026-10-22：完成微重力房间静态表（`Gt6KtfruRooms`）、激光聚变脉冲蓄能与冷却状态机（`Gt6KtfruLaserFusion`）、量子通讯中继器配对与 128 QU/t 发射（`Gt6KtfruQuantumRelay`）、研究监视器与显微镜/研究组装/反应釜联动（`Gt6KtfruResearch` / `Gt6KtfruMicroscope` / `Gt6KtfruResearchAssembler`）、以及算力网拓扑与 AI 中心算力分配（`Gt6KtfruCompute` / `Gt6KtfruLateBehavior`）。宽包窗口和异种能量转换统一通过 `auxiliaryEnergy` 满足 `6eba3e1ed` 的 `BehaviorMachine` 规则。
