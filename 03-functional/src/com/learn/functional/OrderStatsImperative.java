package com.learn.functional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 命令式对照：for + HashMap + sort。对照 02 的 OrderStats 写法。
 * 和 Stream 版比：步骤一眼能看懂，但「做什么」藏在怎么改集合里。
 */
public final class OrderStatsImperative {
    private OrderStatsImperative() {
    }

    public static OrderStats from(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders required");
        }

        // 先拷贝再排序：SampleOrders 给的是不可变 List，不能直接 Collections.sort
        // 也避免把调用方手里的顺序改掉
        List<Order> sorted = new ArrayList<Order>(orders);
        Collections.sort(sorted, new Comparator<Order>() {
            @Override
            public int compare(Order a, Order b) {
                // 销量从高到低；相同再按 sku 升序，好和 Stream 版稳定对照
                int byQty = Integer.compare(b.qty(), a.qty());
                if (byQty != 0) {
                    return byQty;
                }
                return a.sku().compareTo(b.sku());
                // Stream 版会写成 Comparator.comparingInt(Order::qty).reversed().thenComparing(...)
            }
        });

        Set<String> citySet = new HashSet<String>();
        Map<String, Integer> totals = new HashMap<String, Integer>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            citySet.add(order.city()); // Set：同一城市加两次，size 不变
            Integer soFar = totals.get(order.sku());
            if (soFar == null) {
                soFar = 0; // Map.get 找不到是 null，不是 0
            }
            totals.put(order.sku(), soFar + order.qty());
        }

        return new OrderStats(sorted, citySet, totals);
    }
}
