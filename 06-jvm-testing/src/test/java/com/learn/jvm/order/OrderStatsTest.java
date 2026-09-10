package com.learn.jvm.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给阶段 2/3 统计语义补测试；参数化覆盖多组 sku 累加。
 */
class OrderStatsTest {

    @Test
    void sortsByQtyDescThenSku() {
        List<Order> orders = List.of(
                order("A", "上海", 3),
                order("B", "北京", 5),
                order("C", "广州", 5));
        OrderStats stats = OrderStats.from(orders);
        assertEquals("B", stats.byQtyDesc().get(0).sku()); // 同 qty 时 sku 字典序 B < C
        assertEquals("C", stats.byQtyDesc().get(1).sku());
        assertEquals("A", stats.byQtyDesc().get(2).sku());
    }

    @Test
    void citiesAreDeduped() {
        OrderStats stats = OrderStats.from(List.of(
                order("A", "上海", 1),
                order("B", "上海", 2),
                order("C", "北京", 1)));
        assertEquals(2, stats.cities().size());
        assertTrue(stats.cities().contains("上海"));
        assertTrue(stats.cities().contains("北京"));
    }

    @Test
    void resultCollectionsAreUnmodifiable() {
        OrderStats stats = OrderStats.from(List.of(order("A", "上海", 1)));
        assertThrows(UnsupportedOperationException.class, () -> stats.byQtyDesc().add(order("Z", "深圳", 9)));
        assertThrows(UnsupportedOperationException.class, () -> stats.cities().add("黑客"));
        assertThrows(UnsupportedOperationException.class, () -> stats.qtyBySku().put("Z", 9));
    }

    @Test
    void emptyInput() {
        OrderStats stats = OrderStats.from(List.of());
        assertTrue(stats.byQtyDesc().isEmpty());
        assertTrue(stats.cities().isEmpty());
        assertTrue(stats.qtyBySku().isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("qtyBySkuCases")
    void aggregatesQtyBySku(String name, List<Order> orders, Map<String, Integer> expected) {
        assertEquals(expected, OrderStats.from(orders).qtyBySku());
    }

    static Stream<Arguments> qtyBySkuCases() {
        return Stream.of(
                Arguments.of(
                        "single sku",
                        List.of(order("A", "上海", 3), order("A", "北京", 2)),
                        Map.of("A", 5)),
                Arguments.of(
                        "two skus",
                        List.of(order("A", "上海", 3), order("B", "北京", 4), order("A", "广州", 1)),
                        Map.of("A", 4, "B", 4)));
    }

    private static Order order(String sku, String city, int qty) {
        return new Order(LocalDate.of(2024, 1, 1), sku, city, qty);
    }
}
