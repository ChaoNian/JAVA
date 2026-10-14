package com.learn.apis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * java.io 的四个日常场景。路径用 NIO.2 的 Path / Files，读写仍是流。
 *
 * 1. 配置：字符流读 UTF-8 文本
 * 2. 日志：字符流追加写入（缓冲，close 时才落盘）
 * 3. 上传/下载：字节流把一份二进制原样复制
 * 4. 临时目录：创建、列出、递归删除
 *
 * 编译（在 02-core-apis 目录）：
 * javac -encoding UTF-8 -d out src\com\learn\apis\Ex07IoCases.java
 * 运行：
 * java -cp out com.learn.apis.Ex07IoCases
 */
public class Ex07IoCases {

    public static void main(String[] args) throws IOException {
        Path work = Files.createTempDirectory("io-cases-");
        System.out.println("工作目录 " + work);
        try {
            readConfig(work);
            System.out.println();
            appendLog(work);
            System.out.println();
            copyBinary(work);
            System.out.println();
            useTempDir();
        } finally {
            deleteTree(work);
            System.out.println();
            System.out.println("工作目录已删除 exists=" + Files.exists(work));
        }
    }

    /** 场景 1：启动时读 application.properties。 */
    private static void readConfig(Path work) throws IOException {
        System.out.println("== 1. 读配置 ==");
        Path config = work.resolve("application.properties");

        try (BufferedWriter writer = Files.newBufferedWriter(config, StandardCharsets.UTF_8)) {
            writer.write("app.name=学习仓库");
            writer.newLine();
            writer.write("db.port=3306");
            writer.newLine();
        }

        Properties props = new Properties();
        // load(Reader) 沿用上面的 UTF-8。load(InputStream) 固定按 ISO-8859-1，中文会坏。
        try (BufferedReader reader = Files.newBufferedReader(config, StandardCharsets.UTF_8)) {
            props.load(reader);
        }

        System.out.println("app.name = " + props.getProperty("app.name"));
        System.out.println("db.port  = " + props.getProperty("db.port"));
    }

    /** 场景 2：运行日志追加到 app.log。两次打开都是 APPEND，不会盖掉上一行。 */
    private static void appendLog(Path work) throws IOException {
        System.out.println("== 2. 写日志 ==");
        Path log = work.resolve("app.log");
        writeLogLine(log, "server started");
        writeLogLine(log, "db connected");

        try (BufferedReader reader = Files.newBufferedReader(log, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
        }
    }

    private static void writeLogLine(Path log, String message) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                log,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(LocalDateTime.now() + " " + message);
            writer.newLine();
        } // close 会 flush：缓冲里最后一截这时才真正写进文件
    }

    /**
     * 场景 3：头像、PDF 这种二进制。用字节流按块复制，读到 -1 表示结束。
     * 对应上传（外部流 → 本地文件）和下载（本地文件 → 外部流）。
     */
    private static void copyBinary(Path work) throws IOException {
        System.out.println("== 3. 复制二进制 ==");
        Path source = work.resolve("avatar.bin");
        byte[] payload = new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47, 1, 2, 3, 4};

        try (OutputStream out = Files.newOutputStream(source)) {
            out.write(payload);
        }

        Path copy = work.resolve("avatar-copy.bin");
        try (InputStream in = Files.newInputStream(source);
             OutputStream out = Files.newOutputStream(copy)) {
            byte[] buf = new byte[4]; // 故意很小，让循环多走几次
            int n;
            int chunks = 0;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n); // 最后一块往往不满，只能写 n 个字节
                chunks++;
            }
            System.out.println("复制块数 = " + chunks);
        }

        byte[] back = Files.readAllBytes(copy);
        System.out.println("字节数一致 = " + (payload.length == back.length));
        System.out.println("内容一致   = " + java.util.Arrays.equals(payload, back));
    }

    /** 场景 4：处理大文件时的临时目录。列出内容后整棵删掉。 */
    private static void useTempDir() throws IOException {
        System.out.println("== 4. 临时目录 ==");
        Path job = Files.createTempDirectory("transcode-");
        try {
            Path chunks = job.resolve("chunks");
            Files.createDirectories(chunks); // 多级目录一次建好
            Path part = chunks.resolve("part.dat");
            try (BufferedWriter writer = Files.newBufferedWriter(part, StandardCharsets.UTF_8)) {
                writer.write("中间数据");
            }

            // Files.walk 返回的是 java.util.stream.Stream，不是 java.io 的输入流。
            try (java.util.stream.Stream<Path> walk = Files.walk(job)) {
                walk.forEach(p -> {
                    Path rel = job.relativize(p);
                    System.out.println("  " + (rel.toString().isEmpty() ? "." : rel));
                });
            }
        } finally {
            deleteTree(job);
            System.out.println("递归删除后 exists=" + Files.exists(job));
        }
    }

    /**
     * 先记下全部路径并关掉目录流，再从叶子往上删。
     * Windows 上边遍历边删会因为目录仍被占用而失败。
     */
    private static void deleteTree(Path root) throws IOException {
        if (Files.notExists(root)) {
            return;
        }
        List<Path> paths;
        try (java.util.stream.Stream<Path> walk = Files.walk(root)) {
            paths = walk.sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        }
        for (Path path : paths) {
            Files.delete(path);
        }
    }
}
