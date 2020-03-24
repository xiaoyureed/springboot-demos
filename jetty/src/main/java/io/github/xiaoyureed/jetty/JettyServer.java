package io.github.xiaoyureed.jetty;

import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * @auther: xiaoyu
 * @date: 2018/10/30 09:45
 * @description:
 */
@Log4j2
public class JettyServer {

    private static final int maxThreads  = 100;
    private static final int minThreads  = 10;
    private static final int idleTimeout = 120;//This value in milliseconds defines how long a
    // thread can be idle before it is stopped and removed
    // from the thread pool
    private static final int port        = 8080;

    private Server server;

    public void start() {
        // setup connector
        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
        server = new Server(threadPool);
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(port);
        server.setConnectors(new Connector[]{serverConnector});

        // setup handler
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(BlockingServlet.class, "/status");
        servletHandler.addServletWithMapping(AsyncServlet.class, "/heavy/async");
        server.setHandler(servletHandler);

        // start
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
