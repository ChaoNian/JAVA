package com.learn.concurrency;

import java.util.List;
import java.util.Map;

/**
 * 固定样例任务，不读文件。{@link CompareApp} 里固定线程池与虚拟线程用同一份输入。
 *
 * A001 出现 3 次（2+3+1=6），B002 两次（1+2=3），C003 一次（4）。
 * 并行汇总后 qtyBySku 应与 {@link #expectedQtyBySku()} 一致。
 */
public final class SampleOrderJobs {
    private SampleOrderJobs() {
        // 工具类：只暴露静态样例
    }

    /** 不可变列表；多线程只读，无需同步。 */
    public static List<OrderJob> all() {
        return List.of(
                new OrderJob(1, "A001", 2, 30),
                new OrderJob(2, "B002", 1, 20),
                new OrderJob(3, "A001", 3, 25),
                new OrderJob(4, "C003", 4, 15),
                new OrderJob(5, "B002", 2, 20),
                new OrderJob(6, "A001", 1, 10)
        );
    }

    /** 按 sku 汇总后的期望销量，用来断言并行处理结果是否正确。 */
    public static Map<String, Integer> expectedQtyBySku() {
        return Map.of( // Map.of（Java 9+）：固定键值对的不可变 Map，参数须成对
                "A001", 6,
                "B002", 3,
                "C003", 4
        );
    }

    public static int expectedTotalQty() {
        return 13;
    }
}
