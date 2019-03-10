package com.cky.demo;

import java.io.IOException;

import java.util.List;

@javax.servlet.annotation.WebServlet(name = "SmallServlet",urlPatterns = "/mall")
public class SmallServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        UserDao userDao = new UserDao();
        List<User> userList = userDao.getAllUser();
        request.setAttribute("user",userList);
        request.getRequestDispatcher("/small.jsp").forward(request,response);


    }

}
