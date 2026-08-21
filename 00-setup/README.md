# 00 · 环境与心智模型

**建议：1 周** · 过关后再进 `01-language-basics`。

## 目标

能编译运行第一个程序，并说清 **JDK、JRE、JVM** 各自干什么。

| 名词 | 干什么 |
|------|--------|
| JDK | 开发工具包：编译器 `javac`、运行器 `java`、以及类库 |
| JRE | 运行环境：跑已经编译好的程序（现代 JDK 已包含它） |
| JVM | 虚拟机：加载 `.class` 字节码并执行 |

源文件 `.java` → `javac` → 字节码 `.class` → `java` 在 JVM 里运行。  
**包名必须和目录一一对应**：`package com.learn.intro` 的源文件要放在 `src/com/learn/intro/`。

## 本阶段要学

- 安装 JDK 21 或 25 LTS，配置 `JAVA_HOME` 与 `PATH`
- `javac` / `java`；用 Cursor 或 IntelliJ 打开本仓库
- 源文件、`.class`、包名与目录的对应关系
- classpath 的直觉（`-cp` / `-d`）
- Maven 或 Gradle 先知道名字即可，本阶段不用

## 练习

本目录已有两份代码：

1. **无包名** [`src/Hello.java`](src/Hello.java) —— 最小可运行程序
2. **有包名、两个类** [`src/com/learn/intro/`](src/com/learn/intro/) —— `App` 调用 `Greeter`

在 **PowerShell** 里进入本目录后执行：

```powershell
# 确认 JDK
java -version
javac -version

# 练习 1
javac -d out src\Hello.java
java -cp out Hello

# 练习 2（可带命令行参数）
javac -d out src\com\learn\intro\Greeter.java src\com\learn\intro\App.java
java -cp out com.learn.intro.App
java -cp out com.learn.intro.App 你的名字
```

也可以直接跑脚本：

```powershell
.\compile-and-run.ps1
```

自己再做一件事：把 `Greeter` 改一句问候语，重新编译，确认输出变了。  
**改源文件却不重新 `javac`，运行的仍是旧 `.class`。**

## 过关标准

不靠 IDE 绿按钮，也能在终端用 `javac` + `java` 跑起来。

能口头回答：

- `.java` 和 `.class` 谁是人写的、谁是编译器生成的？
你打开编辑器改的是 Hello.java 这种源文件：里面是给人类看的 Java 代码。javac 读这份源文件，翻译成 JVM 能执行的字节码，写出 Hello.class。java 命令加载的是 .class，不是 .java。

- 为什么 `com.learn.intro.App` 不能写成 `java App`？
因为 JVM 要找的是完整类名，不是文件名。

App.java 第一行写了 package com.learn.intro;，所以这个类的真名是 com.learn.intro.App，不是 App。java 后面跟的必须是这个真名。

-cp out 表示：把 out 当作包的根目录，然后按包名一层层往下找：
ava -cp out App 会在 out\App.class 找一个没有包名的 App。你的 App.class 不在那里，而在 out\com\learn\intro\ 里，所以找不到。

对比你已经跑通的 Hello：它没有 package 声明，完整类名就是 Hello，文件就在 out\Hello.class，所以可以写 java -cp out Hello。

可以记成：

无包名：java -cp out 类名
有包名：java -cp 包的根目录 包名.类名
包名、目录、java 命令里的名字必须三者一致。这就是为什么不能把 com.learn.intro.App 写成 java App。

- `-d out` 和 `-cp out` 各做什么？
-cp 就是 classpath：JVM 只在你指定的目录里找 .class，不会自动去子文件夹里搜。

## 这一阶段先别碰

Spring、Docker、微服务、Maven 依赖管理。
