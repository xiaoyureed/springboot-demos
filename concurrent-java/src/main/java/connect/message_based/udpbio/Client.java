package connect.message_based.udpbio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * udp 通信无需建立连接， 所以无法双向通信， 如需双向通信， 两端都必须是 server, 也就是 client 也要有监听端口
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Client {
    public static void main(String[] args) throws Exception {
        int            remotePort          = 9527;// 远程 server 端口
        int            listenPort         = 9528;// 本地监听端口

        DatagramSocket listenSocket  = new DatagramSocket(listenPort);
        byte[]         buffer        = new byte[65507];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);// 接收数据流对象

        DatagramSocket socket   = new DatagramSocket();
        InetAddress    remoteAddr   = InetAddress.getByName("localhost");
        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(">>> client listen on port : " + listenPort);
        while (true) {
            String         line      = systemIn.readLine();
            byte[]         lineBytes = line.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packetToRemote    = new DatagramPacket(lineBytes, lineBytes.length, remoteAddr, remotePort);// 传送数据流对象
            socket.send(packetToRemote);
            System.out.println(">>> send to server: " + line);

            if ("exit".equalsIgnoreCase(line.trim())) {
                System.out.println(">>> Client quit!");
                socket.close();
                listenSocket.close();
                break;
            }

            listenSocket.receive(receivePacket);
            String receiveResponse = new String(receivePacket.getData(),
                    0, receivePacket.getLength(), StandardCharsets.UTF_8);
            System.out.println(">>> receive from server: " + receiveResponse);
        }
    }
}
