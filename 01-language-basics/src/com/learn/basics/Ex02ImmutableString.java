package com.learn.basics;

/**
 * String 不可变：s.concat(...) 不会改原来的 s。
 *
 * concat / + / += 都会产生新的 String 对象；旧对象内容不变。
 * += 看起来像「改 s」，其实是「让 s 指向一个新对象」。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\basics\Ex02ImmutableString.java
 * 运行：java -cp out com.learn.basics.Ex02ImmutableString
 */
public class Ex02ImmutableString {
    public static void main(String[] args) {
        // ---------- 1) concat：返回新串，原来的 s 内容不变 ----------
        String s = "Java";                 // s 指向常量池里的 "Java"
        String concatenated = s.concat(" 21"); // 拼出 "Java 21" 这个新对象；s 仍指向 "Java"

        System.out.println("after concat, s     = " + s);            // 仍是 Java
        System.out.println("concatenated        = " + concatenated); // Java 21
        System.out.println("s == concatenated   = " + (s == concatenated)); // false：两个对象

        // ---------- 2) +=：看起来像改 s，其实是给 s 换了一个新对象 ----------
        String rebound = s; // rebound 和 s 此刻指向同一个 "Java"
        s += "!";           // 等价于 s = s + "!"，s 改去指向 "Java!"；"Java" 本身没被改
        System.out.println("after +=, s         = " + s);       // Java!
        System.out.println("old reference still = " + rebound); // 仍是 Java，证明旧对象没变
        System.out.println("s == rebound        = " + (s == rebound)); // false：s 已经指向别处

        System.out.println();
        System.out.println("结论：String 方法返回新对象。后面 Money.plus 也是同一套路。");
    }
}
