# 重写（Override）与重载（Overload）

## 这个概念解决什么问题？

两个词都和「同名方法」有关，但发生的层级不同：

| | **重写 Override** | **重载 Overload** |
|--|-------------------|-------------------|
| 发生在哪 | **父子类之间** | **同一个类里**（或子类里再定义同名） |
| 方法名 | 相同 | 相同 |
| 参数列表 | **必须相同** | **必须不同**（个数/类型/顺序） |
| 返回类型 | 基本相同（或子类协变返回） | 可以不同，但**不能只靠返回类型区分** |
| 目的 | 子类替换父类实现（多态） | 同一操作的多种参数写法 |
| 注解 | 建议 `@Override` | 无 |

口诀：

- **重写** = 换引擎（同一接口，不同实现）
- **重载** = 多扇门（同一意图，不同入参）

## 重写规则（入门）

1. 方法名、参数列表与父类一致  
2. 访问权限不能更严（`protected` → `public` 可以）  
3. 静态方法谈不上多态式重写（隐藏，不按对象分派）  
4. `@Override` 写上：签名写错时编译器直接报错  

```java
class Animal {
    String speak() { return "..."; }
}
class Dog extends Animal {
    @Override
    String speak() { return "wang!"; } // 重写
}
```

## 重载规则（入门）

1. 方法名相同，参数列表不同  
2. 只改返回类型 → **不是**合法重载，编译失败  
3. 构造器也可以重载（你已在 `ConstructorPractice` 里见过）

```java
int add(int a, int b) { ... }
double add(double a, double b) { ... } // 重载
```

## 场景

| 场景 | 用哪个 |
|------|--------|
| 狗、猫各自实现 `speak()` | 重写 → 多态 |
| `print(int)` / `print(String)` | 重载 → 调用方便 |
| `Student()` / `Student(name)` / `Student(name, age)` | 构造器重载 |

## 常见误用

- 把参数改了却以为在「重写」→ 其实是重载（或新方法），多态不会走到  
- 父类方法是 `private` → 子类谈不上重写  
- 用 `@Override` 标了一个参数不同的方法 → 编译报错（这是好事）

## 练习

[`00-setup/src/OverrideOverloadPractice.java`](../00-setup/src/OverrideOverloadPractice.java)
