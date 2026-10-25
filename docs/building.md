# 构建与运行

从仓库根目录运行。需要 Git、JDK 21、Python 3.11+；构建固定 GT6CE 源码还需要 JDK 17。Gradle Wrapper 随仓库提供，首次构建需要下载依赖。

```powershell
.\tools\build-gt6ce.ps1 -DryRun
.\tools\build-gt6ce.ps1
.\gradlew.bat build
.\gradlew.bat runClient
# 或运行开发专用服务器（首次按 Minecraft 要求接受 EULA）
.\gradlew.bat runServer
```

脚本从 `libs/gt6ce/build-commit.txt` 读取完整提交，目前为 `1d1f69fbb267ad31548b1fcb1ec1168435b67bf3`，在忽略目录 `.gt6ce-src` 构建。默认读取旁边的 `../GregTech6`，不存在时克隆；不改该源检出。JDK 查找顺序和下载选项见 [工具说明](../tools/README.md)；缺 JDK 17 可传 `-AutoDownloadJdk`，此下载路径仍待冷环境验证。

`libs/gt6ce/` 必须恰有一份 `gt6ce-addon-sdk-1.21.1-neoforge-*.jar`，另需 `gt6ce-1.21.1-neoforge.jar`。备份 SDK 请移出此目录；多个匹配 jar 会被构建拒绝。两份 jar 不入库，也不打进附属成品。SDK 是唯一 GT6CE 编译依赖，完整 mod jar 仅用于开发运行；发布版 0.8.0 缺少这些 API 0 声明入口，不能代用。

`build` 执行声明/资源契约、包依赖、行为公共件等 JVM 测试，产物在 `build/libs/`。测试报告在 `build/reports/tests/test/index.html`。JVM 测试不能替代放置机器后的合成与传能验收。

```powershell
.\tools\smoke-boot.ps1
.\gradlew.bat documentationInventory
python tools/documentation_reports.py --check
python tools/regenerate.py --check --full
```

冒烟脚本加载模组到 recipes-ready，按日志确认注册成功；没有 GameTest 的提示是预期情况。完整生成检查还需额外输入，见 [重新生成](regenerating.md)。日常 Java 构建不需要 kTFRUAddon 或原版 GT6 源码副本。

本轮干净克隆、构建和实际专用服务器验证的证据与边界见 [展示前自检记录](verification.md)。干净克隆复用已安装的 JDK 与用户级 Gradle/NeoForm 依赖与变换缓存；远端零缓存环境及自动下载 JDK 分支仍未验证。
