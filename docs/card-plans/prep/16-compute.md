# 16 计算机集群和无线计算

- 状态：prep
- 依赖：排在卡 15 之后
- 文件范围：计算控制器、节点和外壳。坐标表存在附属自己的方块上
- 平台：NeoForge 1.21.1

## 背景

无线计算机把对方的坐标和维度记在自己的 NBT 里，tick 时到那个世界找附属自己的方块。能不能加入集群看距离。GT 不负责这段链路。

## 控制器和节点

1103、1105 和三台无线控制器的注册都是送出 512 EU，材料是不锈钢（1103 和 1105 的行没有另写材料）。最小无线控制器的入簇距离是 512 格。`getDistanceFrom` 返回的是距离平方，比较前把 512 再平方一次。

最小结构的层规则是 `ABA`，偏移 -1、0、0。A 层是 3×3 的 `WWW`。B 层可加长，上限 4，每层是 `WWN / W N / WWN`。W 是 GT 不锈钢墙 18002。N 是计算零件。玻璃 31056 在类里声明了，这套结构没有用。大型和巨型只改布局和能装的节点数。

| 源 id | 类 | 材料 |
| --- | --- | --- |
| 1103 | `TestUser` | 注册行没有另写 |
| 1105 | `TileDreamBrain` | 注册行没有另写 |
| 30058 | `ControllerWireless` | StainlessSteel |
| 30059 | `ControllerWirelessLarge` | StainlessSteel |
| 30060 | `ControllerWirelessHuge` | StainlessSteel |
| 30061 | `BiologyComputeControllerT1` | StainlessSteel |
| 30062 | `BiologyComputeControllerT2` | Chromium |
| 30063 | `BiologyComputeControllerT3` | Graphene |
| 30064 | `BiologyComputeControllerT4` | Trinaquadalloy |
| 30065 | `QuantumComputeControllerT1` | Naquadah |
| 30066 | `QuantumComputeControllerT2` | Nq_522 |
| 30067 | `QuantumComputeControllerT3` | Nq_522 |
| 30068 | `QuantumComputeControllerT4` | Trinaquadalloy |
| 30069 | `SpacetimeComputeControllerT1` | Nq_522 |
| 30070 | `SpacetimeComputeControllerT2` | Trinaquadalloy |
| 30079 | `QuantumComputeCenterController` | Trinaquadalloy |
| 32005 | 计算集群外壳 | Chromium |
| 32006 | `MultiBlockPartComputeClusterSimple` | StainlessSteel |

无线节点是普通算力。本地量子零件是量子算力。损耗率按注册原样记，不把它换成百分比。

| 源 id | 材料 | 最大算力 | 损耗率 |
| --- | --- | --- | --- |
| 32010 | Aluminium | 2000 | 0.5 |
| 32011 | StainlessSteel | 20000 | 0.3 |
| 32012 | Chromium | 200000 | 0.2 |
| 32013 | Titanium | 2000000 | 0.1 |
| 32040 | Naquadah | 65536 | 1023 |
| 32041 | Nq_522 | 262144 | 1023 |
| 32042 | Nq_522 | 1048576 | 511 |
| 32043 | Trinaquadalloy | 16777216 | 63 |

生物、量子、时空和 AI 的墙在 31057–31095、31111–31116、31130–31133。结构字符串仍在实施时读，再决定哪些墙要声明成外壳。零件数量可以由控制器用 `level()` 数，零件本身保持外壳。

源码里注释掉的 1100、1102、1104 不恢复。1106 是研究台，在卡 15。

## 不做

- 戴森球界面 32750。
- 把无线距离写成 GT 的能量输送。

## 步骤

- [x] 最小无线控制器的入簇距离、各控制器材料和节点算力已写入上表。
- [ ] 读一台生物阵列的结构字符串。
- [ ] 先注册最小的无线控制器和一档节点。
- [ ] 开发客户端里把两个节点放进距离内，确认集群认它们。

## 验收

- [ ] 编译
- [ ] 开发客户端
- [ ] 玩法：一台无线计算控制器在距离内认下一档节点

## 日志

- 2026-10-22：从卡 04 右列拆出。id 来自活注册。
- 2026-10-22：控制器材料、512 格入簇距离和八只节点的算力、损耗率写入上表。生物阵列的结构字符串仍留到实施。
