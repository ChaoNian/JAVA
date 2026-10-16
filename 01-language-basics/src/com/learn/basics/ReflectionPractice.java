package com.learn.basics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射：运行时通过 Class 创建对象、读写字段、调用方法。
 * 概念说明见 notes/reflection-basics.md
 *
 * 编译（在 01-language-basics 目录，仅本文件即可）：
 * javac -encoding UTF-8 -d out src\com\learn\basics\ReflectionPractice.java
 * 运行：
 * java -cp out com.learn.basics.ReflectionPractice
 */
public class ReflectionPractice {

    public static void main(String[] args) throws Exception {
        demoGetClass();
        System.out.println();
        demoConstructor();
        System.out.println();
        demoField();
        System.out.println();
        demoMethod();
    }

    /** 三种常见拿 Class 的方式（本例用 .class）。 */
    private static void demoGetClass() {
        System.out.println("== 1. Class 对象 ==");
        Class<?> fromLiteral = Person.class;
        Person p = new Person("Ada", 20);
        Class<?> fromInstance = p.getClass();
        System.out.println("Person.class = " + fromLiteral.getName());
        System.out.println("same Class?  = " + (fromLiteral == fromInstance));
    }

    /** getConstructor 只找 public 构造器。 */
    private static void demoConstructor() throws Exception {
        System.out.println("== 2. Constructor ==");
        Class<?> clazz = Person.class;
        Constructor<?> ctor = clazz.getConstructor(String.class, int.class);
        Object person = ctor.newInstance("John", 30);
        System.out.println("created      = " + person);
    }

    /** getDeclaredField 包含 private；setAccessible 才能读写 private。 */
    private static void demoField() throws Exception {
        System.out.println("== 3. Field ==");
        Class<?> clazz = Person.class;
        Object person = new Person("John", 30);

        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        System.out.println("name before  = " + nameField.get(person));

        nameField.set(person, "Doe");
        System.out.println("name after   = " + nameField.get(person));
        System.out.println("toString     = " + person);
    }

    /** getMethod 找 public 方法（含继承的 public）。 */
    private static void demoMethod() throws Exception {
        System.out.println("== 4. Method ==");
        Class<?> clazz = Person.class;
        Object person = new Person("Doe", 30);

        Method greet = clazz.getMethod("greet", String.class);
        greet.invoke(person, "World");

        Method hidden = clazz.getDeclaredMethod("secretCode");
        hidden.setAccessible(true);
        System.out.println("secretCode   = " + hidden.invoke(person));
    }
}

/** 被反射操作的普通类（非 public 也可，同文件内可见）。 */
class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void greet(String message) {
        System.out.println(name + " says: " + message);
    }

    private String secretCode() {
        return name + "-" + age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
