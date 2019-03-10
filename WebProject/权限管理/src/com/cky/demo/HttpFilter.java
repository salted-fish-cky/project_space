package com.cky.demo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的HttpFilter，实现Filter接口
 */
//@WebFilter(filterName = "HttpFilter")
public abstract class HttpFilter implements Filter {
    private FilterConfig filterConfig;
    public void destroy() {
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        doFilter(request,response,chain);
    }

    public abstract void doFilter(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException, IOException;
    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
        init();
    }

    /**
     * 供子类实现初始化的方法，可以通过getFilterConfig()取得FilterConfig对象
     */
    protected void init() {
    }

}
