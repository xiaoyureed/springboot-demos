package io.github.xiaoyureed.springbootjpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * https://github.com/cloudfavorites/favorites-web
 */
@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"io.github.xiaoyureed.springbootjpa"}) // don't need
public class SpringBootJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaApplication.class, args);
    }

    @Autowired
    private DemoRepository demoRepository;

    @Bean
    public ApplicationRunner runner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                List<Demo> all = demoRepository.findAll();
                System.out.println(">>> all = " + all);
            }
        };
    }
}
