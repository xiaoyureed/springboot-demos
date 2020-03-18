package io.github.xiaoyu.java8demo;

import io.github.xiaoyu.java8demo.method_reference.Person;
import io.github.xiaoyu.java8demo.method_reference.PersonFactory;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class MethodReferenceTest {
    @org.junit.Test
    public void testConstructorReference() {
        PersonFactory<Person> factory = Person::new;// 此时compiler 还不知道到底调用Person的哪个构造方法
        Person                tom     = factory.create("Tom", 12);// 此时compiler才确定构造方法
    }

    @Test
    public void testArrayConstruct() {
        //todo
        IntConsumer intConsumer = Person[]::new;
        intConsumer.accept(0);
    }

    @Test
    public void testStaticMethodReference() {
    }

    @Test
    public void testInstanceMethodReference() {
        // 给一个person, 返回名字
        Function<Person, String> getName = Person::getName;
        assertThat(getName.apply(new Person("xy", 22))).isEqualTo("xy");

        // 给person赋予名字
        BiConsumer<Person, String> setName = Person::setName;
        Person                     init    = new Person("init", 22);
        setName.accept(init, "hahaha");
        assertThat(init.getName()).isEqualTo("hahaha");

        // -----------------

        Person   person   = new Person();
        Runnable getName1 = () -> person.getName();
        Runnable getName2 = person::getName;// 这里使用 method reference 没甚效果, 还不如不用
        // 消费string, 设置为 person的name
        Consumer<String> setName1 = person::setName;

        // ---------------

        Consumer<String> print = Person::print;
        //todo
    }
}
