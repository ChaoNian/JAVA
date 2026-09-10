package com.learn.jvm.money;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给阶段 1 的 Money 补测试：断言、异常断言、参数化。
 */
class MoneyTest {

    @Test
    void ofYuanConvertsToCents() {
        assertEquals(1000L, Money.ofYuan(10).cents());
    }

    @Test
    void plusReturnsNewInstanceAndLeavesOriginal() {
        Money ten = Money.ofYuan(10);
        Money result = ten.plus(Money.ofCents(50));
        assertEquals(1000L, ten.cents());
        assertEquals(1050L, result.cents());
        assertNotSame(ten, result);
    }

    @Test
    void minusRejectsOverdraft() {
        Money a = Money.ofYuan(1);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> a.minus(Money.ofYuan(2)));
        assertEquals("insufficient funds", ex.getMessage());
    }

    @Test
    void equalsByValueNotIdentity() {
        assertEquals(Money.ofCents(100), Money.ofYuan(1));
        assertEquals(Money.ofCents(100).hashCode(), Money.ofYuan(1).hashCode());
    }

    @ParameterizedTest(name = "yuan={0} -> cents={1}")
    @CsvSource({
            "0, 0",
            "1, 100",
            "12, 1200"
    })
    void ofYuanParameterized(long yuan, long expectedCents) {
        assertEquals(expectedCents, Money.ofYuan(yuan).cents());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, -100})
    void rejectsNegativeYuan(long yuan) {
        assertThrows(IllegalArgumentException.class, () -> Money.ofYuan(yuan));
    }

    @Test
    void geAndZero() {
        assertTrue(Money.ofYuan(2).ge(Money.ofYuan(1)));
        assertFalse(Money.ofYuan(1).ge(Money.ofYuan(2)));
        assertTrue(Money.zero().isZero());
    }
}
