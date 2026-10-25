# tools

GT6CE 构建脚本和代码、资源生成脚本。生成脚本只通过 [`paths.py`](paths.py) 找输入：缺输入，或者检出不在固定提交上，脚本会先停下并说明要提供什么，不会写任何文件。

## 构建 GT6CE

`build-gt6ce.ps1`（Windows PowerShell 5.1）按 `libs/gt6ce/build-commit.txt` 里的提交，把附属编译和运行用的两个 jar 打进 `libs/gt6ce/`。步骤和 `.github/workflows/build.yml` 的 "Build GT6CE" 一致。开关见 `Get-Help .\tools\build-gt6ce.ps1 -Full`。

- 脚本只读取源仓库，构建发生在 git 忽略的 `.gt6ce-src` 里。
- `-NoBuild` 只把 `.gt6ce-src` 检出到固定提交，不构建。生成脚本读的 GT6CE 源码就是这份。
- Linux 和 macOS 参照 `build.yml`。

## 冒烟启动

`smoke-boot.ps1` 跑 `gradlew runGameTestServer`，把整个模组栈（GT6CE 加附属）加载到通用初始化结束。GT6CE 在这一步把 `onRecipesReady` 派发给附属，所以未知零件、注册表重复键、解析不了的配方栈、启动自检失败都会在这里暴露。附属没有 GameTest，所以起来之后服务器会报 `No test functions were given!`，这条错误是预期的。

- 通过的判据是日志里同时有附属的 `gt6ktfruaddon: ready on` 和 GT6CE 的 `GT addon API <n> ready: [...gt6ktfruaddon...]`（后者只在所有附属的 `onRecipesReady` 都返回之后才打印），而且没有失败标记。模组加载失败时 Gradle 也可能以 0 退出（缺 `.polymer()` 的材料就是这样），所以以日志为准，退出码只是附加检查。没通过时脚本会打印日志里第一处加载错误。
- 不用 `runData`：数据生成运行不触发通用初始化事件，会在 GT6CE 根本没派发的情况下"通过"。
- GameTestServer 不需要 EULA，不用写 `eula.txt`。
- Gradle 的输出留在 `build/smoke-boot.log`。

## 生成脚本的输入

| 输入 | 环境变量 | 默认位置 | 需要它的脚本 |
| --- | --- | --- | --- |
| kTFRUAddon 检出，停在 `6404ab69` | `KTFRU_UPSTREAM` | `<仓库>/kTFRUAddon`（git 忽略） | 有效生成脚本与 `texture_provenance.py` |
| GT6CE 源码，停在 `build-commit.txt` 的提交 | `GT6CE_SOURCE` | `<仓库>/.gt6ce-src` | `extract_machine_recipes.py`、`generate_structure_assets.py`、`port_block_textures.py`、`texture_provenance.py` |
| 原版 GregTech 6（1.7.10）源码的本地副本 | `GT6_REF` | `<仓库上级>/gt6_source_code`，没有再找 `<GT6CE_SOURCE>/work/gt6-ref` | `extract_machine_recipes.py` |
| GT6CE 运行包 jar | 无 | `libs/gt6ce/gt6ce-1.21.1-neoforge.jar` | `extract_machine_recipes.py` |

`ALLOW_INPUT_MISMATCH=1` 允许检出不在固定提交上时继续运行；产物会和已提交的不一致。

kTFRUAddon 检出：

```powershell
git clone https://github.com/kuzuanpa/kTFRUAddon kTFRUAddon
git -C kTFRUAddon checkout 6404ab6979de9fce832ce0c41171000decf00e69
```

原版 GregTech 6 的本地副本只被 `extract_machine_recipes.py` 读一个文件：`<GT6_REF>/gregtech6/src/main/java/gregtech/loaders/b/Loader_MultiTileEntities.java`，用来还原电线和电缆的数字 id。GT6CE 仓库里没有文档提到 `gt6-ref`，仓库里也没有这份副本，来源需要使用者自己提供。

## 生成和检查

需要 Python 3.11 或更新版本；无需第三方 Python 包。从仓库根目录执行：

```powershell
python tools/regenerate.py                 # 完整输入预检后，更新当前工作区
python tools/regenerate.py --check         # 始终查文件头；输入齐全时隔离重生成
python tools/regenerate.py --check --full  # 必须完整重生成，缺输入即失败
python tools/regenerate.py --headers-only # 明确只查来源头与映射结构
python -m unittest discover -s tools -p test_generated.py
```

干净克隆后，按上表提供两个固定提交检出、原版 GT6 副本和运行包 jar，再执行生成命令。原版 GT6 副本没有已确认的可下载固定提交，因此 CI 不自行猜测或下载。`--check` 缺输入时明确打印缺哪些输入与 headers-only 模式；已有检出的提交不符、非法映射、生成失败、差异都视为失败。严格检查忽略 `ALLOW_INPUT_MISMATCH`；普通生成允许该调试开关，不能用它证明固定输入可复现。

检查在系统临时目录复制源码与工具，通过环境变量引用只读上游，复制运行 jar；比较全部 `src/` 和 `tools/own_items.json` 的字节哈希，所以意外更改手写文件也会报错。它不会修改当前工作区，不比较构建报告。连续跑两遍验证幂等，再单独运行结构资源脚本确认不会回退模型。检查依赖当前仓库已有手写资源/声明，不声称能从空 `src/` 恢复整个项目。

