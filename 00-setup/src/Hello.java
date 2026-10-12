/**
 * 最小可运行程序：无包名，类名必须和文件名一致。
 *
 * 编译：javac -d out src\Hello.java
 * 运行：java -cp out Hello
 */ 
import java.util.regex.Pattern;
/**
 * 枚举类型
 */
class FreshJuice {
 enum FreshJuiceSize {SMALL, MEDIUM, LARGE}
 FreshJuiceSize size;
}







public class Hello {
    public static void main(String[] args) {
        // System.out.println("Hello, Java.");
        String content = "This is a test" + "This is another test";
        String Pattern = "This is a test";

        boolean isMatch = content.matches(Pattern);
        System.out.println("Is match: " + isMatch);
        FreshJuice juice = new FreshJuice();
        juice.size = FreshJuice.FreshJuiceSize.MEDIUM;
        // System.out.println("Size: " + juice.size);
        // System.out.println("This file compiled to bytecode, then the JVM ran it.");
    }
}
