package com.cky.bookstore.web;

import com.cky.bookstore.domian.ShoppingCart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BookStoreWebUtils {

    /**
     * 从session中获取购物车对象
     * @param request
     * @return
     */
    public static ShoppingCart getShoppingCart(HttpServletRequest request){

        HttpSession session = request.getSession();

        ShoppingCart sc = (ShoppingCart) session.getAttribute("shoppingCart");
        if(sc==null){
            sc = new ShoppingCart();
            session.setAttribute("shoppingCart",sc);
        }

        return sc;

    }
}
