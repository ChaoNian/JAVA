package com.learn.apis.csv;

import java.time.LocalDate;

/**
 * CSV 里的一行订单。普通类，不用 record（那是 05）。
 * 字段都是 final：造出来就不能改，避免当 Map 的 key 时踩 Ex04 的坑。
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
