/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed;

import io.github.xiaoyureed.listener.AsyncDemoListener;
import lombok.extern.log4j.Log4j2;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @auther: xiaoyu
 * @date: 2018/10/29 13:10
 * @description: complex demo.  The sync thread return sth. to frontend
 */
@WebServlet(urlPatterns = "/asyncReturn", name = "asyncReturn", asyncSupported = true)
@Log4j2
public class AsyncReturnServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.service(req, resp);
        log.debug(">>> Enter [asyncReturn] servlet");

        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(10 * 1000);
        asyncContext.addListener(new AsyncDemoListener());// register async listener
        asyncContext.start(new AsyncProcessor(asyncContext));

        req.getRequestDispatcher("/index.ftl").forward(req, resp);// the template file path begin with "/" or not both ok
    }
}

class AsyncProcessor implements Runnable {
    private AsyncContext asyncContext;

    public AsyncProcessor(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        longProcessing(3 * 1000);
        //ServletRequest req = asyncContext.getRequest();
        //req.setAttribute("", "");
        try {
            PrintWriter writer = asyncContext.getResponse().getWriter();
            writer.write("longProcessing end"); // 会追加到 先一步加载完成的 页面上
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        asyncContext.complete();//异步完成
    }

    private void longProcessing(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
