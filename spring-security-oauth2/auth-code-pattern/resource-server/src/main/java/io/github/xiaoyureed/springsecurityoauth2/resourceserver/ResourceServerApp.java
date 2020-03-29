package io.github.xiaoyureed.springsecurityoauth2.resourceserver;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xiaoyureed.springbootdemos.common.util.CollectionUtils;
import io.github.xiaoyureed.springbootdemos.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xiaoyu
 * date: 2020/3/24
 */
@SpringBootApplication
public class ResourceServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApp.class, args);
    }
}

@Configuration
@EnableResourceServer
class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                anonymous().disable()
                .authorizeRequests()
                .antMatchers("/users/**").authenticated()
                .and().
                exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}

@Configuration
// @EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsServiceCustomized;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceCustomized).passwordEncoder(passwordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration               config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers("/api-docs/**").permitAll();
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

}

@Component
@Slf4j
class UserDetailsServiceCustomized implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 从 db 加载 users
     * <p>
     * 懒加载, 需要验证才加载
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        User condition = new User();
        condition.setUsername(username);
        List<User> usersFromDb = userMapper.conditionalQuery(condition);
        if (CollectionUtils.isEmpty(usersFromDb)) {
            throw new UsernameNotFoundException("User [" + username + "] was not found ind db.");
        }

        User userFromDb = usersFromDb.get(0);
        log.info(">>>  load user: " + userFromDb);

        // 设置 role
        return new org.springframework.security.core.userdetails.User(
                username, userFromDb.getPassword(), Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + userFromDb.getRole())));
    }
}

@RestController
class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getUserList() {
        User condition = new User();
        return userMapper.conditionalQuery(condition);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public void saveUser(@RequestBody User req) {
        userMapper.insert(req);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long id) {
        userMapper.deleteById(id);
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
