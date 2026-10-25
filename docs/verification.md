# 展示前自检（2026-10-25）

对照 WP7 完成后的 `a2f4bd9`。自检覆盖构建、声明、公共行为逻辑、生成一致性与服务器加载；未完成的具体机器玩法仍见 [未验收清单](unverified-gameplay.md)。

| 最低线 | 核对证据 |
| --- | --- |
| M1 干净克隆构建 | 独立 `git clone --no-local`，无原工作区 jar、build 或 .gradle 产物；按构建指南重打固定 GT6CE SDK/运行包再构建附属。结果见下文 |
| M2 包结构 | 根包仅 Ktfru/KtfruAddon；110 个 Java 文件，最长手写类 Materials 336 行；PackageDependencyTest 检查方向与无环，Java 编译检查跨包可见性 |
| M3 单一登记 | Feature/Features 汇总与缓存；声明自检遍历 blockIds 和自有物品，不维护手写方块 id 列表；Materials 也缓存声明 |
| M4 行为与生命周期 | RecipeRunner、EnergyBridge、LinkList/PosCodec、TtlRegistry 公共实现；BehaviorLifecycle 绑定服务器停止/维度卸载/玩家退出；生命周期与临时集合守卫测试通过 |
| M5 生成代码 | 三份 Java 来源头及映射检查；固定输入的隔离重生成、重复生成和独立资源生成比较字节一致 |
| M6 测试与 CI | build 执行 130 项 JVM 测试；Python 检查器 10 项、文档解析器 3 项；CI 同时检查文档清单。jar 校验和是一次本地构建的字节记录，不保证跨环境同字节，因此源提交仍固定而校验和差异只警告，日志写明原因 |
| M7 已知缺陷 | WP4/WP6 的付款、概率输出、配对、计时、档位、电极、颜色与飞轮修复由对应测试覆盖；不能恢复的行为边界列在唯一账本 |
| M8 文档事实 | 唯一账本、开发指南、卡片状态与未验收清单；487 条中文同文、408 个方块直接配方产物索引（113 命中/295 未发现），不称完整生存可达 |
| M9 来源 | README/NOTICE/生成文件头记录上游、固定提交和协议；贴图来源未明的 6 个文件仍在 NOTICE 如实列出 |

## 构建与服务器证据

本地检查日志（`build/` 为忽略目录，不随仓库发布）：

- `build/wp8-build.log`：本工作区 `documentationInventory build` 成功；130 项 JVM 测试结果为 0 失败、0 跳过。
- `build/wp8-python.log`：13 项 Python 检查全部通过；`documentation_reports.py --check` 通过。
- `build/wp8-generation.log`：严格全量重生成逐字节一致，重复生成及结构资源单跑检查通过。
- `build/wp8-server-console.log`：真实 `runServer` 输出两条 recipes-ready 标记，随后 `Done (0.880s)`；发送 `stop` 后所有维度保存，`Behavior lifecycle: cleared 3 registries`，Gradle 成功退出。
- 首轮非交互启动也到达 `Done (0.968s)`，但 stdin 已关闭，因而停止该进程后重跑上面的交互式验证；最终使用第二轮正常停机证据。控制台输入通过临时 Gradle init 脚本转发，未改生产代码或加入临时模组测试。

冷克隆验收通过：

- `build/wp8-cold` 从本机 Git 对象以 `git clone --no-local` 检出 `a2f4bd9`，未带原工作区忽略的 SDK/运行 jar、build 或项目 .gradle。
- `tools/build-gt6ce.ps1 -Source D:/Projects/java/GregTech6` 从固定 `1d1f69fbb` 重建两份 jar。日志 `build/wp8-cold-sdk.log` 显示成功；运行 jar SHA-256 为 `b82e423c0c47911b6c6b2233cd7e38828692e4e20c4ce538f54b7b9b2a263e93`，与记录一致。共享资源首次处理 73,075 个文件；多版本源码准备和首次资源快照耗时较长，不是仅复制现成 jar。
- 随后在克隆中运行 `gradlew build documentationInventory --no-build-cache`，9 项任务全部实际执行，39 秒成功；130 项 JVM 测试，0 失败/错误/跳过。日志 `build/wp8-cold-build.log`。
- 克隆中 13 项 Python 检查及文档报告 `--check` 均通过，`git status --short` 无输出；普通生成检查缺上游、原版 GT6 副本时明确降为来源头模式。日志 `build/wp8-cold-python.log`、`build/wp8-cold-documentation.log`、`build/wp8-cold-generation-auto.log`。
- 本轮没有修改机器、配方、资源或运行行为；基线之后只补 CI 与验收文档。推送后 CI 状态可在仓库 Actions 查看，不用本机结果冒充远端执行。

## 验证边界

- 干净克隆来自本机已提交的 Git 对象，不复制工作区未跟踪输入；GT6CE 固定源码由构建脚本从本机源仓库检出。使用已安装的 JDK 17/21 和用户级 Gradle/NeoForm 依赖与变换缓存；附属冷构建显式禁用 Gradle 构建缓存，未验证“空系统、零缓存、全网络下载”或自动下载 JDK 分支。
- 公共生成器输入包括本机固定上游及原版 GT6 副本；后者的独立固定远端来源尚未确立，不把本机复现当成来源证明。
- 专用服务器启动到 Done 只证明注册、配方加载和服务器启动；不证明每台机器的合成、结构 IO、研究、配对或传能。历史玩法未勾选项继续保留。
- WP2 严格/宽松配方失败策略与 CI 冒烟仍是独立后续项；当前遇到无效配方仍可能在模组加载时失败。非最低线门槛的翻译、资源去重、配方数据化与旧 id 迁移也不在本次完成范围。
- 本轮未联系上游作者；展示前征询为可选项。
