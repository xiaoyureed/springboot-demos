package io.github.xiaoyureed.springsecuritydemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaoyu
 * date: 2020/3/21
 */
@SpringBootApplication
public class SpringSecurityDemoApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApp.class, args);
    }
}

@Configuration
class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加 view controller
     * <p>
     * 将 url path 和 template 对应, 无需添加任何的 controller 代码
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/admin-page").setViewName("admin-page");
    }
}


@Configuration
// 启用Spring Security的Web安全支持, spring boot 自动配置已经加了
// @EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Spring Security 中提供了 BCryptPasswordEncoder 密码编码工具，
     * 可以非常方便的实现密码的加密加盐，相同明文加密出来的结果总是不同，
     * 这样就不需要用户去额外保存盐的字段了，这一点比 Shiro 要方便很多。
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * WebSecurity 是 HTTPSecurity 超集
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 默认配置为
        //http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic()
        http
                .authorizeRequests()// 开启登录配置
                // 任何符合的path不需要验证, 放行
                .antMatchers("/", "/home").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                // denyAll 拦截所有
//                .antMatchers("...").denyAll()

                // 请求发送的IP匹配时返回true
                // 也支持通配符
                // .antMatchers("/product/**").hasIpAddress('192.168.1.0/24'))

                // admin-page 需要访问者 有 admin role   // 还有 hasAnyRole("", ""...)
                // 角色名默认会带有 "ROLE_"前缀
                // .hasAuthority([authority])  等同于hasRole,但不会带有ROLE_前缀
                .antMatchers("/admin-page").hasRole("ADMIN")

//                .antMatchers("/api/**").authenticated() //    /api/** 下的需要验证
                .anyRequest().authenticated() // 剩余的其他 api 需要验证才能访问

                // 当前用户既不是anonymous也不是rememberMe用户时返回true
                //fullAuthenticated()

                .and()

                // 开启表单登录验证
                //自带表单页面
                .formLogin()
                // 自定义验证表单页面, 拦截得到需要验证的请求, 跳转到这个 api
                .loginPage("/login")
                //自定义登录处理接口
                // .loginProcessingUrl("/doLogin")

                // 登录失败跳转地址
                // .failureUrl( "/login-error" )
                // 成功跳转地址
                // .successForwardUrl("")

                //定义登录时，用户名的 key，默认为 username
                // .usernameParameter("uname")
                //定义登录时，用户密码的 key，默认为 password
                // .passwordParameter("passwd")

                // 不需要session（不创建会话）
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //登录成功的处理器
                // .successHandler(new AuthenticationSuccessHandler() {
                //     @Override
                //     public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                //          //若是前后分离的项目, 返回 json 即可
                //         resp.setContentType("application/json;charset=utf-8");
                //         PrintWriter out = resp.getWriter();
                //         out.write("success");
                //         out.flush();
                //     }
                // })

                // //登录失败处理器
                // .failureHandler(new AuthenticationFailureHandler() {
                //     @Override
                //     public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
                //         resp.setContentType("application/json;charset=utf-8");
                //         PrintWriter out = resp.getWriter();
                //         out.write("fail");
                //         out.flush();
                //     }
                // })
                // 和表单登录相关的接口统统都直接通过放行
                .permitAll()

                .and()

                .logout()

                // 登出接口
                .logoutUrl("/logout")

                // 登出成功后调用
                // .logoutSuccessUrl("/")

                // .logoutSuccessHandler(new LogoutSuccessHandler() {
                //     @Override
                //     public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                //         resp.setContentType("application/json;charset=utf-8");
                //         PrintWriter out = resp.getWriter();
                //         out.write("logout success");
                //         out.flush();
                //     }
                // })

                .permitAll()

                .and()

                //
                // 权限拒绝后跳转页面
                // .exceptionHandling().accessDeniedPage( "/401" )
                // 如果希望拒绝后返回json
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                        httpServletResponse.setContentType("application/json;charset=UTF-8");
                        httpServletResponse.getWriter().write(e.getMessage());
                    }
                });
                // 异常处理
                // .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
                //     @Override
                //     public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                //         httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                //     }
                // });

                // .and()
                // 若不自定义验证接口, 需要打开 httpBasic,
                // .httpBasic()

                // 允许跨域
                // .and().cors()
                // .and()
                // 禁止跨站请求伪造
                // .csrf().disable();

        // 防止H2 web 页面的Frame 被拦截
        http.headers().frameOptions().disable();



        // 自定义权限表达式 (通过 springEL 表达式 提供支持)
        // access("hasRole('JAVA') or hasRole('DOCKER')")

        // 意思就是 去testPermissionEvaluator这个bean里来执行check方法，这里需要注意check 方法必须返回值是boolean
        //access("@testPermissionEvaluator.check(authentication)")
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 在这里配置忽略, 即该地址不走 Spring Security 过滤器链
        web.ignoring().antMatchers("/ignore-page");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                // Spring security 5.0中新增了多种加密方式，强制要求加密
                .passwordEncoder(bCryptPasswordEncoder())
                .withUser("user").password(bCryptPasswordEncoder().encode("user")).roles("USER")
                .and()
                .withUser("root").password(bCryptPasswordEncoder().encode("root")).roles("ADMIN", "USER");

        //使用 数据库 需要注入userDetailsService的实现类
        // auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());

        // 若暂时希望关闭 passwordEncoder, 可以自定义
        // auth.userDetailsService(userService).passwordEncoder(
        //         new PasswordEncoder() {
        //             @Override
        //             public String encode(CharSequence charSequence) {
        //                 return charSequence.toString();
        //             }
        //
        //             @Override
        //             public boolean matches(CharSequence raw, String encoded) {
        //                // raw = DigestUtils.md5DigestAsHex(raw.toString().getBytes());
        //                 return encoded.equals(raw.toString());
        //             }
        //         });
    }

}