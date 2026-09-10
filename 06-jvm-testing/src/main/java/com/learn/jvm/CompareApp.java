package com.learn.jvm;

import com.learn.jvm.debug.CheckoutService;
import com.learn.jvm.debug.OrderLookup;
import com.learn.jvm.immutable.TagBag;
import com.learn.jvm.immutable.UserProfile;
import com.learn.jvm.money.Money;
import com.learn.jvm.order.Order;
import com.learn.jvm.order.OrderStats;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 非 JUnit 的对照入口：不可变 / 防御性拷贝 / 读 NPE 堆栈。
 * JVM 极限错误（OOM、栈溢出）请用独立 main，见 jvm 包与 README。
 *
 * <pre>
 *   mvn -q test
 *   mvn -q exec:java
 * </pre>
 */
public final class CompareApp {
    public static void main(String[] args) {
        runMoneySmoke();
        System.out.println();
        runOrderStatsSmoke();
        System.out.println();
        runImmutableDemo();
        System.out.println();
        runNpeStackDemo();
    }

    private static void runMoneySmoke() {
        System.out.println("== money ==");
        Money a = Money.ofYuan(10);
        Money b = a.plus(Money.ofCents(50));
        System.out.println(a + " + 0.50 = " + b);
        System.out.println(a.cents() == 1000 && b.cents() == 1050 ? "MONEY OK" : "MONEY FAIL");
    }

    private static void runOrderStatsSmoke() {
        System.out.println("== order stats ==");
        List<Order> orders = List.of(
                new Order(LocalDate.of(2024, 1, 2), "A", "上海", 3),
                new Order(LocalDate.of(2024, 1, 3), "B", "北京", 5),
                new Order(LocalDate.of(2024, 1, 4), "A", "上海", 2));
        OrderStats stats = OrderStats.from(orders);
        System.out.println("top=" + stats.byQtyDesc().get(0).sku()
                + " cities=" + stats.cities()
                + " A=" + stats.qtyBySku().get("A"));
        boolean ok = "B".equals(stats.byQtyDesc().get(0).sku())
                && stats.cities().size() == 2
                && Integer.valueOf(5).equals(stats.qtyBySku().get("A"));
        System.out.println(ok ? "STATS OK" : "STATS FAIL");
    }

    private static void runImmutableDemo() {
        System.out.println("== immutable / defensive copy ==");
        List<String> shared = new ArrayList<>();
        shared.add("vip");
        UserProfile profile = new UserProfile("Ada", shared);
        shared.add("hacked"); // 改调用方 List，不应污染 profile
        System.out.println("profile tags after external add: " + profile.tags());

        TagBag bag = new TagBag();
        bag.add("vip");
        bag.tags().add("hacked"); // 拿到内部引用，能改进去
        System.out.println("bag size after leak add: " + bag.size());

        boolean ok = profile.tagCount() == 1 && bag.size() == 2;
        System.out.println(ok ? "IMMUTABLE DEMO OK" : "IMMUTABLE DEMO FAIL");
    }

    private static void runNpeStackDemo() {
        System.out.println("== NPE stack ==");
        CheckoutService checkout = new CheckoutService(new OrderLookup().put("ORD-1", "上海"));
        try {
            checkout.checkout("ORD-MISSING");
            System.out.println("NPE FAIL (should have thrown)");
        } catch (NullPointerException e) {
            System.out.println("caught: " + e);
            System.out.println("--- stack (find com.learn.jvm.debug.CheckoutService) ---");
            e.printStackTrace(System.out);
            System.out.println("NPE DEMO OK");
        }
    }
}
