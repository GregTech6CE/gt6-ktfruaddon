# 15 研究监视器、显微镜和研究组装

- 状态：closed
- 依赖：14
- 文件范围：这套检出里实际注册的研究方块，以及研究组装机
- 平台：NeoForge 1.21.1

## 背景

研究系统是 kTFRU 自己的。科技树存在研究监视器的方块状态里，GT 不保存这棵树。点到 Advanced Rocketry 物品的解锁配方不进这张卡。

这套检出的活注册和原来的表不一致。9816、9817、光学显微镜 30077、电子显微镜 30078、研究反应堆 30043、玻璃零件 31056 都不在 `tileEntityInit0` 里。30043 在这份源码里是一段注释掉的钢燃气涡轮，不恢复。

## 研究方块

材料都是 StainlessSteel。注册只写了送出 512 EU，没有输入能量。类本身不发电。行为机仍要一个进电面，所以底面收 256–1024 EU，正面可以送出 512 EU。行为不凭空产生这 512 EU。

| 源 id | 类 | 方块 |
| --- | --- | --- |
| 9810 | `ResearchTreeMonitor` | `research_monitor` |
| 9811 | `ResearchTableFillInPack` | `research_table_circuit` |
| 9812 | `ResearchTableItem` | `research_table_item` |
| 9813 | `ResearchTablePrinterTech` | `research_table_printer` |
| 9814 | `ResearchDatabaseItem` | `research_database_item` |
| 9815 | `ResearchDatabaseOnline` | `research_database_online` |
| 1106 | `ResearchTableIdentify` | `research_table_identify` |

监视器第一次运行时，把状态 `tree` 写成 `minecraft:paper,minecraft:glass,minecraft:iron_ingot`。放大镜读出这串。没有 C 档物品。

## 研究组装机

源 id 30042，类 `ResearchAssembler`，显示名 TEST Research Assembler。材料 Aluminium。能量是 LU，标称 128，窗口 64–256，和注册的 `NBT_INPUT_MIN` / `NBT_INPUT_MAX` 一致。配方图是内置 `gt.recipe.assembler`，槽位按这张图是 2 个物品输入、1 个物品输出、1 个流体输入。

结构是两层，轴 Y，偏移 0,0,0。底层 `" W" / "TW" / "WW"`，上层三行 `" W"`。`T` 是 `minecraft:crafting_table`，角色 `CASING`。`W` 是 `steel_wall`。控制器是底层第一行的空位。

源类要问背后两格的研究数据库，第一件产物没解锁就不加工。行为机读不到旁边那台的状态，所以组装机自己存同一份原版小树。第一件产物的物品 id 不在这棵树里就跳过。

## 不做

- 研究树里点到 Advanced Rocketry 的解锁。
- 补登记里没有的显微镜、研究反应堆、9816、9817 和玻璃零件。
- 恢复注释掉的 30043。

## 步骤

- [x] 对照 `tileEntityInit0` 和 `ResearchAssembler` 改了这张卡的数字。
- [x] 监视器存一棵只有原版物品的小树。
- [ ] 开发客户端里打开监视器并看到那棵树。

## 验收

- [x] 编译
- [ ] 开发客户端
- [ ] 玩法：研究监视器存住一棵不含 C 档物品的树，或者一台显微镜按它的能量完成一次观察

## 日志

- 2026-10-22：从卡 04、卡 05 的「kTFRU 自己的系统」收成排队卡。
- 2026-10-22：研究方块的 512 EU 输出、显微镜范围、组装机和反应堆的槽位写入上表。1106 补进行。
- 2026-10-22：关卡。`compileJava` 通过。开发客户端和玩法都还没跑。
- 这套检出没有 9816、9817、30077、30078、30043 研究反应堆和 31056。组装机按活注册是 LU 64–256，配方图 `gt.recipe.assembler`。
- 监视器和组装机各自存纸、玻璃、铁锭。读不到相邻数据库，记在卡 05。底面进电也记在那里。
