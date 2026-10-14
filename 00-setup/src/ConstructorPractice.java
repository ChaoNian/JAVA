/**
 * 构造方法入门：无参 / 有参 / 重载 / this() / 校验。
 * 概念卡片：notes/constructor-basics.md
 *
 * 编译：javac -encoding UTF-8 -d out src\ConstructorPractice.java
 * 运行：java -cp out ConstructorPractice
 */
public class ConstructorPractice {
    public static void main(String[] args) {
        demoNoArgAndArgs();
        System.out.println();
        demoOverloadAndThis();
        System.out.println();
        demoValidation();
    }

    /** 场景：用不同构造造对象。 */
    private static void demoNoArgAndArgs() {
        System.out.println("== 1. 无参 / 有参 ==");

        Student a = new Student();              // 无参：默认名字
        Student b = new Student("Ada", 18);     // 有参：创建时就带齐数据

        System.out.println(a.describe());
        System.out.println(b.describe());
    }

    /** 场景：多种创建方式；this() 复用初始化逻辑。 */
    private static void demoOverloadAndThis() {
        System.out.println("== 2. 重载 + this() ==");

        Student c = new Student("Bob");         // 只给名字，年龄走默认
        System.out.println(c.describe());
    }

    /** 场景：非法数据在构造阶段直接失败，别让坏对象流入后面逻辑。 */
    private static void demoValidation() {
        System.out.println("== 3. 构造里校验 ==");
        try {
            new Student("", -1);
            System.out.println("SHOULD NOT REACH");
        } catch (IllegalArgumentException e) {
            System.out.println("caught: " + e.getMessage());
        }
    }
}

/** 练习用的小模型：专门展示构造器写法。 */
class Student {
    private String name;
    private int age;

    /** 无参构造：给「还能 new Student()」留门。 */
    Student() {
        this("anonymous", 0); // 委托给全参构造，避免重复赋值代码
    }

    /** 只传名字：年龄默认 0。 */
    Student(String name) {
        this(name, 0);
    }

    /**
     * 全参构造：真正做校验和赋值的地方。
     * 名字必须和类名相同；不能写返回类型。
     */
    Student(String name, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name required");
        }
        if (age < 0) {
            throw new IllegalArgumentException("age must be >= 0");
        }
        this.name = name; // this.name = 字段；右边 name = 参数
        this.age = age;
    }

    String describe() {
        return "Student{name=" + name + ", age=" + age + "}";
    }
}
