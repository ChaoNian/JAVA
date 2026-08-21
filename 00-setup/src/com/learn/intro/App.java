package com.learn.intro;

/**
 * 入口类。包名 com.learn.intro 必须对应目录 src/com/learn/intro/。
 *
 * 编译：javac -d out src\com\learn\intro\Greeter.java src\com\learn\intro\App.java
 * 运行：java -cp out com.learn.intro.App
 *       java -cp out com.learn.intro.App Alice
 */
public class App {
    public static void main(String[] args) {
        String name = args.length > 0 ? args[0] : "";
        Greeter greeter = new Greeter();
        System.out.println(greeter.greet(name));
    }
}
