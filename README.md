# Java 语言学习仓库

按阶段练 **Java 语言和 JDK**，不先上 Spring。

建议环境：**JDK 21 或 25 LTS**。全职大约 22 周；业余每周 8–10 小时大约 6–9 个月。

## 怎么用

1. 从 [`00-setup`](00-setup/README.md) 开始，用命令行编译，不要只点 IDE 运行。
2. 每个阶段目录里的 README 写了：要学什么、练习题、过关标准、这一阶段先别碰什么。
3. 练习代码放在对应阶段的 `src/` 下；自己的概念卡片放 [`notes/`](notes/README.md)。
4. 过关后再进下一阶段。未完成语言主线前，不要建 Spring 工程。

## 阶段

| 目录 | 内容 | 建议周数 |
|------|------|----------|
| [00-setup](00-setup/README.md) | 环境、JDK/JRE/JVM、命令行编译 | 1 |
| [01-language-basics](01-language-basics/README.md) | 语法、面向对象 | 5 |
| [02-core-apis](02-core-apis/README.md) | 集合、异常、I/O、时间 | 4 |
| [03-functional](03-functional/README.md) | lambda、Stream | 3 |
| [04-concurrency](04-concurrency/README.md) | 线程、锁、虚拟线程 | 3 |
| [05-modern-java](05-modern-java/README.md) | record、sealed、pattern matching | 3 |
| [06-jvm-testing](06-jvm-testing/README.md) | JUnit、调试、JVM 直觉 | 3 |

语言过关后再学：SQL/JDBC → HTTP/JSON → **一个** Spring Boot 小服务。

## 每日节奏

- **读**不超过 40 分钟，立刻用代码验证。
- **写**一个小练习类，例如 `Ex01EqualsVsEq.java`。
- **讲**：阶段结束用自己的话在该目录 README 下补「踩坑」。
