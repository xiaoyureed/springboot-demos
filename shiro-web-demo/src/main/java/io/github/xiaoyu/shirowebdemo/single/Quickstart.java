package io.github.xiaoyu.shirowebdemo.single;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Simple Quickstart application showing how to use Shiro's API.
 *
 * @since 0.9 RC2
 */
public class Quickstart {

    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);


    public static void main(String[] args) {

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);

        // Now that a simple Shiro environment is set up, let's see what you can do:

        // get the currently executing user:
        // 只是一个 指代，具体是谁还需要后面确定
        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        // 这里的 session 类似 HttpSession， 但是 不必在 HTTP environment!
        // 非 web 环境下 ，shiro 默认使用 这个 session
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("[x] Retrieved the correct value! [" + value + "]");
        }

        // let's login the current user so we can check against roles and permissions:
        if (!currentUser.isAuthenticated()) {// 如果还没有认证，进入认证逻辑
            UsernamePasswordToken token = new UsernamePasswordToken("xiaoyu", "123456");
            token.setRememberMe(true);// 记住我
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {// 没有这个账号
                log.info("[x] There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {// 密码错误
                log.info("[x] Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {// 账户锁了
                log.info("[x] The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {// 关于认证的顶级exception
                //unexpected condition?  error?
            }
        }

        //say who they are:
        //print their identifying principal (in this case, a username):
        // getPrincipal() -> 获取当前 subject name
        log.info("[x] User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        if (currentUser.hasRole("admin")) {
            log.info("[x] current user 'xiaoyu' is a [admin]!");
        } else {
            log.info("[x] 'xiaoyu' is not a [admin]");
        }

        //test a typed permission (not instance-level)
        if (currentUser.isPermitted("hahah:hahsdf")) {
            log.info(">> 'xiaoyu' has permission [any permission]");
        } else {
            log.info(">> 'xiaoyu' has no permission [any permission]");
        }

        //all done - log out!
        currentUser.logout();

        System.exit(0);
    }
}