package com.learn.jvm.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆内存耗尽演示：不断分配大数组直到 {@link OutOfMemoryError}。
 * <p>
 * 必须用很小的堆观察，例如：
 * <pre>
 *   java -Xmx16m -cp target/classes com.learn.jvm.jvm.OomDemo
 * </pre>
 * 排查直觉：OOM 在堆；StackOverflow 在栈。别一上来就调 GC 玄学参数。
 */
public final class OomDemo {
    public static void main(String[] args) {
        List<byte[]> retained = new ArrayList<>();
        int chunk = 1024 * 1024; // 每次约 1MB
        try {
            for (int i = 1; ; i++) {
                retained.add(new byte[chunk]);
                if (i % 4 == 0) {
                    System.out.println("allocated ~" + i + " MB (retained)");
                }
            }
        } catch (OutOfMemoryError oom) {
            // 故意 catch 一次，打印提示后重新抛出，避免「吞掉 Error」的坏习惯被当成推荐做法
            System.err.println("caught OutOfMemoryError after retaining " + retained.size() + " chunks");
            System.err.println("tip: re-run with -Xmx16m and note which line allocates");
            throw oom;
        }
    }
}
