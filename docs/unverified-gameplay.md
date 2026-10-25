# 未验收清单

closed 表示该卡代码范围收尾，不表示已在游戏里验证。下列逐项保留卡片尚未勾选的检查（含客户端、玩法和其他未完成前提），不将构建成功替代玩法验收。完成后同时更新原卡与此清单。

当前临时 GameTest 只证明生命周期/飞轮物品适配，未覆盖放置机器后的完整合成、配对和传能。

## [02-single-block-machines](card-plans/closed/02-single-block-machines.md)

- [ ] 开发客户端里放下一台热混合器并跑完一条配方。
- [ ] 玩法：一台 EU 配方机器吃进物品、吐出物品

## [03-fuel-generators](card-plans/closed/03-fuel-generators.md)

- [ ] 玩法：钢壳轮机烧掉一种气体并在相邻面输出 RU

## [04-multiblocks](card-plans/closed/04-multiblocks.md)

- [ ] 开发客户端里把结构搭起来并跑完那条配方。
- [ ] 玩法：一台左列多方块成型并加工

## [07-compressor-pipes](card-plans/closed/07-compressor-pipes.md)

- [ ] 开发客户端里把一台压缩机对着一只气罐灌进气体。
- [ ] 开发客户端到达标题画面，启动检查通过
- [ ] 玩法：青铜压缩机把气体推进相邻的钢压缩气罐

## [08-kinetic-behaviors](card-plans/closed/08-kinetic-behaviors.md)

- [ ] 开发客户端里让一台虚空漏斗或一台手摇马达跑起来。
- [ ] 开发客户端
- [ ] 玩法：上表里一台机器按它自己的能量转完一个周期

## [09-gas-battery](card-plans/closed/09-gas-battery.md)

- [ ] 开发客户端
- [ ] 玩法：4U 气体电池输出 EU

## [10-transformer-parts](card-plans/closed/10-transformer-parts.md)

- [ ] 开发客户端
- [ ] 玩法：一台零件按它的输出包大小和安培送出一包能量

## [11-mask-aligner-cnc](card-plans/closed/11-mask-aligner-cnc.md)

- [ ] 开发客户端
- [ ] 玩法：一台掩膜机或 CNC 成型并加工

## [12-heat-oil-tidal](card-plans/closed/12-heat-oil-tidal.md)

- [ ] 开发客户端里把那一台跑完。
- [ ] 开发客户端
- [ ] 玩法：上表里一台机器成型并产出热量、流体或加工结果

## [13-turbines-dynamos](card-plans/closed/13-turbines-dynamos.md)

- [ ] 开发客户端里用内置燃气表的氢让它送出 RU。
- [ ] 开发客户端
- [ ] 玩法：一台涡轮或扩展马达烧掉一种内置燃料并送出 RU

## [14-batteries](card-plans/closed/14-batteries.md)

- [ ] 开发客户端里让它收下并送出一包能量。
- [ ] 开发客户端
- [ ] 玩法：一台流体电池或变换电池完成一次能量交接

## [15-research](card-plans/closed/15-research.md)

- [ ] 读监视器的 NBT 和一台显微镜的结构。
- [ ] 先注册监视器，让它能存一棵只有原版或已有材料的小树。
- [ ] 开发客户端里打开监视器并看到那棵树。
- [ ] 开发客户端
- [ ] 玩法：研究监视器存住一棵不含 C 档物品的树，或者一台显微镜按它的能量完成一次观察

## [16-compute](card-plans/closed/16-compute.md)

- [ ] 读一台生物阵列的结构字符串。
- [ ] 先注册最小的无线控制器和一档节点。
- [ ] 开发客户端里把两个节点放进距离内，确认集群认它们。
- [ ] 开发客户端
- [ ] 玩法：一台无线计算控制器在距离内认下一档节点

## [17-wireless-energy](card-plans/closed/17-wireless-energy.md)

- [ ] 注册感应塔和感应接收器。
- [ ] 开发客户端里让塔把一包能量送到接收器，接收器再送进相邻电缆。
- [ ] 开发客户端
- [ ] 玩法：感应塔向配对的接收器送出一包能量

## [18-fusion-late-machines](card-plans/closed/18-fusion-late-machines.md)

- [ ] 先注册一台 `MultiTileEntityBasicMachine`，并给一条不依赖 C 档物品的配方。
- [ ] 托卡马克、激光聚变、催化反应器和 AI 中心读完结构再注册。量子金、观测塔、微重力房间和晶体机的结构已经写在上面。
- [ ] 开发客户端里把那台配方机跑完一条配方。
- [ ] 开发客户端
- [ ] 玩法：上表里一台配方机加工完一条配方

## [19-crucible](card-plans/closed/19-crucible.md)

- [ ] 开发客户端里让坩埚按一种已知材料的熔点完成一次加热或凝固。
- [ ] 开发客户端
- [ ] 玩法：坩埚在不查询任意物品组成的前提下，完成一次升温或一种已知材料的熔化

## [20-card15-18-reconcile](card-plans/closed/20-card15-18-reconcile.md)

- [ ] 开发客户端
- [ ] 玩法：卡 15–18 补齐后的机器各自按原卡验收项跑通

## [21-addon-side-subsystems](card-plans/closed/21-addon-side-subsystems.md)

- [ ] 开发客户端
- [ ] 玩法：微重力、研究解锁、激光脉冲、量子中继和算力网各自跑通

## [22-own-prefixes-recipes-textures](card-plans/closed/22-own-prefixes-recipes-textures.md)

- [ ] 开发客户端
- [ ] 玩法：纯度提纯、掩膜光刻、催化反应器、激光聚变脉冲与机器外观

## [23-api-gap-fill](card-plans/closed/23-api-gap-fill.md)

- [ ] 界面里搭一台托卡马克，确认 LU 充能后进度才开始。
- [ ] 玩法：托卡马克启动能量、流体电池危险流体、计算格二选一各跑一次
