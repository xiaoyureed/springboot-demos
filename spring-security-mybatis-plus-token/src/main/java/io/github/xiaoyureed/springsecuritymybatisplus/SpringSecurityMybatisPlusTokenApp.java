package io.github.xiaoyureed.springsecuritymybatisplus;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author xiaoyu
 * date: 2020/3/21
 */
@SpringBootApplication
public class SpringSecurityMybatisPlusTokenApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityMybatisPlusTokenApp.class, args);
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

    @Autowired
    private AuthenticationManager       authenticationManager;
    @Autowired
    private UserDetailServiceCustomized userDetailServiceCustomized;
    @Autowired
    private TokenProviderJavaJwtImpl    tokenProviderJavaJwtImpl;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<AuthResp> token(
            @RequestBody AuthReq req) {
        // 构造 authentication token
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
        // 认证
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        // 保存认证信息到 security context
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        // 生成 token
        UserDetails userDetails = userDetailServiceCustomized.loadUserByUsername(req.getUsername());
        return ResponseEntity.ok(tokenProviderJavaJwtImpl.generateToken(userDetails));
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

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private TokenValidateToSessionFilter tokenValidateToSessionFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/f-f", "/authenticate").permitAll()
                .antMatchers("/user-info").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin-info/**").hasRole("ADMIN")
                .anyRequest().authenticated()

                // 注意, 如果是 token 验证, 一定要去掉 httpBasic
                // .and().formLogin()
                // .and().httpBasic()

                //前后分离的app, 不使用 session, 这里配置为无状态; 默认是 ifRequired (如果需要就创建)
                // stateless 也就是server无法保持住登录状态, 每次请求都需要验证一次
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // auth exception handing

                // access denied handing, 比如 角色不对造成的全限不足
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                        httpServletResponse.setContentType("application/json;charset=UTF-8");
                        httpServletResponse.getWriter().write(e.getMessage());
                    }
                })
                // 添加 验证 token 的filter
                // .and().apply(new SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
                // });
                // 推荐这样添加 filter
                .and().addFilterBefore(tokenValidateToSessionFilter, UsernamePasswordAuthenticationFilter.class)

                // 注意: 如果是 token 验证方式, 需要关闭 跨站请求伪造 拦截
                .csrf().disable();


    }

    /**
     * 自定义 authenticationManager
     */
    // @Bean
    // public AuthenticationManager authenticationManager() {
    //     return new AuthenticationManager() {
    //         @Override
    //         public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    //             // 自定义认证逻辑
    //             if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
    //                 throw new RuntimeException(">>> authentication type wrong, there " +
    //                         " is  no UsernamePasswordAuthenticationToken.");
    //             }
    //             UsernamePasswordAuthenticationToken usernamePasswordAuth =
    //                     (UsernamePasswordAuthenticationToken) authentication;
    //             String usernameTobeChecked = (String) usernamePasswordAuth.getPrincipal();
    //             String passwordTobeChecked = (String) usernamePasswordAuth.getCredentials();
    //
    //             // 开始检查
    //             UserDetails userDetails    = userDetailServiceCustomized.loadUserByUsername(usernameTobeChecked);
    //             String      passwordFromDb = userDetails.getPassword();
    //             if (!passwordFromDb.contentEquals(passwordEncoder.encode(passwordTobeChecked))) {
    //                 // AuthenticationException是一个抽象类，因此代码逻辑并不能实例化一
    //                 // 个AuthenticationException异常并抛出，实际上抛出的异常通常是其实
    //                 // 现类，如DisabledException,LockedException,BadCredentialsException
    //                 throw new BadCredentialsException(">>> password wrong");
    //             }
    //             authentication.setAuthenticated(true);
    //             ((UsernamePasswordAuthenticationToken) authentication).setDetails(userDetails);
    //             return authentication;
    //         }
    //     };
    // }

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

}

/**
 * 拦截客户端请求，解析其中的token，复原无状态下的"session"，
 * 让当前请求处理线程中具有认证授权数据，后面的业务逻辑才能执行。
 */
@Slf4j
@Component
class TokenValidateToSessionFilter extends OncePerRequestFilter {

    private static final String REQUEST_HEADER_NAME_TOKEN = "Authorization";

    @Autowired
    private TokenProviderJavaJwtImpl tokenProviderJavaJwtImpl;

