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
 * - stream() 只是搭流水线；中间操作（sorted / map）惰性，碰到 collect 才真正跑
 * - 方法引用 Order::sku 等价于 o -> o.sku()
 * - 默认别用 parallelStream：小集合更慢，还有共享可变状态的坑
 *
 * 和命令式版一样算了三块，但拆成三条独立流水线（可读性 vs 少扫一遍的取舍）。
 */
public final class OrderStatsStream {
    private OrderStatsStream() { // private 构造器：外部无法 new，强制走静态工厂 from()
        // 工具类：统计逻辑都在静态 from 里
    }

    public static OrderStats from(List<Order> orders) { // static：通过类名 OrderStatsStream.from(...) 调用
        if (orders == null) {
            throw new IllegalArgumentException("orders required");
        }

        // --- 1. 排序：sorted 是中间操作，collect 是终端操作 ---
        // sorted 不改原 List，产出新顺序；toUnmodifiableList 结果不可再 add
        List<Order> byQtyDesc = orders.stream() // stream()：把 List 变成 Stream 流水线
                .sorted(Comparator.comparingInt(Order::qty).reversed() // 方法引用 Order::qty ≡ o -> o.qty()
                        .thenComparing(Order::sku))                   // thenComparing：主键相等时的次排序
                .collect(Collectors.toUnmodifiableList()); // 终端 collect：物化结果；toUnmodifiableList 不可再 add

        // --- 2. 城市去重：map 变换 + 收集成 Set ---
        Set<String> cities = orders.stream()
                .map(Order::city) // 中间操作 map：每个元素映射成新值，仍惰性
                .collect(Collectors.toUnmodifiableSet()); // 收集为不可变 Set

        // --- 3. 按 sku 求和：过关点 groupingBy + 下游 collector ---
        // 不要先 groupingBy 成 Map<String, List<Order>> 再手写 for 求和
        Map<String, Integer> qtyBySku = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::sku,                       // 分组键提取器
                        Collectors.summingInt(Order::qty) // 下游收集器：对每组 qty 求和
                ));

        return new OrderStats(byQtyDesc, cities, qtyBySku); // new：在堆上创建对象并返回引用
    }
}
