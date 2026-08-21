# 02 · 核心类库

**建议：4 周** · 先完成 [`01-language-basics`](../01-language-basics/README.md)。

## 目标

熟练使用集合、异常、I/O 与时间 API，不再手写低效数据结构。

## 要学什么

- 异常：checked vs unchecked、try-with-resources、自定义异常
- 集合：`List` / `Set` / `Map`，`ArrayList` vs `LinkedList`，`HashMap` 原理直觉
- 泛型：类型擦除、`<T extends>`、通配符 PECS
- `java.time`（`LocalDateTime`、`Duration`、时区）；旧 `Date` 只认不写
- NIO.2 文件 API、字符编码；`Scanner` 仅作练习

## 练习题（写入本目录 `src/`）

读一个 CSV，统计、去重、排序后写出结果。异常路径要覆盖空文件和坏行。

可以自备 `data/sample.csv`，或先手写 10 行测试数据。

## 过关标准

能独立选对 `List` / `Set` / `Map`，并解释 `HashMap` 的 key 为什么必须稳定 `hashCode`。

## 这一阶段先别碰

Netty、自定义类加载器。
