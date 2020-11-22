package connect.remote_call.webservice;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * server-side service
 *
 * @author xiaoyu
 * @since 1.0
 */
// 在目标目录下生成 book/chapter1/webservice/Business.java 和 book/.../BusinessService.java, 这样就可以编写 client端了
@WebService(name="Business", // webservice name
        serviceName="BusinessService", // class name generated on client
        // package name on client
        // 倒着的， 最后一段 string 是 生成代码所在的 文件夹名
        targetNamespace="http://client.webservice.remote_call.connect_demo.distributed_java.other.concurrentjava.xiaoyureed.github.io/client_generate")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class BusinessImpl implements Business {
    @Override
    public String echo(String message) {
        if("quit".equalsIgnoreCase(message.toString())){
            System.out.println("Server will be shutdown!");
            System.exit(0);
        }
        System.out.println("Message from client: "+message);
        return "Server response："+message;
    }
}
