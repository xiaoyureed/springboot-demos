package io.github.xiaoyureed.mockitomybatisplusdemo.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaoyu
 * date: 2020/1/24
 */
@Data
public class BaseResp<T> implements Serializable {
    private String code;

    private String msg;

    private T data;

    public BaseResp(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResp(String code, String msg) {
        this(code, msg, null);
    }

    public BaseResp(T data) {
        this("0", "", data);
    }

}
