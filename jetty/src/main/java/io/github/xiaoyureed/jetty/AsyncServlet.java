package io.github.xiaoyureed.jetty;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @auther: xiaoyu
 * @date: 2018/10/30 11:21
 * @description:
 */
public class AsyncServlet extends HttpServlet {
    private static String HEAVY_RESOURCE
            = "This is some heavy resource that will be served in an async way";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);

        ByteBuffer content = ByteBuffer.wrap(HEAVY_RESOURCE.getBytes(StandardCharsets.UTF_8));

        AsyncContext        async = req.startAsync();
        ServletOutputStream out   = resp.getOutputStream();
        out.setWriteListener(new WriteListener() {
            @Override
            public void onWritePossible() throws IOException {
                while (out.isReady()) {
                    if (!content.hasRemaining()) {// 如果content没有剩余了
                        resp.setStatus(200);
                        async.complete();
                        return;
                    }
                    out.write(content.get());
                }
            }

            @Override
            public void onError(Throwable t) {
                getServletContext().log("Async Error", t);
                async.complete();
            }
        });
    }
}
