package com.learn.functional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 统计结果容器。Imperative / Stream 两边都产出这个形状，方便 CompareApp 对照。
 *
 * 三种集合一起用（和 02 一样）：
 * - List：按销量排好的订单
 * - Set：去重后的城市
 * - Map：sku -> 总销量
 */
public final class OrderStats {
    private final List<Order> byQtyDesc;
    private final Set<String> cities;
    private final Map<String, Integer> qtyBySku;

    public OrderStats(List<Order> byQtyDesc, Set<String> cities, Map<String, Integer> qtyBySku) {
        if (byQtyDesc == null || cities == null || qtyBySku == null) {
            throw new IllegalArgumentException("stats parts required");
        }
        this.byQtyDesc = byQtyDesc;
        this.cities = cities;
        this.qtyBySku = qtyBySku;
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
}
