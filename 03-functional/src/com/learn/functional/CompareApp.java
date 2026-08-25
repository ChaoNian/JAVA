package com.learn.functional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 入口：只接线。同一份样例，命令式 vs Stream，打印对照。
 *
 * 业务不写在 main 里：样例是 SampleOrders，统计是两个 *from，这里只调用和比较。
 *
 * 编译：在 03-functional 目录执行 .\compile-and-run.ps1
 * 运行：java -cp out com.learn.functional.CompareApp
 */
public class CompareApp {
    public static void main(String[] args) {
        List<Order> orders = SampleOrders.all();
        System.out.println("sample size = " + orders.size());
        System.out.println();

        // 同一输入、两种实现 → 应得到语义相同的 OrderStats
        OrderStats imperative = OrderStatsImperative.from(orders);
        OrderStats stream = OrderStatsStream.from(orders);

        printBlock("imperative", imperative);
        printBlock("stream", stream);

        boolean match = sameStats(imperative, stream);
        // 故意改一边排序键时，这里应变成 MISMATCH（自学验证用）
        System.out.println(match ? "MATCH" : "MISMATCH");
    }

    static void printBlock(String title, OrderStats stats) {
        System.out.println("-- " + title + " --");
        System.out.println("cities     = " + stats.cities());       // Set：城市去重
        System.out.println("qty by sku = " + stats.qtyBySku());     // Map：sku -> 总销量
        System.out.println("sorted qty = " + stats.byQtyDesc());    // List：按销量排序后的订单
        System.out.println();
    }

    static boolean sameStats(OrderStats a, OrderStats b) {
        return sameCities(a.cities(), b.cities())
                && sameQtyBySku(a.qtyBySku(), b.qtyBySku())
                && sameOrderSequence(a.byQtyDesc(), b.byQtyDesc());
    }

    // Set.equals：只要元素相同即可，不比插入顺序
    static boolean sameCities(Set<String> a, Set<String> b) {
        return Objects.equals(a, b);
    }

    // Map.equals：键值对相同即可
    static boolean sameQtyBySku(Map<String, Integer> a, Map<String, Integer> b) {
        return Objects.equals(a, b);
    }

    /**
     * 比业务字段序列，不依赖 Order.equals（本阶段未实现）。
     * 排序结果必须逐位一致，否则一边的 Comparator 写错了。
     */
    static boolean sameOrderSequence(List<Order> a, List<Order> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            Order x = a.get(i);
            Order y = b.get(i);
            if (!x.date().equals(y.date())
                    || !x.sku().equals(y.sku())
                    || !x.city().equals(y.city())
                    || x.qty() != y.qty()) {
                return false;
            }
        }
        return true;
    }
}
