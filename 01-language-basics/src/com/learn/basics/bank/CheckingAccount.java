package com.learn.basics.bank;

/**
 * 活期：取款额外收 1 元手续费。这是本章唯一一次「继承后改行为」。
 *
 * extends Account：活期「是一种」账户，复用 id / 余额 / 存款。
 * final class：不要再往下继承了，组合优先于层层继承。
 */
public final class CheckingAccount extends Account {
    private static final Money FEE = Money.ofYuan(1); // 静态常量：所有活期共用这一份手续费

    public CheckingAccount(String id, String owner, Money openingBalance) {
        super(id, owner, openingBalance); // 必须先调父类构造，把 private 字段初始化好
    }

    /** 调用方仍写 withdraw(200)，实际扣 201。Bank.transfer 不必知道有手续费。 */
    @Override
    public void withdraw(Money amount) {
        super.withdraw(amount.plus(FEE));
    }

    @Override
    public String typeName() {
        return "Checking";
    }
}
