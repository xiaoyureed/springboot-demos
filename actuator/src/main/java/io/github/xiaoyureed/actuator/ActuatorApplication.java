package io.github.xiaoyureed.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ActuatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActuatorApplication.class, args);
	}

//	@Bean
//	public SecurityWebFilterChain securityWebFilterChain(
//			ServerHttpSecurity http) {
//		return http.authorizeExchange()
//				.pathMatchers("/actuator/**").permitAll()
//				.anyExchange().authenticated()
//				.and().build();
//	}
}
