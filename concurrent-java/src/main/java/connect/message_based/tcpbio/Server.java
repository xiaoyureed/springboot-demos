package connect.message_based.tcpbio;

import io.github.xiaoyureed.concurrentjava.util.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 这个 server 只支持 单个 client 连接
 *
 * 多个 client 连接 server怎么办? 首先要 pass 掉 "在 server 中 创建多个 socket"的想法, 因为 生成 socket 是非常重的操作, 占
 * 用server资源非常多
 *
 * 通常用采用 "连接池", 好处是 🐶 能够限制创建的 socket个数; 🐶 避免重复创建 socket
 * 但是这种方式还是有问题: 连接池中 socket 总是有限的总有不够用的时候; server 需要设置超时时间, 防
 * 止 server 连带 client挂掉
 *
 * 如果要支持多个 client 连接， 可采用 线程池， 每个 socket 新开一个 thread。 这么做有缺点： 无论是否是有效请求， server
 * 都要耗费一个 thread
 * 味了避免 过多的 thread 耗尽 server的资源， 线程池必须是限定大小的。 <=> 采用 bio 方式的 server 支撑的连接数是有限的
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Server {

    //private ServerSocket serverSocket;
    //private BufferedReader readerFromClient;
    //private PrintWriter writerToClient;
    //
    //public void initBlock(int port) throws IOException {
    //    this.serverSocket = new ServerSocket(port);
    //    this.readerFromClient = new BufferedReader(new InputStreamReader(serverSocket.accept().getInputStream()));
    //}

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 9527;
        ServerSocket socketServer = new ServerSocket(port);
        socketServer.setSoTimeout(60 * 1000); // unit : milliseconds
        System.out.println(">>> server listen on port: " + port);
        Socket socketClient = socketServer.accept(); // start

        BufferedReader readerFromClient = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        //BufferedWriter writerToClient   = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
        PrintWriter writerToClient = new PrintWriter(socketClient.getOutputStream(), true);

        while (true) {
            String line = readerFromClient.readLine(); // start

            if (StringUtils.isBlank(line)) {// read nothing
                Thread.sleep(100);
                continue;
            }
            if ("exit".equalsIgnoreCase(line)) {
                // close
                readerFromClient.close();
                writerToClient.close();
                socketServer.close();

                System.out.println(">>> server exit");
                break;
            }

            System.out.println(">>> msg from client: " + line);

            writerToClient.println(line);
            System.out.println(">>> msg to client: " + line);
            Thread.sleep(100);
        }
        System.exit(0);

    }
}
