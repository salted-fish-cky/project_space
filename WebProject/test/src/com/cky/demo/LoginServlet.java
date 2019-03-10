package com.cky.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = "cky";
        String userpass = "123456";
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        if(!username.equals(name)){
            //用户不存在
            //跳到用户登录错误界面
            request.setAttribute("errorMessage","用户不存在");
            request.getRequestDispatcher("loginError.jsp").forward(request,response);
        }else if(!userpass.equals(password)){
            //密码错误
            request.setAttribute("errorMessage","密码错误");
            request.getRequestDispatcher("loginError.jsp").forward(request,response);
        }else{
            //登陆成功
//            response.sendRedirect("http://baidu.com");
            response.sendRedirect("/zz/home.html");
        }
    }
}
