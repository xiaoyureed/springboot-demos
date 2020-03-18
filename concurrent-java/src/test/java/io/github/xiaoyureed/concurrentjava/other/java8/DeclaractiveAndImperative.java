package io.github.xiaoyureed.concurrentjava.other.java8;

import java.util.Arrays;

/**
 * DeclaractiveAndImperative
 */
public class DeclaractiveAndImperative {

    // private int[] arr;
    // public DeclaractiveAndImperative(int[] arr) {
    //     this.arr = arr;
    // }

    public static void main(String[] args) {
        // arr = new int[] { 32, 888, 22, 94382, 33 };
        int[] arr = {32, 888, 22, 94382, 33};

        // imperative(arr);
        // declaractive(arr);
        immutable(arr);
        declaractive(arr);
    }

    private static void imperative(int[] arr) {
        for (int i : arr) {
            System.out.print(i+", ");
        }
    }

    private static void declaractive(int[] arr) {
        Arrays.stream(arr).forEach(System.out::println);
    }

    private static void immutable(int[] arr) {
        Arrays.stream(arr).map((ele -> (ele + 1))).forEach(System.out::println);
    }
}