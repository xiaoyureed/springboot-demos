package io.github.xiaoyu.shirowebdemo.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Data
public class User {
    private long uid;
    private String name;
    private String nick;
    private String pwd;
    private String salt;
    private Date create;
    private Date update;
    private Set<String> roles;
    private Set<String> perms;

}
