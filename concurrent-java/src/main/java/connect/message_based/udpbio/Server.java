package connect.message_based.udpbio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Server {
    public static void main(String[] args) throws Exception{
        int            listenPort          =9527;
        int            remotePort         =9528;
        DatagramSocket server        =new DatagramSocket(listenPort);
        DatagramSocket client        =new DatagramSocket();
        InetAddress    serverAddress =InetAddress.getByName("localhost");
        byte[]         buffer        =new byte[65507];
        DatagramPacket packet        =new DatagramPacket(buffer,buffer.length);
        System.out.println(">>> server listen on port : " + listenPort);
        while(true){
            server.receive(packet);
            String line=new String(packet.getData(),0,packet.getLength(), StandardCharsets.UTF_8);
            if("exit".equalsIgnoreCase(line.trim())){
                server.close();
                client.close();
                System.exit(0);
            }
            else{
                System.out.println(">>> Message from client: "+ line);
                packet.setLength(buffer.length);
                byte[] lineBytes=line.getBytes(StandardCharsets.UTF_8);
                DatagramPacket responsePacket=new DatagramPacket(lineBytes,lineBytes.length,serverAddress,remotePort);
                client.send(responsePacket);
                System.out.println(">>> send to client: " + line);
                Thread.sleep(100);
            }
        }
    }
}
