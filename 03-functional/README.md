# 03 · 函数式与 Stream

**建议：3 周** · 先完成 [`02-core-apis`](../02-core-apis/README.md)。

## 目标

用 lambda 和 Stream 表达数据变换，同时知道何时不该用。

## 要学什么

- 函数式接口、方法引用、lambda 捕获与 effectively final
- `Optional` 的正确用法（不要把 `Optional` 当字段滥用）
- Stream：`map` / `filter` / `reduce` / `collect` / `groupingBy`
- 惰性求值、短路；并行流默认别用
- `Comparator` 链式比较；不可变收集 `Collectors.toUnmodifiableList`

## 练习题（写入本目录 `src/`）

把阶段 2 的 CSV 统计改写成 Stream；再写一份命令式对照，比较可读性。

## 过关标准

能手写 `groupingBy` + 下游 collector，并指出一段「伪函数式」代码的问题。

## 这一阶段先别碰

响应式全家桶（Reactor 深水区）。
