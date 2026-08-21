package com.learn.basics.bank;

/**
 * 账户抽象：余额是 private，外界只能 deposit / withdraw。
 * 子类通过覆写 withdraw 改变规则，不能直接改字段。
 *
 * abstract：不能 new Account(...)，只能 new CheckingAccount / SavingsAccount。
 * 账户按 id 查找，不按值比较，所以这里不覆写 equals。
 */
public abstract class Account {
    private final String id;       // 开户后不能改
    private final String owner;
    private Money balance;         // 可变：每次存取都换成新的 Money 对象
    private AccountStatus status;

    /**
     * protected：只允许子类构造时调用 super(...)。
     * 包外的 BankApp 不能直接 new Account，也符合「抽象类不当入口」。
     */
    protected Account(String id, String owner, Money openingBalance) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id required");
        }
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("owner required");
        }
        this.id = id;
        this.owner = owner;
        this.balance = openingBalance == null ? Money.zero() : openingBalance;
        this.status = AccountStatus.ACTIVE;
    }

    // final 方法：子类不能覆写。id / 余额的读法全程序统一。
    public final String id() {
        return id;
    }

    public final String owner() {
        return owner;
    }

    public final Money balance() {
        return balance;
    }

    public final AccountStatus status() {
        return status;
    }

    /** 抽象方法：子类必须自己说自己是 Checking 还是 Savings。 */
    public abstract String typeName();

    /** 存款规则所有账户一样，所以 final，活期/储蓄都不能改。 */
    public final void deposit(Money amount) {
        requireActive();
        requirePositive(amount);
        this.balance = this.balance.plus(amount); // 不改旧 Money，换一个新的
    }

    /**
     * 不 final：子类可以改规则。
     * CheckingAccount 加手续费，SavingsAccount 加最低余额，都是覆写这个方法。
     */
    public void withdraw(Money amount) {
        requireActive();
        requirePositive(amount);
        this.balance = this.balance.minus(amount);
    }

    public final void freeze() {
        this.status = AccountStatus.FROZEN;
    }

    /** protected：子类能调，BankApp 不能调。用来在覆写 withdraw 时复用校验。 */
    protected final void requireActive() {
        if (!status.allowsTransactions()) {
            throw new IllegalStateException("account is " + status);
        }
    }

    protected static void requirePositive(Money amount) {
        if (amount == null || amount.isZero()) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }

    @Override
    public String toString() {
        // typeName() 会走到子类实现：打印时看到 Checking / Savings，这就是多态
        return typeName() + "{id=" + id + ", owner=" + owner
                + ", balance=" + balance + ", status=" + status + "}";
    }
}
