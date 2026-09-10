package com.learn.jvm.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志概念演示（JDK 自带 JUL，不额外加依赖）。
 * <p>
 * 生产更常见：业务代码依赖 SLF4J API（{@code LoggerFactory.getLogger}），
 * 运行时再接 Logback / Log4j2。本阶段 README 要求依赖只放 JUnit，
 * 所以这里用 JUL 练「级别 / 占位 / 不要用异常当流程」的习惯。
 */
public final class AuditLogDemo {
    private static final Logger LOG = Logger.getLogger(AuditLogDemo.class.getName());

    public static void main(String[] args) {
        demoLevels();
        demoException();
    }

    static void demoLevels() {
        LOG.info("order paid id=ORD-1");           // 正常业务轨迹
        LOG.warning("retry shipping id=ORD-1");    // 可恢复异常信号
        LOG.fine("sku map size=3");                // 默认往往看不到；调试时再开
    }

    static void demoException() {
        try {
            explode();
        } catch (IllegalStateException e) {
            // 第二个参数传 Throwable：堆栈会进日志，比只 e.getMessage() 有用
            LOG.log(Level.SEVERE, "checkout failed id=ORD-9", e);
        }
    }

    private static void explode() {
        throw new IllegalStateException("illegal transition: Shipped -> Paid");
    }
}
