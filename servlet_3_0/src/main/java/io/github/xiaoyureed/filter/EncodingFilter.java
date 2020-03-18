/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed.filter;

import io.github.xiaoyureed.util.StringUtils;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Enumeration;

/**
 * @auther: xiaoyu
 * @date: 2018/10/28 21:10
 * @description:
 */
@WebFilter(filterName = "encoding", urlPatterns = "/*", asyncSupported = true, initParams = {
        @WebInitParam(name = "demoFilterP1", value = "demoFilterV1"),
        @WebInitParam(name = "encoding", value = "utf-8"),
        @WebInitParam(name = "contentType", value = "text/html"),
})
@Log4j2
public class EncodingFilter implements Filter {

    private static String encoding;
    private static String contentType;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug(">>> Enter [encoding] filter init()");

        Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String name  = initParameterNames.nextElement();
            String value = filterConfig.getInitParameter(name);

            log.debug(">>> demo filter param -- [{}]: [{}]", name, value);
        }

        encoding = filterConfig.getInitParameter("encoding");
        contentType = filterConfig.getInitParameter("contentType");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug(">>> Enter [encoding] filter");

        HttpServletRequest  req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // post type
        req.setCharacterEncoding(encoding);
        resp.setContentType(contentType + ";charset=" + encoding);

        // get type
        // 处理 req.getParameter()
        HttpServletRequest reqProxy = (HttpServletRequest) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),// classloader
                new Class[]{HttpServletRequest.class},// parents
                (proxy, method, args) -> {// handler of corresponding method

                    String methodName = method.getName();
                    if ("getParameter".equals(methodName)) {

                        String valueOnUrl = req.getParameter(args[0].toString());
                        if (StringUtils.isBlank(valueOnUrl)) {
                            return valueOnUrl;
                        }

                        String reqType = req.getMethod();
                        if (!"GET".equals(reqType)) {
                            return valueOnUrl;
                        }

                        return new String(valueOnUrl.getBytes("ISO8859-1"), encoding);

                    }

                    try {
                        return method.invoke(req, args);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                }
        );

        chain.doFilter(reqProxy, resp);
    }
}
