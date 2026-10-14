/**
 * 多态：同一个调用，运行时按真实对象选方法体。
 *
 * 1. 重写：父类引用指向子类，pay() 走子类
 * 2. 抽象类：父类只规定 send()，子类必须给出实现
 * 3. 接口：参数写成 Payment，调用处不用 if-else
 * 4. 对照：字段看左边的类型，实例方法才动态绑定
 *
 * 编译：javac -encoding UTF-8 -d out src\PolymorphismPractice.java
 * 运行：java -cp out PolymorphismPractice
 */
public class PolymorphismPractice {
    public static void main(String[] args) {
        demoOverride();
        System.out.println();
        demoAbstract();
        System.out.println();
        demoInterface();
        System.out.println();
        demoFieldVsMethod();
    }

    /** 条件：继承 + 重写 + 父类引用指向子类对象。 */
    private static void demoOverride() {
        System.out.println("== 1. 重写 ==");
        Employee[] team = {
                new Employee("Ada", 8000),
                new CommissionEmployee("Bob", 8000, 10),
                new HourlyEmployee("Cai", 100, 160)
        };
        for (int i = 0; i < team.length; i++) {
            Employee person = team[i];
            // slip() 写在父类里，里面的 pay() 仍按真实对象走
            System.out.println(person.slip());
        }
        // person.hourlyWage(); // 编译失败：左边是 Employee，没有这个方法
    }

    /** 抽象类：Animal 那种「必须实现」的方法。这里用通知渠道。 */
    private static void demoAbstract() {
        System.out.println("== 2. 抽象类 ==");
        Channel[] channels = {
                new EmailChannel(),
                new SmsChannel()
        };
        for (int i = 0; i < channels.length; i++) {
            channels[i].send("订单已支付");
        }
    }

    /** 接口：checkout 只依赖 Payment。换支付宝或银行卡，方法本身不用改。 */
    private static void demoInterface() {
        System.out.println("== 3. 接口 ==");
        java.util.List<Payment> ways = new java.util.ArrayList<Payment>();
        ways.add(new Alipay());
        ways.add(new BankCard());
        for (int i = 0; i < ways.size(); i++) {
            checkout(ways.get(i), 20);
        }
    }

    private static void checkout(Payment payment, int yuan) {
        payment.pay(yuan);
    }

    /**
     * 字段不参与多态：编译器按左边的类型选字段。
     * 实例方法才在运行时按右边的对象选。
     */
    private static void demoFieldVsMethod() {
        System.out.println("== 4. 字段 vs 方法 ==");
        LabelParent ref = new LabelChild();
        System.out.println("字段 = " + ref.label);
        System.out.println("方法 = " + ref.text());
    }
}

class Employee {
    private final String name;
    private final int basePay;

    Employee(String name, int basePay) {
        this.name = name;
        this.basePay = basePay;
    }

    int pay() {
        return basePay;
    }

    String slip() {
        return name + " 实发=" + pay();
    }
}

/** 底薪 + 提成。 */
class CommissionEmployee extends Employee {
    private final int ratePercent;

    CommissionEmployee(String name, int basePay, int ratePercent) {
        super(name, basePay);
        this.ratePercent = ratePercent;
    }

    @Override
    int pay() {
        return super.pay() + super.pay() * ratePercent / 100;
    }
}

/** 时薪 × 工时。父类底薪用不到，传 0。 */
class HourlyEmployee extends Employee {
    private final int hourly;
    private final int hours;

    HourlyEmployee(String name, int hourly, int hours) {
        super(name, 0);
        this.hourly = hourly;
        this.hours = hours;
    }

    @Override
    int pay() {
        return hourly * hours;
    }
}

abstract class Channel {
    abstract void send(String message);
}

class EmailChannel extends Channel {
    @Override
    void send(String message) {
        System.out.println("邮件: " + message);
    }
}

class SmsChannel extends Channel {
    @Override
    void send(String message) {
        System.out.println("短信: " + message);
    }
}

interface Payment {
    void pay(int yuan);
}

class Alipay implements Payment {
    @Override
    public void pay(int yuan) {
        System.out.println("支付宝支付 " + yuan);
    }
}

class BankCard implements Payment {
    @Override
    public void pay(int yuan) {
        System.out.println("银行卡支付 " + yuan);
    }
}

class LabelParent {
    String label = "parent-field";

    String text() {
        return "parent-method";
    }
}

class LabelChild extends LabelParent {
    String label = "child-field";

    @Override
    String text() {
        return "child-method";
    }
}
