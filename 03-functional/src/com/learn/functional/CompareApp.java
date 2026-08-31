package com.learn.functional; // package：声明命名空间，须与 src 下目录路径一致

import java.util.List;   // import：导入类型，代码里可直接写 List 而非全限定名
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 入口：只接线。同一份样例，命令式 vs Stream，打印对照。
 *
 * 业务不写在 main 里：
 * - 样例数据：{@link SampleOrders#all()}
 * - 统计逻辑：{@link OrderStatsImperative#from} 与 {@link OrderStatsStream#from}
 * - 本类只负责调用、打印、断言两边结果一致
 *
 * 对照的三块（见 {@link OrderStats}）：
 * 1. {@code cities}：Set，城市去重
 * 2. {@code qtyBySku}：Map，按 sku 累加销量
 * 3. {@code byQtyDesc}：List，按 qty 降序、同 qty 时 sku 升序
 *
 * 编译：在 03-functional 目录执行 .\compile-and-run.ps1
 * 运行：java -cp out com.learn.functional.CompareApp
 */
public class CompareApp { // public class：对外可见；类名须与文件名 CompareApp.java 一致
    // static void main(String[] args)：JVM 入口；static 表示不依赖实例即可调用
    public static void main(String[] args) {
        // 只读样例；两种实现都不得修改这份 List（Stream 版 sorted 也不改原列表）
        List<Order> orders = SampleOrders.all();
        System.out.println("sample size = " + orders.size()); // 应是 5
        System.out.println();

        // 同一输入、两种实现 → 应得到语义相同的 OrderStats
        OrderStats imperative = OrderStatsImperative.from(orders);
        OrderStats stream = OrderStatsStream.from(orders);

        printBlock("imperative", imperative);
        printBlock("stream", stream);

        // 三块都一致才打印 MATCH；故意改一边 Comparator 或 groupingBy 键时应变 MISMATCH
        boolean match = sameStats(imperative, stream);
        System.out.println(match ? "MATCH" : "MISMATCH"); // 三元运算符：条件 ? 真值 : 假值
    }

    /**
     * 打印一块统计，方便肉眼比两种写法输出是否一样。
     * 期望心里应有：cities 共 3 个；SKU-A=19, SKU-B=5, SKU-C=3；排第一的是 SKU-A x10。
     */
    // private static：仅本类可见；static 方法通过类名调用，无需 new CompareApp()
    private static void printBlock(String title, OrderStats stats) {
        System.out.println("-- " + title + " --");
        System.out.println("cities     = " + stats.cities());       // Set：不比顺序
        System.out.println("qty by sku = " + stats.qtyBySku());     // Map：sku -> 总销量
        System.out.println("sorted qty = " + stats.byQtyDesc());    // List：顺序有意义
        System.out.println();
    }

    /**
     * 过关条件：城市集合、sku 汇总、排序列表三者都一致。
     * Set/Map 用 {@link Objects#equals}；List 要逐位比字段（见 {@link #sameOrderSequence}）。
     */
    private static boolean sameStats(OrderStats a, OrderStats b) {
        return sameCities(a.cities(), b.cities()) // && 短路：前面 false 则后面不再求值
                && sameQtyBySku(a.qtyBySku(), b.qtyBySku())
                && sameOrderSequence(a.byQtyDesc(), b.byQtyDesc());
    }

    /** Set.equals：元素相同即可，不比遍历/插入顺序。 */
    // 泛型 Set<String>：编译期限定集合元素类型为 String，取元素时无需强转
    private static boolean sameCities(Set<String> a, Set<String> b) {
        return Objects.equals(a, b);
    }

    /** Map.equals：键值对相同即可，不比 key 的插入顺序。 */
    private static boolean sameQtyBySku(Map<String, Integer> a, Map<String, Integer> b) {
        return Objects.equals(a, b);
    }

    /**
     * 比排序后的订单序列。不依赖 {@link Order#equals}（本阶段未实现）。
     *
     * List 必须逐位一致：若一边 Comparator 主键/次键写反，这里会第一个不匹配就 false。
     * 四个业务字段全比：date / sku / city / qty。
     */
    private static boolean sameOrderSequence(List<Order> a, List<Order> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) { // 经典 for：用下标遍历 List
            Order x = a.get(i); // get(i) 按下标取元素，越界抛 IndexOutOfBoundsException
            Order y = b.get(i);
            if (!x.date().equals(y.date())   // || 短路：任一字段不等即 true
                    || !x.sku().equals(y.sku())
                    || !x.city().equals(y.city())
                    || x.qty() != y.qty()) { // int 用 != 比内容；引用类型用 equals
                return false;
            }
        }
        return true;
    }
}
