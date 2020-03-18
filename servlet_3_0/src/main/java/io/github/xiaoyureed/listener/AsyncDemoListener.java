/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @auther: xiaoyu
 * @date: 2018/10/29 13:00
 * @description:
 */
@Log4j2
public class AsyncDemoListener implements AsyncListener {
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        log.debug(">>> Enter [AsyncDemoListener] listener onComplete()");
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        ServletResponse resp   = event.getAsyncContext().getResponse();
        PrintWriter     writer = resp.getWriter();
        writer.write("async time out");
        writer.flush();
        writer.close();
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        ServletResponse resp   = event.getAsyncContext().getResponse();
        PrintWriter     writer = resp.getWriter();
        writer.write("error on async processing");
        writer.flush();
        writer.close();
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        log.debug(">>> Enter [AsyncDemoListener] listener onStartAsync()");
    }
}
