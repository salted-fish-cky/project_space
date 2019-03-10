package com.cky.demo;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "AuthorityFilter")
public class AuthorityFilter extends HttpFilter {


    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //获取servletPath
        String servletPath = request.getServletPath();
        System.out.println(servletPath);
        //不需要拦截的url列表
        List<String> unCheckedUrls = Arrays.asList("/403.jsp", "/articles.jsp", "/login.jsp", "/logout.jsp", "/authority-manager.jsp");
        if(unCheckedUrls.contains(servletPath)){
            chain.doFilter(request,response);
            return;
        }

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return;
        }
        List<Authority> authorities = user.getAuthorities();
        Authority authority = new Authority(null, servletPath);
        if(authorities.contains(authority)){
            chain.doFilter(request,response);
            return;
        }

        response.sendRedirect(request.getContextPath()+"/403.jsp");
        return;

    }
}
