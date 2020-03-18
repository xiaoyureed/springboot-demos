package io.github.xiaoyureed.concurrentjava.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class ClientSessionHandler extends IoHandlerAdapter {

    /**
     * {@inheritDoc}
     *
     * a connection opened
     * */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        //super.sessionOpened(session);
        readFromCliAndSend(session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        //super.messageReceived(session, message);



    }


    private void readFromCliAndSend(IoSession session) throws IOException {
        BufferedReader readerToCli = new BufferedReader(new InputStreamReader(System.in));
        String         line           = readerToCli.readLine();
        session.write(line);
        System.out.println(">>> send to server: " + line);

        // exit
        if ("exit".equalsIgnoreCase(line.trim())) {
            System.out.println(">>> client closed");
            session.closeNow();
        }

    }
}
