package com.learn.functional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 统计结果容器。Imperative / Stream 两边都产出这个形状，方便 CompareApp 对照。
 *
 * 三种集合一起用（和 02 一样）：
 * - List：按销量排好的订单
 * - Set：去重后的城市
 * - Map：sku -> 总销量
 *
 * 注意：02 的 OrderStats 在构造里算；这里拆成两个 *from，只负责装结果。
 */
public final class OrderStats { // 不可变结果容器：字段 final，构造后不能换引用
    private final List<Order> byQtyDesc;       // 泛型 List<Order>：有序列表，元素类型为 Order
    private final Set<String> cities;          // 无序：只关心有哪些城市
    private final Map<String, Integer> qtyBySku; // 无序：只关心每个 sku 卖了多少

    public OrderStats(List<Order> byQtyDesc, Set<String> cities, Map<String, Integer> qtyBySku) {
        if (byQtyDesc == null || cities == null || qtyBySku == null) { // null 检查：避免 NPE 拖到更深处
            throw new IllegalArgumentException("stats parts required");
        }
        this.byQtyDesc = byQtyDesc; // this.字段：区分形参与同名字段
        this.cities = cities;
        this.qtyBySku = qtyBySku;
    }

    public List<Order> byQtyDesc() { // 访问器（getter）：只读暴露内部引用，调用方不应修改返回的 List
        return byQtyDesc;
    }

    public Set<String> cities() {
        return cities;
    }

    public Map<String, Integer> qtyBySku() {
        return qtyBySku;
    }
}
