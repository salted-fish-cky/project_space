package com.cky.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "UserNameFilter",urlPatterns = "/hello.jsp")
public class UserNameFilter implements Filter {
    public void destroy() {
    }
    private FilterConfig filterConfig;
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String username = filterConfig.getServletContext().getInitParameter("username");
        System.out.println(username);
        String username1 = req.getParameter("username");
        if(!username.equals(username1)){
            String message = "用户名错误！";
            req.setAttribute("message",message);
            req.getRequestDispatcher("/login.jsp").forward(req,resp);
            return;
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
