package com.cky.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "InsertServlet",urlPatterns = "/insert")
public class InsertServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String nickName = request.getParameter("nickName");
        String gender = request.getParameter("gender");
        String tel = request.getParameter("tel");
        String email = request.getParameter("email");
        User user = new User(account,password,nickName,gender,tel,email);
        UserDao userDao = new UserDao();
        int result = userDao.insertUser(user);
        if(result == 1){
            request.getRequestDispatcher("/success.jsp").forward(request,response);
        }else{
            request.getRequestDispatcher("/error.jsp").forward(request,response);
        }
    }
}
