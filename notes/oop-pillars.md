# 封装 · 继承 · 多态

## 这个概念解决什么问题？

面向对象用三个习惯把代码拆清楚：

| 特性 | 一句话 | 你在防什么 |
|------|--------|------------|
| **封装** | 数据藏起来，只开受控的门 | 外面随便改字段把对象改坏 |
| **继承** | 子类复用父类，再扩展自己的 | 复制粘贴一整份相似代码 |
| **多态** | 同一句话，不同对象不同表现 | 到处写 `if (类型==狗)` |

## 封装（Encapsulation）

- 字段尽量 `private`
- 对外提供 `getXxx` / `setXxx` 或业务方法（如 `deposit`）
- 校验放在方法/构造里，保证对象始终合法

```java
private int balance;
public void deposit(int amount) { /* 校验后再改 balance */ }
```

场景：账户余额、订单金额不能被外面直接 `obj.balance = -1`。

## 继承（Inheritance）

- `class 子类 extends 父类`
- 子类拥有父类非 private 的成员，可追加字段/方法，可 `@Override` 重写
- 构造器里常先 `super(...)` 初始化父类部分
- 适合「是一种」（is-a）：`Dog` 是一种 `Animal`；不适合硬套继承时用组合

场景：`SavingsAccount` 是一种 `Account`，多了计息，复用存款逻辑。

## 多态（Polymorphism）

同一父类型引用，指向不同子类对象，调用**重写后的方法**时走子类实现：

```java
Animal a = new Dog();
a.speak(); // 实际跑 Dog 的 speak
```

要点：

1. **编译看左边**（父类型有没有这个方法）
2. **运行看右边**（真正对象是谁，就跑谁的重写版本）
3. 访问修饰符重写时只能一样或更开，不能更严

场景：一组 `Animal[]` 里既有狗又有猫，循环里统一 `speak()`，不用写一堆 `instanceof`。

## 三者怎么叠在一起

```text
封装：每个类把自己的字段管好
继承：子类在父类壳子上扩展
多态：用父类型引用调用，运行时自动分派到子类方法
```

## 常见误用

- 字段全 `public`（没封装）
- 为了复用一点代码就乱 `extends`（应优先组合）
- 父类没声明的方法，用父类型引用去调（编译不过）
- 以为多态改的是字段绑定——主要是**实例方法重写**；`static` 方法不能靠多态分派

## 延伸

重写支撑多态；同名不同参是重载：见 [`override-overload.md`](override-overload.md)。

## 练习

[`00-setup/src/OopPillarsPractice.java`](../00-setup/src/OopPillarsPractice.java)
