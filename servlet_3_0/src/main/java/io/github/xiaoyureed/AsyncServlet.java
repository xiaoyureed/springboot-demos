/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed;

import lombok.extern.log4j.Log4j2;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: xiaoyu
 * @date: 2018/10/29 10:40
 * @description: 异步 简单的demo, 异步thread无需返回结果
 */
@WebServlet(name = "async", urlPatterns = "/async", asyncSupported = true)
@Log4j2
public class AsyncServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.service(req, resp);

        log.debug(">>> Enter [async] servlet");

        // 耗时操作新开一个线程
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(10 * 1000);//10s
        asyncContext.start(() -> {
            try {
                Thread.sleep(3 * 1000);
                log.debug(">>> sleep end and [awake]");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        req.getRequestDispatcher("/WEB-INF/pages/index.html").forward(req, resp);
        log.debug(">>> dispatch ok");
        /* 结果
            >>> Enter [encoding] filter
        *   >>> Enter [Second] filter
            >>> Enter [async] servlet
            >>> dispatch ok
            >>> sleep end and [awake]
        * */
    }
}
