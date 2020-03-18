package io.github.xiaoyureed.concurrentjava.other.parallel_sort;

import java.util.concurrent.CountDownLatch;

/**
 * Main
 */
public class Main {

    private static int[] arr;

    // private static boolean exchFlag = false;
    // private static synchronized void updateExchFlag(boolean flag) {
    //     exchFlag = flag;
    // }
    public static class OddEvenSortTask implements Runnable {

        private int i; // 当前比较元素的索引
        private CountDownLatch latch;

        public OddEvenSortTask(int i, CountDownLatch latch) {
            this.i = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            boolean compareAndSwap = compareAndSwap(arr, i);
            if (compareAndSwap) {
                
            }
        }
    
        
    }

    public static void main(String[] args) {
        arr = new int[] { 763, 203, 2, 32, 8, 99, 455, 777, 343, 234 };

        // bubbleSort(arr);
        oddEvenSortSerial(arr);
        
        for (int i : arr) {
            
            System.out.print(i+", ");//2, 8, 32, 99, 203, 234, 343, 455, 763, 777,
        }
    }

    public static void bubbleSort(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                compareAndSwap(arr, j);
            }
        }
    }

    public static void oddEvenSortSerial(int[] arr) {
        boolean exch = true;// 本次循环是否进行了数据交换
        int startIndex = 0;// 1: 奇交换, 0: 偶交换

        while (exch || startIndex == 1) {// 如果上一次循环发生了交换 or 奇交换, 重复循环
                                    // 当没有元素交换, 且当前进行的是偶交换 - 退出循环
            exch = false;

            for (int i = startIndex; i < arr.length - 1; i += 2) {
                boolean compareAndSwap = compareAndSwap(arr, i);
                if (compareAndSwap) {
                    exch = true;
                }
            }

            if (startIndex == 0) {
                startIndex = 1;
            } else {
                startIndex = 0;
            }
        }
    }

    public static void oddEvenSortParallel(int[] arr) {
        
    }

    private static boolean compareAndSwap(int[] arr, int i) {
        if (arr[i] > arr[i+1]) {
            int tmp = arr[i];
            arr[i] = arr[i+1];
            arr[i+1] = tmp;

            return true;
        }
        return false;
    }
}