package com.learn.modern;

import java.time.LocalDateTime;

/**
 * 已发货。三个组件：订单号、发货时间、运单号。
 * 终态：业务上不能再离开（由 OrderTransitions 的 switch 保证，不是语法强制）。
 */
public record Shipped(String orderId, LocalDateTime shippedAt, String trackingNo) implements OrderStatus {
    public Shipped {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId required");
        }
        if (shippedAt == null) {
            throw new IllegalArgumentException("shippedAt required");
        }
        if (trackingNo == null || trackingNo.isBlank()) {
            throw new IllegalArgumentException("trackingNo required");
        }
    }
}
