package com.learn.functional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Stream 版：声明「要什么」，由流水线完成变换。
 *
 * 要点：
 * - 中间操作（sorted / map）惰性；碰到 collect 才真正跑
 * - 方法引用 Order::sku 等价于 o -> o.sku()
 * - 默认别用 parallelStream：小集合更慢，还有共享可变状态的坑
 */
public final class OrderStatsStream {
    private OrderStatsStream() {
    }

    public static OrderStats from(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders required");
        }

        // sorted 不改原 List，产出新顺序；toUnmodifiableList 结果不可再 add
        List<Order> byQtyDesc = orders.stream()
                .sorted(Comparator.comparingInt(Order::qty).reversed()
                        .thenComparing(Order::sku))
                .collect(Collectors.toUnmodifiableList());

        // map 抽出城市字段，再收集成 Set（自动去重）
        Set<String> cities = orders.stream()
                .map(Order::city)
                .collect(Collectors.toUnmodifiableSet());

        // 过关点：groupingBy + 下游 collector
        // 不要先 groupingBy 成 Map<String, List<Order>> 再手写 for 求和
        Map<String, Integer> qtyBySku = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::sku,                      // 按什么分组
                        Collectors.summingInt(Order::qty) // 每组怎么汇总
                ));

        return new OrderStats(byQtyDesc, cities, qtyBySku);
    }
}
