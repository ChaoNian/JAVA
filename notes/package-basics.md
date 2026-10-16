# Java 包（package）

## 这个概念解决什么问题？

类一多，单靠短类名会**重名**、难查找、难控制谁能访问谁。  
**包**给类型加上**命名空间前缀**（全限定名 `包名.类名`），并用**同名目录**存放源码，配合 `import` 在代码里写短名。

## 本质

| 机制 | 含义 |
|------|------|
| 全限定名 | `com.learn.intro.App` = 包 `com.learn.intro` 里的 `App` |
| 目录对应 | `package a.b.c;` → 源文件必须在 `a/b/c/` 下（相对 `src` 或源码根） |
| `import` | 只影响编译期名字解析，不拷贝代码 |
| 无名包 | 源文件没有 `package` → 默认包；练习可用，工程里应尽量避免 |

编译 `javac -d out` 后，`.class` 路径与包名一致：`out/com/learn/intro/App.class`。  
运行：`java -cp out com.learn.intro.App`（**全限定名**，不是文件名）。

## 语法

```java
package com.learn.example;   // 第一行，每文件最多一个

import java.util.List;      // 在 package 之后、类定义之前
import com.learn.other.Clock;
```

- **`java.lang`** 自动可用（`String`、`System` 等），不必 import。
- **同包**类型互相可见，不必 import。
- **`import com.foo.*`** 只导入 `com.foo` 下类型，不导入子包。

## 包访问（默认权限）

没有写 `public` 的类/成员是**包私有**：只有**同一个包**里的代码能直接访问。  
`public` 类文件名必须和类名一致；一个 `.java` 文件最多一个 `public` 顶级类。

## 常见 JDK 包（先有印象）

| 包 | 内容 |
|----|------|
| `java.lang` | 语言核心 |
| `java.util` | 集合等 |
| `java.io` / `java.nio.file` | I/O |
| `java.time` | 日期时间 |

自定义包常用**反写域名**：`com.learn.apis`，降低与别人冲突的概率。

## 最佳实践

- 包名全小写，按模块划分子包（`bank`、`apis`），不要把无关类堆进一个包。
- 命令行编译时先确认 **package 与文件夹一致**，`-d out` 输出根与 `-cp` 一致。
- 两个同名类：只 import 一个，另一个用**全限定名**；或两个都写全名。
- 新代码尽量**不要**放在无名包；与 [`Hello.java`](../00-setup/src/Hello.java) 对照理解即可。

## 常见误用

- 包名写了 `com.a.b` 却放在 `src/com/a/`（少一层 `b`）。
- 运行写 `java App` 而类在包里 → 应写 `java com.learn.intro.App`。
- 以为 `import` 等于把类「包含进来」。
- `java.util.Date` 与 `java.sql.Date` 混用时不 import、不写全名，编译歧义。

## 仓库内对照

| 示例 | 说明 |
|------|------|
| [`Hello.java`](../00-setup/src/Hello.java) | 无名包，`java -cp out Hello` |
| [`com.learn.intro`](../00-setup/src/com/learn/intro/) | 有包 + 目录 |
| [`00-setup/README.md`](../00-setup/README.md) | 包与目录、classpath 说明 |

## 练习

[`00-setup/src/com/learn/packages/demo/PackagePractice.java`](../00-setup/src/com/learn/packages/demo/PackagePractice.java)
