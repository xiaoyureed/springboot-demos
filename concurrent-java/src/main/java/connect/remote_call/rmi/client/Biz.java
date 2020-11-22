package connect.remote_call.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author xiaoyu
 * @since 1.0
 */
public interface Biz extends Remote {
    String echo(String message) throws RemoteException;
}
