/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @auther: xiaoyu
 * @date: 2018/10/29 10:23
 * @description:
 */
@WebListener
@Log4j2
public class DemoListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.debug(">>> ServletContext destroyed");

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug(">>> ServletContext initialized");
    }
}
