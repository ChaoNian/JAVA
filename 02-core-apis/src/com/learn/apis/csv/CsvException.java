package com.learn.apis.csv;

/**
 * 读 CSV 失败时抛出的 checked 异常。调用方必须 catch 或 throws（对照 Ex01）。
 * 继承 Exception，不是 RuntimeException：空文件、文件不存在都属于「必须面对」的失败。
 */
public class CsvException extends Exception {
    public CsvException(String message) {
        super(message); // e.getMessage() 才能读到这句话
    }

    /** cause 是底层真正的 IOException，打印堆栈时能看到原始原因。 */
    public CsvException(String message, Throwable cause) {
        super(message, cause);
    }
}
