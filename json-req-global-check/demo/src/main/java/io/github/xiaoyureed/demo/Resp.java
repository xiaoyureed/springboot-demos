package io.github.xiaoyureed.demo;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/13
 */
public class Resp {
    public Long code;
    public String errMsg;

    public Resp(long code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public static Resp ok() {
        return new Resp(0, "");
    }

    public static Resp err() {
        return new Resp(1, "error");
    }
}
