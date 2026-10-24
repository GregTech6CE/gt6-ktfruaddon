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
| kTFRUAddon 检出，停在 `6404ab69` | `KTFRU_UPSTREAM` | `<仓库>/kTFRUAddon`（git 忽略） | 除 `gen_card20_assets.py` 外的全部 |
| GT6CE 源码，停在 `build-commit.txt` 的提交 | `GT6CE_SOURCE` | `<仓库>/.gt6ce-src` | `extract_machine_recipes.py`、`gen_materials.py`、`port_block_textures.py` |
| 原版 GregTech 6（1.7.10）源码的本地副本 | `GT6_REF` | `<仓库上级>/gt6_source_code`，没有再找 `<GT6CE_SOURCE>/work/gt6-ref` | `extract_machine_recipes.py` |
| GT6CE 运行包 jar | 无 | `libs/gt6ce/gt6ce-1.21.1-neoforge.jar` | `extract_machine_recipes.py` |

`ALLOW_INPUT_MISMATCH=1` 允许检出不在固定提交上时继续运行；产物会和已提交的不一致。

kTFRUAddon 检出：

```powershell
git clone https://github.com/kuzuanpa/kTFRUAddon kTFRUAddon
git -C kTFRUAddon checkout 6404ab6979de9fce832ce0c41171000decf00e69
```

原版 GregTech 6 的本地副本只被 `extract_machine_recipes.py` 读一个文件：`<GT6_REF>/gregtech6/src/main/java/gregtech/loaders/b/Loader_MultiTileEntities.java`，用来还原电线和电缆的数字 id。GT6CE 仓库里没有文档提到 `gt6-ref`，仓库里也没有这份副本，来源需要使用者自己提供。

## 运行顺序

读代码得到的先后约束，没列出的脚本之间没有核实过依赖：

1. `gen_card22_items.py` 写 `tools/own_items.json`，`extract_machine_recipes.py` 读它。
2. `extract_machine_recipes.py` 读 `Gt6KtfruMaterials.java`。这个文件现在是手改过的，不再由 `gen_materials.py` 生成（见下）。
3. `gen_card20_layouts.py` 写 `tools/card20-parts.txt`（不入库的中间文件），`gen_card20_assets.py` 读它。
4. `gen_card20_assets.py` 给卡 20 的机器写通用模板的 blockstate 和物品模型；`port_block_textures.py` 重写 `blockstates/` 下的全部文件。所以 `gen_card20_assets.py` 必须先于 `port_block_textures.py`，单独重跑它会把这些 blockstate 退回模板。
5. `gen_card20_layouts.py` 会扫描 `src/main/java` 里已有的零件声明（跳过 `Gt6KtfruGeneratedLayouts.java` 本身），避免重复声明。

产物都写进 `src/main/`；`extract_machine_recipes.py` 另外把跳过账本写到 `build/source-recipe-report.txt`。

## 复现性核对（2026-10-24）

环境：kTFRUAddon 在 `6404ab69`，GT6CE 在 `1d1f69fbb`（`build-gt6ce.ps1` 检出的 `.gt6ce-src`），原版 GregTech 6 源码在 `../gt6_source_code`，运行包 jar 由 `build-gt6ce.ps1` 重打（sha256 与 `.sha256` 记录一致）。每个脚本重新生成后，用 `git status` 对比已提交的文件。

| 脚本 | 结果 |
| --- | --- |
| `gen_card22_items.py` | 与已提交的文件逐字节一致 |
| `gen_card20_layouts.py` | 与已提交的文件逐字节一致 |
| `gen_card20_assets.py`，然后 `port_block_textures.py` | 按这个顺序运行后一致。单独运行 `gen_card20_assets.py` 会改动 94 个文件：47 个 blockstate 和 47 个物品模型退回通用模板 |
| `gen_materials.py` | 不一致。已提交的 `Gt6KtfruMaterials.java` 在生成之后被手改过（`prefixes()`、`.polymer()`、Bittern 液体、部分 `.gas()` 和 `.liquid()` 标志），`assets/gt6ce/lang/` 里也多了手加的条目。现在要加 `--force` 才会运行，重新生成会丢掉这些手改 |
| `extract_machine_recipes.py` | 与已提交的 `Gt6KtfruSourceRecipes.java` 逐字节一致（解析 689 行）。需要原版 GregTech 6 源码副本，和 `build-gt6ce.ps1` 打出的运行包 jar |
