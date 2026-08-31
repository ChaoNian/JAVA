package com.learn.functional;

import java.time.LocalDate; // java.time 包：Java 8+ 日期 API，替代旧版 java.util.Date

/**
 * 本阶段自带的最小订单模型（不依赖 02 的 classpath，专心练 Stream）。
 * 普通类，不用 record（那是 05）。
 * 字段都是 final：造出来就不能改，方便当 Stream 里的元素反复传递。
 */
public final class Order { // final class：禁止被继承（子类化）
    private final LocalDate date; // private：仅本类可访问；final 字段须在构造器中赋值且之后不可改
    private final String sku;     // 商品编号，Map / groupingBy 的分组键
    private final String city;    // 城市，收集进 Set 去重
    private final int qty;        // 销量，排序和 summingInt 都用它

    public Order(LocalDate date, String sku, String city, int qty) { // 构造器：与类同名、无返回类型
        // 构造里校验：坏数据尽早失败，后面 Stream 就不用每步都 if
        if (date == null) {
            throw new IllegalArgumentException("date required"); // throw：抛出未检查异常，调用方不必声明 throws
        }
        if (sku == null || sku.isBlank()) { // || 短路；isBlank() 为 true 当字符串为空或仅空白
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
    public LocalDate date() { // 无 static：实例方法，须先有 Order 对象再调用
        return date; // return 把字段值返回给调用方（基本类型/引用都是值传递）
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

    @Override // 注解：标记重写 Object.toString()；编译器会校验签名是否匹配父类
    public String toString() {
        return sku + " x" + qty + " @ " + city + " on " + date; // + 对 String 会拼接（非数值加法）
    }
}
