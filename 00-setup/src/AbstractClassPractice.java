/**
 * 什么时候用抽象类。
 *
 * 1. 姓名和发薪单格式是共性，抽到 Staff。没有人只是「一个员工」，所以 Staff 不能 new。
 * 2. computePay 没有统一公式，子类必须自己写。
 * 3. closeMonth 是 final 模板：校验 -> 算薪 -> 记账。子类只能填空，不能改顺序。
 * 4. Exportable 表示「能导出」。月薪员工是一种员工，同时又能导出；日报不是员工，但也能导出。
 *
 * 编译：javac -encoding UTF-8 -d out src\AbstractClassPractice.java
 * 运行：java -cp out AbstractClassPractice
 */
public class AbstractClassPractice {
    public static void main(String[] args) {
        demoSharedSkeleton();
        System.out.println();
        demoInterface();
    }

    /** 步骤 1–3：共享状态 + 强制算薪 + 模板方法。 */
    private static void demoSharedSkeleton() {
        System.out.println("== 抽象类：员工发薪 ==");
        // new Staff("无名"); // 编译失败：Staff 是抽象类
        Staff[] people = {
                new SalariedStaff("张三", 8000),
                new HourlyStaff("李四", 100, 160)
        };
        for (int i = 0; i < people.length; i++) {
            System.out.println(people[i].closeMonth());
        }
    }

    /** 步骤 4：接口表示能力。参数类型是 Exportable，不是 Staff。 */
    private static void demoInterface() {
        System.out.println("== 接口：能导出 ==");
        System.out.println(exportOf(new SalariedStaff("张三", 8000)));
        System.out.println(exportOf(new DailyReport("周三汇总")));
        // exportOf(new HourlyStaff("李四", 100, 160)); // 编译失败：小时工没有实现 Exportable
    }

    private static String exportOf(Exportable item) {
        return item.export();
    }
}

/**
 * 员工是抽象概念。姓名是共享状态，closeMonth 是共享骨架。
 */
abstract class Staff {
    private final String name;

    Staff(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name required");
        }
        this.name = name;
    }

    /** 顺序固定：校验、算薪、记账。子类不能重写这个方法。 */
    public final String closeMonth() {
        checkReady();
        int pay = computePay();
        record(pay);
        return name + " 实发=" + pay;
    }

    /** 共性步骤：父类已经写好。 */
    protected void checkReady() {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name required");
        }
    }

    /** 没有统一公式，子类必须实现。漏写则编译失败。 */
    protected abstract int computePay();

    /** 钩子：默认不记账。需要时子类再重写。 */
    protected void record(int pay) {
    }

    public String getName() {
        return name;
    }
}

/** 月薪员工是一种员工，并且能导出。 */
class SalariedStaff extends Staff implements Exportable {
    private final int salary;

    SalariedStaff(String name, int salary) {
        super(name);
        if (salary < 0) {
            throw new IllegalArgumentException("salary >= 0");
        }
        this.salary = salary;
    }

    @Override
    protected int computePay() {
        return salary;
    }

    @Override
    public String export() {
        return getName() + ",salary," + salary;
    }
}

/** 小时工是一种员工。record 选择记账，没有实现 Exportable。 */
class HourlyStaff extends Staff {
    private final int hourly;
    private final int hours;

    HourlyStaff(String name, int hourly, int hours) {
        super(name);
        if (hourly < 0 || hours < 0) {
            throw new IllegalArgumentException("hourly and hours >= 0");
        }
        this.hourly = hourly;
        this.hours = hours;
    }

    @Override
    protected int computePay() {
        return hourly * hours;
    }

    @Override
    protected void record(int pay) {
        System.out.println("  记账 " + getName() + " " + pay);
    }
}

/** 能力：能导出成一行文本。谁实现谁就能交给 exportOf。 */
interface Exportable {
    String export();
}

/** 日报不是员工，只是具备导出能力。 */
class DailyReport implements Exportable {
    private final String title;

    DailyReport(String title) {
        this.title = title;
    }

    @Override
    public String export() {
        return "report," + title;
    }
}
