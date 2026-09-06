package com.learn.modern;

/**
 * 已取消。组件 {@code reason}：取消原因字符串。
 * {@code implements OrderStatus} 后自动具备 orderId()，满足接口方法。
 */
public record Cancelled(String orderId, String reason) implements OrderStatus {
    public Cancelled {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId required");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("reason required");
        }
    }
}
