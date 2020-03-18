package io.github.xiaoyureed.concurrentjava.other.distributed_java.connect_demo.remote_call.rmi;

import io.github.xiaoyureed.concurrentjava.other.distributed_java.connect_demo.remote_call.rmi.client.Biz;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Server {
    public static void main(String[] args) throws Exception{
        int    port     =9527;
        String name     ="BusinessDemo";
        Biz    business =new BizImpl();
        UnicastRemoteObject.exportObject(business, port);

        Registry registry = LocateRegistry.createRegistry(1099);// rmi 注册对象, 在 1099 上监听
        registry.rebind(name, business);// 接口以 string 形式绑定
    }
}
