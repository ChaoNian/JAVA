package com.learn.jvm.money;

/**
 * 从阶段 1 银行练习抽来的金额类型，专门给本阶段补单元测试。
 * 不可变：plus / minus 返回新对象；字段 final。
 */
public final class Money {
    private final long cents;

    private Money(long cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("cents must be >= 0");
        }
        this.cents = cents;
    }

    public static Money zero() {
        return new Money(0);
    }

    public static Money ofYuan(long yuan) {
        if (yuan < 0) {
            throw new IllegalArgumentException("yuan must be >= 0");
        }
        return new Money(yuan * 100);
    }

    public static Money ofCents(long cents) {
        return new Money(cents);
    }

    public Money plus(Money other) {
        requireOther(other);
        return new Money(this.cents + other.cents);
    }

    public Money minus(Money other) {
        requireOther(other);
        if (this.cents < other.cents) {
            throw new IllegalArgumentException("insufficient funds");
        }
        return new Money(this.cents - other.cents);
    }

    public boolean ge(Money other) {
        requireOther(other);
        return this.cents >= other.cents;
    }

    public boolean isZero() {
        return cents == 0;
    }

    public long cents() {
        return cents;
    }

    private static void requireOther(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("other required");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money money)) {
            return false;
        }
        return cents == money.cents;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(cents);
    }

    @Override
    public String toString() {
        long yuan = cents / 100;
        long frac = cents % 100;
        String fracText = frac < 10 ? "0" + frac : Long.toString(frac);
        return yuan + "." + fracText + " CNY";
    }
}
