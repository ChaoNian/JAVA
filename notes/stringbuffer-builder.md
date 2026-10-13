# StringBuffer 与 StringBuilder

## 这个概念解决什么问题？

`String` 不可变：每次 `+` / `concat` 都可能产生新对象。要在内存里**反复改同一段字符**（尤其是循环拼接），用可变的字符缓冲区：`StringBuilder` 或 `StringBuffer`。

## 三者怎么选

| | `String` | `StringBuilder` | `StringBuffer` |
|--|----------|-----------------|----------------|
| 可变？ | 否 | 是 | 是 |
| 线程安全？ | 不可变，天然安全 | 否（单线程用） | 是（方法带同步） |
| 速度 | 少量拼接 OK | 快（首选） | 相对慢 |
| 典型场景 | 常量、短文本、当 Map 的 key | 循环拼日志、拼 SQL/HTML | 多线程共享同一缓冲（少见） |

口诀：**单线程拼字符串用 `StringBuilder`；需要同步才考虑 `StringBuffer`。**

## 常用操作（两者 API 几乎一样）

| 方法 | 作用 |
|------|------|
| `append(x)` | 末尾追加 |
| `insert(i, x)` | 在下标 i 插入 |
| `delete(start, end)` | 删区间（end 不含） |
| `replace(start, end, s)` | 替换区间 |
| `reverse()` | 反转 |
| `toString()` | 变成不可变 `String` 交给别处用 |
| `length()` / `capacity()` | 当前长度 / 内部数组容量 |

## 场景

1. **for 循环拼很多段** → `StringBuilder`，最后 `toString()`
2. **改中间某一截** → `delete` / `insert` / `replace`，不必每次 new 新 String
3. **多线程同时往同一个缓冲写** → 才考虑 `StringBuffer`（更多时候用别的并发结构）

## 常见误用

- 循环里 `s = s + x` 拼几千次（慢）
- 单线程还用 `StringBuffer`（能跑，但没必要的同步开销）
- 拼完忘了 `toString()`，把 Builder 直接当业务字符串传来传去

## 练习

[`00-setup/src/StringBufferBuilderPractice.java`](../00-setup/src/StringBufferBuilderPractice.java)
