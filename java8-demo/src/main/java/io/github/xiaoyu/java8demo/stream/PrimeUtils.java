package io.github.xiaoyu.java8demo.stream;

/**
 * 判断是否是质数
 * @author xiaoyu
 * @since 1.0
 */
public class PrimeUtils {
    /**
     * 是否是质数
     * @param target
     * @return
     */
    public static boolean isPrimeNumber(int target) {
        if (target < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(target); i++) {
            if (target % i == 0) {
                return false;
            }
        }
        return true;
    }
}
