package com.learn.functional;

import java.time.LocalDate;

/**
 * 本阶段自带的最小订单模型（不依赖 02 的 classpath，专心练 Stream）。
 * 普通类，不用 record（那是 05）。
 * 字段都是 final：造出来就不能改，方便当 Stream 里的元素反复传递。
 */
public final class Order {
    private final LocalDate date; // java.time，不要用 Date
    private final String sku;
    private final String city;
    private final int qty;

    public Order(LocalDate date, String sku, String city, int qty) {
        if (date == null) {
            throw new IllegalArgumentException("date required");
        }
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku required");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("city required");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("qty must be positive");
        }
        this.date = date;
        this.sku = sku;
        this.city = city;
        this.qty = qty;
    }

    // 方法名故意写成 date()/sku()：Stream 里可以写 Order::sku（方法引用）
    public LocalDate date() {
        return date;
    }

    public String sku() {
        return sku;
    }

    public String city() {
        return city;
    }

    public int qty() {
        return qty;
    }

    @Override
    public String toString() {
        return sku + " x" + qty + " @ " + city + " on " + date;
    }
}
