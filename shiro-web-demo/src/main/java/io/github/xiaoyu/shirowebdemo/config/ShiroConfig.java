package io.github.xiaoyu.shirowebdemo.config;

import io.github.xiaoyu.shirowebdemo.realm.CustomRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Configuration
@Slf4j
public class ShiroConfig {

    @Bean
    public Realm customRealm() {
        return new CustomRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager(Realm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm);
        return securityManager;
    }

    //@Bean
    //public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
    //    DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
    //    //setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
    //    // * 在@Controller注解的类的方法中加入@RequiresRole注解，会导致该方法无法映射请求，导致返回404。
    //    // * 加入这项配置能解决这个bug
    //    creator.setUsePrefix(true);
    //    return creator;
    //}

    /**
     * shiro 默认提供的 realm
     * 类似 shiro.ini 中的配置
     * @return
     */
    //@Bean
    //public Realm textRealm() {
    //    TextConfigurationRealm realm = new TextConfigurationRealm();
    //    realm.setUserDefinitions("joe.coder=password,user\n" +
    //            "jill.coder=password,admin");
    //
    //    realm.setRoleDefinitions("admin=read,write\n" +
    //            "user=read");
    //    realm.setCachingEnabled(true);
    //    return realm;
    //}

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        /*
        * shiro提供和多个默认的过滤器
        * 权限粒度大, 可对多个 controller 限制
        * */
        chainDefinition.addPathDefinition("/login", "anon");// 可以匿名访问
        chainDefinition.addPathDefinition("/logout", "logout");

        // 访问 "/admin/**"的用户必须登陆,且角色为 admin
        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");

        // 访问用户必须登陆, 权限为 "document:read"
        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");

        // 对于其他所有路径, 访问者必须登陆
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }

    @ModelAttribute(name = "subject")// 在 modelAndView 中加入键值对
    public Subject subject(DefaultWebSecurityManager securityManager) {
        SecurityUtils.setSecurityManager(securityManager);
        return SecurityUtils.getSubject();
    }
}
