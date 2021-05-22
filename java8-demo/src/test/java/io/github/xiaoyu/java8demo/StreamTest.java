package io.github.xiaoyu.java8demo;

import io.github.xiaoyu.java8demo.method_reference.Person;

import static org.assertj.core.api.Assertions.*;

import io.github.xiaoyu.java8demo.stream.PrimeUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class StreamTest {
    private static List<Person> persons;

    @BeforeClass
    public static void init() {
        persons = Arrays.asList(
                new Person("aaa1", 18),
                new Person("bbb1", 23),
                new Person("aaa2", 23),
                new Person("bbb2", 12),
                new Person("bbb3", 44),
                new Person("ccc1", 22));
    }

    @Test
    public void t2() {
        // Stream<String> hello = Optional.of(new String[] {"hello", "aa", "bb"}).map(Stream::of).orElseGet(Stream::empty);
        Supplier<Stream<String>> supplier = () -> Stream.of("hello", "aa", "bb");
        boolean                  aa       = supplier.get().allMatch(s -> s.equalsIgnoreCase("aa"));
        boolean                  aa1      = supplier.get().anyMatch(s -> s.equalsIgnoreCase("aa"));
        System.out.println(aa);
        System.out.println(aa1);

        IntStream.range(1, 4)
                .forEach(System.out::println);

        Arrays.stream(new int[] {1, 2, 3})
                .map(n -> 2 * n + 1)
                .average()
                .ifPresent(System.out::println);
    }

    @Test
    public void testFilter() {
        long a = persons.stream()
                .filter(p -> p.getName().startsWith("a"))// 返回true则通过
                .count();
        assertThat(a).isEqualTo(2);
    }

    /**
     * map() - receive a Function , convert every element
     */
    @Test
    public void testMap() {
        List<String> collect = persons.stream()
                .map(Person::getName)
                .collect(Collectors.toList());
        assertThat(collect.size()).isEqualTo(6);
    }

    /**
     * flatmap() - 扁平化 (粉碎), 将几个小的 collection 转化为一个大大的collection
     */
    @Test
    public void testFlatMap() {
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("1", "2", "3");
        List<List<String>> list3 = Arrays.asList(list1, list2);

         List<String> result = list3.stream()
                 .flatMap((List<String> x) -> {
                     return x.stream();
                 })
                 .collect(Collectors.toList());

    }

    /**
     * peek() - receive a consumer handling every element
     */
    @Test
    public void testPeek() {
        List<Person> collect = persons.stream()
                .peek(p -> p.setName(p.getName().toUpperCase()))
                .collect(Collectors.toList());
        // .forEach(System.out::println);
        assertThat(collect).extracting("name")
                .contains("AAA1", "BBB1", "AAA2", "BBB2", "BBB3", "CCC1")
                .doesNotContain("aaa1", "bbb1");
    }

    @Test
    public void testFileStream() throws IOException, URISyntaxException {
        // Stream<String> lines = Files.lines(Paths.get("."), Charset.defaultCharset());

        Stream<String> lines = Files.lines(
                new File("src/main/java/io/github/xiaoyu/java8demo/Java8DemoApplication.java").toPath()
                ,StandardCharsets.UTF_8);
        lines.forEach(System.out::println);
    }

    @Test
    public void testParallel1() {
        long count = persons.parallelStream()
                .filter(p -> p.getName().startsWith("a"))
                .count();
        assertThat(count).isEqualTo(2);
    }


    @Test
    public void testParallel2() {
        long count = persons.stream().parallel()
                .filter(p -> p.getName().startsWith("b"))
                .count();
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void testParallel3() {
        long count = IntStream.rangeClosed(1, 10000).parallel()
                .filter(PrimeUtils::isPrimeNumber)
                .count();
        System.out.println(count);
    }

    @Test
    public void testParallel4() {
        int[] ints = new int[100];
        Arrays.setAll(ints, ele -> new Random().nextInt(100));
        // Arrays.parallelSetAll(ints, ele -> new Random().nextInt(100));

        // Arrays.sort(ints);
        Arrays.stream(ints).sorted().forEach(System.out::println);
    }

    @Test
    public void testSort() {
        persons.stream()
                .sorted(Comparator.comparing(Person::getName, Comparator.reverseOrder()))
                .forEach(System.out::println);
    }

    @Test
    public void testMatch1() {
        boolean match = persons.stream()
                .anyMatch(p -> p.getName().startsWith("a"));
        assertThat(match).isEqualTo(true);
    }

    @Test
    public void testMatch2() {
        boolean match = persons.stream()
                .allMatch(p -> p.getName().startsWith("a"));
        assertThat(match).isEqualTo(false);
    }

    @Test
    public void testMatch3() {
        boolean match = persons.stream()
                .noneMatch(p -> p.getName().startsWith("z"));
        assertThat(match).isEqualTo(true);
    }

    @Test
    public void testCount() {
        long count = persons.stream()
                .filter(p -> p.getName().startsWith("a"))
                .count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testReduce() {
        Optional<Person> reduce = persons.stream()
                .sorted(Comparator.comparing(Person::getName, Comparator.reverseOrder()))
                .reduce((p1, p2) ->
                        new Person(p1.getName() + "#" + p2.getName(),
                                p1.getAge() + p2.getAge()));
        reduce.ifPresent(System.out::println);
    }

    @Test
    public void testCollect1() {
        List<Person> filteredPersons = persons.stream()
                .filter(s -> s.getName().startsWith("a"))
                .collect(Collectors.toList());
        assertThat(filteredPersons.size()).isEqualTo(2);
    }

    @Test
    public void testCollect2() {
        Map<Integer, String> map = persons
                .stream()
                .collect(Collectors.toMap(
                        Person::getAge, // key mapper
                        Person::getName, // value mapper
                        (oldName, newName) -> oldName + ";" + newName));// merge function, 如果key相同时对于value的操作
        System.out.println(map);
        //{18=aaa1, 22=ccc1, 23=bbb1;aaa2, 44=bbb3, 12=bbb2}
    }

    @Test
    public void testCollect3() {
        // 分组
        Map<Integer, List<Person>> personByAge = persons.stream().collect(Collectors.groupingBy(Person::getAge));
        personByAge.forEach((age, person) -> System.out.format("age %s: %s\n", age, person));
        // age 18: [Person[aaa1, 18]]
        // age 22: [Person[ccc1, 22]]
        // age 23: [Person[bbb1, 23], Person[aaa2, 23]]
        // age 44: [Person[bbb3, 44]]
        // age 12: [Person[bbb2, 12]]
    }

    @Test
    public void testCollect4() {
        // 平均值
        Double averageAge = persons
                .stream()
                .collect(Collectors.averagingInt(Person::getAge));

        System.out.println(averageAge);     // 23.666666666666668
    }

    @Test
    public void testCollect5() {
        // 对age简单分析
        IntSummaryStatistics ageSummary =
                persons
                        .stream()
                        .collect(Collectors.summarizingInt(Person::getAge));

        System.out.println(ageSummary);
// IntSummaryStatistics{count=4, sum=76, min=12, average=19.000000, max=23}
    }

    @Test
    public void testCollect6() {
        // 拼接
        String phrase = persons
                .stream()
                .filter(p -> p.getAge() >= 18)
                .map(Person::getName)
                .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

        System.out.println(phrase);
// In Germany Max and Peter and Pamela are of legal age.
    }

    @Test
    public void testCollect7() {
        // 构造自己的collector
        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),          // supplier
                        (j, p) -> j.add(p.getName().toUpperCase()),  // accumulator
                        StringJoiner::merge,               // combiner
                        StringJoiner::toString);                // finisher

        String names = persons
                .stream()
                .collect(personNameCollector);

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }

    @Test
    void testGroupBy() {
        //根据多个属性（年龄和性别）进行分组
        //得到的集合是一个年龄下分为两种性别（嵌套调用groupby）
        //Map<String, Map<String, List>> groupedMap = list.stream()
        //.collect(Collectors.groupingBy(Student::getAge, Collectors.groupingBy(Student::getSex)));
    }

    @Test
    public void testSequentialStream() {
        //todo
        //https://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
    }

}
