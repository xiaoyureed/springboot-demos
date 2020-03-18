package io.github.xiaoyureed.demospringbootstarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaoyu
 * date: 2020/3/16
 */
@Configuration
@EnableConfigurationProperties({IServiceProperties.class})
@ConditionalOnClass(IService.class)
@ConditionalOnProperty(value = "i-service.enabled", havingValue = "true")
public class IServiceAutoConfiguration {

    private final IServiceProperties iServiceProperties;

    @Autowired
    public IServiceAutoConfiguration(IServiceProperties iServiceProperties) {
        this.iServiceProperties = iServiceProperties;
    }

    @Bean
    // @ConditionalOnMissingBean(name = {"hah", "hoo"})
    // @ConditionalOnMissingBean
    // @ConditionalOnBean
    @ConditionalOnBean(name = {"hah", "hoo"})
    public IService iService() {
        if (iServiceProperties.getName().contentEquals("demo")) {
            return new DemoServiceImpl(iServiceProperties);
        }
        if (iServiceProperties.getName().contentEquals("demo2")) {
            return new DemoServiceImpl(iServiceProperties);
        }
        return null;
    }
}
