package connect.remote_call.rmi.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Client {
    public static void main(String[] args) throws Exception {
        Registry       registry = LocateRegistry.getRegistry("localhost");
        String         name     = "BusinessDemo";
        // create a proxy object of BusinessDemo, 在 localhost:1099 上寻找 name=BusinessDemo的对象
        Biz business = (Biz) registry.lookup(name);

        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String command = systemIn.readLine();
            String resp    = null;
            try {
                resp = business.echo(command);
            } catch (Exception e) {
                // IGNORE
            }
            System.out.println(">>> receive from server: "  + resp);

            if (command == null || "quit".equalsIgnoreCase(command.trim())) {
                System.out.println("Client quit!");
                System.exit(0);
            }

        }
    }
}
