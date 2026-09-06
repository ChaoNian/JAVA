package com.learn.modern;

import java.time.LocalDateTime;

/**
 * 合法状态流转：用 pattern matching 的 {@code switch} 表达式穷尽所有子类型。
 *
 * 规则：
 * <pre>
 *   Pending  → Paid | Cancelled
 *   Paid     → Shipped | Cancelled
 *   Shipped  → （终态）
 *   Cancelled→ （终态）
 * </pre>
 *
 * 关键语法：
 * - {@code return switch (x) { case ... -> ... };}：switch <strong>表达式</strong>，有返回值
 * - {@code case Pending(String id)}：记录模式——既判断类型，又解构出组件绑到 id
 * - {@code case Paid p}：类型模式——匹配 Paid，整对象绑到变量 p
 * - {@code throw ...} 可作为某一 case 的结果（该分支不返回 Paid，而是抛异常）
 * - 漏写 case：javac 报「switch 表达式未覆盖所有可能的输入值」（得益于 sealed）
 */
public final class OrderTransitions { // final class：禁止被继承
    private OrderTransitions() { // 私有构造：工具类，不允许 new
    }

    /**
     * 静态方法：用类名调用 {@code OrderTransitions.pay(...)}。
     * 返回类型写 {@link Paid}：成功时一定是已支付状态。
     */
    public static Paid pay(OrderStatus current, LocalDateTime paidAt) {
        // switch 表达式：每个 case 用 -> 给出值（或 throw）；末尾要有分号（因为是 return 的表达式）
        return switch (current) {
            case Pending(String id) -> new Paid(id, paidAt); // 解构 Pending.orderId → id，再 new Paid
            case Paid p -> throw illegal(p, "pay");         // 已是 Paid，不能再 pay
            case Shipped s -> throw illegal(s, "pay");
            case Cancelled c -> throw illegal(c, "pay");
        };
    }

    public static Shipped ship(OrderStatus current, LocalDateTime shippedAt, String trackingNo) {
        return switch (current) {
            // 只需要 id；第二个组件仍须写出绑定名（JDK 21；更新版本可用 _ 表示未使用）
            case Paid(String id, LocalDateTime ignored) -> new Shipped(id, shippedAt, trackingNo);
            case Pending p -> throw illegal(p, "ship"); // 未支付不能发货
            case Shipped s -> throw illegal(s, "ship");
            case Cancelled c -> throw illegal(c, "ship");
        };
    }

    public static Cancelled cancel(OrderStatus current, String reason) {
        return switch (current) {
            case Pending(String id) -> new Cancelled(id, reason);
            case Paid(String id, LocalDateTime ignored) -> new Cancelled(id, reason);
            case Shipped s -> throw illegal(s, "cancel"); // 已发货不允许取消（本示例规则）
            case Cancelled c -> throw illegal(c, "cancel");
        };
    }

    /** 私有静态工厂：拼异常消息；getClass().getSimpleName() 取出如 "Shipped"。 */
    private static IllegalStateException illegal(OrderStatus status, String action) {
        return new IllegalStateException(
                "cannot " + action + " when status is " + status.getClass().getSimpleName());
    }
}
