package io.github.xiaoyu.redisdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)// 30天失效,
                                                // 原来的server.session.timeout 属性不再生效
public class SessionConfig {
}
