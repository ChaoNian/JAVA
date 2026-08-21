package com.learn.basics.bank;

/**
 * 入口：只负责创建对象、调用、打印。业务规则在 Account / Bank / Money 里。
 *
 * 先 cd 到 01-language-basics（不要停在 E:\JAVA\JAVA）。
 * PowerShell 不会展开 *.java，要用 Get-ChildItem：
 *   $src = Get-ChildItem src\com\learn\basics\bank\*.java
 *   javac -encoding UTF-8 -d out $src.FullName
 *   java -cp out com.learn.basics.bank.BankApp
 * 或直接：.\compile-and-run.ps1
 */
public class BankApp {
    public static void main(String[] args) {
        Bank bank = new Bank("LearnBank", 8);
        // 变量类型写 Account 也行：父类引用可以指向子类对象
        bank.open(new CheckingAccount("C-001", "Ada", Money.ofYuan(1000)));
        bank.open(new SavingsAccount("S-001", "Bob", Money.ofYuan(2000), 250)); // 250 = 年利率 2.50%

        // Ada 活期转 200 给 Bob：活期实际扣 201（含 1 元手续费），储蓄到账 200
        bank.transfer("C-001", "S-001", Money.ofYuan(200));

        Account[] all = bank.accounts();
        for (int i = 0; i < all.length; i++) {
            Account account = all[i];
            System.out.println(account); // 内部会调子类的 typeName()，打印 Checking 或 Savings
            // instanceof：是不是「能计息」。活期不是，储蓄是。
            if (account instanceof InterestBearing) {
                InterestBearing interestBearing = (InterestBearing) account;
                Money interest = interestBearing.accrueMonthlyInterest();
                System.out.println("  monthly interest = " + interest);
                System.out.println("  after interest   = " + account);
            }
        }

        // 值对象：两个 ofYuan(10) 不是同一个对象（== false），但内容相等（equals true）
        Money ten = Money.ofYuan(10);
        Money alsoTen = Money.ofYuan(10);
        System.out.println("Money ==      " + (ten == alsoTen));
        System.out.println("Money equals  " + ten.equals(alsoTen));
        System.out.println("hashCode same " + (ten.hashCode() == alsoTen.hashCode()));
    }
}
