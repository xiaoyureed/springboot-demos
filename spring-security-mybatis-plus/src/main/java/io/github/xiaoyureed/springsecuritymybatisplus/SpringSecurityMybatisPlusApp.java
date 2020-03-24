package io.github.xiaoyureed.springsecuritymybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xiaoyu
 * date: 2020/3/21
 */
@SpringBootApplication
public class SpringSecurityMybatisPlusApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityMybatisPlusApp.class, args);
    }
}

@RestController
class DemoController {

    @RequestMapping("/f-f")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("free");
    }

    @RequestMapping("/user-info")
    public ResponseEntity<String> userRole() {
        return ResponseEntity.ok("user info");
    }

    @RequestMapping("/admin-info")
    public String adminRole() {
        return "admin info";
    }
}


@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceCustomized userDetailServiceCustomized;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    // @Profile({"prod"})
    public PasswordEncoder passwordEncoderProd() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // @Profile("dev")
    // public PasswordEncoder passwordEncoderDev() {
    //     return NoOpPasswordEncoder.getInstance();
    // }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailServiceCustomized)// 自定义 userDetailService
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/f-f").permitAll()
                .antMatchers("/user-info").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin-info/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin()
                .and().httpBasic();

    }
}

@Component
@Slf4j
class UserDetailServiceCustomized implements UserDetailsService {

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
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>(1);
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userFromDb.getRole()));

        return new org.springframework.security.core.userdetails.User(
                username, userFromDb.getPassword(), grantedAuthorities);
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
@AllArgsConstructor
@NoArgsConstructor
class User {
    private Long   id;
    private String username;
    private String password;
    private String role;
}

final class StringUtils {
    private StringUtils() {
    }

    public static boolean isStrictEmpty(String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    public static boolean isStrictNotEmpty(String str) {
        return !isStrictEmpty(str);
    }
}

final class CollectionUtils {
    public static boolean isEmpty(Collection collection) {
        return (collection == null) || collection.size() == 0;
    }
}


