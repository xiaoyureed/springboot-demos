package io.github.xiaoyureed.demospringbootstarter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiaoyu
 * date: 2020/3/17
 */
@ConfigurationProperties(prefix = "i-service")
public class IServiceProperties {

    private boolean enabled = true;

    private String name = "demo";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
