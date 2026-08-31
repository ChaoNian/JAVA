package com.learn.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * {@link CompletableFuture} 组合异步步骤的极简示例。
 *
 * 依赖关系（可画成 DAG）：
 * <pre>
 *   fetchBasePrice ──► applyDiscount ──┐
 *                                      ├──► sum (应付总额)
 *   shipping (并行) ───────────────────┘
 * </pre>
 *
 * executor 由调用方传入并在 future 完成后再关闭；若在 supplyAsync 返回前就关池，任务会失败。
 */
public final class AsyncPricing {
    private AsyncPricing() {
    }

    /**
     * 异步：查基价 → 打折 → 与运费并行计算 → 合并成应付金额（分）。
     *
     * @param sku     商品编号
     * @param qty     数量；≥3 时行级 9 折且免运费（与 CompareApp 断言一致）
     * @param executor 跑 supplyAsync / thenApplyAsync 的线程池（本阶段用虚拟线程池）
     */
    public static CompletableFuture<Integer> quoteTotalAsync(
            String sku, int qty, ExecutorService executor) { // 返回泛型 CompletableFuture<Integer>
        // supplyAsync( Supplier<T>, Executor )：无参 lambda 提供异步结果
        CompletableFuture<Integer> basePrice = CompletableFuture.supplyAsync(
                () -> fetchBasePriceCents(sku), executor); // () -> 表达式：Supplier 函数式接口

        // thenApplyAsync：上一步完成后，把结果映射成新值（仍异步）
        CompletableFuture<Integer> discounted = basePrice.thenApplyAsync(
                price -> applyDiscountCents(price, qty), executor); // 单参数 lambda：price 是上一阶段结果

        CompletableFuture<Integer> shipping = CompletableFuture.supplyAsync(
                () -> shippingCents(qty), executor);

        // thenCombine：两个 Future 都完成后，用 BiFunction 合并（此处 Integer::sum 是方法引用）
        return discounted.thenCombine(shipping, Integer::sum);
    }

    private static int fetchBasePriceCents(String sku) {
        simulateIo(20);
        return switch (sku) { // switch 表达式（Java 14+）：每个分支用 -> 产出值
            case "A001" -> 1200; // case 标签匹配后执行右侧表达式并作为 switch 结果
            case "B002" -> 800;
            case "C003" -> 1500;
            default -> 500; // default：无 case 匹配时的兜底分支
        };
    }

    private static int applyDiscountCents(int unitPriceCents, int qty) {
        simulateIo(10);
        int line = unitPriceCents * qty;
        return qty >= 3 ? (int) (line * 0.9) : line; // (int) 显式窄化转换：double 截断为 int
    }

    private static int shippingCents(int qty) {
        simulateIo(15);
        return qty >= 3 ? 0 : 300;
    }

    private static void simulateIo(int millis) {
        try {
            Thread.sleep(millis); // sleep：让当前线程暂停指定毫秒（受检 InterruptedException）
        } catch (InterruptedException e) { // catch：捕获受检异常，须在方法签名 throws 或此处处理
            Thread.currentThread().interrupt(); // 恢复中断标志，供上层感知
            throw new IllegalStateException(e); // 包装后抛未检查异常，避免吞掉中断语义
        }
    }
}
