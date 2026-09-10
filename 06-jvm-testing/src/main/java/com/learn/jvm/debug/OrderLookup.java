package com.learn.jvm.debug;

import java.util.HashMap;
import java.util.Map;

/**
 * 故意留下一个「查不到就返回 null」的查找器，方便制造 NPE。
 * 生产代码更常见的是 Optional 或直接抛业务异常；这里是为了练读堆栈。
 */
public final class OrderLookup {
    private final Map<String, String> cityByOrderId = new HashMap<>();

    public OrderLookup put(String orderId, String city) {
        cityByOrderId.put(orderId, city);
        return this;
    }

    /** 找不到时返回 null（坏味道，但便于演示 NPE）。 */
    public String cityOf(String orderId) {
        return cityByOrderId.get(orderId);
    }
}
