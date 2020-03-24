package io.github.xiaoyureed.jetty;

import io.github.xiaoyureed.jetty.JettyServer;
import org.junit.After;
import org.junit.Before;

/**
 * @auther: xiaoyu
 * @date: 2018/10/30 11:40
 * @description:
 */
public class Base {

    JettyServer jettyServer;

    @Before
    public void setup() {
        jettyServer = new JettyServer();
        jettyServer.start();
    }

    @After
    public void end() {
        jettyServer.stop();
    }
}
