package com.cky.demo.servlet;

import com.cky.demo.bean.ShoppingCart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AddToCartServlet",urlPatterns = "/addToCart")
public class AddToCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        int price = Integer.parseInt(request.getParameter("price"));
        HttpSession session = request.getSession();
        ShoppingCart sc = (ShoppingCart) session.getAttribute("sc");
        if(sc == null){
             sc = new ShoppingCart();
             session.setAttribute("sc",sc);
        }
        //把点击的选项加入到购物车中
        sc.addToCart(id,price);
        //准备json对象
        StringBuffer sb = new StringBuffer();
        sb.append("{")
                .append("\"bookName\":\""+id+"\"")
                .append(",")
                .append("\"totalBookNumber\":"+sc.getTotalBookNumber())
                .append(",")
                .append("\"totalBookMoney\":"+sc.getTotalMoney())
                .append("}");

//        response.setCharacterEncoding("utf-8");
        response.setContentType("text/javascript");
        response.getWriter().print(sb.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
