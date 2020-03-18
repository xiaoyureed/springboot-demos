package io.github.xiaoyureed.concurrentjava.other.distributed_java.connect_demo.message_based.tcpbio;

import io.github.xiaoyureed.concurrentjava.util.StringUtils;

import java.io.*;
import java.net.Socket;

/**
 * 系统间进行通信， 需要两个步骤：
 *
 * - 数据传输.  - 借助 tcp/ip, 或 udp/ip 协议
 *
 * - 数据处理（读取or 写入） - 同步io 或者 异步io
 *
 *      - 同步io分为 bio, nio
 *
 *          bio - 当发起io操作, 线程间为阻塞关系, 当前 thread 只有将流读取/写入完毕才会释放资源给其他等待的 thread
 *
 *          nio - 基于"事件驱动" 通过 Reactor模式 实现   thread 间非阻塞, 当 socket 有 stream 可读 或 有 stream 待被 socket
 *          写入时, 发出事件(如 连接建立事件, 流读取事件, 流写入事件), 由操作系统通知 app来处理
 *
 *      - 异步io 分为 aio
 *
 *          aio - 同样基于事件驱动, 通过 Proactor模式实现;
 *          和nio对比, 1. 简化编程, io操作由操作系统完成, app只要调用api即可; 2. 省略了 nio中需要遍历 事件通知队列(Selector)
 *          的时间
 *
 * 这样组合的话, 有四种通信方式: tcp/ip + bio, tcp/ip  + nio, udp/ip + bio, udp/ip + nio
 *
 * *************************************************************************************************
 *
 * 下面是 bio + tcp 方式
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Client {

    //private String url = "127.0.0.1";
    //private int port = 9527;
    private Socket socket;
    private BufferedReader readerOnServer;
    private PrintWriter printerOnServer;
    private BufferedReader readerOnCli;

    public void init(String url, int port) throws IOException {
        socket = new Socket(url, port);
        readerOnServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printerOnServer = new PrintWriter(socket.getOutputStream(), true);
        readerOnCli   = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start() throws IOException {
        while (true) {
            String line = readerOnCli.readLine();
            // exit
            if (testExit(line)) {
                break;
            }

            System.out.println(">>> send to server: " + line);
            // send to server
            printerOnServer.println(line);

            // receive from server
            String respLine = readerOnServer.readLine();
            System.out.println(">>> receive from server: " + respLine);
        }
    }

    private boolean testExit(String test) throws IOException {
        if (StringUtils.isBlank(test) || "exit".equalsIgnoreCase(test)) {
            printerOnServer.write("exit");
            close();
            System.out.println(">>> client exit.");
            return true;
        }
        return false;
    }

    private void close() throws IOException {
        printerOnServer.close();
        readerOnCli.close();
        readerOnServer.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.init("127.0.0.1", 9527);
        client.start();
    }
}
