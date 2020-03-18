package io.github.xiaoyureed.concurrentjava.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * tcp
 *
 * 可使用 `telnet ip port` 测试
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Server {

    private static final int port = 9527;

    public static void main(String[] args) throws IOException {
        //we need an object that will be used to listen for incoming connections
        IoAcceptor acceptor = new NioSocketAcceptor();// tcp/ip protocol

        // acceptor configuration

        // add filters
        // LoggingFilter: log all info, such as  session created/closed, msg received/sent
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        // 编解码转换 filter
        acceptor.getFilterChain().addLast( "codec",
                new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));

        acceptor.setHandler(new ServerHandler());

        // session settings, in other words, socket settings
        acceptor.getSessionConfig().setReadBufferSize( 2048 );
        //第一个参数定义在确定会话是否空闲时要检查的操作，第二个参数定义在会话被视为空闲之前必须发生的时间长度（以秒为单位）。
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );

        acceptor.bind(new InetSocketAddress(port));
    }
}
