package io.github.xiaoyu.javabasic;

/**
 * @author xiaoyu
 * @date 2019/5/16
 */
public class SwitchDemo {
    public static void main(String[] args) {
        // String test = "";
        char test = 's';
        switch (test) {// 可以接受int,byte,char,short,不能接受其他类型.
            case 's':
                System.out.println("sdfd");
                break;// 必须有； 否则会继续执行后面的case， 不管匹配上了没有
            case 'a':
                System.out.println("xxx");
                break;
            default: // default就是如果没有符合的case就执行它,default并不是必须的.
                System.out.println("default");
        }
    }
}