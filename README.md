# gt6-ktfruaddon

GT6CE 的 NeoForge 1.21.1 附属，用来试现在的附属接口能接住多少。仓库名和显示名是 gt6-ktfruaddon，游戏里的模组 id 是 `gt6ktfruaddon`（模组 id 不能带连字符）。这是一条新写的探针，不是 [kuzuanpa/kTFRUAddon](https://github.com/kuzuanpa/kTFRUAddon) 1.7.10 源码的移植。项目协议是 AGPL-3.0-only，与上游相同。上游检出在仓库根的 `kTFRUAddon/`，已忽略，不进版本库。

可移植范围在 [`docs/ktfruaddon-gt6-surface.md`](docs/ktfruaddon-gt6-surface.md)，从 CrucibleCraft 拷来。阶段卡在 [`docs/card-plans/README.md`](docs/card-plans/README.md)。表面材料卡已关。单方块配方机器在卡 02，流体燃料机在卡 03。占位氢燃烧器已撤。

## 开发

需要 git、Python 3 和 JDK 21；构建 GT6CE 还要 JDK 17。编译只依赖 GT6CE 的加载器 SDK（`libs/gt6ce/gt6ce-addon-sdk-1.21.1-neoforge-*.jar`）。侧面、廉价超频和上方光照都在这份 API 0 的配方机器构建器上；0.8.0 的构建器没有这些方法。`runClient` 和 `runServer` 使用同一份源码打出来的 `gt6ce-1.21.1-neoforge.jar`（模组版本 0.0.0）。两个 jar 都不进 git，也不打进成品；`tools\build-gt6ce.ps1` 按 `libs/gt6ce/build-commit.txt` 里的提交把它们打出来。

依赖模组是 `gt6ce`，`versionRange="[0.0.0,0.8)"`。0.8.0 的 `MachineDeclaration` 装不下这些字段，装上会在注册时崩溃，所以范围不包含它。

```powershell
.\tools\build-gt6ce.ps1   # 第一次，或者 build-commit.txt 变了之后
.\gradlew.bat build
.\gradlew.bat runClient
```

脚本只读取源仓库：默认用旁边的 `..\GregTech6`（要包含固定提交），没有就从 GitHub 克隆；构建发生在 git 忽略的 `.gt6ce-src` 里，不会动你正在用的 GregTech6 检出。JDK 17 和 21 会在 `JAVA17_HOME`、`JAVA21_HOME`、常见安装目录和 `~/.gradle/jdks` 里找，缺 17 时加 `-AutoDownloadJdk` 让 Gradle 下载；`-DryRun` 只打印会用到什么。`libs/gt6ce/build-commit.txt` 钉住的是 GT6CE `main` 上的 `1d1f69fbb`（API 0，模组版本 0.0.0），CI 和脚本按同一个提交构建。`.sha256` 记的是一次本地构建的运行包，换机器重打不一定逐字节相同，CI 只警告；换了 `build-commit.txt` 之后用 `-UpdateChecksum` 更新它。这一版删掉了旧的 `MachineDeclaration` 一族，机器从 `addonMachines()` 返回。`addon/behavior-energy` 上的 `0f77a888` 给燃料机加了液体马达提示，并去掉一部分 “(no auto)” 面标签；这一版运行包没有那次改动。

## 许可

GNU Affero General Public License 第 3 版。`LICENSE` 是 AGPL-3.0 原文，同样的文本在 `src/main/resources/META-INF/licenses/AGPL-3.0.txt`，随 jar 发布。
