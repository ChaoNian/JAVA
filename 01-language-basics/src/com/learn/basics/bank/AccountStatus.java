package com.learn.basics.bank;

/**
 * 账户状态。枚举不只是常量列表，也可以带行为。
 *
 * 枚举的实例只有这里列出的这几个，不能 new AccountStatus()。
 * 比较枚举用 == 即可（每个常量全程序只有一份），和 String 不一样。
 */
public enum AccountStatus {
    ACTIVE,
    FROZEN,
    CLOSED;

    /** this 是「当前这个枚举值」。只有 ACTIVE 允许存取款。 */
    public boolean allowsTransactions() {
        return this == ACTIVE;
    }
}
