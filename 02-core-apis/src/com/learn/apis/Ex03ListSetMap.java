package com.learn.apis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 三种集合各管一件事：别再用数组硬撑。
 *
 * - List：有序、可重复。要保留出现顺序、允许同一元素两次，用它。
 * - Set：只要「有没有」，不要重复。
 * - Map：按键查找、计数、分组。
 *
 * 左边写接口（List / Set / Map），右边写实现（ArrayList / HashSet / HashMap）。
 * 换实现时调用代码不用改。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\apis\Ex03ListSetMap.java
 * 运行：java -cp out com.learn.apis.Ex03ListSetMap
 */
public class Ex03ListSetMap {
    public static void main(String[] args) {
        String[] raw = {"apple", "pear", "apple", "banana", "pear", "apple"};

        // ---------- 1) List：记下每一次出现，apple 会有 3 次 ----------
        // 变量类型写 List，真正干活的是 ArrayList（数组实现，按下标读写快）
        List<String> every = new ArrayList<String>();
        for (int i = 0; i < raw.length; i++) {
            every.add(raw[i]); // add 总是追加，不管这个词以前有没有
        }
        System.out.println("List size (keeps duplicates) = " + every.size()); // 6
        System.out.println("List                         = " + every);       // 顺序和 raw 一样

        // ---------- 2) Set：同样的 add，重复的会被丢掉 ----------
        // HashSet 用 hashCode 判重；不保证打印顺序等于加入顺序
        Set<String> unique = new HashSet<String>();
        for (int i = 0; i < raw.length; i++) {
            unique.add(raw[i]); // 第二次 add("apple") 会被忽略，size 不增加
        }
        System.out.println("Set size (unique only)       = " + unique.size()); // 3
        System.out.println("Set                          = " + unique);

        // ---------- 3) Map：键是水果名，值是次数 ----------
        // get 找不到时返回 null，不是 0。第一次见到要先当成 0 再加 1。
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < raw.length; i++) {
            String fruit = raw[i];
            Integer soFar = counts.get(fruit); // Integer 是包装类型，可能为 null
            if (soFar == null) {
                soFar = 0;
            }
            counts.put(fruit, soFar + 1); // put 同一键会覆盖旧值
        }
        System.out.println("Map counts                   = " + counts);
        System.out.println("apple count                  = " + counts.get("apple")); // 按键查找，不用再扫一遍

        System.out.println();
        System.out.println("结论：要顺序/重复用 List；只要唯一用 Set；按名字计数用 Map。");
        System.out.println("ArrayList 日常默认；HashSet / HashMap 查找快，但不保证顺序。");
    }
}
