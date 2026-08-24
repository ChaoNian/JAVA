package com.learn.apis.csv;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * 入口：只接线。读文件、统计、打印。空文件和坏行都要跑一遍。
 *
 * 业务不写在 main 里：读是 CsvReader，统计是 OrderStats，这里只调用和打印。
 *
 * 编译：在 02-core-apis 目录执行 .\compile-and-run.ps1
 * 运行：java -cp out com.learn.apis.csv.CsvApp
 */
public class CsvApp {
    public static void main(String[] args) {
        CsvReader reader = new CsvReader();
        // Path.of("data", "sample.csv") 拼成 data/sample.csv。
        // 相对的是「启动 java 时的当前目录」，所以要在 02-core-apis 下跑。
        runOne(reader, Path.of("data", "sample.csv"));   // 全是好行
        runOne(reader, Path.of("data", "bad-rows.csv")); // 坏行记下来，好行照样统计
        runOne(reader, Path.of("data", "empty.csv"));    // 整份失败，进 catch
    }

    static void runOne(CsvReader reader, Path path) {
        System.out.println("-- " + path + " --");
        try {
            // read 声明了 throws CsvException，所以这里必须 try（对照 Ex01）
            CsvReader.Result result = reader.read(path);
            printBad(result.badLines());
            OrderStats stats = new OrderStats(result.orders());
            System.out.println("orders     = " + result.orders().size());
            System.out.println("cities     = " + stats.cities());       // Set：城市去重
            System.out.println("qty by sku = " + stats.qtyBySku());     // Map：sku -> 总销量
            System.out.println("sorted qty = " + stats.byQtyDesc());    // List：按销量排序后的订单
            Map<String, Integer> totals = stats.qtyBySku();
            // 按键查找，不用再扫 List。empty.csv 进不了这里；没有 SKU-A 时 get 返回 null
            System.out.println("SKU-A total looks up in Map = " + totals.get("SKU-A"));
        } catch (CsvException e) {
            // 只有「整份读失败」才到这：文件不存在、空文件、IO 错误。
            // 某一行日期写错不会到这，那条只出现在 badLines 里。
            System.out.println("failed: " + e.getMessage());
        }
        System.out.println();
    }

    static void printBad(List<String> badLines) {
        if (badLines.isEmpty()) {
            System.out.println("bad lines  = none");
            return; // 提前结束，下面的 for 不用跑
        }
        System.out.println("bad lines  = " + badLines.size());
        for (int i = 0; i < badLines.size(); i++) {
            System.out.println("  " + badLines.get(i));
        }
    }
}
