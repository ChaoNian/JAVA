package com.learn.jvm.order;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 阶段 2/3 订单模型的精简版：本模块自带，不依赖其它阶段的 classpath。
 */
public final class Order {
    private final LocalDate date;
    private final String sku;
    private final String city;
    private final int qty;

    public Order(LocalDate date, String sku, String city, int qty) {
        this.date = Objects.requireNonNull(date, "date");
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku required");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("city required");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("qty must be positive");
        }
        this.sku = sku;
        this.city = city;
        this.qty = qty;
    }

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
