package io.github.xiaoyu.javabasic;

/**
 * @author xiaoyu
 * @date 2019/5/16
 */
public class AnonymousClassAndFinal {
    public static void main(String[] args) {
        new AnonymousClassAndFinal().func();
    }

    private void func() {
        Dog dog = new Dog();
        // 这里是一个匿名内部类, 其实Java就是把外部类的一个变量拷贝给
        // 了内部类里面的另一个变量。
        Runnable r = () -> {
            while (dog.getAge() < 50) {
                dog.growUp();
            }
        };
        new Thread(r).start();
        System.out.println("---------------");
        // 这里的操作是非法的；因为这里相当于在内部类外部将dog重新赋值
        // 内部类的变量dog和外部的dog不一样了， 指向了不同对象;
        // 最好将dog变量加上 final;
        //由于一个拷贝的动作，使得内外两个变量无法实时同步，其中一方修改，另外一方都无法同步修改，因此要加上final限制变量不能修改
        //dog = new Dog();
    }

    static class Dog {
        private int age;

        public int getAge() {
            return this.age;
        }

        public void growUp() {
            System.out.println("Happy birthday, you are " + this.age);
            this.age++;
        }
    }
}
