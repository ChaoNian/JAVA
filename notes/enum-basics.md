# Java 枚举（enum）

## 这个概念解决什么问题？

有些字段只能取**有限几种固定值**（订单状态、账户状态、尺寸）。用 `int` 或 `String` 容易写错、拦不住非法值。  
**枚举**在类型系统里把合法取值写死，每个常量全局**单例**，比较用 `==` 即可。

## 本质

- 语法像类，编译后是 `java.lang.Enum` 的子类。
- **不能** `new`；实例只有 `{ A, B, C }` 里列出的那些。
- 默认实现 `Serializable`、`Comparable`（按声明顺序比 `ordinal()`）。

## 继承自带的方法（java.lang.Enum）

| 方法 | 作用 | 实践建议 |
|------|------|----------|
| `values()` | 全部常量的数组 | 遍历、测试、下拉选项 |
| `ordinal()` | 声明顺序下标，从 0 起 | **不要**当数据库 id、协议编号 |
| `valueOf(String)` | 按**常量名**解析 | 区分大小写；非法名抛 `IllegalArgumentException` |
| `name()` | 常量名本身 | 对外协议、日志常用 |
| `compareTo` | 按 `ordinal` 比较 | 仅当「声明顺序即业务顺序」时用 |

编译器还会为每个枚举类生成静态 `values()`、`valueOf(String)`（若未手写覆盖）。

## 枚举类成员

与普通类类似，可以有字段、构造方法、实例/静态方法：

- **构造方法**只能是 **`private`**（不写也是 private），外部不能 `new`。
- 常量可带参：`RED(255, 0, 0)` 会调用对应构造器。
- 字段建议 **`final`**，常量创建后不变。
- 若有 **`abstract` 方法**，每个常量必须用 `{ ... }` 块给出实现（见练习里的 `PayChannel`）。

## 和 switch、集合

```java
switch (status) {
    case ACTIVE:
        break;
    case FROZEN:
        break;
    default:
        break;
}
```

- 键全是枚举时，用 **`EnumSet`**、**`EnumMap`** 比 `HashSet`/`HashMap` 更合适。

## 最佳实践（口诀）

> **类型锁死合法值；比较用 `==`；持久化用 `name` 或自定义 `code`，别用 `ordinal`；行为按常量分用抽象方法；大段业务别堆在枚举里。**

| 该做 | 别做 |
|------|------|
| 状态、类型、档位用 enum | 用 `ordinal()` 存库 |
| `==` 比较常量 | 配置字符串直接 `valueOf` 不处理异常 |
| 构造器 + `final` 字段存展示名、错误码 | 在枚举里写整条业务流程 |
| 状态机在普通类，枚举只表示「是什么」 | 用 int 常量冒充枚举（除非对接遗留系统） |

## 常见误用

- 中间插入新常量后，依赖 `ordinal` 的旧数据全乱。
- `valueOf("active")` 想匹配 `ACTIVE` → 抛异常。
- 以为枚举可以 `extends` 别的类（单继承已被 `Enum` 占用；可实现接口）。
- 需要 `System.in` 那种：`close()` 枚举无此问题；但 **`valueOf` 来自不可信输入** 必须校验。

## 仓库内相关代码

- 账户状态 + 方法：[`AccountStatus.java`](../01-language-basics/src/com/learn/basics/bank/AccountStatus.java)
- 订单状态（仅常量）：[`Order.java`](../00-setup/src/Order.java) 内 `Status`

## 练习

[`01-language-basics/src/com/learn/basics/EnumPractice.java`](../01-language-basics/src/com/learn/basics/EnumPractice.java)
