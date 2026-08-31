package com.learn.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 故意写出「会丢更新」的共享计数，再给出同步修复版。
 *
 * 共享变量：{@code BrokenState.count}。多线程同时做 {@code count++} 不是原子操作
 * （读-改-写三步），所以会丢增量。CompareApp 用 8 线程 × 10 万次，期望值 80 万。
 *
 * 修复思路：
 * - {@link #fixedWithSynchronized}：同一把锁包住临界区，建立 happens-before
 * - {@link #fixedWithAtomic}：用 {@link AtomicInteger} 把读-改-写变成一条 CAS 链
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\concurrency\RaceCounter.java
 * （通常通过 CompareApp 一起编译运行）
 */
public final class RaceCounter {
    private RaceCounter() {
    }

    /** 未同步的共享状态 —— 用来复现竞态。多个线程持有同一引用。 */
    static final class BrokenState { // static 嵌套类：不依赖外部类实例即可创建
        int count; // 默认包可见；非 final 可被任意持有引用的线程改写
    }

    static final class LockedState {
        private final Object lock = new Object(); // 专用锁对象，避免 this 被外部 synchronized
        int count;

        void increment() { // 包可见实例方法
            synchronized (lock) { // synchronized 块：同一时刻只有一个线程能进入
                count++; // 非原子：读-改-写，块外仍可能竞态
            }
        }
    }

    /**
     * 多个平台线程各做 {@code perThread} 次自增。期望总数 = threads × perThread。
     * 未同步时经常小于期望值（竞态）；多跑几次 broken 版，结果也可能略有波动。
     */
    public static int brokenParallelIncrement(int threads, int perThread) throws InterruptedException {
        BrokenState shared = new BrokenState();
        Thread[] workers = new Thread[threads]; // 数组：长度固定，元素类型 Thread
        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> { // Thread(Runnable, name)：lambda 作任务体
                for (int n = 0; n < perThread; n++) {
                    shared.count++; // 竞态点：非 synchronized，多线程不安全
                }
            }, "broken-worker"); // 第二个参数：线程名，便于日志区分
            workers[i].start(); // start() 启动新线程；勿用 run()（会在当前线程同步执行）
        }
        for (Thread worker : workers) {
            worker.join(); // join()：当前线程等待 worker 终止
        }
        return shared.count;
    }

    /** {@link Thread} + {@code synchronized} 修复版。 */
    public static int fixedWithSynchronized(int threads, int perThread) throws InterruptedException {
        LockedState shared = new LockedState();
        Thread[] workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                for (int n = 0; n < perThread; n++) {
                    shared.increment();
                }
            }, "sync-worker");
            workers[i].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
        return shared.count;
    }

    /** {@link AtomicInteger#incrementAndGet()} 修复版，无显式锁。 */
    public static int fixedWithAtomic(int threads, int perThread) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0); // 原子类：CAS 实现无锁自增
        Thread[] workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                for (int n = 0; n < perThread; n++) {
                    counter.incrementAndGet(); // 原子读-改-写，等价于安全版 count++
                }
            }, "atomic-worker");
            workers[i].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
        return counter.get(); // get() 读取当前原子值
    }
}
