package com.learn.apis;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/**
 * 日期时间只用 java.time。java.util.Date / Calendar 只认不写。
 *
 * LocalDate 没有时区；跨时区才用 ZonedDateTime。算间隔用 Duration / Period。
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\apis\Ex06JavaTime.java
 * 运行：java -cp out com.learn.apis.Ex06JavaTime
 */
public class Ex06JavaTime {
    public static void main(String[] args) {
        LocalDate start = LocalDate.parse("2024-01-03"); // ISO 日期，不要自己拆字符串
        LocalDate end = start.plusDays(10);
        System.out.println("start            = " + start);
        System.out.println("plus 10 days     = " + end);
        System.out.println("is before?       = " + start.isBefore(end));

        LocalDateTime meeting = LocalDateTime.parse("2024-01-03T09:30:00");
        Duration length = Duration.ofMinutes(90);
        System.out.println("meeting ends     = " + meeting.plus(length));
        System.out.println("duration minutes = " + length.toMinutes()); // toHours() 会丢掉不足一小时的部分

        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        ZoneId newYork = ZoneId.of("America/New_York");
        ZonedDateTime here = ZonedDateTime.of(meeting, shanghai);
        ZonedDateTime there = here.withZoneSameInstant(newYork);
        System.out.println("Shanghai         = " + here);
        System.out.println("same instant NY  = " + there);

        try {
            LocalDate.parse("03/01/2024"); // 不是 ISO，会失败
        } catch (DateTimeParseException e) {
            System.out.println("bad pattern      = " + e.getParsedString());
        }

        System.out.println();
        System.out.println("结论：解析用 parse，加减用 plus/minus，时长用 Duration。不要 new Date()。");
    }
}
