package io.github.xiaoyureed.mockitomybatisplusdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan({"io.github.xiaoyureed.mockitomybatisplusdemo.mapper"})
public class MockitoDemoApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(MockitoDemoApplication.class, args);
    }

}
