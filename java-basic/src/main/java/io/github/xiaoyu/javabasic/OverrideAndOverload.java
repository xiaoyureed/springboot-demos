package io.github.xiaoyu.javabasic;

/**
 * @author xiaoyu
 * @date 2019/5/16
 */
public class OverrideAndOverload {
    public static void main(String[] args) {
        A ab = new B();
        B b   = new B();
        ab.func(b);// B func(A)
        // 解释：
        // A ab = new B() 表示 ab为 A 类型, 只是内存地址指向 一个B类型的实例
        // ab.func(b) 先在 A 中找方法 func(B), 没找到, 将参数
        // b 的类型提升为其父类型A, 此时可以在 A 类中找到匹配的方法 func(A), 然
        // 后由于有重写, 属于动态绑定, 最终执行 实例中的重写方法;
    }

    static class A {
        void func(A a) {
            System.out.println("A func(A)");
        }

        ////////////overloading 方法签名必须不同////////////////////
        // 方法返回值不在方法签名里, 因此可以不管
        String test(String a) {
            return "";
        }
        boolean test(String a, String b) {
            return false;
        }

        // compile error
        //String test(String a, String b) {
        //    return "";
        //}

        // compile error
        //boolean test(String a) {
        //    return false;
        //}
        /////////////////////////////////////////////////////


    }

    static class B extends A {
        void func(A a) {
            System.out.println("B func(A)");
        }
        void func(B b) {
            System.out.println("B func(B)");
        }
    }


}