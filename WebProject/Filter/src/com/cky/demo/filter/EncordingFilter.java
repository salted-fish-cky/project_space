package com.cky.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "EncordingFilter" ,urlPatterns = "/encording/*")
public class EncordingFilter extends HttpFilter {
    private FilterConfig filterConfig;
    private String encording;
    @Override
    protected void init() {
        this.filterConfig = getFilterConfig();
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        encording = filterConfig.getServletContext().getInitParameter("encording");
        request.setCharacterEncoding(encording);
        chain.doFilter(request,response);

    }
}
