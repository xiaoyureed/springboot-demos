package io.github.xiaoyureed.springsecurityoauth2.authserver;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xiaoyureed.springbootdemos.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoyu
 * date: 2020/3/24
 *
 * https://www.cnblogs.com/fernfei/p/12200226.html
 */
@SpringBootApplication
public class AuthServerApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApp.class, args);
    }
}

@EnableAuthorizationServer
@Configuration
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String CLIENT_ID                       = "demo-client";
    private static final String CLIENT_SECRET                   = "{noop}demo-secret";
    private static final String GRANT_TYPE_PASSWORD            = "password";
    private static final String GRANT_TYPE__AUTHORIZATION_CODE = "authorization_code";
    private static final String GRANT_TYPE_REFRESH_TOKEN       = "refresh_token";
    private static final String GRANT_TYPE_IMPLICIT            = "implicit";
    private static final String SCOPE_READ                     = "read";
    private static final String SCOPE_WRITE                    = "write";
    private static final String SCOPE_TRUST                    = "trust";
    private static final int    ACCESS_TOKEN_VALIDITY_SECONDS  = 1*60*60;
    private static final int    REFRESH_TOKEN_VALIDITY_SECONDS = 6*60*60;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(CLIENT_ID)
                .secret(CLIENT_SECRET)
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD,
                        GRANT_TYPE__AUTHORIZATION_CODE, GRANT_TYPE_REFRESH_TOKEN, GRANT_TYPE_IMPLICIT)
                .scopes(SCOPE_READ, SCOPE_WRITE, SCOPE_TRUST)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS).
                refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager);
    }
}


@Mapper
interface UserMapper extends BaseMapper<User> {

    default List<User> conditionalQuery(User condition) {
        if (condition == null) {
            return new ArrayList<>(0);
        }
        return this.selectList(new LambdaQueryWrapper<User>()
                .eq(StringUtils.isStrictNotEmpty(condition.getUsername()),
                        User::getUsername, condition.getUsername())
                .eq(StringUtils.isStrictNotEmpty(condition.getPassword()),
                        User::getPassword, condition.getPassword())
                .eq(StringUtils.isStrictNotEmpty(condition.getRole()),
                        User::getRole, condition.getRole()));
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    private String username;
    private String password;
    private String role;
}
