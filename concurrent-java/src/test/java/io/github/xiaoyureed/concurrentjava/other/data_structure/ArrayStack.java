package io.github.xiaoyureed.concurrentjava.other.data_structure;

import java.lang.reflect.Array;

/**
 * ArrayStack - 模拟 Stack 类, 数组实现
 */
public class ArrayStack<T> {

    private T[] bucket;
    private int count; // 元素个数

    public ArrayStack(Class<T> clazz, int size) {
        Object newInstance = Array.newInstance(clazz, size);
        bucket = (T[]) newInstance;
        count = 0; // 初始化为 0 个 元素
    }

    public void push(T value) {
        bucket[count] = value;
        count = count + 1;
    }

    public T pop() {
        int index = count -1;
        T ret = bucket[index];
        bucket[index] = null;
        count = count -1;
        return ret;
    }

    public T top() {
        int index = count -1;
        return bucket[index];
    }
}