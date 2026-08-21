package com.learn.basics;

/**
 * Java 只有值传递：参数拿到的是副本。
 *
 * - 基本类型：改的是副本，调用方看不到。
 * - 引用类型：副本里存的是「同一地址」，所以能改对象内部；把参数指向新对象，调用方看不到。
 * - Integer 有 -128..127 缓存，== 比较包装类型会骗人，和 Ex01 同一类坑。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\basics\Ex03PassByValue.java
 * 运行：java -cp out com.learn.basics.Ex03PassByValue
 */
public class Ex03PassByValue {

    /** 可改内部字段的小对象，用来对比「改对象」和「换对象」。 */
    static class Holder {
        int value;

        Holder(int value) {
            this.value = value; // this.value 是字段；右边的 value 是构造参数
        }
    }

    public static void main(String[] args) {
        // ---------- 1) 基本类型：拷贝的是数值本身 ----------
        int n = 1;
        bumpPrimitive(n); // 传入的是 1 的副本，方法里 n++ 改不到这里的 n
        System.out.println("after bumpPrimitive, n = " + n + "  (仍是 1)");

        // ---------- 2) 数组：拷贝的是「指向数组的地址」----------
        int[] cells = {1};
        bumpSlot(cells); // 副本和调用方指向同一块数组，改 cells[0] 双方都看得见
        System.out.println("after bumpSlot, cells[0] = " + cells[0] + "  (变成 2)");

        rebindArray(cells); // 方法里 cells = 新数组，只改了副本的指向，调用方仍拿着旧数组
        System.out.println("after rebindArray, cells[0] = " + cells[0] + "  (仍是 2，不是 9)");

        // ---------- 3) 普通对象：和数组同一条规则 ----------
        Holder holder = new Holder(1);
        mutateHolder(holder); // 沿地址改字段，调用方看得到
        System.out.println("after mutateHolder, holder.value = " + holder.value + "  (变成 2)");
        rebindHolder(holder); // 参数改去指向新 Holder，调用方的 holder 不变
        System.out.println("after rebindHolder, holder.value = " + holder.value + "  (仍是 2)");

        // ---------- 4) 装箱：Integer 是对象，== 比的是引用不是数值 ----------
        // 自动装箱：int 被包成 Integer。JVM 缓存 -128..127，超出则每次 new。
        Integer cachedA = 127;
        Integer cachedB = 127;       // 命中缓存，和 cachedA 是同一个对象，== 碰巧为 true
        Integer uncachedA = 128;
        Integer uncachedB = 128;     // 未缓存，两个对象，== 为 false
        System.out.println("127 == 127 (Integer) = " + (cachedA == cachedB) + "  (缓存，碰巧同一对象)");
        System.out.println("128 == 128 (Integer) = " + (uncachedA == uncachedB) + "  (未缓存)");
        System.out.println("128 equals 128       = " + uncachedA.equals(uncachedB));

        System.out.println();
        System.out.println("结论：没有引用传递这回事。传递的是引用的副本。包装类型比较用 equals。");
    }

    /** 形参 n 是实参的一份 int 拷贝。++ 只作用于这份拷贝。 */
    static void bumpPrimitive(int n) {
        n++;
    }

    /** 形参是数组引用的拷贝，仍指向调用方那块数组，所以改槽位会反映回去。 */
    static void bumpSlot(int[] cells) {
        cells[0]++;
    }

    /** 形参被改去指向新数组；调用方的变量仍指向旧数组。 */
    static void rebindArray(int[] cells) {
        cells = new int[] {9};
    }

    /** 沿引用改对象内部。调用方的 holder 还是这个对象，所以 value 变了。 */
    static void mutateHolder(Holder holder) {
        holder.value++;
    }

    /** 让形参指向新对象。调用方的 holder 变量没有被赋值，所以仍是原来那个。 */
    static void rebindHolder(Holder holder) {
        holder = new Holder(9);
    }
}
