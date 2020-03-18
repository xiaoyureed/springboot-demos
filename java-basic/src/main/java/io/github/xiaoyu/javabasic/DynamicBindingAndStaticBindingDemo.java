package io.github.xiaoyu.javabasic;

/**
 * 绑定指的是一个方法的调用与方法所在的类(方法主体)关联起来
 *
 * 静态绑定使用的是类信息，而动态绑定使用的是对象信息
 *
 * 重载方法(overloaded methods)使用的是静态绑定，而重写方法(overridden methods)使用的是动态绑定
 *
 * final，static，private和构造方法是前期(静态)绑定 (因为都不能被继承/覆盖)
 *
 * @author xiaoyu
 * @date 2019/5/16
 */
public class DynamicBindingAndStaticBindingDemo {
    public static void main(String[] args) {
        A a_b = new B();
        System.out.println(a_b.getI());// 2 // 动态绑定, 在运行期决定调用哪个方法
        System.out.println(a_b.i);//1 // 静态绑定 (编译期即确定了) ，返回的是接收引用对象 a_b 中的 i
    }

    static class A {
        int i; // field 成员变量始于静态绑定
        A() { // exec first
            i = 1;
            System.out.println("A constructor");
        }

        int getI() {
            return i;
        }
    }

    static class B extends A {
        int i;// 这里的 i 不会覆盖 A 中的 i
        B() {// exec second
            i = 2;
            System.out.println("B constructor");
        }

        int getI() {
            return i;
        }
    }
}
