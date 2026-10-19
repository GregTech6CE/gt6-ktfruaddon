# 01 表面材料：粉、液体、气体

- 状态：active
- 依赖：无
- 文件范围：`src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruMaterials.java`，`src/main/java/com/lombinaxmasson/gt6ktfruaddon/Gt6KtfruAddon.java` 的启动检查，`src/main/resources/assets/gt6ce/lang/`，`tools/gen_materials.py`
- 平台：NeoForge 1.21.1

## 背景

表面清单的材料在 kTFRUAddon `api/material/materialPreInit.java`，大约 22000 段。接口能声明 `dust()`、`liquid()`、`gas()` 和 `metal()`。不能新增前缀。

## 目标

- 把这份表里能用现有形态表达的材料声明出来。名字、显示名、颜色、化学式和温度跟源表。摄氏的条目按源码 `registerC` 加 273 后写入。密度源表没有，写 1。
- 英文语言键用显示名。中文用上游 `zh_CN.lang` 的 `gt.material`；对不上的用源码上一行的短注释。粉和流体显示名都走 `material.gt6ce.<小写名>`。
- 启动时能找到重铬酸铵粉、甲醇和乙炔。

## 接口缺口

用不上 `AddonMaterialContributor` 的，记在这里，并已补到卡 05。

- 硅橡胶、丁苯橡胶、PEEK 的板和箔。`forms()` 可以请求 plate 和 foil，但材料没有锭时这个请求会在启动时被拒绝。这三样源表要板和箔，没有锭，所以 `forms()` 留空。
- 智金、二氧化钼的矿。矿石不是生成前缀，请求会在启动时失败。
- 飞轮、大涡轮叶片、三级纯尘。新前缀不在接口里。

## 不做

- `HenSoPretty`。
- 改源表里明显抄错的化学式。丙二醇的式子和丙醇相同，先照抄。
- 坩埚成分和合金。那是后面的材料卡，不在这张。

## 步骤

- [x] 从 `6404ab69` 的源表生成声明，跳过玩笑材料。138 条，没有跟内置材料撞名。
- [x] 启动检查改到重铬酸铵、甲醇、乙炔。占位拉晶炉不再引用已删掉的单晶硅。
- [x] 开发客户端到了标题画面。启动检查确认重铬酸铵粉、甲醇流体和乙炔气体都在。
- [ ] 创造栏里能翻到全部粉、金属锭和流体显示物。上一版只有拉晶炉、氢燃烧器和一撮重铬酸铵粉，栏已改，还没在新客户端里点开。

## 验收

- [x] 编译
- [x] 开发客户端到达标题画面
- [ ] 玩法：在创造栏或 EMI 里点开重铬酸铵粉和甲醇

## 风险

- 源表把乙烷标成液体。声明照这个标，不改成气体。
- 带 `liquid()` 或 `gas()` 的材料会生成 `gt6ce` 流体。如果以后 GT 自己登记了同名材料，启动会因为字段不一致失败。
- 138 种材料远小于卡 19 测过的 2000 种，但仍会加配方行和 EMI 条目。

## 日志

- 2026-10-19：按 `kTFRUAddon` `6404ab69` 的 `materialPreInit` 生成 138 条，跳过 `HenSoPretty`。`forms()` 留空。硅橡胶、丁苯橡胶、PEEK 缺板和箔。智金、二氧化钼缺矿。`compileJava` 通过。开发客户端到标题画面，日志是 `gt6ktfruaddon: ready on neoforge 1.21.1 (GT6CE addon API 8)`，启动检查没有抛缺粉、缺甲醇、缺乙炔。创造栏当时只有拉晶炉、氢燃烧器和重铬酸铵粉。随后把每条材料的粉、锭和 `fluid_item_` 放进同一个栏。中文先是英文拷贝；现已从上游语言表写进 `zh_cn.json`，138 条都有中文。重新进游戏才能看见。
