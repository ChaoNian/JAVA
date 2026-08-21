package com.learn.basics.bank;

/**
 * 银行「拥有」一组账户：这是组合，不是继承。
 * 本章不用 ArrayList，容量用数组表示。
 *
 * Bank 不是一种 Account（没有 extends Account）。
 * 它只是把账户放在自己的数组里帮忙查找、转账。
 */
public final class Bank {
    private final String name;
    private final Account[] slots; // 数组里可以同时放 CheckingAccount 和 SavingsAccount（多态）
    private int count;             // 已经开了几个户；空槽不要拿来遍历

    public Bank(String name, int capacity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.name = name;
        this.slots = new Account[capacity]; // 槽位先全是 null，open 时再放进去
    }

    public String name() {
        return name;
    }

    public void open(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("account required");
        }
        if (count >= slots.length) {
            throw new IllegalStateException("bank is full");
        }
        if (find(account.id()) != null) {
            throw new IllegalStateException("duplicate id: " + account.id());
        }
        slots[count] = account;
        count++;
    }

    /** 按账号找。字符串内容比较用 equals，不用 ==（对照 Ex01）。 */
    public Account find(String id) {
        for (int i = 0; i < count; i++) {
            if (slots[i].id().equals(id)) {
                return slots[i];
            }
        }
        return null;
    }

    /**
     * 转账只调 withdraw / deposit。
     * 活期会自己加手续费：这就是多态——Bank 不写 if (活期) 扣 1 元。
     */
    public void transfer(String fromId, String toId, Money amount) {
        Account from = requireAccount(fromId);
        Account to = requireAccount(toId);
        from.withdraw(amount);
        to.deposit(amount);
    }

    /** 返回拷贝，避免调用方改到银行内部的 slots 数组（对照 Ex03 的「改槽位会漏出去」）。 */
    public Account[] accounts() {
        Account[] copy = new Account[count];
        for (int i = 0; i < count; i++) {
            copy[i] = slots[i];
        }
        return copy;
    }

    private Account requireAccount(String id) {
        Account found = find(id);
        if (found == null) {
            throw new IllegalArgumentException("unknown account: " + id);
        }
        return found;
    }
}
