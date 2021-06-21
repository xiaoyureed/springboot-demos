package io.github.xiaoyureed.mockitomybatisplusdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiaoyu
 * date: 2020/1/22
 */
@SpringBootTest
@Slf4j
public class MybatisPlusTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMybatisPlus() {
        List<User> users = userMapper.selectList(null);
        log.info(">>> users: {}", users );
        assertThat(users.size()).isEqualTo(5);

        User user = userMapper.selectById("1");
        log.info(">>> user: {}", user);
        assertThat(user.getName()).isEqualTo("Jone");

        user.setName("xiaoyureed");
        user.setId(6L);
        userMapper.insert(user);

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, 6L);
        User userFind = userMapper.selectOne(userLambdaQueryWrapper);
        assertThat(userFind.getName()).isEqualTo("xiaoyureed");

        userMapper.deleteById(6L);
    }

}
