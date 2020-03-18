package io.github.xiaoyureed;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: xiaoyu
 * @date: 2018/10/28 19:27
 * @description:
 */
@WebServlet(name = "index", value = "/index")// 如果这里配置为 value="/", 所有请求都会被映射到这个servlet --> StackOverflow
@Log4j2
public class IndexServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.service(req, resp);

        String xx = req.getParameter("xx");
        log.debug(">>> params on url -- [xx]: [{}]", xx);

        req.getRequestDispatcher("/WEB-INF/pages/index.html").forward(req, resp);

    }
}
