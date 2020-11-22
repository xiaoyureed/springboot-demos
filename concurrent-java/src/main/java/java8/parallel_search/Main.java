package java8.parallel_search;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main
 */
public class Main {

    private static int[] arr;
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private static final int thread_num = 2;
    private static AtomicInteger result = new AtomicInteger(-1);// 多个 thread 通过这个共享信息

    private static class SearchTask implements Callable<Integer> {

        private int begin;
        private int end;
        private int target;

        public SearchTask(int begin, int end, int target) {
            this.begin = begin;
            this.end = end;
            this.target = target;
        }

        @Override
        public Integer call() throws Exception {
            return search(target, begin, end);
        }

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        arr = new int[] {2, 4, 5, 9, 11, 80, 294305, 777, 83};
        int index = parallelSearch(777);
        System.out.println("search finished, index=" + index);
    }

    private static int parallelSearch(int target) throws InterruptedException, ExecutionException {
        ArrayList<Future<Integer>> re = new ArrayList<Future<Integer>>();

        int subArrSize = arr.length / (thread_num + 1);// 宁愿每个子数组size小一些, 数组个数多一些
        for (int i = 0; i < arr.length; i += subArrSize) {
            int end = i + subArrSize;
            if (end > arr.length) {// 可以去掉
                end = arr.length;
            }
            re.add(pool.submit(new SearchTask(i, end, target)));
        }

        for (Future<Integer> fu : re) {
            Integer i = fu.get();
            if (i >= 0) {
                return i;
            }
        }
        return -1;
    }

    private static int search(int target, int begin, int end) {
        if (result.get() >= 0) {// another thread should has finished the search
            return result.get();
        }

        for (int i = begin; i < end; i++) {
            if (arr[i] == target) {
                boolean ok = result.compareAndSet(-1, i);// get the index of target, and put it into result;
                if (ok) {
                    return i;
                }
                // 如果设置失败, 表示其他线程抢先找到了
                return result.get();
            }
        }
        // 遍历完还是没找到, 返回 -1
        return -1;
    }

    
}