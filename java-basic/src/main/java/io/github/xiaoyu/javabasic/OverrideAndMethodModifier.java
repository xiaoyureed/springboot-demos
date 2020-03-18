package io.github.xiaoyu.javabasic;

/**
 * @author xiaoyu
 * @date 2019/5/16
 */
public class OverrideAndMethodModifier {
    static class A {
        protected void func() {

        }
    }

    static class B extends A {
        @Override
        public void func() {// 子类中的override方法的修饰符范围必须 >= 父类中的方法
            super.func();
        }
    }
}
