package com.cky.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "PasswordFilter",urlPatterns = "/hello.jsp")
public class PasswordFilter implements Filter {
    public void destroy() {
    }
    private FilterConfig filterConfig;
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String password = filterConfig.getServletContext().getInitParameter("password");
        String password1 = req.getParameter("password");
        if(!password.equals(password1)){
            String message = "密码错误！";
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
