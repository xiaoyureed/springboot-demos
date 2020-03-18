package io.github.xiaoyureed.mockitomybatisplusdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po.User;
import org.springframework.util.StringUtils;

/**
 * @author xiaoyu
 * date: 2020/1/22
 */
public interface UserMapper extends BaseMapper<User> {

    default User conditionalQuery(User condition) {
        if (condition == null) {
            return null;
        }
        return selectOne(new LambdaQueryWrapper<User>()
                .eq(!StringUtils.isEmpty(condition.getName().trim()), User::getName, condition.getName())
                .eq(!StringUtils.isEmpty(condition.getPwd().trim()), User::getPwd, condition.getPwd()));
    }
}
