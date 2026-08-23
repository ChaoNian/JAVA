package com.learn.apis;

/**
 * checked vs unchecked，以及为什么要自定义异常。
 *
 * - checked：继承 Exception（不含 RuntimeException）。编译器强迫你 catch 或 throws。
 * - unchecked：继承 RuntimeException。编译器不强迫，表示「调用方本不该让它发生」。
 * - 自定义异常：给失败一个名字，而不是到处 throw new Exception("出错了")。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\apis\Ex01CheckedVsUnchecked.java
 * 运行：java -cp out com.learn.apis.Ex01CheckedVsUnchecked
 */
public class Ex01CheckedVsUnchecked {

    /**
     * 自定义 checked 异常：空输入是「调用方必须面对」的情况，
     * 所以走 Exception，方法要写 throws，调用处必须处理。
     */
    static class BlankInputException extends Exception {
        BlankInputException(String message) {
            super(message);
        }
    }

    /**
     * 自定义 unchecked 异常：负数是编程错误（不该把负数量传来），
     * 所以走 RuntimeException，调用处可以不写 catch。
     */
    static class NegativeNumberException extends RuntimeException {
        NegativeNumberException(String message) {
            super(message);
        }
    }

    /**
     * throws BlankInputException：这个 checked 异常会传到调用方。
     * NumberFormatException、NegativeNumberException 都是 unchecked，不必写进 throws。
     */
    static int parsePositive(String raw) throws BlankInputException {
        if (raw == null || raw.isBlank()) {
            throw new BlankInputException("input is blank");
        }
        int n = Integer.parseInt(raw);
        if (n < 0) {
            throw new NegativeNumberException("must be >= 0, got " + n);
        }
        return n;
    }

    public static void main(String[] args) {
        tryOk();
        tryBlank();
        tryNegative();
        tryNotANumber();

        System.out.println();
        System.out.println("结论：checked 必须处理；unchecked 表示编程错误。");
        System.out.println("自定义异常用来区分「空输入」和「负数」，不要全用 Exception。");
    }

    static void tryOk() {
        try {
            System.out.println("parsePositive(\"12\") = " + parsePositive("12"));
        } catch (BlankInputException e) {
            System.out.println("unexpected: " + e.getMessage());
        }
    }

    static void tryBlank() {
        try {
            parsePositive("  ");
        } catch (BlankInputException e) {
            // 没有这个 catch（或 throws），上面那一行编译不过
            System.out.println("blank -> caught " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    static void tryNegative() {
        try {
            parsePositive("-3");
        } catch (BlankInputException e) {
            System.out.println("unexpected blank: " + e.getMessage());
        } catch (NegativeNumberException e) {
            // 这个 catch 不是编译器逼的；写上是为了演示，并让程序继续往下跑
            System.out.println("negative -> caught " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    static void tryNotANumber() {
        try {
            parsePositive("abc");
        } catch (BlankInputException e) {
            System.out.println("unexpected blank: " + e.getMessage());
        } catch (NumberFormatException e) {
            // JDK 自带的 unchecked：Integer.parseInt 失败时抛出
            System.out.println("not a number -> caught " + e.getClass().getSimpleName());
        }
    }
}
