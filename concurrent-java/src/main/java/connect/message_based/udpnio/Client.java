package connect.message_based.udpnio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;

/**
 * DatagramChannel 负责监听端口and读写; ByteBuffer 数据传输
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Client {
    public static void main(String[] args) throws Exception{
        int listenPort=9528;
        int remotePort=9527;

        DatagramChannel receiveChannel=DatagramChannel.open();
        receiveChannel.configureBlocking(false);
        receiveChannel.socket().bind(new InetSocketAddress(listenPort));
        Selector selector= Selector.open();
        receiveChannel.register(selector, SelectionKey.OP_READ);

        DatagramChannel sendChannel =DatagramChannel.open();
        sendChannel.configureBlocking(false);
        sendChannel.connect(new InetSocketAddress("127.0.0.1",remotePort));

        BufferedReader systemIn =new BufferedReader(new InputStreamReader(System.in));

        while(true){
            String command=systemIn.readLine();
            sendChannel.write(Charset.forName("UTF-8").encode(command));
            if("quit".equalsIgnoreCase(command.trim())){
                systemIn.close();
                sendChannel.close();
                selector.close();
                System.out.println("Client quit!");
                System.exit(0);
            }
            int nKeys=selector.select(1000);
            if(nKeys>0){
                for (SelectionKey key : selector.selectedKeys()) {
                    if(key.isReadable()){
                        ByteBuffer buffer= ByteBuffer.allocate(1024);
                        DatagramChannel dc=(DatagramChannel) key.channel();
                        dc.receive(buffer);
                        buffer.flip();
                        System.out.println(Charset.forName("UTF-8").decode(buffer).toString());
                    }
                }
                selector.selectedKeys().clear();
            }
        }
    }
}
