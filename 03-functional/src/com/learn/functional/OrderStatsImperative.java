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
 *
 * 伪函数式反例（本类没这么写）：
 * orders.stream().forEach(o -> totals.put(...)) —— 在 forEach 里改外部 Map，别学。
 */
public final class OrderStatsImperative {
    private OrderStatsImperative() {
        // 工具类：统计逻辑都在静态 from 里
    }

    public static OrderStats from(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders required");
        }

        // --- 1. 排序：List 保留顺序，所以要单独一份 sorted ---
        // 先拷贝再排序：SampleOrders 给的是不可变 List，不能直接 Collections.sort
        // 也避免把调用方手里的顺序改掉
        List<Order> sorted = new ArrayList<Order>(orders); // 泛型构造：拷贝一份可变 List
        Collections.sort(sorted, new Comparator<Order>() { // 匿名内部类：实现接口 Comparator<Order>
            @Override // 实现 compare：负数 a<b，0 相等，正数 a>b
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

        // --- 2. 一趟循环：Set 去重城市，Map 按 sku 累加 ---
        Set<String> citySet = new HashSet<String>(); // HashSet：基于哈希表，add 去重
        Map<String, Integer> totals = new HashMap<String, Integer>(); // HashMap：键值对，get 无键时返回 null
        for (int i = 0; i < orders.size(); i++) { // 索引 for：可随机访问 List
            Order order = orders.get(i);
            citySet.add(order.city()); // Set：同一城市加两次，size 不变
            Integer soFar = totals.get(order.sku()); // Integer 是 int 的包装类，可为 null
            if (soFar == null) { // 拆箱前须判 null，否则自动拆箱会 NPE
                soFar = 0; // Map.get 找不到是 null，不是 0（对照 02 Ex03）
            }
            totals.put(order.sku(), soFar + order.qty()); // put：有则覆盖，无则插入
        }

        return new OrderStats(sorted, citySet, totals);
    }
}
