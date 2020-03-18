package io.github.xiaoyureed.concurrentjava.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * Request handler: handle all request from client
 *
 * The handler class is a class that must implement the interface IoHandler; IoHandlerAdapter 是 adapter design pattern
 * 的产物
 *
 * @author xiaoyu
 * @since 1.0
 */
public class ServerHandler extends IoHandlerAdapter {
    /**
     * Handle exceptions
     * Should always be defined
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        //super.exceptionCaught(session, cause);

        cause.printStackTrace();
        session.closeNow();
    }

    /**
     * Depending on the protocol codec that you use, the object (second parameter) that
     * gets passed in to this method will be different, as well as the object that you
     * pass in to the session.write(Object) method. If you do not specify a protocol codec,
     * you will most likely receive a IoBuffer object, and be required to write out a IoBuffer object.
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        //super.messageReceived(session, message);

        String receivedStr = message.toString();// 根据 acceptor 中 filter
        if ("exit".equalsIgnoreCase(receivedStr.trim())) {
            session.closeNow();
            System.out.println(">>> connection closed");
            return;
        }

        Date   date        = new Date();
        session.write(date.toString());
        System.out.println(">>> send to client: " + date.toString());
    }

    /**
     * The sessionIdle method will be called once a session has remained idle for the amount of time specified
     * in the call acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );.
     * */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        //super.sessionIdle(session, status);
        System.out.println( ">>> IDLE " + session.getIdleCount( status ));
    }
}
