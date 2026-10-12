public class Forcetransform {
    public static void main(String[] args) {
        int i1 = 123;
        byte b = (byte) i1; // 强制类型转换 (byte)告诉编译器：把右边的值当成 byte 处理
        System.out.println("b = " + b); // 输出 123
        int i2 = 456;
        byte b2 = (byte) i2; // 强制类型转换
        System.out.println("b2 = " + b2); // 输出 456
        int i3 = 123456;
        byte b3 = (byte) i3; // 强制类型转换
        System.out.println("b3 = " + b3); // 输出 123456
        int i4 = 1234567;
        byte b4 = (byte) i4; // 强制类型转换
        System.out.println("b4 = " + b4); // 输出 1234567
    }
}
