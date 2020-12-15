package io.github.xiaoyureed.springbootjpahibernatevalidation;

import io.github.xiaoyureed.springbootjpahibernatevalidation.repository.IProductRepo;
import io.github.xiaoyureed.springbootjpahibernatevalidation.repository.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "io.github.xiaoyureed.springbootjpahibernatevalidation.repository")
public class SpringBootJpaHibernateValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaHibernateValidationApplication.class, args);
    }

    @Autowired
    private IProductRepo productRepo;

    @Bean
    public ApplicationRunner initDb() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                productRepo.save(new Product("apple",
                        new BigDecimal("11.11"), 3));
                productRepo.save(new Product("banana",
                        new BigDecimal("22.22"), 2));
            }
        };
    }

}
