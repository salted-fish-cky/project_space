package com.cky.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(name = "LoginServlet",urlPatterns = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methond = request.getParameter("methond");
        try {
            Method methodName = getClass().getMethod(methond,HttpServletRequest.class,HttpServletResponse.class);
            methodName.invoke(this,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private UserDao userDao = new UserDao();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        User user = userDao.get(username);
        request.getSession().setAttribute("user",user);
        response.sendRedirect(request.getContextPath()+"/articles.jsp");
    }
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //使httpSession失效
        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath()+"/login.jsp");

    }

}
