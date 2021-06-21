package io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po;

import lombok.Data;

/**
 * @author xiaoyu
 * date: 2020/1/22
 */
@Data
public class User {
    private Long    id;
    private String  name;
    private String  pwd;
    private Integer age;
    private String  email;
}
