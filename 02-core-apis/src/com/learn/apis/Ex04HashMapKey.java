package com.learn.apis;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap 的 key 必须稳定：equals 和 hashCode 在放进去之后不能变。
 *
 * HashMap 用 hashCode 找桶，再用 equals 确认是不是同一个键。
 * 放进去之后改了 id，hashCode 变了，再 get 会去另一个桶里找，于是「丢了」。
 * 对照 01 的 Money：金额不可变，所以能当 key。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\apis\Ex04HashMapKey.java
 * 运行：java -cp out com.learn.apis.Ex04HashMapKey
 */
public class Ex04HashMapKey {

    /** 故意可变的键。真正项目里不要这样写。 */
    static class MutableKey {
        int id; // 不是 final：一会儿会被改掉，这是本例要演示的坑

        MutableKey(int id) {
            this.id = id;
        }

        /** 两个 MutableKey 是否「同一个键」，只看 id，不看是不是同一个对象。 */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MutableKey)) {
                return false;
            }
            MutableKey other = (MutableKey) o;
            return id == other.id;
        }

        /**
         * 必须和 equals 用同一组字段。HashMap 先靠它选桶。
         * id 一改，hashCode 跟着变，条目还在旧桶里，新查找走新桶。
         */
        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return "Key(" + id + ")";
        }
    }

    public static void main(String[] args) {
        Map<MutableKey, String> map = new HashMap<MutableKey, String>();
        MutableKey key = new MutableKey(1);
        map.put(key, "Ada"); // 按当时的 hashCode()==1 放进某个桶

        System.out.println("put Key(1) -> Ada");
        System.out.println("get same object, id still 1 -> " + map.get(key)); // 还能找到

        // 对象还是 map 里那个，只是字段变了。put 时的桶不会跟着搬家。
        key.id = 99;
        System.out.println("changed key.id to 99");
        System.out.println("get same object now         -> " + map.get(key));
        // 现在 hashCode 是 99，去 99 号桶找，那里没有；所以常常是 null

        System.out.println("get new Key(1)              -> " + map.get(new MutableKey(1)));
        // 新对象 id=1，去 1 号桶找。旧条目还在，但 equals 时它的 id 已经是 99，对不上

        System.out.println("map.size() still            -> " + map.size());
        // size 仍是 1：值没删，只是用 get 找不到。打印能看到 {Key(99)=Ada}
        System.out.println("map contents                -> " + map);

        System.out.println();
        System.out.println("结论：当 key 的对象要不可变。equals 和 hashCode 必须一起覆写，且依赖的字段放进 Map 后不能改。");
    }
}
