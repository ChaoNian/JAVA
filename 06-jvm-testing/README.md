# 06 · JVM、测试与工程习惯

**建议：3 周** · 先完成语言阶段 01–05。本阶段再引入 JUnit。

## 目标

代码可测、可调试、能看懂常见 JVM 报错，而不是只会「再跑一遍」。

## 要学什么

- JUnit 5、断言、参数化测试；测试金字塔直觉
- 调试器：断点、求值、调用栈；读 stack trace
- 堆、栈、GC 代际模型直觉；`OutOfMemoryError` / `StackOverflowError`
- 日志（SLF4J 概念）、不可变对象与防御性拷贝
- 构建：Maven 生命周期、把单元测试接入构建

## 练习题（本目录）

本阶段第一次加 `pom.xml`，依赖**只放 JUnit 5**。业务代码是 01–03 语义的自包含精简版，方便在一个模块里写测试。

| 包 / 类 | 练什么 |
|---------|--------|
| `money.Money` + `MoneyTest` | 阶段 1 金额；`assertEquals` / `assertThrows` / `@ParameterizedTest` |
| `order.OrderStats` + `OrderStatsTest` | 阶段 2/3 统计；不可变结果；`@MethodSource` |
| `immutable.UserProfile` / `TagBag` | 防御性拷贝 vs 泄漏内部 List |
| `debug.CheckoutService` + 测试 | 故意 NPE，堆栈定位到 `resolveCity` |
| `jvm.StackOverflowDemo` / `OomDemo` | 栈 vs 堆；`-Xss` / `-Xmx16m` |
| `logging.AuditLogDemo` | JUL 练级别；注释里对照 SLF4J 分工 |
| `CompareApp` | 串起非极限路径的对照输出 |

在本目录执行：

```powershell
.\compile-and-run.ps1
.\run-jvm-demos.ps1
```

或直接：

```powershell
mvn test
mvn exec:java
```

练习 2 / 3：跑 `CompareApp` 或测试里的 NPE；再用 `run-jvm-demos.ps1` 看 OOM，把排查步骤记到 [`notes/`](../notes/README.md)。

## 过关标准

面对异常堆栈能定位到你的代码行，并用测试锁住修复。

## 这一阶段先别碰

JVM 调优玄学、火焰图（留到有真实性能问题时再学）。不要在本模块堆 Mockito / Spring。
