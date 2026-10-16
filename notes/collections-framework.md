# Java 集合框架（Collections）

## 这个概念解决什么问题？

程序里要存很多对象时，不能只靠数组：**长度固定、中间插删贵、没有「按键查」「去重」的现成语义。**  
集合框架提供一组标准容器，本质是在选**数据在内存里怎么摆、怎么连**——从而在**查找速度、空间、是否有序**之间做权衡。

## 本质：几种内存组织方式

| 组织方式 | 代表类型 | 查找/访问 | 中间增删 | 有序 |
|----------|----------|-----------|----------|------|
| 连续内存 + 下标 | 数组、`ArrayList` | 按下标 O(1) | O(n) 要搬后面 | List 保插入序 |
| 节点 + 指针 | `LinkedList` | 找第 n 个 O(n) | 已知节点旁 O(1) | List 保插入序 |
| 哈希 + 数组槽 | `HashMap`、`HashSet` | 理想接近 O(1) | 同左 | **不保证**遍历顺序 |
| 红黑树 | `TreeMap`、`TreeSet` | O(log n) | O(log n) | **按键/元素排序** |
| 只在一端操作 | 栈、队列、`ArrayDeque` | 栈顶/队头 | 受限位置 | FIFO / LIFO |
| 堆（数组存完全二叉树） | `PriorityQueue` | 取极值 O(log n) | 入堆 O(log n) | 按优先级，非 FIFO |

**口诀**：连续块查下标快；哈希换空间换查找；树换一点速度换有序；栈/队列是「只允许在特定端操作」的线性表。

## 现代常用结构（接口 vs 实现）

声明尽量用**接口**，`new` 时用具体类，以后好换实现：

```java
List<String> items = new ArrayList<String>();
Set<String> unique = new HashSet<String>();
Map<String, Integer> counts = new HashMap<String, Integer>();
```

| 接口 | 常用实现 | 一句话 |
|------|----------|--------|
| `List` | `ArrayList` | 动态数组，**默认 List**；随机访问快，中间插删慢 |
| `List` | `LinkedList` | 双向链表；两端增删还行，**少按 get(i) 随机访问** |
| `Set` | `HashSet` | 去重、查找快，无序 |
| `Set` | `TreeSet` | 去重且**自动排序**；元素需可比较；一般不允许 `null` |
| `Map` | `HashMap` | 按键查/改，无序；允许 **一个** `null` 键 |
| `Map` | `TreeMap` | 按键**有序**；**不允许** `null` 键 |
| `Deque` | `ArrayDeque` | **栈、队列**首选（别从 `java.util.Stack` 入门） |
| `Queue` | `PriorityQueue` | **最小堆**（或自定义比较器当最大堆）；Top-K、调度 |

`HashSet` 内部相当于「值当 key」的 `HashMap`，所以 **Set 元素、Map 的 key** 都要稳定、一致的 `equals` + `hashCode`。

## List / Set / Map 各管什么事

| 需求 | 选谁 | 例子 |
|------|------|------|
| 要顺序、可重复 | `List` | 购物车行、日志按时间一条条 |
| 只要「有没有」、不要重复 | `Set` | 已登录用户 id、标签去重 |
| 按 key 查 value、计数、分组 | `Map` | 用户 id → 对象、词 → 出现次数 |

详见练习 [`Ex03ListSetMap`](../02-core-apis/src/com/learn/apis/Ex03ListSetMap.java)。

## 栈、队列、堆

| 结构 | 规则 | Java 写法 | 场景 |
|------|------|-----------|------|
| 栈 Stack | LIFO 后进先出 | `ArrayDeque`：`push` / `pop` | 括号匹配、DFS、撤销 |
| 队列 Queue | FIFO 先进先出 | `ArrayDeque` 或 `LinkedList` 作 `Queue` | BFS、任务排队 |
| 堆 Heap | 每次取当前最小/最大 | `PriorityQueue` | Top-K、多路归并、Dijkstra |

`PriorityQueue` **不是**先来先服务；出队的是优先级最高（或最低）的元素。

## 树与图（语言库不包办）

- **树**：层级关系（目录、组织架构）。手写节点示例：

```java
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}
```

- **图**：多对多关系（社交、导航）。Java 无标准 `Graph` 类，常用**邻接表** `Map<节点, List<邻居>>` 或邻接矩阵。

## 遗留类（结构一样，别当默认选型）

| 遗留 | 本质 | 现代替代 |
|------|------|----------|
| `Vector` | 线程安全的动态数组 | `ArrayList`；要多线程用并发包 |
| `Hashtable` | 线程安全的哈希表 | `HashMap` / `ConcurrentHashMap` |
| `Stack` | 栈（继承 Vector） | `ArrayDeque` |
| `Dictionary` | 老式键值 | `Map` |

淘汰原因多是**粗粒度同步、API 旧**，不是数据结构错了。

## 四问选型（写代码前过一遍）

1. **要不要按 key 快速查？** → `HashMap`（要排序 → `TreeMap`）
2. **要不要去重？** → `Set`（要排序 → `TreeSet`）
3. **要不要保序、可重复、按下标？** → `List`（默认 `ArrayList`）
4. **增删多还是查多？** 查第 i 个多 → 数组系；两端进出 → `Deque`；每次取极值 → `PriorityQueue`

**总结**：复用状态和骨架用类继承；容器选型是在**时间、空间、有序性**之间交易——日常 **`ArrayList` + `HashMap` + 偶尔 `HashSet`** 覆盖大部分业务。

## 常见误用

- 字段类型写死 `ArrayList`，方法参数却到处要换实现 → 左边写 `List`
- 在 `HashMap` 里用**会变的对象**当 key（改字段后 `get` 找不到）→ 见 [`Ex04HashMapKey`](../02-core-apis/src/com/learn/apis/Ex04HashMapKey.java)
- 以为 `HashSet` 遍历顺序等于加入顺序
- 用 `Stack` 类写新代码；用 `PriorityQueue` 当普通 FIFO 队列
- 在 `TreeMap` 里放 `null` 键（会 NPE）
- 循环里 `list.get(i)` 配 `LinkedList` 做大列表（慢）

## 练习

- 入门三分法：[`Ex03ListSetMap`](../02-core-apis/src/com/learn/apis/Ex03ListSetMap.java)
- HashMap 的 key：[`Ex04HashMapKey`](../02-core-apis/src/com/learn/apis/Ex04HashMapKey.java)
- 结构对照与 Top-K、简单图：[`Ex09CollectionsCases`](../02-core-apis/src/com/learn/apis/Ex09CollectionsCases.java)
