package com.learn.jvm.jvm;

/**
 * 栈溢出演示：无限递归占满调用栈。
 * <p>
 * 运行（在 06-jvm-testing 目录）：
 * <pre>
 *   mvn -q -DskipTests package
 *   java -cp target/classes com.learn.jvm.jvm.StackOverflowDemo
 * </pre>
 * 可选：{@code -Xss256k} 把每线程栈调小，更快触发。
 * <p>
 * 读报错：{@code StackOverflowError} 通常没有「业务 message」，看堆栈重复帧即可。
 */
public final class StackOverflowDemo {
    public static void main(String[] args) {
        System.out.println("calling boom(0) ...");
        boom(0);
    }

    /** depth 只是给人看的计数；真正耗栈的是每一层方法帧。 */
    private static void boom(int depth) {
        boom(depth + 1);
    }
}
