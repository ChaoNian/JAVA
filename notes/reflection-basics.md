# Java 反射（Reflection）

## 这个概念解决什么问题？

编译期写死的 `new Person()` 无法在运行时按配置换类名、无法在框架里统一「扫描注解再创建对象」。  
**反射**让程序在运行时通过 **`Class<?>`** 查询并操作类的构造器、字段、方法。

## 本质

- 每个类型在 JVM 里对应一个 **`Class` 对象**（类的描述）。
- `java.lang.reflect` 里的 **`Constructor` / `Field` / `Method`** 是对应成员的句柄。
- **`invoke` / `get` / `set` / `newInstance`** 在运行时执行，编译器不检查名字是否写对。

## 获取 Class 的常见方式

| 写法 | 场景 |
|------|------|
| `Person.class` | 编译期已知类型 |
| `obj.getClass()` | 已有实例 |
| `Class.forName("com.example.Person")` | 类名在字符串里（插件、JDBC 驱动） |

## API 速查

| 需求 | API | 说明 |
|------|-----|------|
| public 构造器 | `getConstructor(参数类型...)` | 非 public 用 `getDeclaredConstructor` + `setAccessible` |
| 字段 | `getField` / `getDeclaredField` | Declared 含 private；改 private 需 `setAccessible(true)` |
| 方法 | `getMethod` / `getDeclaredMethod` | `method.invoke(对象, 参数...)`；静态方法对象传 `null` |

## 典型用途

- 框架：Spring 注入、JUnit 跑 `@Test`、Jackson 填字段
- 动态加载：类名来自配置文件
- **业务代码里应少用**：破坏封装、性能差、错误推迟到运行期

## 常见误用

- 用反射改 private 字段代替正规 setter
- 热路径里频繁 `getMethod` 不缓存
- 混淆 `getMethod`（public + 继承）与 `getDeclaredMethod`（本类全部声明）

## 练习

[`01-language-basics/src/com/learn/basics/ReflectionPractice.java`](../01-language-basics/src/com/learn/basics/ReflectionPractice.java)
