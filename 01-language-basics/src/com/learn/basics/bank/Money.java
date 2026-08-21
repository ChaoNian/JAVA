package com.learn.basics.bank;

/**
 * 金额用「分」存储，避免 double 精度误差。
 * 不可变：plus / minus 都返回新对象，和 String.concat 同一套路。
 *
 * final class：别人不能再 extends Money，保证运算规则不会被改掉。
 */
public final class Money {
    // final 字段：对象一旦造出来，cents 就不能再改。要「加钱」只能 new 另一个 Money。
    private final long cents;

    /** 构造私有：外界不能 new Money(50)，必须走 ofYuan / ofCents，方便统一校验。 */
    private Money(long cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("cents must be >= 0");
        }
        this.cents = cents; // this.cents 是字段；右边是参数
    }

    public static Money zero() {
        return new Money(0);
    }

    /** 工厂方法：1 元 = 100 分。静态方法属于类，调用写成 Money.ofYuan(10)。 */
    public static Money ofYuan(long yuan) {
        if (yuan < 0) {
            throw new IllegalArgumentException("yuan must be >= 0");
        }
        return new Money(yuan * 100);
    }

    public static Money ofCents(long cents) {
        return new Money(cents);
    }

    /** 加法不改 this，返回新对象。对照 Ex02 的 s.concat(...) 不会改 s。 */
    public Money plus(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("other required");
        }
        return new Money(this.cents + other.cents);
    }

    public Money minus(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("other required");
        }
        if (this.cents < other.cents) {
            throw new IllegalArgumentException("insufficient funds");
        }
        return new Money(this.cents - other.cents);
    }

    /** ge = greater or equal，大于等于。 */
    public boolean ge(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("other required");
        }
        return this.cents >= other.cents;
    }

    public boolean isZero() {
        return cents == 0;
    }

    public long cents() {
        return cents;
    }

    /**
     * 值相等：两个 Money 只要分一样就相等，即使不是同一个对象。
     * 对照 Ex01：比较内容用 equals，不要用 ==。
     * @Override 是注解（annotation），写在方法上面，告诉编译器：这个方法是在覆盖父类（或接口）里已有的那个方法。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { // 同一个引用，一定相等
            return true;
        }
        if (!(o instanceof Money)) { // 不是 Money（或是 null），一定不等
            return false;
        }
        Money other = (Money) o; // 经典强转；本章不用 instanceof 模式匹配
        return cents == other.cents;
    }

    /** 相等的对象必须有相同 hashCode，否则以后放进 HashMap 会丢。 */
    @Override
    public int hashCode() {
        return Long.hashCode(cents);
    }

    /** 打印给人看：1000 分 -> "10.00 CNY"。 */
    @Override
    public String toString() {
        long yuan = cents / 100;
        long frac = cents % 100;
        String fracText = frac < 10 ? "0" + frac : Long.toString(frac);
        return yuan + "." + fracText + " CNY";
    }
}
