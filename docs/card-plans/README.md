# 计划卡

阶段以 [`docs/ktfruaddon-gt6-surface.md`](../ktfruaddon-gt6-surface.md) 为准。那份清单里的 C 档（TFC、Advanced Rocketry、Botania、整合包脚手架）不进卡。

编号只增不复用。正在做的在 `active/`，排队的在 `prep/`，做完的移到 `closed/`。

表面清单上的一项用不上 GT6CE 附属接口时，写在当前卡的「接口缺口」里，并补到[卡 05](prep/05-api-gaps.md)。记下源里的名字、想用的入口、缺的是什么。不要改用 GT 内部类绕过去。

| 编号 | 标题 | 状态 | 依赖 |
| --- | --- | --- | --- |
| 01 | 表面材料：粉、液体、气体 | closed | 无 |
| 02 | 单方块配方机器 | active | 01 |
| 06 | 单方块机器的源配方和 EMI | closed | 02。排在 03、04 之前 |
| 03 | 单方块流体燃料机 | active | 01。排在 06 之后 |
| 04 | 现有零件能搭的多方块 | prep | 01、02。排在 06 之后 |
| 05 | 表面里接口接不住的部分 | prep | 无 |
