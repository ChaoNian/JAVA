package com.learn.jvm.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 阶段 2 风格的统计：List 排序、Set 去重城市、Map 按 sku 累加。
 * 返回不可变视图，方便测试「调用方改不动结果」。
 */
public final class OrderStats {
    private final List<Order> byQtyDesc;
    private final Set<String> cities;
    private final Map<String, Integer> qtyBySku;

    private OrderStats(List<Order> byQtyDesc, Set<String> cities, Map<String, Integer> qtyBySku) {
        this.byQtyDesc = byQtyDesc;
        this.cities = cities;
        this.qtyBySku = qtyBySku;
    }

    public static OrderStats from(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders required");
        }

        List<Order> sorted = new ArrayList<>(orders);
        sorted.sort(Comparator.comparingInt(Order::qty).reversed().thenComparing(Order::sku));

        Set<String> citySet = new HashSet<>();
        Map<String, Integer> totals = new HashMap<>();
        for (Order order : orders) {
            citySet.add(order.city());
            totals.merge(order.sku(), order.qty(), Integer::sum);
        }

        return new OrderStats(
                List.copyOf(sorted),
                Set.copyOf(citySet),
                Map.copyOf(totals));
    }

    public List<Order> byQtyDesc() {
        return byQtyDesc;
    }

    public Set<String> cities() {
        return cities;
    }

    public Map<String, Integer> qtyBySku() {
        return qtyBySku;
    }

    /** 空输入时的哨兵结果，测试里用来断言 size 全是 0。 */
    public static OrderStats empty() {
        return new OrderStats(List.of(), Set.of(), Map.of());
    }

    /** 只读快照：防止测试里误用可变包装。 */
    public Map<String, Integer> qtyBySkuSnapshot() {
        return Collections.unmodifiableMap(qtyBySku);
    }
}
