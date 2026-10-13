# String 分类与使用场景

## 这个概念解决什么问题？

`String` 是不可变的字符序列。先分清「你要干什么」，再选对方法，避免用 `==` 比内容、或在循环里疯狂 `+` 拼接。

## 分类一览

| 分类 | 常见 API | 典型场景 |
|------|----------|----------|
| 创建 | `"hi"`、`new String(...)`、`valueOf` | 字面量最常用；`new` 一般练习用 |
| 查信息 | `length`、`isEmpty`、`charAt`（11+：`isBlank`） | 校验输入是否为空 |
| 比较 | `equals`、`equalsIgnoreCase`、`compareTo` | 登录名、状态码、排序 |
| 查找 | `contains`、`indexOf`、`startsWith`、`endsWith` | 搜关键词、判断后缀 `.jpg` |
| 截取变换 | `substring`、`trim`（11+：`strip`）、`toUpperCase`、`replace` | 清洗输入、脱敏、格式化 |
| 拆分合并 | `split`、`String.join`、`concat` | CSV 一行拆字段、拼路径 |
| 不可变 | 任何「修改」都返回**新**字符串 | 理解为何 `s.toUpperCase()` 不改原来的 `s` |
| 大量拼接 | `StringBuilder` / `StringBuffer` | 循环里拼日志、拼 HTML；详见 [`stringbuffer-builder.md`](stringbuffer-builder.md) |

## 场景速查

1. **判断两个字符串内容是否一样** → `equals`（不要 `==`）
2. **忽略大小写比** → `equalsIgnoreCase`
3. **用户输入去空格** → `trim()`（JDK 11+ 也可用 `strip()`）
4. **是否以某前缀/后缀开头结尾** → `startsWith` / `endsWith`
5. **取出中间一段** → `substring(from, to)`（to 不含）
6. **替换敏感词** → `replace` / `replaceAll`（正则慎用）
7. **按逗号拆** → `split(",")`
8. **循环拼很多段** → `StringBuilder`，不要 `s = s + x`

## 常见误用

- 用 `==` 比较内容（常量池有时碰巧为 true，会骗人）
- 写了 `s.toUpperCase()` 却以为原来的 `s` 变了
- 在 `for` 里用 `+` 拼几千次字符串（慢、产生大量临时对象）
- `split` 的参数是正则：`"."` 会拆成一堆空串，应写 `"\\."`

## 反例 / 正例

见练习：[`00-setup/src/StringPractice.java`](../00-setup/src/StringPractice.java)

更深入的 `==` vs `equals`：[`01-language-basics/.../Ex01EqualsVsEq.java`](../01-language-basics/src/com/learn/basics/Ex01EqualsVsEq.java)
