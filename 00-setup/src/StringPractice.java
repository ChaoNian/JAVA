/**
 * String 分类练习：创建 / 查询 / 比较 / 查找 / 变换 / 拆分 / 不可变 / 拼接。
 * 概念卡片：notes/string-basics.md
 *
 * 编译：javac -encoding UTF-8 -d out src\StringPractice.java
 * 运行：java -cp out StringPractice
 */
public class StringPractice {
    public static void main(String[] args) {
        demoCreate();
        System.out.println();
        demoInspect();
        System.out.println();
        demoCompare();
        System.out.println();
        demoSearch();
        System.out.println();
        demoTransform();
        System.out.println();
        demoSplitJoin();
        System.out.println();
        demoImmutable();
        System.out.println();
        demoBuilder();
    }

    /** 场景：拿到一段文本——字面量最常用。 */
    private static void demoCreate() {
        System.out.println("== 1. 创建 ==");
        String a = "Java";                 // 字面量，进入字符串常量池
        String b = new String("Java");     // 堆上新对象（练习用，日常少写）
        String c = String.valueOf(21);     // 其它类型 → 字符串 "21"
        System.out.println(a + " / " + b + " / JDK" + c);
    }

    /** 场景：表单校验——有没有字、多长。 */
    private static void demoInspect() {
        System.out.println("== 2. 查信息 ==");
        String name = "Ada";
        String blank = "   ";
        System.out.println("length=" + name.length());
        System.out.println("isEmpty=\"\"? " + "".isEmpty());
        // JDK 8：用 trim().isEmpty() 判断「去掉空白后是否为空」
        // JDK 11+ 可写 blank.isBlank()
        System.out.println("blank after trim empty? " + blank.trim().isEmpty());
        System.out.println("charAt(0)=" + name.charAt(0));
    }

    /** 场景：登录、状态码——比的是内容，不是是不是同一个对象。 */
    private static void demoCompare() {
        System.out.println("== 3. 比较 ==");
        String x = "java";
        String y = new String("java");
        System.out.println("==        -> " + (x == y));          // 多半 false：不同对象
        System.out.println("equals    -> " + x.equals(y));       // true：内容相同
        System.out.println("ignoreCase-> " + "Java".equalsIgnoreCase("java"));
    }

    /** 场景：搜关键词、判断文件后缀。 */
    private static void demoSearch() {
        System.out.println("== 4. 查找 ==");
        String path = "photo.JPG";
        System.out.println("contains photo? " + path.toLowerCase().contains("photo"));
        System.out.println("endsWith .jpg?  " + path.toLowerCase().endsWith(".jpg"));
        System.out.println("indexOf '.' = " + path.indexOf('.'));
    }

    /** 场景：清洗输入、截取、替换。 */
    private static void demoTransform() {
        System.out.println("== 5. 截取变换 ==");
        String raw = "  hello JAVA  ";
        String clean = raw.trim();                     // 去首尾空白（JDK 11+ 也可用 strip）
        System.out.println("trim       -> [" + clean + "]");
        System.out.println("substring  -> " + clean.substring(0, 5)); // hello（不含结束下标）
        System.out.println("toLower    -> " + clean.toLowerCase());
        System.out.println("replace    -> " + clean.replace("JAVA", "Java"));
    }

    /** 场景：CSV 一行拆字段；再拼回去。 */
    private static void demoSplitJoin() {
        System.out.println("== 6. 拆分合并 ==");
        String line = "A,上海,3";
        String[] parts = line.split(",");
        System.out.println("sku=" + parts[0] + " city=" + parts[1] + " qty=" + parts[2]);
        String joined = String.join("-", parts);
        System.out.println("join -> " + joined);
    }

    /** 场景：以为改了原字符串——其实每次都返回新对象。 */
    private static void demoImmutable() {
        System.out.println("== 7. 不可变 ==");
        String s = "java";
        s.toUpperCase();                 // 返回值丢掉了，s 仍是 "java"
        System.out.println("忘记接收返回值: " + s);
        s = s.toUpperCase();             // 必须赋回变量
        System.out.println("赋回之后:     " + s);
    }

    /** 场景：循环拼很多段——用 StringBuilder。 */
    private static void demoBuilder() {
        System.out.println("== 8. 大量拼接 ==");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i > 1) {
                sb.append(',');
            }
            sb.append("item").append(i);
        }
        System.out.println(sb.toString()); // item1,item2,item3,item4,item5
    }
}
