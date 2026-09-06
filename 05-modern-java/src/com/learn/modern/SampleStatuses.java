package com.learn.modern;

import java.time.LocalDateTime;

/**
 * 固定样例数据：工具类，只提供静态常量与静态工厂。
 */
public final class SampleStatuses {
    private SampleStatuses() { // 私有构造：禁止 new SampleStatuses()
    }

    /**
     * {@code public static final}：类级常量，全大写命名习惯。
     * {@code LocalDateTime.of(年, 月, 日, 时, 分)}：静态工厂，不是构造器 new。
     */
    public static final LocalDateTime T1 = LocalDateTime.of(2024, 1, 3, 10, 0);
    public static final LocalDateTime T2 = LocalDateTime.of(2024, 1, 4, 15, 30);

    /** 返回类型写具体 record {@link Pending}，调用方也可用父类型 {@link OrderStatus} 接收。 */
    public static Pending newPending() {
        return new Pending("ORD-1001"); // new record：走紧凑构造器里的校验
    }

    public static Pending cancelPathPending() {
        return new Pending("ORD-1002");
    }
}
