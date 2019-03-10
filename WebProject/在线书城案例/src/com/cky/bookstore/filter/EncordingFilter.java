package com.cky.bookstore.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "EncordingFilter",urlPatterns = "/*")
public class EncordingFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String encording = filterConfig.getServletContext().getInitParameter("encording");
        req.setCharacterEncoding(encording);
        chain.doFilter(req, resp);

    }

    private FilterConfig filterConfig = null;
    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
