package com.learn.functional;

import java.time.LocalDate;
import java.util.List;

/**
 * 手写样例，语义对齐 02-core-apis/data/sample.csv。
 * 本阶段不读文件：CSV I/O 已经在 02 练过，这里专心比命令式 vs Stream。
 *
 * 跑完 CompareApp 后，心里应有这些数：
 * - cities = {Shanghai, Beijing, Hangzhou}，共 3 个
 * - qtyBySku：SKU-A=19, SKU-B=5, SKU-C=3
 * - byQtyDesc 第一位是 SKU-A x10（销量最高）
 */
public final class SampleOrders {
    private SampleOrders() {
        // 工具类：不允许 new，只提供静态工厂
    }

    /**
     * List.of 返回不可变 List：调用方不能 add/remove。
     * 统计类里若要排序，必须先拷贝（命令式版）或用 stream().sorted（Stream 版）。
     */
    public static List<Order> all() {
        return List.of( // List.of（Java 9+）：固定个数的不可变列表，元素不能为 null
                new Order(LocalDate.of(2024, 1, 3), "SKU-A", "Shanghai", 10), // LocalDate.of 工厂方法
                new Order(LocalDate.of(2024, 1, 3), "SKU-B", "Beijing", 5),
                new Order(LocalDate.of(2024, 1, 4), "SKU-A", "Shanghai", 7), // SKU-A 第二笔
                new Order(LocalDate.of(2024, 1, 5), "SKU-C", "Hangzhou", 3),
                new Order(LocalDate.of(2024, 1, 5), "SKU-A", "Beijing", 2)  // SKU-A 第三笔，合计 19
        );
    }
}
