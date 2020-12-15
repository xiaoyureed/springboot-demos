package io.github.xiaoyureed.restapiscaffold.respapimavenplugin;

import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.util.StringUtils;
import org.apache.maven.plugin.logging.Log;

/**
 * @author xiaoyu
 * date: 2020/7/27
 */
public class GlobalConfig {

    private GlobalConfig() {}

    private static final class Holder {
        private static final GlobalConfig me = new GlobalConfig();
    }

    public static GlobalConfig me() {
        return Holder.me;
    }

    public String mapperLocations;

    public String basePackage;

    public String jdbcUrl;
    public String driverClassName;
    public String username;
    public String password;

    public String tableName;
    public String domainName;

    public void selfCheck(Log log) {
        log.info(">>> your configuration properties 👇, right?");
        log.info(toString());

        if (StringUtils.isNotValid(basePackage)) {
            log.error(">>> basePackage is invalid");
        }
    }

    @Override
    public String toString() {
        return "GlobalConfig{" +
                "basePackage='" + basePackage + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tableName'" + tableName + '\'' +
                ", domainName'" + domainName + '\'' +
                "}";
    }
}
