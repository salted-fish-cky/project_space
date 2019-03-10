package com.cky.demo.struts;

import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class TestServletActionContextAction {

    public String execute(){
        /**
         * ServletActionContext:可以从中获取当前Action对象需要的一切Servlet api相关的对象，
         *
         */

        HttpServletRequest request =    ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        ServletContext servletContext = ServletActionContext.getServletContext();
        System.out.println(request);
        System.out.println(session);
        System.out.println(servletContext);
        return "success";
    }
}
