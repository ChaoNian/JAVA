package com.learn.apis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Scanner 的三个练习场景。输入都来自字符串或临时文件，点运行不会卡住等键盘。
 *
 * 1. 控制台菜单：循环读指令，直到 exit
 * 2. 竞赛输入：空格和换行都只是分隔符，nextInt 连续读数字
 * 3. 小文本：按 UTF-8 用 Scanner 拆「姓名 分数」
 *
 * 真正接键盘时，把来源换成 new Scanner(System.in)，并且不要 close。
 * close 会把 System.in 一起关掉，后面就再也读不到键盘。
 *
 * 编译（在 02-core-apis 目录）：
 * javac -encoding UTF-8 -d out src\com\learn\apis\Ex08ScannerCases.java
 * 运行：
 * java -cp out com.learn.apis.Ex08ScannerCases
 */
public class Ex08ScannerCases {

    public static void main(String[] args) throws IOException {
        demoMenu();
        System.out.println();
        demoContest();
        System.out.println();
        demoScoreFile();
    }

    /** 场景 1：学生名单菜单。脚本按行模拟用户依次输入。 */
    private static void demoMenu() {
        System.out.println("== 1. 控制台菜单 ==");
        String script = "1\n张三\n1\n李四\n2\nexit\n";
        List<String> students = new ArrayList<String>();

        try (Scanner in = new Scanner(script)) {
            while (in.hasNextLine()) {
                System.out.println("1 添加  2 列表  exit 退出");
                String choice = in.nextLine().trim();
                System.out.println("输入: " + choice);

                if ("1".equals(choice)) {
                    String name = in.nextLine().trim();
                    students.add(name);
                    System.out.println("已添加 " + name);
                } else if ("2".equals(choice)) {
                    System.out.println("学生: " + students);
                } else if ("exit".equals(choice) || "0".equals(choice)) {
                    System.out.println("再见");
                    break;
                } else {
                    System.out.println("未知选项 " + choice);
                }
            }
        }
    }

    /**
     * 场景 2：竞赛式输入。
     * 第一行是用例个数 T。每个用例先给 n，再给 n 个整数，求这 n 个数的和。
     * nextInt 会跳过空格和换行，所以不用自己按行 split。
     */
    private static void demoContest() {
        System.out.println("== 2. 竞赛输入 ==");
        String input = "2\n3\n1 2 3\n4\n10 20 30 40\n";

        try (Scanner in = new Scanner(input)) {
            int cases = in.nextInt();
            for (int c = 1; c <= cases; c++) {
                int n = in.nextInt();
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    sum += in.nextInt();
                }
                System.out.println("case " + c + " sum=" + sum);
            }
        }
    }

    /** 场景 3：空格分隔的成绩文件。字符集写明 UTF-8，中文姓名才不会乱。 */
    private static void demoScoreFile() throws IOException {
        System.out.println("== 3. 读取成绩文件 ==");
        Path dir = Files.createTempDirectory("scanner-scores-");
        File file = dir.resolve("scores.txt").toFile();
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
                writer.write("张三 90");
                writer.newLine();
                writer.write("李四 85");
                writer.newLine();
            }

            int total = 0;
            int count = 0;
            try (Scanner in = new Scanner(file, "UTF-8")) {
                while (in.hasNext()) {
                    String name = in.next();
                    int score = in.nextInt();
                    System.out.println(name + " -> " + score);
                    total += score;
                    count++;
                }
            }
            System.out.println("平均 = " + (total / (double) count));
        } finally {
            Files.deleteIfExists(file.toPath());
            Files.deleteIfExists(dir);
        }
    }
}
