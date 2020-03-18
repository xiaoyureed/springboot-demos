/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @auther: xiaoyu
 * @date: 2018/10/29 00:07
 * @description:
 */
@WebFilter(urlPatterns = "/*", filterName = "secondFilter", asyncSupported = true)
@Log4j2
public class SecondFilter implements Filter {
    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug(">>> Enter [secondFilter] filter [init()]");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug(">>> Enter [secondFilter] filter");

        chain.doFilter(request, response);
    }
}
