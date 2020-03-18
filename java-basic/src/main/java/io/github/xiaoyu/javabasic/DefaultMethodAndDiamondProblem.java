package io.github.xiaoyu.javabasic;

/**
 * @author xiaoyu
 * @date 2019/5/16
 */
public class DefaultMethodAndDiamondProblem {
    interface A extends C {
        default void func() {
            System.out.println("A func");
        }
    }

    interface B extends C {
        default void func() {
            System.out.println("B func");
        }
    }

    interface C {
        void func();
    }

    static class Demo implements A, B {

        @Override
        public void func() {// java 的做法是强制实现 interface 中的 default method
                        // 本来接口默认方法是不必实现的
                        // 这样避免了菱形继承问题
        }
    }
}
