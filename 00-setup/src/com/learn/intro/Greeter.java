package com.learn.intro;

/**
 * 问候语逻辑单独放在一个类里，main 只负责启动。
 * 后面学面向对象时，会反复用到这种拆分。
 */
public class Greeter {
    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, Java learner!";
        }
        return "Hello, " + name + "!";
    }
}
