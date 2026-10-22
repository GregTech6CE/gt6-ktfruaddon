# 08 虚空漏斗、矿机、水车、手摇和飞轮

- 状态：closed
- 依赖：07
- 文件范围：`Gt6KtfruVoidHopper.java`、`Gt6KtfruMiner.java`、`Gt6KtfruWaterMill.java`、`Gt6KtfruManualMotor.java`、`Gt6KtfruFlywheel.java`，`Gt6KtfruMachines.java` 里对应的 `BehaviorMachine`，方块状态、模型和语言
- 平台：NeoForge 1.21.1

## 背景

这些都是单方块行为，入口已经有了。卡 02、卡 03 把它们留在外面。这一张卡排队，不在卡 07 里注册。

实施前先读源类的 tick，再写行为。`collectItems` 不能过滤，`breakBlock` 不提供硬度或工具速度，`emit` 只送正数包。硬度可以用 `level().getBlockState` 读原版方块。过滤可以自己扫物品实体再 `insertOutput`。螺丝刀会不会在 `onUse` 之前被 GT 吃掉，先对 `AddonTools` 再写反转开关。

## 机器

| 源 id | 类 | 要点 |
| --- | --- | --- |
| 21010–21013 | `VoidHopper` | `Kinetic_T[0..3]`：木、青铜、钢、钛。RU 64 / 32 / 128 / 512，范围 4 / 8 / 16 / 24。源里有过滤槽、黑白名单、螺丝刀反转、软锤清空。卡 02 写过的 27550 在这份注册里没有 |
| 27520 | `Miner` | `Electric_T[0]`，材料注册名 `TinAlloy`，RF 128，范围 4，速度 1，硬度上限 8 |
| 27521–27523 | `Miner` | 镀锌钢 / 铝 / 不锈钢，EU 32 / 128 / 512，范围 12 / 20 / 32，速度 4 / 6 / 8，硬度上限 20 / 32 / 64。`maxRange` 上限就是 32 |
| 9800 | `WaterMill` | 材料 `WoodTreated`。扭矩来自相邻液体的流向，正负 RU 从正面和背面送出，还有腐烂计时和符号不对时的过载。`FluidState.getFlow` 能搬公式。`emit` 不带符号 |
| 10021 / 10022 | `ManualGenerator` | 处理木 RU 4–10、最长 400 tick；青铜 RU 14–22、最长 800 tick。右键走 `onUse`，每次加 8 tick |
| 10026 / 10027 | `ManualGenerator` | 处理木 KU 3–8、最长 200 tick；青铜 KU 6–16、最长 400 tick |
| 15000–15055 | `FlywheelBox` / `FlywheelBoxElec` | `Kinetic_T[0..5]` 六种材料，每种六台：普通 15000、大型 15010、电动 15020、大型电动 15030、真空 15040、大型真空 15050，都加材料索引。输入上限和输出上限是 `VMAX[j]`：8、32、128、512、2048、8192。输出下限是 gregapi `VMIN[j]`。库存 1 或 4，安培 4 或 16，损耗 0.1 / 0.15 / 0.07 / 0.1 / 0.0001 / 0.00012。索引 0 是 `Wood`，索引 5 的本地名是 `Ad` |

方块 id：`void_hopper_wood` 到 `titanium`；`miner_tin_alloy`、`miner_lv`、`miner_mv`、`miner_hv`；`water_mill`；`manual_motor_wood`、`manual_motor_bronze`、`manual_kinetic_motor_wood`、`manual_kinetic_motor_bronze`；飞轮是 `flywheel_`、`large_flywheel_`、`electric_flywheel_`、`large_electric_flywheel_`、`vacuum_flywheel_`、`large_vacuum_flywheel_` 再加材料后缀。界面沿用 `textures/gui/machines/gascompressor.png`。

## 不做

- 符文矿石扫描 27530–27533。注册包在 `isBotaniaTFRU` 里，C 档。
- 用 `collectItems` 假装已经有过滤器。
- 把带符号的水车扭矩写成「旁边有水就出 RU」。

## 步骤

- [x] 读 `VoidHopper`、`Miner`、`WaterMill`、`ManualGenerator`、`FlywheelBox` 的 tick。
- [x] 按上表注册。对不上的硬度、过滤和符号写在这张卡的日志里，不另造接口缺口。
- [ ] 开发客户端里让一台虚空漏斗或一台手摇马达跑起来。

## 验收

- [x] 编译
- [ ] 开发客户端
- [ ] 玩法：上表里一台机器按它自己的能量转完一个周期

## 日志

- 2026-10-22：从卡 02、卡 03 的延期收成排队卡。参数来自 `tileEntityInit0` 的活注册。
- 2026-10-22：飞轮六档上限写成电压档 8 到 8192。输出下限仍是 `VMIN[j]`。
- 2026-10-22：关卡。`compileJava` 通过。开发客户端和玩法仍未勾。
- `AddonTools` 只有锹、通用锹和放大镜。虚空漏斗的黑名单和清空、矿机的重置都是界面开关。清空把过滤物送进输出槽。放大镜仍走 `onUse`。
- 虚空漏斗自己扫物品实体。每 tick 最多看 64 个，通过过滤的最多收 16 个。匹配用物品和组件，空过滤在白名单下收全部、黑名单下不收。
- 矿机用 `getDestroySpeed` 做硬度和工具速度，`isCorrectToolForDrops` 代替假人的 `canHarvestBlock`。`breakBlock` 用空手收掉落，不再扫 1.5 格。声明范围是 32，所以低档矿机的水平方阵仍按 4 / 12 / 20，向下到 32 格或世界 y < 1 就停。
- 水车按流向叉乘算扭矩。不透明且没有液体的邻居让整台扭矩归零。包固定是正的 2，流量绝对值大于 2 时送 2 安培，先正面再背面。源类的负包、朝向吸附（西改东、北改南、下改上）和过载没有做。顶面是行为机必须的 RU 输入，标称 2。腐烂 9600 tick，空右手看扭矩，路径里同时有 `ring` 和 `bronze` 的物品在腐烂进度到 75% 之后可以清零。
- 手摇马达右键加 8 tick 并消耗 0.5 点饥饿。速率按剩余时间的五等分，从输出下限走到上限。KU 的正负交替做不到，送出的包总是正的。底面是必须的输入面。
- 飞轮箱背面进、正面出，包大小是 `VMAX[j]`，安培 4 或 16。容量保持 0：材料接口没有质量、工具品质和工具速度，飞轮前缀也没注册，跨 tick 存不住。`VMIN[j]` 写不进输出下限。输入窗口是标称的一半到两倍，不是源类的硬上限。满箱爆炸和电动箱的钳制都做不到，缓存自己钳制。右键可以放入路径含 `flywheel` 的物品，也可以取出，但放入不改变容量。损耗公式在缓存里还有能量时会跑。
