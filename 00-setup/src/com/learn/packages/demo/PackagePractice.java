package com.learn.packages.demo;

import com.learn.packages.util.Clock;

/**
 * 包与 import 对照。
 *
 * 1. 同包：LocalHelper 不用 import
 * 2. import：util.Clock 写短名 Clock
 * 3. 同名类：other.Clock 用全限定名
 *
 * 概念卡片：notes/package-basics.md
 *
 * 编译（在 00-setup 目录）：
 * javac -encoding UTF-8 -d out （列出 demo、util、other 下全部 .java）
 * 运行：
 * java -cp out com.learn.packages.demo.PackagePractice
 */
public class PackagePractice {
    public static void main(String[] args) {
        demoSamePackage();
        demoImport();
        demoSameSimpleName();
        demoFullyQualified();
    }

    private static void demoSamePackage() {
        System.out.println("== 1. 同包 ==");
        LocalHelper helper = new LocalHelper();
        System.out.println("  " + helper.tag());
    }

    private static void demoImport() {
        System.out.println("== 2. import util.Clock ==");
        Clock clock = new Clock();
        System.out.println("  " + clock.now());
    }

    /** 只 import 一个 Clock 时，另一个必须写全名。 */
    private static void demoSameSimpleName() {
        System.out.println("== 3. 两个 Clock ==");
        Clock util = new Clock();
        com.learn.other.Clock other = new com.learn.other.Clock();
        System.out.println("  " + util.now());
        System.out.println("  " + other.now());
    }

    /** 不 import 也可以，全部写全限定名。 */
    private static void demoFullyQualified() {
        System.out.println("== 4. 全限定名 ==");
        System.out.println("  " + new com.learn.packages.util.Clock().now());
        System.out.println("  this class = " + PackagePractice.class.getName());
    }
}
