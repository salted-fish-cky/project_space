package com.cky.demo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@javax.servlet.annotation.WebServlet(name = "TestServlet",urlPatterns = "/test")
public class TestServlet extends javax.servlet.http.HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //servletConfig 得到servlet配置信息里面的值
//        ServletConfig servletConfig = this.getServletConfig();
//        System.out.println(servletConfig.getInitParameter("name"));
        ServletContext servletContext = this.getServletContext();
        //servletContext 得到web全局的配置信息
        Enumeration<String> initParameterNames = servletContext.getInitParameterNames();


    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.print("post");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.print("get");

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        System.out.println("账号："+name+",密码"+password);

        //登陆后的响应
        String result = "登陆成功";
//        ServletOutputStream out = response.getOutputStream();
//        out.write(result.getBytes());
        //第二种方法输出要设置编码，否则会乱码
        response.setContentType("text/html:charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(result);


    }
}
