package com.learn.jvm.debug;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 故意触发 NPE，断言堆栈里能定位到 CheckoutService——用测试锁住「读堆栈」习惯。
 */
class CheckoutServiceTest {

    @Test
    void missingOrderProducesNpeInResolveCity() {
        CheckoutService service = new CheckoutService(new OrderLookup().put("ORD-1", "上海"));

        NullPointerException npe = assertThrows(
                NullPointerException.class,
                () -> service.checkout("ORD-MISSING"));

        String stack = stackTraceOf(npe);
        assertTrue(
                stack.contains("com.learn.jvm.debug.CheckoutService.resolveCity"),
                () -> "expected resolveCity in stack, got:\n" + stack);
        assertTrue(
                stack.contains("CheckoutService.java"),
                () -> "expected file name in stack, got:\n" + stack);
    }

    @Test
    void existingOrderSucceeds() {
        CheckoutService service = new CheckoutService(new OrderLookup().put("ORD-1", "上海"));
        org.junit.jupiter.api.Assertions.assertEquals("SHIP_TO=上海", service.checkout("ORD-1"));
    }

    private static String stackTraceOf(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
