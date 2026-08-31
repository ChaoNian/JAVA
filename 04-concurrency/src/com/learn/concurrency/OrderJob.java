package com.learn.concurrency;

/**
 * 一条待处理的「履约任务」：模拟多线程并行处理订单行。
 *
 * 字段都是 final：任务对象在线程间只传引用、不被修改，属于「安全发布」的不可变数据。
 * 真正会被多线程读写的共享状态放在 {@link ConcurrentHashMap} 等容器里，不放在本类。
 */
public final class OrderJob {
    private final int id;           // 基本类型 int：栈上存值，按值传递
    private final String sku;       // 商品编号，汇总时的 Map 键
    private final int qty;          // 本行销量，worker 完成后 merge 进共享 Map
    private final int workMillis;   // 模拟 IO / 下游 RPC 耗时（Thread.sleep）

    public OrderJob(int id, String sku, int qty, int workMillis) {
        // 构造里校验：坏数据尽早失败，worker 里就不用每步都 if
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku required");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("qty must be positive");
        }
        if (workMillis < 0) {
            throw new IllegalArgumentException("workMillis must be >= 0");
        }
        this.id = id;
        this.sku = sku;
        this.qty = qty;
        this.workMillis = workMillis;
    }

    public int id() { // 访问器返回基本类型，调用方得到的是值的副本
        return id;
    }

    public String sku() {
        return sku;
    }

    public int qty() {
        return qty;
    }

    public int workMillis() {
        return workMillis;
    }

    @Override
    public String toString() {
        return "job#" + id + " " + sku + " x" + qty;
    }
}
