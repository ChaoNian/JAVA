package com.learn.modern; // 包声明：源文件须放在 src/com/learn/modern/ 下

/**
 * 订单状态的封闭层次（sealed）：只允许下面几个已知子类型。
 *
 * 语法拆解：
 * - {@code sealed interface}：封闭接口，子类型名单写在 {@code permits} 里
 * - {@code permits A, B, ...}：显式列出允许实现本接口的类型（须同模块/同包）
 * - 接口方法 {@code String orderId();}：抽象方法，各 record 实现（record 的组件访问器刚好匹配）
 *
 * 和普通 interface 的差别：编译器知道全部子类型，
 * 所以 {@code switch (status)} 可以<strong>穷尽匹配</strong>——漏写一个 case 会编译失败。
 */
public sealed interface OrderStatus // sealed：本接口不能被 permits 以外的类型实现
        permits Pending, Paid, Shipped, Cancelled { // permits：封闭名单，逗号分隔

    /**
     * 抽象实例方法：无方法体，由实现类提供。
     * record Pending(String orderId) 会自动生成 public String orderId()，正好满足本契约。
     */
    String orderId();
}
