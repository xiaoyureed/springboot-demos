package io.github.xiaoyureed.springbootjpahibernatevalidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resp<T> {
    private Integer code;
    private String errorMsg;
    private T data;

    public static Resp<?> ok() {
        Resp<?> resp = new Resp<>();
        resp.setCode(0);
        resp.setErrorMsg("");
        return resp;
    }

    public static <T> Resp<T> ok(T data) {
        return new Resp<>(0, "", data);
    }

    public static Resp<?> error(String errorMsg) {
        Resp<?> resp = new Resp<>();
        resp.setCode(1);
        resp.setErrorMsg(errorMsg);
        return resp;
    }
}
