package io.github.xiaoyureed.springsecurityoauth2.resourceserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiaoyu
 * date: 2020/3/24
 */
@SpringBootTest
public class ResourceServerTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMybatis() {
        User user = new User();
        user.setUsername("user");
        List<User> users = userMapper.conditionalQuery(user);
        assertThat(users.size()).isEqualTo(1);
    }
}
