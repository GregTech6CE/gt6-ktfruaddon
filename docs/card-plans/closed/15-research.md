# 15 研究监视器、显微镜和研究组装

- 状态：closed
- 依赖：排在卡 14 之后
- 文件范围：研究方块自己的方块实体、显微镜和研究组装的行为机、玻璃和工作台 `CASING` 格
- 平台：NeoForge 1.21.1

## 背景

研究系统是 kTFRU 自己的。科技树存在研究监视器的 `researchTree` 里，数据库回答某件物品解锁了没有，研究数据物品在 kTFRU 的 `ItemList` 里。GT 不保存这棵树。附属自己注册这些方块并自己存。

点到 Advanced Rocketry 物品的解锁配方不进这张卡。

## 方块

9810–9817 和 1106 的注册只写了送出 512 EU，没有输入能量。显微镜、组装机和反应堆的数字在表里。

| 源 id | 类 | 注册 |
| --- | --- | --- |
| 9810 | `ResearchTreeMonitor` | 送出 512 EU |
| 9811 | `ResearchTableFillInPack`，电路研究台 | 送出 512 EU |
| 9812 | `ResearchTableItem` | 送出 512 EU |
| 9813 | `ResearchTablePrinterTech` | 送出 512 EU |
| 9814 | `ResearchDatabaseItem` | 送出 512 EU |
| 9815 | `ResearchDatabaseOnline` | 送出 512 EU |
| 9816 | `ResearchTableCompute` | 送出 512 EU |
| 9817 | `ResearchTableLinkGame`，电流控制 | 送出 512 EU |
| 1106 | `ResearchTableIdentify` | 送出 512 EU。活注册，原来这张表漏了 |
| 30077 | `OpticalMicroscope` | 铝，EU 128，范围 16–512 |
| 30078 | `ElectronMicroscope` | 不锈钢，EU 512，范围 16–4096 |
| 30042 | `ResearchAssembler` | 铝，EU 131072，范围 64–524288，配方图 `ResearchAssembler`，槽位 9、6、0、3、3、0、1 |
| 30043 | `ResearchReactor` | 不锈钢，EU 131072，范围 64–524288，配方图 `ResearchReactor`，槽位 9、6、0、6、6、0、1 |

槽位顺序是输入物品、输出物品、最少物品、输入流体、输出流体、最少流体、最少输入。玻璃 31056 是 `GlassPart`。工作台一类的原版方块写成 `CASING`。研究组装机自己用 `AddonRecipes.find` 查行，产物没解锁就不加工。

## 不做

- 研究树里点到 Advanced Rocketry 的解锁。
- 等 GT 提供研究数据库接口。没有这个接口要等。

## 步骤

- [x] 上表的能量和两张配方图槽位已从注册行写入。
- [ ] 读监视器的 NBT 和一台显微镜的结构。
- [ ] 先注册监视器，让它能存一棵只有原版或已有材料的小树。
- [ ] 开发客户端里打开监视器并看到那棵树。

## 验收

- [x] 编译
- [ ] 开发客户端
- [ ] 玩法：研究监视器存住一棵不含 C 档物品的树，或者一台显微镜按它的能量完成一次观察

## 日志

- 2026-10-22：从卡 04、卡 05 的「kTFRU 自己的系统」收成排队卡。
- 2026-10-22：研究方块的 512 EU 输出、显微镜范围、组装机和反应堆的槽位写入上表。1106 补进行。
- 2026-10-22：`e164df5` 误读旧检出，漏掉了 9816、9817、30077、30078、30043、31056，并把 30042 写成了旧版 LU 128 / `gt.recipe.assembler`。恢复原卡文本，差异与补齐见卡 20。
