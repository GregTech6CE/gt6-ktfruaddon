# gt6-ktfruaddon 工作规则

GT6CE 的 NeoForge 1.21.1 附属探针。模组 id 是 `gt6ktfruaddon`，包名 `com.lombinaxmasson.gt6ktfruaddon`，协议 AGPL-3.0-only。用中文回答我；文档和卡写中文，代码标识符和提交信息写英文。

## GT6CE 的边界

GT6CE 的本地检出在 `D:\Baobao\projects\dev\java\GregTech6CommunityEdition\GregTech6`。

- **可以读、搜索、编译。** 需要确认公开入口、注册名、材料形态或行为时，直接去看它的源码和文档。
- **不要改。** 不编辑源码、`build.gradle`、文档、配置，不切分支、不 rebase、不提交、不跑格式化。它是别人的仓库，改动只应通过向 GT6CE 提 issue/PR 进行，不在本目录顺手做。
- 编译 GT6CE 只允许产生它自己的构建产物；不要把本仓库的改动写进去。
- 需要 GT6CE 新增入口时，把来源、源名字、试过的入口和缺什么记进当前卡的「接口缺口」和 [卡 05](docs/card-plans/prep/05-api-gaps.md)，然后另开实施卡。不要用 GT 内部类绕过公开接口，也不要改 GT6CE 来迁就附属。

上游 1.7.10 源码检出在仓库根的 `kTFRUAddon/`，已被 git 忽略。它同样是只读参考，不修改、不提交。

## 构建与运行

需要 JDK 21：

```powershell
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.6.7-hotspot'
.\gradlew.bat compileJava --offline
.\gradlew.bat build
.\gradlew.bat runClient
```

- 编译只依赖 `libs/gt6ce/gt6ce-addon-sdk-1.21.1-neoforge-0.0.0.jar`；`runClient`/`runServer` 用同源码打出的 `gt6ce-1.21.1-neoforge.jar`。
- 本地运行包对应 `libs/gt6ce/build-commit.txt`（`addon/functional-parts` 的 `6eba3e1ed`）。
- `update-gt.ps1` 下载的是主线 0.8.0 产物，**不能**直接覆盖本地这两个 jar：0.8.0 的 `MachineDeclaration` 装不下当前字段，注册时会崩。
- 两个 jar 都不进 git、不打进成品。

## 卡与文档

- 阶段和卡表以 [docs/card-plans/README.md](docs/card-plans/README.md) 为准；正在做的卡在 `active/`，排队的在 `prep/`，做完的移到 `closed/`。
- 同一时间只开一张实施卡。卡号只增不复用。
- 卡里的步骤和验收要如实勾选；没在开发客户端里跑过的玩法验收不要勾。
- 每碰到接口接不住的东西，写进当前卡的「接口缺口」并补到卡 05，记下源里的名字、想用的入口、缺的是什么。
- 卡 22 之后自有内容（前缀、物品、配方图、贴图）已经落地；新增机器先看卡 02/03/04 的边界，不往已关的卡里加东西。
- C 档（TFC、Advanced Rocketry、Botania、小行星、火箭、符文扫描、戴森球、TFC 坩埚模型）不进卡；源码里整段注释掉的注册不恢复。

## 资源与提取脚本

- 机器贴图从 `kTFRUAddon` 和 GT6 拷贝到 `src/main/resources/assets/gt6ktfruaddon/`。机器模型必须放在 `models/block/machine/basic/<机器名>.json`，并配 `_active`、`_running`，否则物品栏缺贴图。
- 不要留下白混凝土或其他占位模型；缺图就补图或明确记录。
- 提取/生成脚本在 `tools/`，物品对照表是 `tools/own_items.json`。脚本改动后重跑，并检查生成的 Java、模型、语言文件和资源是否一致。
- 不提交 `build/`、`run/`、`libs/gt6ce/*.jar`、`kTFRUAddon/` 和 `__pycache__/`。

## 提交

- 一次卡或一个明确的小改动一个提交，提交信息写英文，说明关了哪张卡或做了什么。
- 不主动 push、不主动开 PR，除非我明确要求。
