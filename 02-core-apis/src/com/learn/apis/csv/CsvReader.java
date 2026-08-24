package com.learn.apis.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用 NIO.2 读 UTF-8 CSV。空文件抛 CsvException；坏行记下来，不让整个文件失败。
 */
public final class CsvReader {

    /** 一次读取的结果：好行进 orders，坏行进 badLines。 */
    public static final class Result {
        private final List<Order> orders;
        private final List<String> badLines;

        Result(List<Order> orders, List<String> badLines) {
            this.orders = orders;
            this.badLines = badLines;
        }

        public List<Order> orders() {
            return orders;
        }

        public List<String> badLines() {
            return badLines;
        }
    }

    /**
     * throws CsvException：checked，CsvApp 必须处理。
     * Path 是 NIO.2 的路径类型，不要用旧的 java.io.File 写新代码。
     */
    public Result read(Path path) throws CsvException {
        if (path == null) {
            throw new CsvException("path required");
        }
        if (!Files.exists(path)) {
            throw new CsvException("missing file: " + path);
        }

        List<Order> orders = new ArrayList<Order>();
        List<String> badLines = new ArrayList<String>();

        // try-with-resources：读完或抛错都会 close（对照 Ex02）
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                throw new CsvException("empty file: " + path); // 空文件：整份失败
            }

            int lineNo = 1; // 表头算第 1 行，数据从 2 开始，好和编辑器行号对上
            String line;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) {
                    continue;
                }
                Order order = parseLine(line, lineNo, badLines);
                if (order != null) {
                    orders.add(order);
                }
            }
        } catch (CsvException e) {
            throw e; // 不要包成「cannot read」，否则 empty file 的原因被盖住
        } catch (IOException e) {
            throw new CsvException("cannot read " + path, e); // 磁盘/编码问题
        }

        return new Result(orders, badLines);
    }

    /** 解析失败返回 null，并往 badLines 记一句。不抛，好让后面的行继续读。 */
    private Order parseLine(String line, int lineNo, List<String> badLines) {
        String[] parts = line.split(",", -1); // -1：保留末尾空列，否则 "a,b,c," 会少一段
        if (parts.length != 4) {
            badLines.add("line " + lineNo + ": expected 4 columns, got " + parts.length);
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(parts[0].trim()); // 只接受 2024-01-03 这种 ISO
            String sku = parts[1].trim();
            String city = parts[2].trim();
            int qty = Integer.parseInt(parts[3].trim());
            return new Order(date, sku, city, qty);
        } catch (DateTimeParseException e) {
            badLines.add("line " + lineNo + ": bad date");
            return null;
        } catch (NumberFormatException e) {
            badLines.add("line " + lineNo + ": bad qty");
            return null;
        } catch (IllegalArgumentException e) {
            badLines.add("line " + lineNo + ": " + e.getMessage());
            return null;
        }
    }
}
