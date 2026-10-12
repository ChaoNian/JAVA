/**
 * 命名约定对照（不是语法强制，是可读性约定）。
 * 概念卡片：notes/variable-naming.md
 *
 * 编译：javac -encoding UTF-8 -d out src\NamingRulesDemo.java
 * 运行：java -cp out NamingRulesDemo
 */
public class NamingRulesDemo { // 类名：大驼峰，大写开头

    /** 常量：全大写 + 下划线；通常 static final */
    public static final int MAX_SIZE = 100;

    /** 静态可变：全类共享的计数器，小驼峰 */
    private static int instanceCount = 0;

    /** 实例变量：每个对象一份，小驼峰；一般 private */
    private int itemCount;

    public NamingRulesDemo(int itemCount) { // 参数：小驼峰
        this.itemCount = itemCount;
        instanceCount++;
    }

    public void printSummary(String label) { // 参数 label
        int displayCount = this.itemCount;   // 局部变量：小驼峰，用前必赋值
        System.out.println(label + ": itemCount=" + displayCount
                + ", instanceCount=" + instanceCount
                + ", MAX_SIZE=" + MAX_SIZE);
    }

    public static void main(String[] args) {
        NamingRulesDemo a = new NamingRulesDemo(3);
        NamingRulesDemo b = new NamingRulesDemo(5);
        a.printSummary("objectA");
        b.printSummary("objectB");

        // 错误示范（不要学，仅注释说明）：
        // int 2count = 1;     // 不能以数字开头
        // int class = 1;      // 不能用关键字
        // int Count = 1;      // 能编译，但局部/字段习惯小写开头，易和类名混淆
    }
}
