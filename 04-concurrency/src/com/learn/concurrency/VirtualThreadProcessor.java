package com.learn.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 与 {@link FixedPoolProcessor} 相同的业务语义，改用 Java 21+ 虚拟线程。
 *
 * 模型差异：
 * - 固定池：少量平台线程复用，sleep 期间占住 worker
 * - 虚拟线程：每个任务一条轻量线程，{@link Thread#sleep} 会 unmount，不长期占用载体线程
 *
 * 用到的 API：
 * - {@link Executors#newVirtualThreadPerTaskExecutor}
 * - {@link Callable} + {@link ExecutorService#invokeAll}：提交并等待全部完成
 *
 * 注意：不要在虚拟线程里长时间 pin 载体线程（例如在 {@code synchronized} 里做阻塞 JNI）。
 * 本例临界区只有 map.merge，耗时在 sleep，属于推荐用法。
 */
public final class VirtualThreadProcessor {
    private VirtualThreadProcessor() {
    }

    public static ProcessingSummary process(List<OrderJob> jobs) throws Exception { // throws Exception：汇总多种受检异常
        ConcurrentHashMap<String, Integer> qtyBySku = new ConcurrentHashMap<>(); // 钻石运算符 <>：由左侧推断泛型实参
        long startNanos = System.nanoTime(); // long 字面量；nanoTime 单调递增，适合测耗时

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<String>> callables = new ArrayList<>(); // Callable<V>：有返回值的任务，call() 可抛异常
            for (OrderJob job : jobs) { // 增强 for（for-each）：遍历 Iterable，无需手写索引
                callables.add(() -> { // lambda 实现 Callable：无参，返回 String
                    simulateWork(job.workMillis()); // 闭包捕获 job 变量（实际为 final 或有效 final）
                    qtyBySku.merge(job.sku(), job.qty(), Integer::sum); // merge：BiFunction 合并旧值与新值
                    return Thread.currentThread().toString();
                });
            }

            List<Future<String>> futures = executor.invokeAll(callables); // invokeAll 阻塞直到全部任务结束
            for (Future<String> future : futures) { // for-each 遍历 List<Future>
                future.get(); // get() 阻塞取结果；任务抛异常会包装成 ExecutionException
            }
        } // 离开 try 块，executor 自动 shutdown

        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
        System.out.println("  virtual threads (one per job), elapsedMs = " + elapsedMs);
        return new ProcessingSummary(qtyBySku, jobs.size(), "virtual-thread-per-task");
    }

    private static void simulateWork(int millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted while simulating work", e);
        }
    }
}
