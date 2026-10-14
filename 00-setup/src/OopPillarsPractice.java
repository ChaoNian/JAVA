/**
 * 封装 / 继承 / 多态 入门对照。
 * 概念卡片：notes/oop-pillars.md
 *
 * 编译：javac -encoding UTF-8 -d out src\OopPillarsPractice.java
 * 运行：java -cp out OopPillarsPractice
 */
public class OopPillarsPractice {
    public static void main(String[] args) {
        demoEncapsulation();
        System.out.println();
        demoInheritance();
        System.out.println();
        demoPolymorphism();
    }

    /** 封装：外面不能直接改 balance，只能走 deposit / withdraw。 */
    private static void demoEncapsulation() {
        System.out.println("== 1. 封装 ==");
        Account acc = new Account("A001", 100);
        acc.deposit(50);
        System.out.println(acc.getId() + " balance=" + acc.getBalance());

        try {
            acc.withdraw(1000);
        } catch (IllegalArgumentException e) {
            System.out.println("caught: " + e.getMessage());
        }
        // acc.balance = -1; // 编译失败：private
    }

    /** 继承：SavingsAccount is-a Account，复用存取款，再加利息。 */
    private static void demoInheritance() {
        System.out.println("== 2. 继承 ==");
        SavingsAccount sav = new SavingsAccount("S001", 1000, 5); // 5% 
        sav.deposit(0); // 复用父类方法
        sav.addYearlyInterest();
        System.out.println(sav.getId() + " after interest=" + sav.getBalance());
    }

    /**
     * 多态：左边类型是 Animal，右边可以是 Dog / Cat。
     * 编译看左边有没有 speak；运行看右边真正是谁。
     */
    private static void demoPolymorphism() {
        System.out.println("== 3. 多态 ==");
        Animal[] zoo = {
                new Dog("旺财"),
                new Cat("喵喵"),
                new Dog("小黑")
        };
        for (int i = 0; i < zoo.length; i++) {
            // 同一个调用，不同对象不同叫声
            System.out.println(zoo[i].speak());
        }

        Animal a = new Dog("阿黄");
        System.out.println("parent ref -> " + a.speak());
    }
}

// ---------- 封装 + 继承用的账户 ----------

class Account {
    private final String id; // 封装：字段私有
    private int balance;

    Account(String id, int balance) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id required");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("balance >= 0");
        }
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount >= 0");
        }
        balance += amount;
    }

    public void withdraw(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount >= 0");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("insufficient funds");
        }
        balance -= amount;
    }

    /** 子类改余额时走受控入口（仍保持封装）。 */
    protected void credit(int amount) {
        deposit(amount);
    }
}

/** 继承：储蓄账户 = 账户 + 年利率。 */
class SavingsAccount extends Account {
    private final int ratePercent;

    SavingsAccount(String id, int balance, int ratePercent) {
        super(id, balance); // 先构造父类部分
        if (ratePercent < 0) {
            throw new IllegalArgumentException("rate >= 0");
        }
        this.ratePercent = ratePercent;
    }

    public void addYearlyInterest() {
        int interest = getBalance() * ratePercent / 100;
        credit(interest);
    }
}

// ---------- 多态用的动物 ----------

abstract class Animal {
    private final String name;

    Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /** 子类必须给出自己的叫声。 */
    public abstract String speak();
}

class Dog extends Animal {
    Dog(String name) {
        super(name);
    }

    public String speak() {
        return getName() + ": wang!";
    }
}

class Cat extends Animal {
    Cat(String name) {
        super(name);
    }

    public String speak() {
        return getName() + ": meow!";
    }
}
