package io.github.xiaoyureed;

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
