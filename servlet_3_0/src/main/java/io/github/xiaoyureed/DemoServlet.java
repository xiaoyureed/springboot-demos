package io.github.xiaoyureed;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @auther: xiaoyu
 * @date: 2018/10/26 22:19
 * @description:
 */
@WebServlet(name = "demo", value = "/demo", loadOnStartup = -1,
        initParams = {@WebInitParam(name = "p1", value = "v1"),
                @WebInitParam(name = "p2", value = "v2")})
@Log4j2
public class DemoServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.service(req, resp);
        log.debug(">>> Enter service()");
        log.info(">>> Enter service()");
        ServletContext servletContext = req.getServletContext();

        //@WebServlet 中的 initParams 定义的是 servlet 的 init parameter, so 这里获取不到
        Enumeration<String> initParameterNames = servletContext.getInitParameterNames();
        //log.debug(">>> init param names: {}", initParameterNames.toString());
        while (initParameterNames.hasMoreElements()) {
            String name  = initParameterNames.nextElement();
            String value = servletContext.getInitParameter(name);
            log.debug(">>> init servlet param -- [{}] = [{}]", name, value);
        }

        // do like this, so that we can get the servlet init param
        ServletConfig       servletConfig       = this.getServletConfig();
        Enumeration<String> initParameterNames1 = servletConfig.getInitParameterNames();
        while (initParameterNames1.hasMoreElements()) {
            String name  = initParameterNames1.nextElement();
            String value = servletConfig.getInitParameter(name);
            log.debug(">>> init servlet param -- [{}] = [{}]", name, value);
        }
    }
}
