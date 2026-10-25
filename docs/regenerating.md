# 重新生成代码和资源

需要 Python 3.11+。从仓库根目录运行；生成文件只能改输入/脚本后重生成。

1. 按 [tools/README.md](../tools/README.md#生成脚本的输入) 提供固定 kTFRUAddon `6404ab69`、GT6CE `build-commit.txt` 对应源码、原版 GT6 源码副本和运行 jar。环境变量分别是 `KTFRU_UPSTREAM`、`GT6CE_SOURCE`、`GT6_REF`；默认路径与克隆命令见工具说明。
2. 修改对应脚本或 `tools/mappings.json`。材料声明 `content/Materials.java` 与 `assets/gt6ce/lang` 是手工维护；退役的 `gen_materials.py` 不能覆盖它们。
3. 执行统一入口，检查差异，再跑构建。

```powershell
python tools/regenerate.py
python tools/regenerate.py --check --full
python -m unittest discover -s tools -p test_generated.py
.\gradlew.bat documentationInventory
python tools/documentation_reports.py
python tools/documentation_reports.py --check
.\gradlew.bat build
```

统一流水线依次运行物品、布局、结构资源、机器贴图和源配方生成器。产物归属与来源头规则见 [工具说明](../tools/README.md#流水线与产物归属)。`SourceRecipes.java`、`GeneratedLayouts.java`、`KtfruItems.java` 不手改。

`--check --full` 在临时目录重生成并比较字节、再查幂等；缺输入即失败。普通 `--check` 缺输入时会明确降为来源头检查，不能把它报告为全量复现。原版 GT6 副本没有已确认的可下载固定提交，必须由维护者提供；运行 jar 存在性也不等于独立来源证明。

`documentation_reports.py` 不需要上游检出；先用 SDK 和 Gradle 执行 `documentationInventory` 从实际登记表导出方块全集，再运行 Python，生成两个文档：中文同文/缺键清单和方块直接配方产物清单。它扫描生成源配方的输出参数与数据配方 result，不把输入引用算作产物；别名来自 mappings.json。修改配方调用签名、动态产物或手写方块输出时需同步扩展此工具。清单不是完整进度链求解器。
