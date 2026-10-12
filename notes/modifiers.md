# Java 修饰符汇总

## 这个概念解决什么问题？

修饰符是贴在类 / 方法 / 变量上的**标签**：声明「谁能碰」以及「以什么方式存在」，不是业务计算本身。

## 两大类

| 类型 | 管什么 | 常见关键字 |
|------|--------|------------|
| 访问修饰符 | 谁能看见、谁能用（门禁） | `public` / `protected` / 默认 / `private` |
| 非访问修饰符 | 归属、可变、抽象、并发等契约 | `static` / `final` / `abstract` / `synchronized` / `volatile` |

写法位置：通常在类型声明的最前面，可叠用，例如 `private static final int MAX = 100`。

---

## 一、访问修饰符（门禁）

| 修饰符 | 一句话 | 本类 | 同包 | 子类(异包) | 其他包 |
|--------|--------|------|------|------------|--------|
| `public` | 全世界 | ✓ | ✓ | ✓ | ✓ |
| `protected` | 本包 + 子类 | ✓ | ✓ | ✓ | ✗ |
| 默认（不写） | 仅本包 | ✓ | ✓ | ✗ | ✗ |
| `private` | 仅本类 | ✓ | ✗ | ✗ | ✗ |

要点：

- 外部类不能用 `private` / `protected`，只能 `public` 或默认。
- 字段一般 `private`，对外用 getter/setter（封装）。
- `main` 必须 `public static`，JVM 才能启动。

### 继承时重写方法的权限

只能**一样或更开**，不能更严：

- 父 `public` → 子只能 `public`
- 父 `protected` → 子 `protected` 或 `public`
- 父 `private` → 子**继承不到**，谈不上重写

---

## 二、非访问修饰符（契约）

| 修饰符 | 用在 | 含义 |
|--------|------|------|
| `static` | 变量、方法、嵌套类 | 跟**类**走；全类一份；用 `类名.xxx` |
| `final` | 类、方法、变量 | 类不可继承；方法不可重写；变量不可再赋值 |
| `abstract` | 类、方法 | 抽象模板；抽象方法无方法体，子类必须实现 |
| `synchronized` | 方法、代码块 | 同一时刻一个线程进临界区（并发） |
| `volatile` | 变量 | 多线程可见性相关（并发，细节后学） |
| `native` | 方法 | 方法体在非 Java 代码中（JNI） |
| `transient` | 字段 | 序列化时忽略该字段 |

### `static` 速记

- 静态变量 = 类变量，所有实例共享一份。
- 静态方法不依赖某个对象；**不能直接使用实例变量**。
- **局部变量不能写 `static`**。
- 练习：[`InstanceCounter.java`](../00-setup/src/InstanceCounter.java)（每 `new` 一次，共享计数 +1）。

### `final` 速记

| 修饰 | 效果 |
|------|------|
| `final class` | 不能有子类 |
| `final` 方法 | 子类不能重写 |
| `final` 变量 | 赋值后不能改（常量常写 `static final`，名如 `MAX_SIZE`） |

### `abstract` 速记

- 抽象类不能 `new`，可有普通方法 + 抽象方法。
- 子类必须实现所有抽象方法（除非自己也是抽象类）。

---

## 三、常见组合读法

```text
private static final int MAX_SIZE = 100;
│        │      │
│        │      └─ 不能再改
│        └─ 全类一份
└─ 仅本类直接访问
```

```text
public static void main(String[] args)
│      │
│      └─ 不需要先 new 就能调（JVM 入口）
└─ 外部可见
```

---

## 常见误用

1. 把实例状态做成 `static`，多对象互相踩数据。
2. 字段全 `public`，失去封装。
3. 重写时把 `protected` 收成 `private`（编译失败）。
4. 在静态方法里直接写实例字段名（编译失败）。

## 和本阶段练习的对应

| 练习 | 用到的修饰符 |
|------|----------------|
| `RunoobTest1` | `private` / `public` 字段 |
| `NamingRulesDemo` | `static final` 常量 + `private static` 计数 |
| `InstanceCounter` | `private static` 计数 + `protected static` 查询 |
