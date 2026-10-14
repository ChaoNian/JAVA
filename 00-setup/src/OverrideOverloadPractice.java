/**
 * 重写（Override）vs 重载（Overload）对照。
 * 概念卡片：notes/override-overload.md
 *
 * 编译：javac -encoding UTF-8 -d out src\OverrideOverloadPractice.java
 * 运行：java -cp out OverrideOverloadPractice
 */
public class OverrideOverloadPractice {
    public static void main(String[] args) {
        demoOverload();
        System.out.println();
        demoOverride();
        System.out.println();
        demoTrap();
    }

    /** 重载：同一个类里，同名不同参数。 */
    private static void demoOverload() {
        System.out.println("== 1. 重载 Overload ==");
        Calculator calc = new Calculator();
        System.out.println("add(int,int)       -> " + calc.add(1, 2));
        System.out.println("add(double,double) -> " + calc.add(1.5, 2.5));
        System.out.println("add(int,int,int)   -> " + calc.add(1, 2, 3));
    }

    /**
     * 重写：子类方法签名与父类相同，换掉实现。
     * 多态：左边是父类型，运行时走右边对象的重写版本。
     */
    private static void demoOverride() {
        System.out.println("== 2. 重写 Override ==");
        Printer p1 = new Printer();
        Printer p2 = new FancyPrinter(); // 父类型引用 → 子类对象

        System.out.println("Printer      -> " + p1.format("hi"));
        System.out.println("FancyPrinter -> " + p2.format("hi")); // 跑的是子类重写
    }

    /**
     * 陷阱：参数列表变了 = 重载/新方法，不是重写；
     * 用父类型引用时，仍然只能调到父类那个签名。
     */
    private static void demoTrap() {
        System.out.println("== 3. 易混点 ==");
        Printer p = new BrokenAttempt();
        System.out.println("parent.format(String) -> " + p.format("hi"));
        // p.format(3); // 编译失败：父类型 Printer 没有 format(int)

        BrokenAttempt b = new BrokenAttempt();
        System.out.println("child.format(int)     -> " + b.format(3)); // 这是重载，不是重写
    }
}

/** 重载示例：多种 add。 */
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }

    // int add(int a, int b) { return a - b; } // 不行：参数列表相同，不算重载
    // String add(int a, int b) { return ""; } // 不行：不能只靠返回类型区分
}

/** 父类：待重写的方法。 */
class Printer {
    String format(String text) {
        return "[" + text + "]";
    }
}

/** 重写：签名完全一致，换实现。 */
class FancyPrinter extends Printer {
    @Override
    String format(String text) {
        return "*** " + text + " ***";
    }
}

/**
 * 注意：format(int) 参数不同 → 这是重载（对 BrokenAttempt 而言），
 * 并没有重写父类的 format(String)。
 */
class BrokenAttempt extends Printer {
    String format(int n) {
        return "num=" + n;
    }
}
