package com.cky.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@WebServlet(name = "AuthorityServlet",urlPatterns = "/AuthorityServlet")
public class AuthorityServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methond = request.getParameter("methond");
        try {
            Method methodName = getClass().getMethod(methond,HttpServletRequest.class,HttpServletResponse.class);
            methodName.invoke(this,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private UserDao userDao = new UserDao();
    public void getAuthorities(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        User user = userDao.get(username);
        request.setAttribute("user",user);
        request.setAttribute("authorities",userDao.getAuthorities());
        request.getRequestDispatcher("/authority-manager.jsp").forward(request,response);
    }
    public void updateAuthorities(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String[] authority =request.getParameterValues("authority");

        List<Authority> authorities = userDao.getAuthorities(authority);
        userDao.update(username,authorities);
        response.sendRedirect(request.getContextPath()+"/authority-manager.jsp");
    }
}
