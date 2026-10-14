# Java 数组

## 这个概念解决什么问题？

数组用来存放**固定长度、同一类型**的一串元素，用下标（从 0 开始）快速读写。适合「个数事先知道、类型统一」的数据。

## 分类

| 分类 | 说明 | 例子 |
|------|------|------|
| 一维数组 | 一条线 | `int[] scores` |
| 二维数组 | 表（行×列） | `int[][] matrix` |
| 基本类型数组 | 存 `int`/`double`… 本身 | `int[] a = {1,2,3}` |
| 引用类型数组 | 存对象引用 | `String[] names`；元素默认 `null` |
| 空数组 | 长度 0，不是 `null` | `new int[0]` |

和集合（`List`）比：数组长度**创建后不能变**；需要动态增减时用 `ArrayList`（阶段 02）。

## 声明与创建

```java
// 1) 先声明，再 new（元素得默认值：int→0，对象→null）
int[] a;
a = new int[3];

// 2) 声明时指定长度
int[] b = new int[3];

// 3) 声明并写死初值（最常用）
int[] c = {10, 20, 30};

// 4) 匿名数组（常作参数）
print(new int[] {1, 2, 3});
```

注意：`int a[]` 也能写，更推荐 `int[] a`（类型和变量名分得清）。

## 处理数组（常用操作）

| 操作 | 做法 |
|------|------|
| 读/写 | `a[i]`，`i` 从 `0` 到 `a.length - 1` |
| 长度 | `a.length`（是字段，不是方法） |
| 遍历 | `for` / 增强 for：`for (int x : a)` |
| 拷贝 | `System.arraycopy` / `Arrays.copyOf` |
| 排序、查找、打印 | `java.util.Arrays`：`sort`、`binarySearch`、`toString`、`equals` |
| 多维 | `m[row][col]`；行数 `m.length`，第 r 行列数 `m[r].length` |

越界会抛 `ArrayIndexOutOfBoundsException`。

## 使用场景

1. **固定科目成绩、一周七天** → 一维 `int[]` / `double[]`
2. **一组名字、状态码** → `String[]`
3. **小表格 / 棋盘** → 二维数组
4. **方法一次返回多个同类型值** → 返回数组（或后面用对象/`List`）
5. **需要频繁按下标随机访问** → 数组比链表更合适

不适合：长度经常变、要按名字查找（用 `Map`）、要去重（用 `Set`）——留给集合课。

## 常见误用

- 写成 `a.length()`（数组是 `.length`，`String` 才是 `.length()`）
- 下标用到 `a.length`（合法最大是 `length - 1`）
- `int[] a = new int[3]{1,2,3}`（语法错；要么 `new int[3]`，要么 `{1,2,3}`）
- 把数组变量赋给另一个变量以为拷贝了内容（其实共享同一份，改一个两边变）

## 练习


[`00-setup/src/ArrayPractice.java`](../00-setup/src/ArrayPractice.java)
