package com.learn.concurrency;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 固定线程池处理一批任务（平台线程模型）。
 *
 * 用到的 API：
 * - {@link BlockingQueue}：多生产者单队列，worker 安全地取任务
 * - {@link Executors#newFixedThreadPool}：线程数有上限，适合 CPU/连接池受限
 * - {@link CountDownLatch}：主线程等全部 worker 退出后再汇总
 * - {@link ConcurrentHashMap#merge}：多线程安全地按 sku 累加 qty
 *
 * 共享状态：{@code qtyBySku}。若换成普通 HashMap + 无锁 merge，可能丢更新或数据结构损坏。
 */
public final class FixedPoolProcessor {
    /** 平台 worker 数量；任务只有 6 条，4 条线程足够演示「池化复用」。 */
    private static final int POOL_SIZE = 4;

    private FixedPoolProcessor() {
    }

    public static ProcessingSummary process(List<OrderJob> jobs) throws InterruptedException {
        // 构造时把 jobs 全部放进队列；之后只有 worker 会 poll 取出
        BlockingQueue<OrderJob> queue = new LinkedBlockingQueue<>(jobs); // 构造可传入集合，一次性入队
        ConcurrentHashMap<String, Integer> qtyBySku = new ConcurrentHashMap<>();
        CountDownLatch done = new CountDownLatch(POOL_SIZE); // 倒数计数器：await 等到 count 归零

        long startNanos = System.nanoTime();

        try (ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE)) {
            for (int i = 0; i < POOL_SIZE; i++) { // 经典 for：启动固定数量的 worker
                pool.submit(workerLoop(queue, qtyBySku, done)); // submit(Runnable)：无返回值，异常进 Future（此处未取）
            }
            done.await(); // 当前线程阻塞，直到 countDown 次数达到初始值
        }

        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
        System.out.println("  fixed pool threads = " + POOL_SIZE + ", elapsedMs = " + elapsedMs);
        return new ProcessingSummary(qtyBySku, jobs.size(), "fixed-pool(" + POOL_SIZE + ")");
    }

    /**
     * 每个平台线程跑这个循环：有活就干，队列空且暂时没活就带超时 poll，避免忙等。
     * finally 里 countDown，保证 worker 异常退出时主线程也不会永远 await。
     */
    private static Runnable workerLoop(
            BlockingQueue<OrderJob> queue,
            ConcurrentHashMap<String, Integer> qtyBySku,
            CountDownLatch done) {
        return () -> { // lambda 实现 Runnable：run() 无参无返回值
            try {
                while (true) { // 无限循环；靠 break 退出
                    OrderJob job = queue.poll(50, TimeUnit.MILLISECONDS); // 带超时的 poll，避免永久阻塞
                    if (job == null) {
                        if (queue.isEmpty()) {
                            break; // break：跳出最近一层循环
                        }
                        continue; // continue：跳过本次循环剩余语句，进入下一轮
                    }
                    handle(job, qtyBySku);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally { // finally：无论正常还是异常都会执行
                done.countDown(); // 每退出一个 worker，计数减 1
            }
        };
    }

    private static void handle(OrderJob job, ConcurrentHashMap<String, Integer> qtyBySku) {
        simulateWork(job.workMillis());
        // merge：key 不存在就 put qty；已存在就用 Integer::sum 合并（线程安全）
        qtyBySku.merge(job.sku(), job.qty(), Integer::sum);
    }

    /** sleep 模拟阻塞 IO；固定线程池里这段时间会占住一条平台线程。 */
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
