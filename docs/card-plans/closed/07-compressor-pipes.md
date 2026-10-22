# 07 气体压缩机、压缩气罐和管子

- 状态：closed
- 依赖：03
- 文件范围：`Gt6KtfruGasCompressor.java`，`Gt6KtfruMachines.java` 的压缩机、气罐和管子声明，`Gt6KtfruAddon.java` 的创造栏和启动检查，压缩机的方块状态、模型和语言
- 平台：NeoForge 1.21.1

## 背景

卡 02 留下气体压缩机，卡 03 留下单方块压缩气罐。`6eba3e1ed` 上两边都有入口：压缩机是行为机加 `pushCompressedGas`，气罐是 `compressedGasTanks()`，管子是 `fluidPipes()` 和 `itemPipes()`。

## 气体压缩机

源类 `tankGasCompressedInputer`，22000–22004。材料是 `Kinetic_T[1]` 到 `[5]`：青铜、钢、钛、钨钢、艾德曼。源显示名用材料本地名，艾德曼是 `Ad`。

| 源 id | 方块 | 材料 | KU | 储罐 |
| --- | --- | --- | --- | --- |
| 22000 | `gas_compressor_bronze` | Bronze | 32 | 8000 mB |
| 22001 | `gas_compressor_steel` | Steel | 128 | 20000 mB |
| 22002 | `gas_compressor_titanium` | Titanium | 512 | 80000 mB |
| 22003 | `gas_compressor_tungstensteel` | Tungstensteel | 2048 | 200000 mB |
| 22004 | `gas_compressor_adamantium` | Adamantium | 8192 | 800000 mB |

五台共用机器类型 `gas_compressor`。能量从底面进，类型 KU。主输入不能写 `range`，接口按标称的一半到两倍收包，容量保持默认 0，所以缓存上限就是这个两倍。每个 tick 把缓存里的 KU 全部花掉，按 1 KU = 10 mB 推向背面。背面必须是 `compressedGasTanks()` 那种单罐。大型阀门是行为机，这次推送灌不进去。灌进去多少就从自己的罐扣多少。罐是空的时这一包 KU 仍然花掉，跟源 `doInject` 一样。超过两倍的包由接口拒绝；源里那种过载这里做不到。

储罐角色是 `INPUT`。管子能灌，不能抽。源里进气只开顶面，接口没有按面限制储罐。只收气体，罐里已有一种气体时不再收另一种。非气体会被倒掉。

界面图是 `textures/gui/machines/gascompressor.png`，储罐在 (152, 8)。大型阀门原来写的 `textures/gui/machines/gastank.png` 只有 `textures/gui/recipes/` 里那张，这一轮按声明路径拷了一份。

## 压缩气罐

源类 `CompressedGasTank`，10050–10054。英文显示名沿用源字符串。始终气密。普通灌注关着，只接受 `pushCompressedGas`，抽取仍然可以。温度上限是材料熔点。模型由 GT 生成，这张卡不覆盖。

| 源 id | 方块 | 材料 | 容量 | 耐酸 | 等离子体 | 耐魔 |
| --- | --- | --- | --- | --- | --- | --- |
| 10050 | `steel_gas_drum` | Steel | 12800000 | 否 | 否 | 否 |
| 10051 | `stainless_gas_drum` | StainlessSteel | 12800000 | 是 | 否 | 否 |
| 10052 | `manasteel_gas_drum` | Manasteel | 12800000 | 是 | 否 | 是 |
| 10053 | `netherite_gas_drum` | Netherite | 25600000 | 是 | 是 | 是 |
| 10054 | `tungstensteel_gas_drum` | Tungstensteel | 51200000 | 否 | 否 | 是 |

方块 id 用 `gas_drum`，避开已经占用的 `steel_compressed_gas_tank` 阀门。

## 管子

流体管一行：后缀 `teflon`，材料 `Teflon`，基础容量 800，气密、耐酸、不耐等离子体、耐魔，温度 0（按熔点 × 1.25），可燃 0。GT 展开成 `pipe_tiny_teflon` 到 `pipe_nonuple_teflon`。

物品管一行：后缀 `pvc`，材料 `PVC`，`stepSize` 8192，`invSize` 4。GT 展开成 `item_pipe_medium_pvc`、`large`、`huge`，以及三种 `restrictive_`。

源注册里接触伤害、阻挡和配方开关没有对应字段。接触伤害和阻挡在源里都是关的。配方由 GT 按内置管子规则生成。材料没有弯曲板时那一行合成会被跳过，方块仍然注册。

## 不做

- 把大型阀门改成压缩气罐方块。
- 源过载。
- 按面限制压缩机储罐。
- 气体电池、飞轮、手摇、水车。那些在后面的排队卡里。

## 步骤

- [x] 读 `tankGasCompressedInputer.doInject` 和 10050–10054、26142、25202 的注册参数。
- [x] 声明五台压缩机、五只气罐、两行管子。
- [ ] 开发客户端里把一台压缩机对着一只气罐灌进气体。

## 验收

- [x] 编译
- [ ] 开发客户端到达标题画面，启动检查通过
- [ ] 玩法：青铜压缩机把气体推进相邻的钢压缩气罐

## 日志

- 2026-10-22：五档压缩机共用 `gas_compressor`。气罐 id 用 `*_gas_drum`。特氟龙管和 PVC 管按源参数声明。阀门界面图补到 `textures/gui/machines/gastank.png`。`compileJava --offline` 通过，只有原来的 `EventBusSubscriber` 过时警告。还没进开发客户端。
- 2026-10-22：关卡。开发客户端和灌气玩法仍未勾。
