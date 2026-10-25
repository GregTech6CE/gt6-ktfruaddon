# 架构与新增一台机器

入口 `KtfruAddon` 把声明交给 GT6CE，并在 recipes-ready 注册配方和自检。根包 `Ktfru` 只提供模组常量。`registry.Features` 按固定顺序汇总并缓存各领域的 `Feature`；`registry.Machines` 是服务入口，`registry.Recipes` 调用手写和生成配方。

领域包为 processing、gas、kinetic、power、heat、semiconductor、research、network、quantum、fusion。一台机器的声明、布局、行为放在所属领域。`core` 放无领域依赖的布局、登记、运行器、能量与坐标公共件；`recipe` 放配方图常量和时序类型；`generated` 放生成表，`content` 放手写材料，`event`/`client` 处理交互与渲染。依赖方向由 `PackageDependencyTest` 守卫；领域不能反向依赖 registry。

1. 先确定所属 Feature 和稳定 snake_case id，阅读同领域的现有机器。配方机可参考 `processing.SingleBlockMachines`，行为机参考 `kinetic.KineticMachines`。固定 SDK 不支持的能力先写入 [账本](card-plans/prep/05-api-gaps.md)，不借内部类绕过。
2. 配方图常量放 `recipe.RecipeMaps`，由 Feature.recipeTables() 声明。机器放 machines()；布局放 layouts()、零件放 parts()。行为机在 behaviors() 返回 machineType→构造器映射。图 id、方块 id 和行为类型不是同一个概念。
3. 优先扩展已有 Feature。新增 Feature 时放在 `Features.get()` 列表末尾；已有 Feature 和零件顺序不变，GT6CE 依序给零件编号。在现有 Feature 里追加零件也可能挤动后续编号，必须评估旧存档兼容，不能只看 id 没变。管道等派生方块还需 derivedBlockIds()。
4. 多方块优先用 `core.layout.LayerLayout` 的字符网格；明确 IO 面、包范围、容量、结构角色和辅助能量。图参数用 `RecipeTiming.ticks(...).euPerTick(...)`。行为机复用 `core.behavior.RecipeRunner`；临时跨机器登记必须接入 `core.behavior.BehaviorLifecycle` 生命周期，不新增裸静态全局表。
5. 源配方通过生成脚本加入，手写配方在 registry/Recipes；普通合成放 `data/gt6ktfruaddon/recipe/`。方块状态、模型、贴图和 en_us/zh_cn 同步更新；生成器拥有的资源只能改其输入再生成。来源和适配差异写明。
6. 执行 `gradlew build`、完整生成检查和文档报告更新；现有契约会检查方块 id、资源、布局、行为映射及包依赖。新增的行为规则按风险补测试，并在游戏中验证至少一条配方/燃料或交互；没完成的写入 [未验收清单](unverified-gameplay.md)。

启动自检从登记表推导，不另加一串手写 id。新增 machineType 要和行为表一致；不要靠方块 id 前缀猜行为或材料。着色回调使用注册时解析的颜色。命名兼容假设统一放 `core.Gt6ce` 并记账。
