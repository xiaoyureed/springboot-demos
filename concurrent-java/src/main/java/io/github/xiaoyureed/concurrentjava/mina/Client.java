package io.github.xiaoyureed.concurrentjava.mina;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Client {
    private static final long    TIMEOUT          = 30 * 1000L;
    private static final boolean USE_CUSTOM_CODEC = false;
    private static final int PORT = 9527;
    private static final String HOSTNAME = "localhost";

    public static void main(String[] args) throws Exception {
        NioSocketConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(TIMEOUT);// timeout: 30s

        if (USE_CUSTOM_CODEC) {
            //connector.getFilterChain().addLast("codec",
            //        new ProtocolCodecFilter(new SumUpProtocolCodecFactory(false)));
        } else {
            // object serialization filter
            connector.getFilterChain().addLast("codec",
                    new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        }
        connector.getFilterChain().addLast("logger", new LoggingFilter());

        connector.setHandler(new ClientSessionHandler());
        IoSession session;

        for (; ; ) {
            try {
                // connect is an async task
                ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, PORT));
                future.awaitUninterruptibly();
                session = future.getSession();//Once the connection is complete, we get the associated IoSession
                break;
            } catch (RuntimeIoException e) {
                System.err.println(">>> Failed to connect");
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }

        // wait until the summation is done
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();//Releases any resources allocated by this service
    }
}