package com.cky.demo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@javax.servlet.annotation.WebServlet(name = "ServletTest1",urlPatterns = "/test1")
public class ServletTest1 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data = "zzz";
//        String ecording = "utf-8";
        ServletContext servletContext = this.getServletContext();
        servletContext.setAttribute("name",data);
//        servletContext.setInitParameter("e",ecording);
    }
}
