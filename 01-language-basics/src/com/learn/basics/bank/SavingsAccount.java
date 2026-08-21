package com.learn.basics.bank;

/**
 * 储蓄：实现计息接口；取款后余额不得低于 100 元。
 *
 * 同时「是一种账户」(extends) 和「能计息」(implements)。
 * annualRateBps：年利率，单位基点。250 表示 2.50%。
 */
public final class SavingsAccount extends Account implements InterestBearing {
    private static final Money MIN_BALANCE = Money.ofYuan(100);

    private final int annualRateBps;

    public SavingsAccount(String id, String owner, Money openingBalance, int annualRateBps) {
        super(id, owner, openingBalance);
        if (annualRateBps < 0) {
            throw new IllegalArgumentException("rate must be >= 0");
        }
        this.annualRateBps = annualRateBps;
    }

    /**
     * 先算「扣完还剩多少」，不够 100 元就抛异常，余额保持原样。
     * 不要先 super.withdraw 再检查：那样失败时钱已经扣掉了。
     */
    @Override
    public void withdraw(Money amount) {
        requireActive();
        requirePositive(amount);
        if (!balance().minus(amount).ge(MIN_BALANCE)) {
            throw new IllegalStateException("savings must keep at least " + MIN_BALANCE);
        }
        super.withdraw(amount);
    }

    /** 接口要求实现的方法。月息 = 余额 × 年利率 / 12；整数除法会丢掉分以下的小数。 */
    @Override
    public Money accrueMonthlyInterest() {
        requireActive();
        long interestCents = balance().cents() * annualRateBps / 12 / 10_000;
        Money interest = Money.ofCents(interestCents);
        if (!interest.isZero()) {
            deposit(interest);
        }
        return interest;
    }

    @Override
    public String typeName() {
        return "Savings";
    }
}
