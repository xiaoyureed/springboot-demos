package io.github.xiaoyureed.mockitomybatisplusdemo.service;

import io.github.xiaoyureed.mockitomybatisplusdemo.mapper.UserMapper;
import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * junit5
 * https://tonydeng.github.io/2017/10/10/junit-5-annotations/
 *
 * 部分 mock, 另一种方法
 */
@SpringBootTest
@Slf4j
class MockitoDemoApplicationTests_1 {

    @Autowired
    private UserService userService;

    /**
     * 注解 @MockBean 由 spring-test 提供
     *
     * 替换 spring context 中的同类型 bean 为 mock, 其他bean不受影响
     */
    @MockBean
    private RedisService redisService;

    @MockBean
    private UserMapper userMapper;

    /**
     * 使用 @MockBean 时不需要
     *
     * 初始化被mock注解标注的变量
     * <p>
     * or 在类上使用 @RunWith(org.mockito.runners.MockitoJUnitRunner.class)
     */
    // @BeforeEach
    // public void initMocks() {
    //     MockitoAnnotations.initMocks(this);
    // }

    @Test
    public void testLogin() {
        when(redisService.saveLoginInfo(any(User.class)))
                .then(invocation -> UUID.randomUUID().toString());

        when(userMapper.conditionalQuery(any(User.class))).then(invocation -> {
            User arg = (User) invocation.getArguments()[0];
            if (arg.getName().contentEquals("xiaoyureed")) {
                return new User();
            }
            return null;
        });
        String token = userService.login("xiaoyureed", "1234");
        assertThat(token).isNotBlank();
        try {
            userService.login("nnn", "123");
        } catch (Exception e) {
            assertThat(e).isNotNull();
        }
    }

}
