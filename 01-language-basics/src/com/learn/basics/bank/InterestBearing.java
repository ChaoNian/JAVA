package com.learn.basics.bank;

/**
 * 能计息的账户。本章只用这一次接口：表达「能做什么」，而不是「是什么」。
 *
 * 接口里的方法默认 public abstract，写不写 public 都行。
 * SavingsAccount 写 implements InterestBearing，就必须实现这个方法。
 */
public interface InterestBearing {
    Money accrueMonthlyInterest();
}
