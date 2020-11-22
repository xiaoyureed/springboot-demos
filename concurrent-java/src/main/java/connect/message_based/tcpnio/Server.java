package connect.message_based.tcpnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * tcp + nio
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Server {
    public static void main(String[] args) throws IOException {
        int                 port              = 9527;
        InetSocketAddress   inetSocketAddress = new InetSocketAddress(port);
        ServerSocketChannel ssc               = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(inetSocketAddress);// retrieve server socket and bind to port

        // - register server socket channel with selector;
        //      - `selector.select()` will return directly instead of blocking if `OP_ACCEPT` occurred;
        //      - OP_ACCEPT means the type of the registration; in this case, selector merely reports that a client
        //          attempts a connection to the server; (Other possible options are: OP_CONNECT, which will be used by the client; OP_READ; and OP_WRITE.)
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(">>> server listen on port: " + port);

        outLoop:
        while (true) {
            selector.select(60 * 1000); // block, wait for events recorded on the selector; timeout: 60s

            // work on selected keys
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();// prevent the same key coming up again

                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {// event: the associated client request a connection
                    // retrieve server socket channel from selector
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    // create a socket channel which accepts the connection, creates a standard java socket
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println(">>> connected to: " + socketChannel.socket().getRemoteSocketAddress());

                    // register socket channel with selector, 这个 channel只对 read 感兴趣
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {// event: the server can read
                    SocketChannel sc     = (SocketChannel) key.channel();
                    ByteBuffer    buffer = ByteBuffer.allocate(1024);

                    int readBytes = 0;
                    try {
                        int tmp =  0;
                        try {
                            while ((tmp = sc.read(buffer)) > 0) {
                                readBytes += tmp;
                            }
                        } finally {
                            buffer.flip();
                        }

                        if (readBytes > 0) {
                            String data = StandardCharsets.UTF_8.decode(buffer).toString();
                            System.out.println(">>> receive from client: " + data);

                            if ("exit".equalsIgnoreCase(data)) {
                                sc.close();
                                selector.close();
                                System.out.println(">>> server closed");
                                break outLoop;  // 搬到这里就ok了
                            }

                            sc.write(StandardCharsets.UTF_8.encode(data));
                            System.out.println(">>> send to client: " + data );
                        }
                    } finally {
                        if (buffer != null) {
                            buffer.clear();
                        }
                    }

                    //byte[] data = new byte[numRead];
                    //System.arraycopy(buffer, 0, data, 0, numRead);
                    //java.lang.String data = new java.lang.String(buffer.array());
                    //System.out.println(">>> receive from client: " + data);

                    //if ("exit".equalsIgnoreCase(data)) {
                    //    sc.close();
                    //    selector.close();
                    //    System.out.println(">>> server closed");
                    //    break outLoop;  // 存疑 使用这种方式 client 发送 exit， 这里后面的代码仍旧会执行
                    //}

                    // send msg to client
                    //sc.write(StandardCharsets.UTF_8.encode(data));
                    //System.out.println(">>> send to client: " + data );
                }
            }
        }
    }
}
