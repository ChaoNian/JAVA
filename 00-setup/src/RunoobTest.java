/**
 * 四种变量对照：实例 / 静态 / 参数 / 局部。
 *
 * 编译：javac -encoding UTF-8 -d out src\RunoobTest.java
 * 运行：java -cp out RunoobTest
 */
public class RunoobTest {
    int instanceVar;          // 实例：每个对象各有一份
    static int staticVar;     // 静态：所有对象共享一份

    void method(int paramVar) {   // 参数：调用 method(5) 时，paramVar 就是 5
        int localVar = 10;        // 局部：只用在这个方法里，必须先赋值

        instanceVar = localVar;   // 把局部值存进「这台对象」的字段
        staticVar = paramVar;     // 把参数值存进「全班共用」的字段
    }

    public static void main(String[] args) {
        RunoobTest a = new RunoobTest();
        RunoobTest b = new RunoobTest();
        a.method(1);
        b.method(2);

        System.out.println("a.instanceVar = " + a.instanceVar); // 10
        System.out.println("b.instanceVar = " + b.instanceVar); // 10
        System.out.println("staticVar = " + staticVar);         // 2（最后一次写入）
    }
}
