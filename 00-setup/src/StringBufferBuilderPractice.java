/**
 * StringBuilder（首选）与 StringBuffer（线程安全）对照练习。
 * 概念卡片：notes/stringbuffer-builder.md
 *
 * 编译：javac -encoding UTF-8 -d out src\StringBufferBuilderPractice.java
 * 运行：java -cp out StringBufferBuilderPractice
 */
public class StringBufferBuilderPractice {
    public static void main(String[] args) {
        demoWhyNotPlusInLoop();
        System.out.println();
        demoBuilderBasics();
        System.out.println();
        demoBufferSameApi();
        System.out.println();
        demoWhenToPick();
    }

    /**
     * 场景：循环拼接。
     * String + 每次可能 new 新对象；Builder 在同一块缓冲上改。
     */
    private static void demoWhyNotPlusInLoop() {
        System.out.println("== 1. 为何循环里少用 String + ==");

        String slowStyle = "";
        for (int i = 1; i <= 5; i++) {
            slowStyle = slowStyle + "x" + i; // 能跑，次数多了就浪费
        }
        System.out.println("String +     -> " + slowStyle);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            sb.append('x').append(i);
        }
        System.out.println("StringBuilder-> " + sb.toString());
    }

    /** 场景：追加、插入、删除、反转、产出最终 String。 */
    private static void demoBuilderBasics() {
        System.out.println("== 2. StringBuilder 常用操作 ==");

        StringBuilder sb = new StringBuilder("Java");
        sb.append("21");                 // Java21
        sb.insert(4, " ");               // Java 21
        sb.delete(4, 5);                 // 删掉空格 → Java21
        sb.replace(0, 4, "JDK");         // JDK21
        System.out.println("after edit  -> " + sb);

        sb.reverse();
        System.out.println("reverse     -> " + sb);
        sb.reverse();                    // 转回来方便看 length

        System.out.println("length=" + sb.length() + ", capacity=" + sb.capacity());

        String frozen = sb.toString();   // 变成不可变 String，交给其他 API
        System.out.println("toString    -> " + frozen);
    }

    /**
     * 场景：API 与 Builder 几乎相同；差别在「方法带同步」。
     * 单线程练习里看不出快慢，只记：接口一样、用途不同。
     */
    private static void demoBufferSameApi() {
        System.out.println("== 3. StringBuffer（线程安全版） ==");

        StringBuffer buf = new StringBuffer();
        buf.append("Hello").append(' ').append("Buffer");
        buf.insert(5, ',');
        System.out.println(buf.toString()); // Hello, Buffer

        // 多线程同时 append 同一 StringBuffer 时更安全；
        // 单线程日常请优先 StringBuilder。
    }

    /** 场景速记：怎么选。 */
    private static void demoWhenToPick() {
        System.out.println("== 4. 怎么选 ==");
        System.out.println("短文本、不再改     -> String");
        System.out.println("单线程反复拼接修改 -> StringBuilder（默认选这个）");
        System.out.println("多线程共享同一缓冲 -> StringBuffer");
    }
}
