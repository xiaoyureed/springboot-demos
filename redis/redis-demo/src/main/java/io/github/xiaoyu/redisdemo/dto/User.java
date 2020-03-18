package io.github.xiaoyu.redisdemo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 必须实现 序列化接口
 * @author xiaoyu
 * @since 1.0
 */
@Data
public class User implements Serializable {
    private String id;
    private String name;
    private String remark;

    public User(String id, String name, String remark) {
        this.id = id;
        this.name = name;
        this.remark = remark;
    }
}
