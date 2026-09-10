package com.learn.jvm.debug;

/**
 * 调用链：checkout → resolveCity → city.toUpperCase。
 * 当 orderId 不存在时，cityOf 返回 null，toUpperCase 在本类抛 NPE。
 * 读堆栈时从顶往下找第一个你自己的包名（com.learn.jvm...）。
 */
public final class CheckoutService {
    private final OrderLookup lookup;

    public CheckoutService(OrderLookup lookup) {
        if (lookup == null) {
            throw new IllegalArgumentException("lookup required");
        }
        this.lookup = lookup;
    }

    public String checkout(String orderId) {
        String city = resolveCity(orderId);
        return "SHIP_TO=" + city;
    }

    private String resolveCity(String orderId) {
        String city = lookup.cityOf(orderId);
        // 下一行是堆栈里应定位到的「你的代码行」
        return city.toUpperCase();
    }
}
