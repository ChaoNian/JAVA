package com.learn.apis.csv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 三种集合一起用：List 保留并排序，Set 去重城市，Map 按 sku 累加销量。
 * 这里用 for 循环，03 再改写成 Stream。
 */
public final class OrderStats {
    private final List<Order> byQtyDesc;
    private final Set<String> cities;
    private final Map<String, Integer> qtyBySku;

    public OrderStats(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders required");
        }

        // 先拷贝再排序，避免把调用方手里的 List 顺序改掉
        List<Order> sorted = new ArrayList<Order>(orders);
        Collections.sort(sorted, new Comparator<Order>() {
            @Override
            public int compare(Order a, Order b) {
                return Integer.compare(b.qty(), a.qty()); // 销量从高到低；03 会写成 lambda
            }
        });
        this.byQtyDesc = sorted;

        Set<String> citySet = new HashSet<String>();
        Map<String, Integer> totals = new HashMap<String, Integer>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            citySet.add(order.city()); // Set：同一城市加两次，size 不变
            Integer soFar = totals.get(order.sku());
            if (soFar == null) {
                soFar = 0; // Map.get 找不到是 null，不是 0（对照 Ex03）
            }
            totals.put(order.sku(), soFar + order.qty());
        }
        this.cities = citySet;
        this.qtyBySku = totals;
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
