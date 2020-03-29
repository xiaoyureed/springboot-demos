package io.github.xiaoyureed.springsecuritymybatisplustoken;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author xiaoyu
 * date: 2020/3/22
 */
@SpringBootTest
public class SpringSecurityMybatisPlusTokenAppTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMybatisPlus() {
        User condition = new User();
        condition.setUsername("aa");
        List<User> users = userMapper.conditionalQuery(condition);
        // users.forEach(System.out::println);
        assertThat(users.size()).isEqualTo(1);
    }

}
