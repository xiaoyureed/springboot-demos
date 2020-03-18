package io.github.xiaoyureed.concurrentjava.other.distributed_java.connect_demo.remote_call.webservice;

import javax.xml.ws.Endpoint;

/**
 * server
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Server {
    public static void main(String[] args) {
        // 发布
        // 在 client 端 的 classpath 路径 下使用 jdk 自带命令 `wsimport -keep http://localhost:9527/BusinessService?wsdl`生成辅助代码，
        // 同时会生成 class 文件， 忽略即可
        Endpoint.publish("http://localhost:9527/BusinessService", new BusinessImpl());
        System.out.println("Server has been started");
    }
}
