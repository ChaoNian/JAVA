# 05 · 现代 Java 语法

**建议：3 周** · 先完成 [`04-concurrency`](../04-concurrency/README.md)。

## 目标

用 Java 21–25 的语言特性写更短、更安全的代码。以你本机 JDK 实际支持的特性为准。

## 要学什么

- `record`、`sealed` class、pattern matching `switch`
- text blocks、`switch` 表达式、`instanceof` 模式匹配
- `var` 的边界；局部类型推断不要滥用
- `SequencedCollection`、未命名变量 `_`（按 JDK 版本）
- 模块化 JPMS 只需概念级：多数练习仍用 classpath

## 练习题（写入本目录 `src/`）

用 `sealed` + `record` 建模表达式树或订单状态机，用 `switch` 穷尽匹配。

## 过关标准

能把一个「一堆 getter 的 JavaBean」改成 `record`，并说明何时不能改。

## 这一阶段先别碰

为了新语法而新语法；生产 JDK 未对齐时硬上预览特性。
