/**
 * 成员变量访问：同一类里可以访问 private 字段。
 *
 * 编译：javac -encoding UTF-8 -d out src\RunoobTest1.java
 * 运行：java -cp out RunoobTest1
 */
public class RunoobTest1 {
    private int a; // 私有成员变量 a
    public String b = "Hello"; // 公有成员变量 b

    public static void main(String[] args) {
        RunoobTest1 obj = new RunoobTest1(); // 必须造本类对象，不是 RunoobTest

        obj.a = 10; // 同一类内部可访问 private
        System.out.println("a = " + obj.a);

        obj.b = "World"; // 同一类内部可访问 public
        System.out.println("b = " + obj.b);
    }
}
