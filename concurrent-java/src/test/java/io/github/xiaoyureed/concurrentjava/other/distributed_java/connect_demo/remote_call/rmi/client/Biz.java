package io.github.xiaoyureed.concurrentjava.other.distributed_java.connect_demo.remote_call.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author xiaoyu
 * @since 1.0
 */
public interface Biz extends Remote {
    String echo(String message) throws RemoteException;
}
