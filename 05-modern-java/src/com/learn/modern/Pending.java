package com.learn.modern;

/**
 * 待支付状态。
 *
 * record 语法：
 * {@code public record Pending(String orderId) implements OrderStatus}
 * - {@code record}：特殊类，组件写在类名后的圆括号里（这里只有 orderId）
 * - 编译器自动生成：private final 字段、规范构造器、orderId()、equals/hashCode/toString
 * - {@code implements OrderStatus}：实现 sealed 接口；本类型须在 permits 名单中
 *
 * 对比 03 手写 {@code Order}：语义都是不可变数据，record 样板少很多。
 * 何时不能用：需要 setter、要继承别的类、或框架要求无参构造时（见 LegacyOrderStatusBean）。
 */
public record Pending(String orderId) implements OrderStatus {
    /**
     * 紧凑构造器（compact constructor）：
     * 写法是 {@code public Pending { ... }}，没有参数列表——参数就是 record 头里的组件。
     * 在「自动赋值 this.orderId = orderId」之前执行；这里只做校验，不必手写赋值。
     */
    public Pending {
        // || 短路或：左边为 true 就不算右边；isBlank()：空串或纯空白
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId required"); // 未检查异常，不必声明 throws
        }
    }
}
