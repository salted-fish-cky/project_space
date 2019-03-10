package com.cky.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RefreshServlet",urlPatterns = "/refresh")
public class RefreshServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //在jsp里面实现刷新
        String message = "3秒后会自动跳转，如果没有跳转，请点击<a href='/zz/home.html'>跳转链接</a>";
        request.setAttribute("message",message);
        request.getRequestDispatcher("/refresh.jsp").forward(request,response);
    }

    //在servlet里面实现刷新
    private void refresh(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("refresh","3;/zz/home.html");
        response.getWriter().print("3秒后自动刷新");
    }
}
