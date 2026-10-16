package com.learn.basics;

import com.learn.basics.bank.AccountStatus;

import java.util.EnumSet;

/**
 * 枚举：API、带字段、抽象方法、switch、安全解析。
 * 概念卡片：notes/enum-basics.md
 *
 * 编译（在 01-language-basics 目录）：
 * javac -encoding UTF-8 -d out src\com\learn\basics\EnumPractice.java src\com\learn\basics\bank\*.java
 * 运行：
 * java -cp out com.learn.basics.EnumPractice
 */
public class EnumPractice {
    public static void main(String[] args) {
        demoEnumApi();
        System.out.println();
        demoFieldsAndStableCode();
        System.out.println();
        demoAbstractPerConstant();
        System.out.println();
        demoSwitchAndEnumSet();
        System.out.println();
        demoSafeParse();
    }

    /** values / ordinal / valueOf / name / == */
    private static void demoEnumApi() {
        System.out.println("== 1. Enum API（AccountStatus）==");
        for (AccountStatus s : AccountStatus.values()) {
            System.out.println("  " + s.name() + " ordinal=" + s.ordinal()
                    + " allowsTx=" + s.allowsTransactions());
        }
        AccountStatus parsed = AccountStatus.valueOf("FROZEN");
        System.out.println("valueOf(FROZEN) == FROZEN ? " + (parsed == AccountStatus.FROZEN));
    }

    /** 自定义 code 存库，不用 ordinal。 */
    private static void demoFieldsAndStableCode() {
        System.out.println("== 2. 字段 + 稳定 code ==");
        System.out.println(ErrorCode.NOT_FOUND.getCode() + " -> " + ErrorCode.NOT_FOUND.getMessage());
        ErrorCode fromDb = ErrorCode.fromCode(1001);
        System.out.println("fromCode(1001) = " + fromDb);
    }

    /** 每个常量实现自己的抽象方法。 */
    private static void demoAbstractPerConstant() {
        System.out.println("== 3. 抽象方法 ==");
        System.out.println(PayChannel.ALIPAY.display());
        System.out.println(PayChannel.WECHAT.display());
    }

    private static void demoSwitchAndEnumSet() {
        System.out.println("== 4. switch / EnumSet ==");
        printPolicy(AccountStatus.ACTIVE);
        printPolicy(AccountStatus.CLOSED);

        EnumSet<AccountStatus> canTrade = EnumSet.of(AccountStatus.ACTIVE);
        System.out.println("可交易? FROZEN=" + canTrade.contains(AccountStatus.FROZEN));
        System.out.println("可交易? ACTIVE=" + canTrade.contains(AccountStatus.ACTIVE));
    }

    private static void printPolicy(AccountStatus status) {
        switch (status) {
            case ACTIVE:
                System.out.println("  " + status + " -> 允许存取款");
                break;
            case FROZEN:
                System.out.println("  " + status + " -> 只读，禁止交易");
                break;
            case CLOSED:
                System.out.println("  " + status + " -> 账户已关闭");
                break;
            default:
                System.out.println("  " + status + " -> 未知");
                break;
        }
    }

    /** valueOf 失败要处理；对外可用 fromName 包一层。 */
    private static void demoSafeParse() {
        System.out.println("== 5. 安全解析 ==");
        System.out.println("parse ACTIVE -> " + parseStatus("ACTIVE"));
        System.out.println("parse bad    -> " + parseStatus("active"));
    }

    private static AccountStatus parseStatus(String raw) {
        if (raw == null) {
            return null;
        }
        try {
            return AccountStatus.valueOf(raw.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

/** 错误码：数据库存 code，不存 ordinal。 */
enum ErrorCode {
    OK(0, "成功"),
    NOT_FOUND(1001, "资源不存在"),
    FORBIDDEN(1003, "无权限");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorCode fromCode(int code) {
        for (ErrorCode e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}

enum PayChannel {
    ALIPAY {
        @Override
        String display() {
            return "支付宝";
        }
    },
    WECHAT {
        @Override
        String display() {
            return "微信支付";
        }
    };

    abstract String display();
}
