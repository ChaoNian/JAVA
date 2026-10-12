/**
 * 静态计数器：统计「一共 new 了多少个本类对象」。
 * numInstances 只有一份，所有对象共享——所以适合做全局计数。
 *
 * 编译：javac -encoding UTF-8 -d out src\InstanceCounter.java
 * 运行：java -cp out InstanceCounter
 */
public class InstanceCounter {
    /** 类变量：跟类走，不跟某个对象走；private 仅本类可直接改。 */
    private static int numInstances = 0;

    /** 只读查询当前计数；protected：同包或子类可调用。 */
    protected static int getCount() {
        return numInstances;
    }

    /** 计数 +1；private：外界不能随便调用，只能由构造器触发。 */
    private static void addInstance() {
        numInstances++;
    }

    /**
     * 每次 new InstanceCounter() 都会跑构造器，
     * 于是每造一个对象，共享计数就加一。
     */
    InstanceCounter() {
        InstanceCounter.addInstance(); // 也可用简写 addInstance()
    }

    public static void main(String[] arguments) {
        // 此时还没 new，计数应为 0
        System.out.println("Starting with " + InstanceCounter.getCount() + " instances");

        // 循环造 500 个对象；不必保存引用，目的只是触发构造器里的 +1
        for (int i = 0; i < 500; ++i) {
            new InstanceCounter();
        }

        // 共享的那一份计数变成 500
        System.out.println("Created " + InstanceCounter.getCount() + " instances");
    }
}
