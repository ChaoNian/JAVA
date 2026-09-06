package com.learn.modern;

import java.time.LocalDateTime;

/**
 * 旧式可变 JavaBean：对照 record，看语法与可变性差异。
 *
 * 过关点「何时不能改成 record」：
 * - 字段可被 setter 改 → record 组件默认 final，没有 setXxx
 * - 框架要 public 无参构造 + setter（部分 JPA / 表单）→ record 不合适
 * - 只做不可变 DTO → 优先改成 record（见 {@link Paid}）
 *
 * 本类故意<strong>不是</strong> final、字段<strong>不是</strong> final，以便演示可变。
 */
public class LegacyOrderStatusBean {
    private String orderId;          // 非 final：可被 setter 反复赋值
    private String kind;             // 用字符串标签模拟状态（不如 sealed 类型安全）
    private LocalDateTime paidAt;

    /** public 无参构造：{@code new LegacyOrderStatusBean()}，字段先为 null。 */
    public LegacyOrderStatusBean() {
    }

    // --- JavaBean 命名约定：getXxx / setXxx；record 则是组件名()，如 orderId() ---

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId; // this.字段 = 参数：区分同名
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    /**
     * 静态工厂：从 record 拷贝到 Bean。
     * {@code paid.orderId()} / {@code paid.paidAt()}：record 访问器（不是 getOrderId）。
     */
    public static LegacyOrderStatusBean fromPaid(Paid paid) {
        LegacyOrderStatusBean bean = new LegacyOrderStatusBean();
        bean.setOrderId(paid.orderId());
        bean.setKind("PAID");
        bean.setPaidAt(paid.paidAt());
        return bean;
    }
}
