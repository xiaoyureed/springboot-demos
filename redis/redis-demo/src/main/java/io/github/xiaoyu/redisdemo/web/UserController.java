package io.github.xiaoyu.redisdemo.web;

import io.github.xiaoyu.redisdemo.dto.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author xiaoyu
 * @since 1.0
 */
@RestController
public class UserController {

    @GetMapping("/uid")
    public String getUid(HttpSession session) {
        String uid = (String) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID().toString();
            System.out.println("[x] first access, uid:" + uid);
        }
        System.out.println("[x] get uid:" + uid);
        session.setAttribute("uid", uid);
        return session.getId();
    }

    @GetMapping("/get")
    public User getUser() throws Exception {
        return findUser();
    }

    @GetMapping("/getWithCache")
    @Cacheable("user-key")//缓存到 Redis 中的 key
    public User getUserWithCache() throws Exception {
        return findUser();
    }

    private User findUser() throws InterruptedException {
        User user = new User("IDIDIDID", "Jackson", "mark, mark sdfds");
        Thread.sleep(3000);
        return user;
    }
}
