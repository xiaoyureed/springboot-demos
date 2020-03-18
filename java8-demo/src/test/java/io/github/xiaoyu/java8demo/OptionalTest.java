package io.github.xiaoyu.java8demo;

import io.github.xiaoyu.java8demo.method_reference.Person;
import org.junit.Test;

import java.util.Optional;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class OptionalTest {
    @Test
    public void testOptional() {
        Optional<String> optional = Optional.of("bam");

        optional.isPresent();           // true
        optional.get();                 // "bam"
        optional.orElse("fallback");    // "bam"

        // 不为空才打印
        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
    }

    @Test
    public void test_2() {
        Person person = new Person("name", 11);

        String name   = this.getName(person);

        // 最佳实践
        Person a = Optional.ofNullable(person).filter(p -> p.getName().startsWith("a")).orElseThrow(RuntimeException::new);

    }

    /**
     * 最佳实践
     */
    private String getName(Person p) {
        return Optional.ofNullable(p).map(Person::getName).orElse("null");
    }

    /**
     * 最佳实践
     */
    private String getName2(User_1 user_1) {
        return Optional.ofNullable(user_1)
                .map(user1 -> user1.user_2)
                .map(user_2 -> user_2.name)
                .orElseThrow(RuntimeException::new);
    }

    private static class User_1 {
        private String name;
        private User_2 user_2;
    }
    static class User_2 {
        private String name;
    }
}
