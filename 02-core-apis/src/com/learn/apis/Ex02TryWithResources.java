package com.learn.apis;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * try-with-resources：括号里的资源用完自动 close，异常路径也会关。
 *
 * 资源必须实现 AutoCloseable。JDK 的 Reader / Writer / InputStream 都实现了。
 * 多个资源时，关闭顺序和声明顺序相反：后打开的先关。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\apis\Ex02TryWithResources.java
 * 运行：java -cp out com.learn.apis.Ex02TryWithResources
 */
public class Ex02TryWithResources {

    /** 假资源：只为把 open / close 打印出来。 */
    static class DemoResource implements AutoCloseable {
        private final String name;

        DemoResource(String name) {
            this.name = name;
            System.out.println("  open  " + name);
        }

        void work() {
            System.out.println("  work  " + name);
        }

        void fail() {
            throw new IllegalStateException("boom from " + name);
        }

        @Override
        public void close() {
            System.out.println("  close " + name);
        }
    }

    public static void main(String[] args) {
        System.out.println("-- 正常结束也会 close --");
        try (DemoResource r = new DemoResource("ok")) {
            r.work();
        }

        System.out.println("-- 抛异常也会 close（这是它存在的理由）--");
        try (DemoResource r = new DemoResource("fail")) {
            r.fail();
        } catch (IllegalStateException e) {
            System.out.println("  caught " + e.getMessage());
        }

        System.out.println("-- 两个资源：先开 first，后开 second；关闭相反 --");
        try (DemoResource first = new DemoResource("first");
             DemoResource second = new DemoResource("second")) {
            first.work();
            second.work();
        }

        System.out.println("-- 真实 JDK 类型：BufferedReader 也是 AutoCloseable --");
        try (BufferedReader reader = new BufferedReader(new StringReader("hello\njava"))) {
            System.out.println("  line1 = " + reader.readLine());
            System.out.println("  line2 = " + reader.readLine());
        } catch (java.io.IOException e) {
            // readLine 声明了 checked IOException，必须处理
            System.out.println("  io: " + e.getMessage());
        }

        System.out.println();
        System.out.println("结论：能 AutoCloseable 的，一律放进 try (...)。不要靠 finally 手写 close。");
    }
}
