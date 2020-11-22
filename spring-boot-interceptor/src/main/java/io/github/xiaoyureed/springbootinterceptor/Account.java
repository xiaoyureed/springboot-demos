package io.github.xiaoyureed.springbootinterceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private String name;
    private String pwd;
}
