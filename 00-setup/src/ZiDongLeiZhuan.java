/**
//  * 自动类型转换： 低  ------------------------------------>  高
                    byte,short,char —> int —> long—> float —> double 
 *  ZiDongLeiZhuan
 */
public class ZiDongLeiZhuan {
    public static void main(String[] args) {
        char a = 'a'; // Unicode 字符 97
        int b = a;
        System.out.println("b = " + b); // 输出 97
        int c = 123; // 整数 123
        long d = c; // 长整数 123
        System.out.println("d = " + d); // 输出 123
        float e = d; // 浮点数 123.0
        System.out.println("e = " + e); // 输出 123.0
        double f = e; // 双精度浮点数 123.0
        System.out.println("f = " + f); // 输出 123.0
    }
}