CI 每次跑检查器测试和自动模式；通常缺原版 GT6 与上游检出，只检查文件头，日志明确表示这一限制。输入可得的执行环境使用相同自动命令会运行全量检查，也可用 `--full` 强制要求全量。

## 流水线与产物归属

统一入口严格按下表顺序运行。单独执行脚本也会做各自的输入预检；结构资产脚本额外依赖布局脚本产出的中间清单。

| 顺序/脚本 | 额外输入 | 拥有的输出 |
| --- | --- | --- |
| 1 `generate_items.py` | 上游 ItemList、物品定义和贴图 | `generated/KtfruItems.java`、`tools/own_items.json`；自有物品的模型/贴图；语言文件中自有物品键 |
| 2 `generate_layouts.py` | 上游结构、当前手写零件声明 | `generated/GeneratedLayouts.java`、`build/generated/structure-parts.txt` |
| 3 `generate_structure_assets.py` | 上一步的零件清单、贴图映射判定 | 结构/机器/零件语言键；没有真实贴图映射的机器 blockstate 和物品模型；清理明确列出的废弃模板 |
| 4 `port_block_textures.py` | 两个上游的机器贴图和现有机器 blockstate | 有真实贴图映射的 blockstate、方块模型、机器物品模型及复制的机器/GUI 贴图；`build/texture-port.txt` |
| 5 `extract_machine_recipes.py` | `own_items.json`、手写 `Materials.java`、`mappings.json`、原版 GT6 副本、运行 jar | `generated/SourceRecipes.java`、`build/source-recipe-report.txt` |

结构资源脚本通过贴图脚本的只读 `owns()` 判定跳过真实贴图模型，不再先写占位再覆盖；探测不复制贴图或污染缺失报告。语言文件由不同脚本按键维护，不是整份文件的独占生成物。`content/Materials.java` 和 `assets/gt6ce/lang/` 由人工维护，`gen_materials.py` 已退役，包含 `--force` 的调用也只报错退出；历史实现保留在 Git 历史。

三个生成 Java 文件头记录上游完整提交、GT6CE 固定提交、根目录生成命令及勿手改标记；不包含时间戳或本机路径。配方文件头另列 `tools/mappings.json` 的 14 条方块别名、2 条 GT 替代、3 条原版形态替代，供缺口账本引用。改适配规则须改映射并重生成，不能手改 Java。生成文本统一 LF，相关产物用 `.gitattributes` 保持跨平台换行。

## WP5 评估项

本轮保留 689 行配方的 Java 表达。改为随 jar 发布的数据可以缩小 Java 文件，但仍需要维护注册 id、数量、概率和来源信息的模式，引入运行时解析、错误定位与严格/宽松失败处理；不会消除 SDK 栈解析和适配规则。因此暂不引入新的运行时路径，数据化保留为后续决定。

资源共享父模型可减少重复 JSON，但生成器需保持贴图、染色、状态与现有 id 契约；本轮先消除生成器之间的写入冲突，资源去重作为独立后续工作，不阻塞 M5。

## 来源核对

`texture_provenance.py` 只读：PNG 按字节哈希、`.mcmeta` 统一 LF 后哈希，把 `assets/gt6ktfruaddon/textures/` 里的文件和 kTFRUAddon 检出、GT6CE core 的资源比对，按目录打印有多少文件与哪一边相同，以及哪些两边都找不到。`NOTICE` 里贴图那一行的数字出自这里（2026-10-24：974 个文件，940 个在上游，其中 384 个同时在 GT6CE core，28 个只在 GT6CE core，6 个两边都没有）。

## 历史复现性核对（2026-10-24，脚本名按当前名称列出）

环境：kTFRUAddon 在 `6404ab69`，GT6CE 在 `1d1f69fbb`（`build-gt6ce.ps1` 检出的 `.gt6ce-src`），原版 GregTech 6 源码在 `../gt6_source_code`，运行包 jar 由 `build-gt6ce.ps1` 重打（sha256 与 `.sha256` 记录一致）。每个脚本重新生成后，用 `git status` 对比已提交的文件。

| 脚本 | 结果 |
| --- | --- |
| `generate_items.py` | 与已提交的文件逐字节一致 |
| `generate_layouts.py` | 与已提交的文件逐字节一致 |
| `generate_structure_assets.py`，然后 `port_block_textures.py` | 按这个顺序运行后一致。当时单独运行 `generate_structure_assets.py` 会改动 94 个文件（WP5 已修正）：47 个 blockstate 和 47 个物品模型退回通用模板 |
| `gen_materials.py` | 不一致。已提交的 `Materials.java` 在生成之后被手改过（`prefixes()`、`.polymer()`、Bittern 液体、部分 `.gas()` 和 `.liquid()` 标志），`assets/gt6ce/lang/` 里也多了手加的条目。当时要加 `--force` 才会运行；WP5 已移除覆盖能力 |
| `extract_machine_recipes.py` | 与已提交的 `SourceRecipes.java` 逐字节一致（解析 689 行）。需要原版 GregTech 6 源码副本，和 `build-gt6ce.ps1` 打出的运行包 jar |

## 文档清单

先运行 `gradlew documentationInventory` 从登记表导出全部方块（需要 SDK），再用 `python tools/documentation_reports.py` 更新中文待核对与方块直接配方产物清单；`--check` 比较已提交报告。无需上游输入，统计边界见 [生成指南](../docs/regenerating.md)。
