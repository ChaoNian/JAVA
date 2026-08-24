package com.learn.apis;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型：编译期帮你记住类型；运行时会被擦除。
 *
 * PECS 只记一句：往外取用 extends，往里放用 super。本阶段点到为止，不必刷题。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\apis\Ex05Generics.java
 * 运行：java -cp out com.learn.apis.Ex05Generics
 */
public class Ex05Generics {

    /**
     * T 是类型参数，调用时再填。Box&lt;String&gt; 的 T 就是 String。
     * 编译器因此禁止 names.set(21)。
     */
    static class Box<T> {
        private T value;

        void set(T value) {
            this.value = value;
        }

        T get() {
            return value;
        }
    }

    /**
     * ? extends String：这是一份「能取出 String 的列表」。
     * 实际可能是 List&lt;String&gt;。只能 get，不能 add（除了 null），
     * 因为编译器不知道里面到底是哪种 String 子类型。
     */
    static String firstString(List<? extends String> src) {
        return src.get(0);
    }

    /**
     * ? super String：这是一份「能接受 String 的列表」。
     * List&lt;Object&gt; 可以放进任何对象，所以也能 add("hello")。
     * 取出来只能当 Object 用，因为里面可能混着别的类型。
     */
    static void addHello(List<? super String> dest) {
        dest.add("hello");
    }

    public static void main(String[] args) {
        // ---------- 1) Box&lt;T&gt;：同一套代码，装不同的类型 ----------
        Box<String> names = new Box<String>();
        names.set("Ada");
        System.out.println("Box<String>.get() = " + names.get());

        Box<Integer> numbers = new Box<Integer>();
        numbers.set(21);
        System.out.println("Box<Integer>.get() = " + numbers.get());
        // names.set(21); // 编不过：T 已经是 String

        // ---------- 2) extends：从列表里取 ----------
        List<String> words = new ArrayList<String>();
        words.add("java");
        System.out.println("firstString(words) = " + firstString(words));

        // ---------- 3) super：往列表里放 ----------
        List<Object> objects = new ArrayList<Object>();
        addHello(objects); // Object 是 String 的父类，符合 ? super String
        System.out.println("addHello into List<Object> = " + objects);

        // ---------- 4) 擦除：<> 里的信息编译完就丢掉 ----------
        // 运行时 a 和 b 都是 ArrayList，没有「ArrayList of String」这种单独的类
        List<String> a = new ArrayList<String>();
        List<Integer> b = new ArrayList<Integer>();
        System.out.println("erasure, same class? " + (a.getClass() == b.getClass()));

        System.out.println();
        System.out.println("结论：<> 里的类型只在编译期检查。取用 ? extends，放入用 ? super。");
    }
}
