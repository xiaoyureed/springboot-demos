package io.github.xiaoyureed.springbootdemos.rwseparatedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/16
 */
@Configuration
@Slf4j
public class DatasourceConfig {
    @Bean("master")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource master() {
        return DataSourceBuilder.create().build();
    }

    @Bean("slave1")
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource slave1() {
        return DataSourceBuilder.create().build();
    }



}
