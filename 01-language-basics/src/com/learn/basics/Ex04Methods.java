package com.learn.basics;

/**
 * 重载、可变参数、静态成员。
 *
 * 重载按「最具体的签名」选：max(1, 2) 走两个 int，不会走 int...。
 * 静态成员属于类，不属于某个对象。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\basics\Ex04Methods.java
 * 运行：java -cp out com.learn.basics.Ex04Methods
 */
public class Ex04Methods {
    // 静态字段：只有一份，属于类本身。无论 new 多少个对象，改的都是这一个计数器。
    private static int callCount;

    public static void main(String[] args) {
        // ---------- 1) 重载：编译器挑「参数个数最贴」的那一个 ----------
        System.out.println("max(3, 8)          = " + max(3, 8));       // 走 max(int, int)
        System.out.println("max(3, 8, 5)       = " + max(3, 8, 5));    // 走 max(int, int, int)，不会走 int...
        System.out.println("max(3, 8, 5, 1, 9) = " + max(3, 8, 5, 1, 9)); // 5 个参数，只能走 int...
        // 三参数版本内部又调了两次两参数 max，所以 callCount = 1 + (1+2) + 1 = 5
        System.out.println("static callCount   = " + callCount);

        // ---------- 2) 实例方法 vs 静态字段 ----------
        Ex04Methods first = new Ex04Methods();
        Ex04Methods second = new Ex04Methods();
        System.out.println("instance label()   = " + first.label() + " / " + second.label());
        // 两个对象，callCount 仍是同一份；用 类名.字段 读，强调它不属于 first/second
        System.out.println("仍是同一份 callCount = " + Ex04Methods.callCount);
    }

    /** 两个 int：比可变参数更具体，所以 max(3, 8) 一定进这里。 */
    static int max(int a, int b) {
        callCount++;
        return a >= b ? a : b; // 三元运算符：条件 ? 真值 : 假值
    }

    /** 三个 int：比 int... 更具体。内部复用两参数版本，callCount 会再加 2。 */
    static int max(int a, int b, int c) {
        callCount++;
        return max(max(a, b), c);
    }

    /** 可变参数：调用处写成一串 int，方法里收到的是 int[]。没有更具体的重载时才走这里。 */
    static int max(int... nums) {
        callCount++;
        if (nums.length == 0) {
            throw new IllegalArgumentException("nums must not be empty");
        }
        int peak = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > peak) {
                peak = nums[i];
            }
        }
        return peak;
    }

    /** 没有 static：必须通过对象调用，first.label() / second.label()。 */
    String label() {
        return "instance of Ex04Methods";
    }
}
