package com.cky.demo.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ValiateUserNameServlet",urlPatterns = "/valiateUserName")
public class ValiateUserNameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> userNames = Arrays.asList("aaa", "bbb", "ccc");
        String username = request.getParameter("username");
        String result = null;
        if(userNames.contains(username)){
            result = "<font color='red'>该用户名已经被使用</font>";
        }else{
            result = "<font color='green'>该用户名可以使用</font>";

        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        response.getWriter().print(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
