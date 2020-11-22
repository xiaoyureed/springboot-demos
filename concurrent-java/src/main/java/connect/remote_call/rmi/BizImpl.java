package connect.remote_call.rmi;

import connect.remote_call.rmi.client.Biz;

import java.rmi.RemoteException;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class BizImpl implements Biz {
    @Override
    public String echo(String message) throws RemoteException {
        System.out.println(">>> msg from client: " + message);

        if ("quit".equalsIgnoreCase(message.trim())) {
            System.out.println(">>>server shutdown");
            System.exit(0);
        }

        try {
            return message;
        } finally {
            System.out.println(">>> send to client: " + message);
        }
    }
}
