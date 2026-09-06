package com.learn.modern;

import java.util.Objects; // Objects.equals：空安全比较两个引用

/**
 * 入口：只接线。业务在 Transitions / Describe / Bean 里。
 *
 * 编译：在 05-modern-java 目录执行 .\compile-and-run.ps1
 * 运行：java -cp out com.learn.modern.CompareApp
 */
public class CompareApp {
    /**
     * JVM 入口：{@code public static void main(String[] args)}。
     * 本方法无 throws：非法流转用 try/catch 消化；其它路径不抛受检异常。
     */
    public static void main(String[] args) {
        System.out.print(OrderStatusDescribe.rulesHelp()); // print：不自动换行（text block 末尾已有换行）
        System.out.println();

        runHappyPath();
        System.out.println();

        runCancelPath();
        System.out.println();

        runIllegalTransition();
        System.out.println();

        runBeanVsRecord();
    }

    /** Pending → Paid → Shipped；用 instanceof 记录模式断言最终状态。 */
    private static void runHappyPath() {
        System.out.println("== happy path ==");
        // 左边 OrderStatus：多态——变量静态类型是接口，运行时对象是 Pending/Paid/Shipped
        OrderStatus status = SampleStatuses.newPending();
        System.out.println(OrderStatusDescribe.describe(status));

        status = OrderTransitions.pay(status, SampleStatuses.T1); // 返回 Paid，赋回父类型变量
        System.out.println(OrderStatusDescribe.describe(status));

        status = OrderTransitions.ship(status, SampleStatuses.T2, "SF123");
        System.out.println(OrderStatusDescribe.describe(status));

        // instanceof 记录模式（Java 16+ / 21 完善）：
        // 若 status 是 Shipped，则解构出 id / at / tracking，并作为 && 短路表达式的一部分
        boolean ok = status instanceof Shipped(String id, var at, String tracking)
                && "ORD-1001".equals(id)          // 常量在前：避免 id 为 null 时 NPE
                && SampleStatuses.T2.equals(at)
                && "SF123".equals(tracking);
        System.out.println(ok ? "HAPPY PATH OK" : "HAPPY PATH FAIL"); // 三元运算符
    }

    private static void runCancelPath() {
        System.out.println("== cancel path ==");
        OrderStatus status = SampleStatuses.cancelPathPending();
        status = OrderTransitions.cancel(status, "用户取消");
        System.out.println(OrderStatusDescribe.describe(status));

        boolean ok = status instanceof Cancelled(String id, String reason)
                && "ORD-1002".equals(id)
                && "用户取消".equals(reason);
        System.out.println(ok ? "CANCEL PATH OK" : "CANCEL PATH FAIL");
    }

    /**
     * try/catch：捕获运行时异常 IllegalStateException。
     * try 块里若抛出，跳到匹配的 catch；没有 checked 异常要声明。
     */
    private static void runIllegalTransition() {
        System.out.println("== illegal transition ==");
        OrderStatus shipped = new Shipped("ORD-9", SampleStatuses.T2, "YT1");
        try {
            OrderTransitions.pay(shipped, SampleStatuses.T1); // 终态再 pay → 应抛异常
            System.out.println("ILLEGAL FAIL (should have thrown)");
        } catch (IllegalStateException e) { // e：捕获到的异常对象
            System.out.println("caught: " + e.getMessage()); // getMessage()：构造时传入的字符串
            System.out.println("ILLEGAL OK");
        }
    }

    /** record 无 setter；Bean 有 setOrderId——用语法差异证明可变性。 */
    private static void runBeanVsRecord() {
        System.out.println("== bean vs record ==");
        Paid paid = new Paid("ORD-1001", SampleStatuses.T1);
        LegacyOrderStatusBean bean = LegacyOrderStatusBean.fromPaid(paid);

        // Objects.equals(a,b)：a、b 均可 null；等价于「两者都 null 或 a.equals(b)」
        boolean sameAtStart = Objects.equals(bean.getOrderId(), paid.orderId())
                && Objects.equals(bean.getPaidAt(), paid.paidAt())
                && "PAID".equals(bean.getKind());
        System.out.println(sameAtStart ? "FIELD MATCH" : "FIELD MISMATCH");

        bean.setOrderId("HACKED"); // Bean：可变字段
        // paid 没有 setOrderId；orderId() 永远返回构造时的 "ORD-1001"
        System.out.println("after bean.setOrderId: bean=" + bean.getOrderId()
                + ", record=" + paid.orderId());
        System.out.println(paid.orderId().equals("ORD-1001") && "HACKED".equals(bean.getOrderId())
                ? "MUTABILITY DEMO OK"
                : "MUTABILITY DEMO FAIL");
    }
}
