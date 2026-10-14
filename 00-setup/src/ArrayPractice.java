import java.util.Arrays;

/**
 * 数组：声明创建 / 遍历处理 / 工具类 / 二维 / 场景对照。
 * 概念卡片：notes/array-basics.md
 *
 * 编译：javac -encoding UTF-8 -d out src\ArrayPractice.java
 * 运行：java -cp out ArrayPractice
 */
public class ArrayPractice {
    public static void main(String[] args) {
        demoDeclareCreate();
        System.out.println();
        demoProcess();
        System.out.println();
        demoArraysUtil();
        System.out.println();
        demoTwoDim();
        System.out.println();
        demoScenes();
    }

    /** 分类：几种创建方式；默认值。 */
    private static void demoDeclareCreate() {
        System.out.println("== 1. 声明与创建 ==");

        int[] byLength = new int[3];          // 默认 0,0,0
        int[] byLiteral = {10, 20, 30};       // 写死初值
        String[] names = new String[2];       // 引用类型默认 null

        System.out.println("byLength  -> " + Arrays.toString(byLength));
        System.out.println("byLiteral -> " + Arrays.toString(byLiteral));
        System.out.println("names[0]  -> " + names[0]); // null
        System.out.println("length    -> " + byLiteral.length); // 字段，不是 length()
    }

    /** 处理：读写、遍历、求和、防越界。 */
    private static void demoProcess() {
        System.out.println("== 2. 处理数组 ==");

        int[] scores = {80, 95, 70};
        scores[1] = 98; // 按下标改

        int sum = 0;
        for (int i = 0; i < scores.length; i++) { // 合法下标：0 .. length-1
            sum += scores[i];
        }
        System.out.println("for index sum=" + sum);

        int max = scores[0];
        for (int s : scores) { // 增强 for：只读遍历很方便
            if (s > max) {
                max = s;
            }
        }
        System.out.println("for-each max=" + max);

        // scores[3] = 1; // 越界 → ArrayIndexOutOfBoundsException
    }

    /** 场景：排序、拷贝、内容比较——交给 Arrays。 */
    private static void demoArraysUtil() {
        System.out.println("== 3. Arrays 工具 ==");

        int[] a = {3, 1, 2};
        int[] copy = Arrays.copyOf(a, a.length); // 真拷贝，不是共享引用
        Arrays.sort(copy);
        System.out.println("sorted copy -> " + Arrays.toString(copy));
        System.out.println("original a  -> " + Arrays.toString(a));
        System.out.println("equals?     -> " + Arrays.equals(a, new int[]{3, 1, 2}));
    }

    /** 分类：二维 = 数组的数组（小表格）。 */
    private static void demoTwoDim() {
        System.out.println("== 4. 二维数组 ==");

        // 3 行成绩：语文、数学
        int[][] table = {
                {90, 80},
                {70, 85},
                {88, 92}
        };
        System.out.println("rows=" + table.length + ", row0 cols=" + table[0].length);
        System.out.println("student0 math=" + table[0][1]);

        for (int r = 0; r < table.length; r++) {
            System.out.println("row" + r + " -> " + Arrays.toString(table[r]));
        }
    }

    /** 使用场景速记 + 引用赋值陷阱。 */
    private static void demoScenes() {
        System.out.println("== 5. 场景与陷阱 ==");

        // 场景：一周温度（长度固定）
        double[] weekTemp = {22.5, 23.0, 21.0, 24.5, 25.0, 26.0, 22.0};
        System.out.println("Fri temp=" + weekTemp[4]);

        // 陷阱：= 只复制引用，两边指向同一数组
        int[] one = {1, 2, 3};
        int[] alias = one;
        alias[0] = 99;
        System.out.println("one after alias edit -> " + Arrays.toString(one)); // [99, 2, 3]

        System.out.println("fixed length data     -> array");
        System.out.println("need grow/shrink often-> ArrayList (stage 02)");
    }
}
