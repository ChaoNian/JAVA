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
            super(message); // 把说明交给父类，后面 e.getMessage() 才能读到
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
     * throws 只写 checked。unchecked 也可以抛，但不必出现在方法签名里。
     */
    static int parsePositive(String raw) throws BlankInputException {
        if (raw == null || raw.isBlank()) {
            throw new BlankInputException("input is blank");
        }
        int n = Integer.parseInt(raw); // 不是数字时抛 NumberFormatException（unchecked）
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

    /** 正常路径：try 仍要写，因为 parsePositive 声明了 throws BlankInputException。 */
    static void tryOk() {
        try {
            System.out.println("parsePositive(\"12\") = " + parsePositive("12"));
        } catch (BlankInputException e) {
            System.out.println("unexpected: " + e.getMessage());
        }
    }

    /** 空串：编译器逼你 catch（或继续 throws），否则这一行编不过。 */
    static void tryBlank() {
        try {
            parsePositive("  ");
        } catch (BlankInputException e) {
            System.out.println("blank -> caught " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * 负数：即使不 catch NegativeNumberException 也能编译。
     * 这里写上 catch，只是为了打印并让 main 继续跑后面的例子。
     */
    static void tryNegative() {
        try {
            parsePositive("-3");
        } catch (BlankInputException e) {
            System.out.println("unexpected blank: " + e.getMessage());
        } catch (NegativeNumberException e) {
            System.out.println("negative -> caught " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /** "abc" 过得了空串检查，死在 Integer.parseInt。这也是 unchecked。 */
    static void tryNotANumber() {
        try {
            parsePositive("abc");
        } catch (BlankInputException e) {
            System.out.println("unexpected blank: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("not a number -> caught " + e.getClass().getSimpleName());
        }
    }
}
