package com.learn.concurrency;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture; // java.util.concurrent：并发与异步 API
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 入口：只接线。业务逻辑分散在各 *Processor / RaceCounter / AsyncPricing 里。
 *
 * 运行顺序：
 * 1. 竞态复现与修复（共享变量是 {@link RaceCounter.BrokenState#count}）
 * 2. 同一份 {@link SampleOrderJobs}，固定线程池 vs 虚拟线程，结果应 MATCH
 * 3. {@link CompletableFuture} 报价链
 *
 * 编译：在 04-concurrency 目录执行 .\compile-and-run.ps1
 * 运行：java -cp out com.learn.concurrency.CompareApp
 */
public class CompareApp {
    /** 竞态演示：线程越多、每线程自增次数越多，丢更新越明显。 */
    private static final int RACE_THREADS = 8; // static final：类级常量，编译期可内联；命名全大写
    private static final int RACE_PER_THREAD = 100_000; // 数字字面量可用 _ 分隔，提高可读性
    private static final int RACE_EXPECTED = RACE_THREADS * RACE_PER_THREAD; // 编译期常量表达式

    // throws Exception：受检异常须声明或 try-catch；main 抛出让 JVM 打印栈追踪
    public static void main(String[] args) throws Exception {
        runRaceSection();
        System.out.println();

        // 同一份只读任务列表，两种 executor 应得到相同汇总
        List<OrderJob> jobs = SampleOrderJobs.all();
        System.out.println("sample jobs = " + jobs.size());
        System.out.println();

        ProcessingSummary fixed = FixedPoolProcessor.process(jobs);
        ProcessingSummary virtual = VirtualThreadProcessor.process(jobs);

        printSummary("fixed pool", fixed);
        printSummary("virtual threads", virtual);

        // 两边互相对照，再和手工期望比一遍
        boolean match = sameSummary(fixed, virtual)
                && fixed.totalQty() == SampleOrderJobs.expectedTotalQty() // == 比较 int 值
                && Objects.equals(fixed.qtyBySku(), SampleOrderJobs.expectedQtyBySku()); // Map 用 equals 比内容
        System.out.println(match ? "MATCH" : "MISMATCH");
        System.out.println();

        runAsyncPricingSection();
    }

    /** 第一段：先跑 broken，再跑两种修复，肉眼对比 LOST UPDATES vs OK。 */
    private static void runRaceSection() throws InterruptedException { // throws：调用方须处理或继续声明
        System.out.println("== race: shared int count++ ==");
        System.out.println("expected count = " + RACE_EXPECTED);

        int broken = RaceCounter.brokenParallelIncrement(RACE_THREADS, RACE_PER_THREAD);
        int synced = RaceCounter.fixedWithSynchronized(RACE_THREADS, RACE_PER_THREAD);
        int atomic = RaceCounter.fixedWithAtomic(RACE_THREADS, RACE_PER_THREAD);

        System.out.println("broken (no sync)     = " + broken
                + (broken == RACE_EXPECTED ? "  OK" : "  LOST UPDATES")); // 括号内三元表达式参与字符串拼接
        System.out.println("fixed synchronized   = " + synced
                + (synced == RACE_EXPECTED ? "  OK" : "  FAIL"));
        System.out.println("fixed AtomicInteger  = " + atomic
                + (atomic == RACE_EXPECTED ? "  OK" : "  FAIL"));
    }

    private static void printSummary(String title, ProcessingSummary summary) {
        System.out.println("-- " + title + " [" + summary.executorLabel() + "] --");
        System.out.println("processed  = " + summary.processedCount());
        System.out.println("qty by sku = " + summary.qtyBySku());
        System.out.println("total qty  = " + summary.totalQty());
        System.out.println();
    }

    /** 两种 executor 的 sku 汇总与处理条数一致即可。 */
    private static boolean sameSummary(ProcessingSummary a, ProcessingSummary b) {
        return a.processedCount() == b.processedCount()
                && Objects.equals(a.qtyBySku(), b.qtyBySku());
    }

    /**
     * 第三段：A001 × 3 → 单价 1200，行金额 3600，9 折 3240，满 3 免运费 → 仍 3240。
     * executor 必须在 future.get() 完成后再关闭（try-with-resources 块内 get）。
     */
    private static void runAsyncPricingSection() throws Exception {
        System.out.println("== CompletableFuture: quote A001 x3 ==");
        // try-with-resources：括号内资源须实现 AutoCloseable，块结束自动 close()
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<Integer> future = AsyncPricing.quoteTotalAsync("A001", 3, executor);
            int totalCents = future.get(); // get()：阻塞当前线程直到异步链完成（受检异常向上抛）
            System.out.println("quoted total cents = " + totalCents);
            System.out.println(totalCents == 3240 ? "PRICING OK" : "PRICING MISMATCH");
        }
    }
}
