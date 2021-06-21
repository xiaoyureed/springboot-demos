package io.github.xiaoyureed.mockitomybatisplusdemo.service;

import io.github.xiaoyureed.mockitomybatisplusdemo.mapper.UserMapper;
import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * junit5
 * https://tonydeng.github.io/2017/10/10/junit-5-annotations/
 */
@SpringBootTest
@Slf4j
class MockitoDemoApplicationTests {

    /**
     * userService 中所有属性都受影响
     *
     * 所有字段都需要 mock, 否则为 null
     */
    @InjectMocks
    private UserService userService;

    /**
     * mock RedisService inside UserService
     */
    @Mock
    private RedisService redisService;

    @Mock
    private UserMapper userMapper;

    /**
     * 通过 @spy 将 spring 自动注入的 taskExecutor (真实的) 填充到 userService 中
     */
    @Spy
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 初始化被mock注解标注的变量
     * <p>
     * or 在类上使用 @RunWith(org.mockito.runners.MockitoJUnitRunner.class)
     */
    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

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
