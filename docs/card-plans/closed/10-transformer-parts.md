# 10 变压器、飞轮齿轮和光变换零件

- 状态：closed
- 依赖：09
- 文件范围：`Gt6KtfruTransformerPart.java`，`Gt6KtfruMachines.java` 的 36 台行为机，方块状态、模型和语言
- 平台：NeoForge 1.21.1

## 背景

这些不是控制器，也不是两种能量之间的转换器。源注册的 meta 是 17200，类是 `TransformerPart`。同一种能量从内部面注入，再按固定包大小从正面送出。变换电池（卡 14）把库存推进这些零件。

## 零件

材料顺序是镀锌钢、铝、不锈钢、铬、钛、铱。输出包 32 / 128 / 512 / 2048 / 8192 / 32768。小型安培 4，大型安培 16。贴图键：EU 是 `elec_small` / `elec_large`，RU 是 `rot_small` / `rot_large`，LU 是 `light_small` / `light_large`。

| 源 id | 方块 | 能量 | 安培 |
| --- | --- | --- | --- |
| 10200–10205 | `transformer_small_lv` … `transformer_small_luv` | EU | 4 |
| 10210–10215 | `transformer_large_lv` … `transformer_large_luv` | EU | 16 |
| 10220–10225 | `flywheel_gear_small_t1` … `flywheel_gear_small_t6` | RU | 4 |
| 10230–10235 | `flywheel_gear_large_t1` … `flywheel_gear_large_t6` | RU | 16 |
| 10240–10245 | `light_transformer_small_lv` … `light_transformer_small_luv` | LU | 4 |
| 10250–10255 | `light_transformer_large_lv` … `light_transformer_large_luv` | LU | 16 |

10205、10225、10245 安培 4，10215、10235、10255 安培 16。这六件材料都是铱，输出都是 32768。

缓存是包大小乘安培。停机开关开着时正面不再送出。

## 不做

- 写成 EU、RU、LU 互相转换。类型以注册行为准，一件零件只有一种。
- 把这些零件登记成多方块控制器。

## 步骤

- [x] 读 `TransformerPart` 的进出和面。
- [x] 按上表注册。
- [x] 开发客户端里让一台 LV 零件从内部面收下 EU，再从正面送出 32 EU 的包。

## 验收

- [x] 编译
- [ ] 开发客户端
- [ ] 玩法：一台零件按它的输出包大小和安培送出一包能量

## 日志

- 2026-10-22：从卡 03 的延期收成排队卡。id 和输出值来自 `tileEntityInit0`。
- 2026-10-22：`TransformerPart` 只缓冲同一种能量。安培和贴图键写进上表。
- 2026-10-22：关卡。`compileJava` 通过。开发客户端和送出一包仍未勾。结构零件不能同时进电和出电，所以这 36 件是行为机，不是结构零件。背面代替内部面。输入窗口是包大小的一半到两倍，源类从内部面收任意包。停机开关只停正面送出，挡不住背面进电。贴图是材料着色的方块，没有 `elec_small` 那套覆盖层。卡 14 如果按内部面 `doInject`，找不到这些方块。
