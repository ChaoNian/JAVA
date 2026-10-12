public class Puppy {
    private int age;
    private String name;

    // 构造器
    public Puppy(String name) {
        this.name = name;
        System.out.println("Puppy name: " + name);
    }

    public void setAge(int age) {
        this.age = age;
    }
    // getter 方法
    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        Puppy puppy = new Puppy("Buddy");
        puppy.setAge(2);

        System.out.println("Puppy age: " + puppy.getAge());
        System.out.println("Puppy name: " + puppy.getName());
    }

}
