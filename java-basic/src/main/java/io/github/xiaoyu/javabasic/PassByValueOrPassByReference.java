package io.github.xiaoyu.javabasic;

/**
 * 值传递or引用传递?
 * 只有值传递, 基本类型肯定是值传递, 复合类型也是值传递, 传递的是内存地址
 * @author xiaoyu
 * @date 2019/5/16
 */
public class PassByValueOrPassByReference {
    public static void main(String[] args) {
        int age = 10;
        changeSimpleData(age);
        System.out.println(age);// 10  不会改变

        System.out.println();

        Parent p = new Parent();
        changeComplexData(p);
        System.out.println(p.age);// 2 改变了, 说明对于复合类型, 是传递的内存地址

    }

    public static void changeSimpleData(int age) {
        age += 10;
    }

    public static void changeComplexData(Parent p) {
        p.age = 2;
    }

    static class Parent {
        int age = 1;
    }

}