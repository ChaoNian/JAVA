package com.learn.modern;

import java.time.LocalDateTime; // java.time：现代日期时间 API（不用 java.util.Date）

/**
 * 已支付。record 可有多个组件：{@code (String orderId, LocalDateTime paidAt)}。
 * 不同状态形状可以不同——这是 sealed + record 建模状态机的优势（不必塞进同一个大 Bean）。
 */
public record Paid(String orderId, LocalDateTime paidAt) implements OrderStatus {
    public Paid { // 紧凑构造器：两个组件都会在本块结束后自动赋给字段
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId required");
        }
        if (paidAt == null) { // 引用类型用 == null 判断「没有对象」
            throw new IllegalArgumentException("paidAt required");
        }
    }
}
