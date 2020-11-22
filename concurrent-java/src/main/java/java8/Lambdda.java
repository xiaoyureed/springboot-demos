package java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * Lambdda
 */
public class Lambdda {
    static int[] arr = {32, 888, 45, 23295, 233, 55};
    static class Student {
        private int score;
        public Student(int score) {
            this.score = score;
        }
        
    }

    public static void main(String[] args) {
        // intConsumerAndThenTest();
        // countPrime();
        // average();
        parallelSort();

    }

    private static void parallelSort() {
        int[] bigArr = new int[100];
        Arrays.setAll(bigArr, ele -> new Random().nextInt(200));
        // 并行赋值
        // Arrays.parallelSetAll(bigArr, ele -> new Random().nextInt());

        // 排序
        Arrays.sort(bigArr);
        // 并行排序
        // Arrays.parallelSort(bigArr);

        Arrays.stream(bigArr).forEach(System.out::println);
    }

    private static void average() {
        ArrayList<Student> stuList = new ArrayList<Student>();
        stuList.add(new Student(1));
        stuList.add(new Student(2));
        stuList.add(new Student(3));

        // 从集合获取流
        double asDouble = stuList.stream().mapToInt(stu -> stu.score).average().getAsDouble();
        // 并行流
        double asDouble2 = stuList.stream().parallel().mapToInt(stu -> stu.score).average().getAsDouble();
        System.out.println(asDouble);
    }

    private static long countPrime() {

        // long count = IntStream.range(1, 5).filter(Lambdda::isPrime).count();

        // 使用并行模式
        long count = IntStream.range(1, 10000).parallel().filter(Lambdda::isPrime).count();

        System.out.println(count);
        return count;
    }

    /**
     * 判断是否质数
     * 质数: 只能被 1和它本身整除
     * @param target
     * @return
     */
    private static boolean isPrime(int target) {
        if (target < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(target); i++) {
            if (target % i == 0) {
                return false;
            }
        }
        // System.out.println("prime: " + target);
        return true;
    }

	private static void intConsumerAndThenTest() {
        IntConsumer println = System.out::println;// 输出到标准输出
        IntConsumer errPrintln = System.err::println;// 输出到 错误输出
        Arrays.stream(arr).forEach(println.andThen(errPrintln));
    }
    /* IntConsumer的实现

        @FunctionalInterface
        public interface IntConsumer {

            void accept(int value);

            先调用 accept, 然后after.accept()
            default IntConsumer andThen(IntConsumer after) {
                Objects.requireNonNull(after);
                return (int t) -> { accept(t); after.accept(t); };
            }
        }

    */
}