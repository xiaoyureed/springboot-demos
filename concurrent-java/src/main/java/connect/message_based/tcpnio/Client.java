package connect.message_based.tcpnio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 基于消息的方式实现通信;
 * 数据传输: tcp/ip
 * 数据处理: nio
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Client {

    //private Selector selector;
    //
    //public void init(String ip, int port) throws IOException {
    //    SocketChannel socketChannel = SocketChannel.open();
    //    socketChannel.configureBlocking(false); // configure channel as non blocking
    //    socketChannel.connect(new InetSocketAddress(ip, port));// 对于 non blocking io, 立即返回 false, 表示连接建立中
    //                                            // 用channel.finishConnect();才能完成连接
    //    Selector selector = Selector.open();
    //    socketChannel.register(selector, SelectionKey.OP_CONNECT);
    //}
    //
    //public void listen() {
    //
    //}

    public static void main(String[] args) throws IOException {
        String ip = "127.0.0.1";
        int port = 9527;

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false); // configure channel as non blocking
        // 会立即返回 false, 表示连接建立中; 调用channel.finishConnect()才能完成连接
        socketChannel.connect(new InetSocketAddress(ip, port));

        // register socket channel with selector, 并指定只对 连接 感兴趣
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        BufferedReader readerOnCli = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (socketChannel.isConnected()) {// connection is established; send msg to server
                String line = readerOnCli.readLine();
                socketChannel.write(StandardCharsets.UTF_8.encode(line));

                if ("exit".equalsIgnoreCase(line)) {
                    // exit
                    readerOnCli.close();
                    socketChannel.close();
                    selector.close();
                    System.out.println(">>> client exit.");
                    break;
                }
            }

            // receive from server
            selector.select(60 * 1000);// timeout = 60 s
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();// remove current key from key set to avoid repetition

                if (key.isConnectable()) {// event: 连接事件
                    SocketChannel sc = (SocketChannel) key.channel();
                    sc.configureBlocking(false);
                    // - 这时, sc 对 read 事件也感兴趣了;
                    // - 一般不会直接注册 write 事件 因为在 buffer 未满时一直是可写的, 因此如果在注册了 write 事件而又不使用
                    //   它时 cpu 消耗可能会100%
                    sc.register(selector, SelectionKey.OP_READ);

                    if (sc.isConnectionPending()) {// 如果连接还没完成，则完成连接的建立
                        sc.finishConnect();
                    }
                } else if (key.isReadable()) {// reading event
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer    buffer = ByteBuffer.allocate(1024);

                    int numRead = 0;
                    try {
                        int tmp = 0;
                        try {
                            // read 操作是 block 的， 读取到末尾， 返回 -1
                            while ((tmp = sc.read(buffer)) > 0) {
                                numRead += tmp;
                            }
                        } finally {
                            buffer.flip();
                        }

                        if (numRead > 0) {
                            System.out.println(">>> msg from server: " + StandardCharsets.UTF_8.decode(buffer).toString());
                            buffer = null;
                        }
                    } finally {
                        if (buffer != null) {
                            buffer.clear();
                        }
                    }

                }
            }

        }
    }
}
