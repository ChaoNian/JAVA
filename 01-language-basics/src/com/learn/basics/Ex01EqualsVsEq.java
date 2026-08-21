package com.learn.basics;

/**
 * 为什么字符串不能用 == 比较内容。
 *
 * == 比较的是「是不是同一个对象」；equals 比较的是「内容相不相等」。
 * 字符串字面量会进入常量池，所以 "java" == "java" 常常碰巧为 true，这会骗人。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\basics\Ex01EqualsVsEq.java
 * 运行：java -cp out com.learn.basics.Ex01EqualsVsEq
 */
public class Ex01EqualsVsEq {
    public static void main(String[] args) {
        String literalA = "java";
        String literalB = "java";
        String constructed = new String("java");
        String runtimeConcat = "ja" + new String("va");

        System.out.println("literalA == literalB          -> " + (literalA == literalB));
        System.out.println("literalA == constructed       -> " + (literalA == constructed));
        System.out.println("literalA == runtimeConcat     -> " + (literalA == runtimeConcat));
        System.out.println("literalA.equals(constructed)  -> " + literalA.equals(constructed));
        System.out.println("literalA.equals(runtimeConcat)-> " + literalA.equals(runtimeConcat));

        System.out.println();
        System.out.println("结论：比较内容一律用 equals。== 为 true 只说明是同一个引用。");
    }
}
