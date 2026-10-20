# gt6-ktfruaddon

GT6CE 的 NeoForge 1.21.1 附属，用来试现在的附属接口能接住多少。仓库名和显示名是 gt6-ktfruaddon，游戏里的模组 id 是 `gt6ktfruaddon`（模组 id 不能带连字符）。这是一条新写的探针，不是 [kuzuanpa/kTFRUAddon](https://github.com/kuzuanpa/kTFRUAddon) 1.7.10 源码的移植。项目协议是 AGPL-3.0-only，与上游相同。上游检出在仓库根的 `kTFRUAddon/`，已忽略，不进版本库。

可移植范围在 [`docs/ktfruaddon-gt6-surface.md`](docs/ktfruaddon-gt6-surface.md)，从 CrucibleCraft 拷来。阶段卡在 [`docs/card-plans/README.md`](docs/card-plans/README.md)。表面材料卡已关。单方块配方机器在卡 02，流体燃料机在卡 03。占位氢燃烧器已撤。

## 开发

需要 JDK 21。编译只依赖 GT6CE 的加载器 SDK `gt6ce-addon-sdk-1.21.1-neoforge-0.8.0.jar`。`runClient` 和 `runServer` 使用完整的 `gt6ce-1.21.1-neoforge.jar`。两个 jar 都不进 git，也不打进成品。

依赖模组是 `gt6ce`，`versionRange="[0.8,)"`。当前钉住的 GT6CE 提交写在 `libs/gt6ce/build-commit.txt`。

```powershell
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.6.7-hotspot'
.\update-gt.ps1
.\gradlew.bat build
.\gradlew.bat runClient
```

`update-gt.ps1` 从 SaltNya/GregTech6 最近一次成功的 NeoForge 构建下载 SDK 和模组 jar，并核对校验和。CI 不下载产物，而是按 `build-commit.txt` 自己构建同一提交。

## 许可

GNU Affero General Public License 第 3 版。`LICENSE` 是 AGPL-3.0 原文，同样的文本在 `src/main/resources/META-INF/licenses/AGPL-3.0.txt`，随 jar 发布。
