package io.github.xiaoyureed.concurrentjava.other.parallel_search;

import java.util.concurrent.Callable;

/**
 * SearchTask
 */
public class SearchTask implements Callable<Integer> {

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
        // return search();
        return null;
    }

    
}