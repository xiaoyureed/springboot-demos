package connect.remote_call.webservice.client;

import connect.remote_call.webservice.client.client_generate.Business;
import connect.remote_call.webservice.client.client_generate.BusinessService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Client {
    public static void main(String[] args) throws Exception{
        BusinessService businessService =new BusinessService();
        Business        business        =businessService.getBusinessPort();
        BufferedReader  systemIn        =new BufferedReader(new InputStreamReader(System.in));
        while(true){
            String command=systemIn.readLine();
            if(command==null || "quit".equalsIgnoreCase(command.trim())){
                System.out.println("Client quit!");
                try{
                    business.echo(command);
                }
                catch(Exception e){
                    // IGNORE
                }
                System.exit(0);
            }
            System.out.println(business.echo(command));
        }
    }
}
