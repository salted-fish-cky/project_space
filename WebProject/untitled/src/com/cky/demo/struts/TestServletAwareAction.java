package com.cky.demo.struts;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.util.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 通过实现servletxxxAware接口的方式可以由Struts2注入需要Servlet相关对象
 */
public class TestServletAwareAction implements ServletRequestAware,ServletContextAware {

    public String execute(){

        return "success";
    }

    private HttpServletRequest request;
    private ServletContext context;
    @Override
    public void setServletRequest(HttpServletRequest request) {
        System.out.println(request);
        this.request = request;
    }

    @Override
    public void setServletContext(ServletContext context) {
        System.out.println(context);
        this.context = context;
    }
}
