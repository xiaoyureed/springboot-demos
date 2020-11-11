package io.github.xiaoyureed.customized_protocol;

import lombok.Data;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/10
 */
@Data
public class CustomProtocol {
    /**
     * 开始标志, 4 byte, 固定值
     *
     * 一般我们的应用于某个端口对外开放，为了防止该端口被意外调用，我们可以在收到报文后，
     * 取前4个字节与魔数比对，如果不相同，则直接拒绝并关闭连接。
     */
    private int flag = Consts.FLAT_START;
    /**
     * 版本号： 1字节，一般是预留字段，为了支持协议升级
     */
    private byte  version = 1;
    /**
     * 序列化算法：1字节，表示如何将java对象转化为二进制数据，以及如何反序列化
     */
    private byte algo;
    /**
     * 指令：1字节，表示该消息的意图，如私聊、群聊、登录等。最多支持256种指令。
     */
    private byte cmd;
    /**
     * 数据长度：4字节，表示该字段后数据部分的长度
     */
    private int len;
    /**
     * 数据：具体数据的内容。每种指令对应的数据是不同的。
     */
    private String content;

    public CustomProtocol(int len, String content) {
        this.len = len;
        this.content = content;
    }

    public CustomProtocol(String content) {
        this.content = content;
        this.len = content.getBytes().length;
    }
}
