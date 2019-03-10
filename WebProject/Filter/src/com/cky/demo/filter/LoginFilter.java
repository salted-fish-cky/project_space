package com.cky.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "LoginFilter",urlPatterns = "/login/*")
public class LoginFilter extends HttpFilter {

    //从web.xml文件中获取sessionKey,redirectPage,unChecked
    private String sessionKey;
    private String redirectPage;
    private String unCheckedUrls;
    @Override
    protected void init() {
        ServletContext servletContext = getFilterConfig().getServletContext();
        sessionKey = servletContext.getInitParameter("userSessionKey");
        redirectPage = servletContext.getInitParameter("redirectPage");
        unCheckedUrls = servletContext.getInitParameter("uncheckedUrls");
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //获取请求的url
        String requestUrl = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String servletPath = request.getServletPath();
        System.out.println(requestUrl);
        System.out.println(requestURI);
        System.out.println(servletPath);

        //检查获取的servletPath是否为要检查的url，若是，则放行
        List<String> urls = Arrays.asList(unCheckedUrls.split(","));
        if(urls.contains(servletPath)){
            chain.doFilter(request,response);
            System.out.println("login");
            return;
        }
        //从session中获取sessionKey对应的值
        String user = (String) request.getSession().getAttribute(sessionKey);
        if(user == null){
            response.sendRedirect(request.getContextPath()+redirectPage);
            return;
        }
        chain.doFilter(request,response);
    }
}
