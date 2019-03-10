package com.cky.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@javax.servlet.annotation.WebServlet(name = "ServletTest3",urlPatterns = "/test3")
public class ServletTest3 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream resourceAsStream = this.getServletContext().getResourceAsStream("/db.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        String url =  properties.getProperty("url");
        String name =  properties.getProperty("name");
        String password =  properties.getProperty("password");
        System.out.println(url+name+password);
    }
}