    @Autowired
    private UserDetailServiceCustomized userDetailServiceCustomized;

    static {
        log.info(">>> TokenValidateToSessionFilter loaded");
    }


    @Override
    public void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws ServletException, IOException {
        String tokenTobeChecked = req.getHeader(REQUEST_HEADER_NAME_TOKEN);
        if (StringUtils.isStrictNotEmpty(tokenTobeChecked)) {
            String username = tokenProviderJavaJwtImpl.resolveUsername(tokenTobeChecked);
            // 验证 token 中的 username
            UserDetails userDetails = userDetailServiceCustomized.loadUserByUsername(username);

            // 验证 token中的其余部分
            if (tokenProviderJavaJwtImpl.isTokenValid(tokenTobeChecked, userDetails)) {
                //存放认证信息，如果没有走这一步，下面的doFilter就会提示登录了
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails,
                                userDetails.getPassword(), userDetails.getAuthorities()));
                // auth token 似乎无需要加上这一步? //todo
                //usernamePasswordAuthenticationToken
                // .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
        }

        filterChain.doFilter(req, resp);
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

interface ITokenProvider {
    AuthResp generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}

@Component
@Slf4j
class TokenProviderJavaJwtImpl implements ITokenProvider {
    /**
     * 失效时间 1 min
     */
    private static final Long   timeoutInMinutes = 1L;
    /**
     * jwt secret
     */
    private static final String JWT_SECRET       = "hahhoohei";

    private static final String JWT_CLAIM_KEY_USERNAME = "username";

    public AuthResp generateToken(UserDetails userDetails) {
        Date expiresAt = Date.from(LocalDateTime.now().atZone(
                ZoneId.systemDefault()).plusMinutes(timeoutInMinutes).toInstant());
        String jwtToken = JwtUtils.createJwtToken(userDetails.getUsername(), expiresAt, JWT_SECRET);
        return new AuthResp(jwtToken, expiresAt.getTime());
    }

    public String resolveUsername(String token) {
        return JwtUtils.resolveUsername(token, JWT_SECRET, JWT_CLAIM_KEY_USERNAME);
    }

    /**
     * 验证token
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        //todo
        return true;
    }

}

@Slf4j
final class JwtUtils {

    private static final String CLAIM_KEY_USERNAME = "username";

    public static String createJwtToken(String username, Date expiresAt, String jwtSecret) {
        // 构造 jwt header
        HashMap<String, Object> jwtHeader = new HashMap<>(2);
        jwtHeader.put("alg", "HS256");
        jwtHeader.put("typ", "JWT");

        return JWT.create()
                .withHeader(jwtHeader) // header
                // payload
                .withClaim(CLAIM_KEY_USERNAME, username)
                // 签名时间
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(
                        ZoneId.systemDefault()).toInstant()))
                // token 过期时间
                .withExpiresAt(expiresAt)
                // signature
                .sign(Algorithm.HMAC256(jwtSecret));

    }

    public static Map<String, Claim> resolveClaims(String token, String jwtSecret) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getClaims();
        } catch (Exception e) {
            log.error(">>> error of resolve token");
            return null;
        }
    }

    public static String resolveUsername(String token, String jwtSecret, String jwtClaimKeyUsername) {
        Map<String, Claim> claims = resolveClaims(token, jwtSecret);
        if (claims == null) {
            log.error(">>> error of resolve token");
            return "";
        }
        return claims.get(CLAIM_KEY_USERNAME).asString();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthResp {
    private String token;
    /**
     * 何时失效
     */
    private Long   expires;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AuthReq {
    private String username;
    private String password;
}

@Slf4j
final class EncodingUtils {
    private static String computeToken(UserDetails userDetails) {
        ObjectMapper objectMapper = new ObjectMapper();
        // MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
        // 信息摘要是安全的单向哈希函数，它接收任意大小的数据，输出固定长度的哈希值
        // 长度固定为16。
        //https://www.cnblogs.com/androidsuperman/p/10296668.html
        // try {
        //     MessageDigest.getInstance("md5");
        // } catch (NoSuchAlgorithmException e) {
        //     log.error(">>> md5 error");
        //     e.printStackTrace();
        // }

        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(userDetails);
        } catch (JsonProcessingException e) {
            log.error(">>> error of objectMapper write byte array");
            e.printStackTrace();
        }
        return new String(DigestUtils.md5Digest(bytes));
    }

}

