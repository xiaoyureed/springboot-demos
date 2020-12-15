package io.github.xiaoyureed.springbootdemos.freemarkerh2jps;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/15
 */
@SpringBootApplication
public class FreemarkerH2JpaApp {
    public static void main(String[] args) {
        SpringApplication.run(FreemarkerH2JpaApp.class, args);
    }

    @Bean
    public ApplicationRunner init(StudentRepo studentRepo) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                studentRepo.saveAll(Arrays.asList(
                        new Student("a", GenderEnum.MALE, 11, LocalDate.now(), 10000, ""),
                        new Student("b", GenderEnum.FEMALE, 12, LocalDate.now(), 10001, "")
                ));
            }
        };
    }
}
