package com.learn.modern;

/**
 * 把状态变成可读描述：再练穷尽 switch，并示范 text block 与模式里的 {@code var}。
 */
public final class OrderStatusDescribe {
    private OrderStatusDescribe() {
    }

    /**
     * {@code case Pending(String id)}：记录模式 = instanceof + 解构字段。
     * 老写法：{@code if (status instanceof Pending p) { String id = p.orderId(); ... }}
     *
     * {@code var paidAt}：在模式里用局部类型推断，编译器从 Paid 的组件类型推出 LocalDateTime。
     * var 边界：只能用于局部变量 / 模式绑定，不能写在方法参数、字段上滥用。
     */
    public static String describe(OrderStatus status) {
        return switch (status) {
            case Pending(String id) -> "订单 " + id + " 待支付"; // +：字符串拼接
            case Paid(String id, var paidAt) -> "订单 " + id + " 已于 " + paidAt + " 支付";
            case Shipped(String id, var shippedAt, String tracking) -> // case 箭头后可换行写表达式
                    "订单 " + id + " 已于 " + shippedAt + " 发货，运单 " + tracking;
            case Cancelled(String id, String reason) -> "订单 " + id + " 已取消：" + reason;
        };
    }

    /**
     * text block（文本块，Java 15+）：
     * 以 {@code """} 开头和结尾的多行字符串字面量，内容里的换行会保留进字符串。
     * 结束 {@code """} 的缩进决定「公共缩进」会被剥掉——本方法返回的行首不会带多余空格。
     */
    public static String rulesHelp() {
        return """
                合法流转：
                  Pending  → Paid | Cancelled
                  Paid     → Shipped | Cancelled
                  Shipped  → （终态）
                  Cancelled→ （终态）
                """;
    }
}
