package io.github.xiaoyu.java8demo;

import io.github.xiaoyu.java8demo.method_reference.Person;
import io.github.xiaoyu.java8demo.method_reference.PersonFactory;
import org.junit.Test;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class BuiltinInterfaceTest {

    /**
     * 断言, 就是一段判断逻辑
     */
    @org.junit.Test
    public void testPredicate() {
        // 判断: 如果string长度大于0, 返回true
        // 接收的是一段判断逻辑
        Predicate<String> predicate = (s) -> s.length() > 0;

        predicate.test("foo");              // true
        predicate.negate().test("foo");     // false

        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull  = Objects::isNull;

        Predicate<String> isEmpty    = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();
    }

    /**
     * 接收一个参数, 返回一个结果
     */
    @org.junit.Test
    public void testFunction_1() {
        Function<String, Integer> toInteger = Integer::valueOf;
        // Stream.of(toInteger.apply("12")).forEach(System.out::println);
        assertThat(toInteger.apply("12")).isEqualTo(12);
    }

    /**
     * function1.andThen(function2) - 先执行 function1, 后执行 function2
     */
    @Test
    public void testFunction_2() {
        Function<String, Integer> toInteger    = Integer::valueOf;
        Function<String, String>  backToString = toInteger.andThen(String::valueOf);

        backToString.apply("123");     // "123"
    }

    /**
     * func1.compose(func2) - 先 func2, 后 func1
     */
    @Test
    public void testFunction_3() {
        Function<Integer, Integer> func1 = x -> x * 2;
        Function<Integer, Integer> func2 = x -> x * x;

        Integer result = func1.compose(func2).apply(4);
        assertThat(result).isEqualTo(32);
    }

    /**
     * 就是一个工厂, 不接收参数, 返回一个对象
     */
    @Test
    public void testSupplier() {
        Supplier<Person> personSupplier = Person::new;
        Person           person1        = personSupplier.get();// new Person
        assertThat(person1).extracting(Person::getAge, Person::getName).contains(0, "");

        PersonFac personFac = Person::new;
        Person    person2    = personFac.create();
        assertThat(person2).extracting(Person::getAge, Person::getName).contains(0, "");
    }

    @FunctionalInterface
    interface PersonFac<T extends Person> {
        T create();
    }

    @Test
    public void testSupplier_1() {
        PersonFactory<Person> personPersonFactory = Person::new;
        Person                person                = personPersonFactory.create("name", 10);
        assertThat(person).extracting(Person::getAge, Person::getName).contains(10, "name");
    }

    /**
     * 接收一个参数, 对其做出处理, 没有返回值
     */
    @Test
    public void testConsumer() {
        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.getName());
        greeter.accept(new Person("Luke", 55));
    }

    /**
     * consumer1.andThen(consumer2) - 先执行consumer1, 后 consumer2
     */
    @Test
    public void testConsumer_1() {
        Consumer<Person> consumer1 = System.out::println;
        Consumer<Person> consumer2 = p -> System.out.println(p.getName());

        PersonFactory<Person> personFactory = Person::new;
        consumer1.andThen(consumer2).accept(personFactory.create("name", 22));
    }

    /**
     * 比较器
     */
    @Test
    public void testComparator() {
        Comparator<Person> comparator = Comparator.comparing(Person::getName);
        // (p1, p2) -> p1.getName().compareTo(p2.getName())

        Person p1 = new Person("John", 66);
        Person p2 = new Person("Alice", 77);

        int compare = comparator.compare(p1, p2);// > 0
        assertThat(compare > 0).isEqualTo(true);
        int compare1 = comparator.reversed().compare(p1, p2);// < 0
        assertThat(compare1 < 0).isEqualTo(true);
    }
}
