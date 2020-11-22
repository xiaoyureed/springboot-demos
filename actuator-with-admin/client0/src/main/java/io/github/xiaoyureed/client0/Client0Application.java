package io.github.xiaoyureed.client0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class Client0Application {

    public static void main(String[] args) {
        SpringApplication.run(Client0Application.class, args);
    }

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().anyRequest().permitAll()
                        .and().csrf().disable();
            }
        };
    }
}
