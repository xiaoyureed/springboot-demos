package io.github.xiaoyureed.actuator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.IOException;

@SpringBootApplication
@RestController
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HikariDataSource dataSource;

    @RequestMapping("/jdbc-url")
    public ResponseEntity<String> jdbcUrl() {
        return ResponseEntity.ok(dataSource.getJdbcUrl());
    }

    @Bean
    @RefreshScope
    public HikariDataSource h2DataSource() throws IOException {
        final FileSystemResource config = new FileSystemResource("config.json");
        final Data data = objectMapper.readValue(config.getFile(), Data.class);
        final DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUrl(data.getUrl());
        dataSourceProperties.setPassword(data.getPassword());
        dataSourceProperties.setUsername(data.getUsername());
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @lombok.Data
    static class Data {
        private final String url;
        private final String username;
        private final String password;
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
