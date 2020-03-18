package io.github.xiaoyureed.demospringbootstartertest;

import io.github.xiaoyureed.demospringbootstarter.Demo2ServiceImpl;
import io.github.xiaoyureed.demospringbootstarter.IService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoSpringBootStarterTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringBootStarterTestApplication.class, args);
    }

    @Bean
    public IService iServiceMyOwn() {
        return new IService() {
            @Override
            public void exec() {
                System.out.println(">>> my own service");
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    @Bean
    public IService hah() {
        return new IService() {
            @Override
            public void exec() {
                System.out.println(">>> hello hah");
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    @Bean
    public IService hoo() {
        return new IService() {
            @Override
            public void exec() {
                System.out.println(">>> hello hoo");
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

}
