package io.github.xiaoyu.java8demo.method_reference;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Person {
    private String name;
    private int    age;

    public Person() {
        this.name = "";
        this.age = 0;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + this.getName() + ", " + this.getAge()  + "]";
    }

    public static void print(String str) {
        System.out.println(str);
    }
}
