package com.learn.concurrency;

import java.util.Map;
import java.util.Objects;

/**
 * 并行处理后的汇总视图：sku → 总销量、处理条数、以及用了哪种 executor。
 *
 * 构造时用 {@link Map#copyOf} 做防御性拷贝，避免调用方拿到内部 Map 再被别的线程改。
 */
public final class ProcessingSummary {
    private final Map<String, Integer> qtyBySku; // 不可变快照
    private final int processedCount;            // 应等于输入 jobs.size()
    private final String executorLabel;          // 例如 fixed-pool(4) / virtual-thread-per-task

    public ProcessingSummary(Map<String, Integer> qtyBySku, int processedCount, String executorLabel) {
        this.qtyBySku = Map.copyOf(qtyBySku); // Map.copyOf：浅拷贝为不可变 Map，防外部篡改
        this.processedCount = processedCount;
        this.executorLabel = Objects.requireNonNull(executorLabel); // requireNonNull：null 时立即 NPE
    }

    public Map<String, Integer> qtyBySku() {
        return qtyBySku;
    }

    public int processedCount() {
        return processedCount;
    }

    public String executorLabel() {
        return executorLabel;
    }

    /** 各 sku 销量之和，对照 {@link SampleOrderJobs#expectedTotalQty()}。 */
    public int totalQty() {
        int sum = 0;
        for (int qty : qtyBySku.values()) { // values() 返回 Collection<Integer>，增强 for 遍历
            sum += qty; // 复合赋值 += 等价于 sum = sum + qty
        }
        return sum;
    }
}
