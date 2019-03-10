package com.cky.bookstore.servlet;

import com.cky.bookstore.domian.*;
import com.cky.bookstore.service.AccountService;
import com.cky.bookstore.service.BookService;
import com.cky.bookstore.service.UserService;
import com.cky.bookstore.web.BookStoreWebUtils;
import com.cky.bookstore.web.CriteriaBook;
import com.cky.bookstore.web.Page;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "BookServlet",urlPatterns = "/bookServlet")
public class BookServlet extends HttpServlet {

    private BookService bookService = new BookService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        try {
            Method declaredMethod = getClass().getDeclaredMethod(method, HttpServletRequest.class, HttpServletResponse.class);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public void getBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNoStr = request.getParameter("pageNo");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        int pageNo = 1;
        int minPrice = 0;
        int maxPrice = Integer.MAX_VALUE;

        try{
            pageNo = Integer.parseInt(pageNoStr);
        }catch (Exception e){

        }try{
            minPrice = Integer.parseInt(minPriceStr);
        }catch (Exception e){

        }try{
            maxPrice = Integer.parseInt(maxPriceStr);
        }catch (Exception e){

        }
        CriteriaBook book = new CriteriaBook(minPrice,maxPrice,pageNo);
        Page<Book> page = bookService.getPage(book);
        request.setAttribute("page",page);
        request.getRequestDispatcher("/WEB-INF/pages/books.jsp").forward(request,response);

    }


    public void getBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int id = -1;
        Book book = null;
        try {
            id = Integer.parseInt(idStr);

        }catch (Exception e){

        }

        if(id>0){
            book = bookService.getBook(id);
        }

        if(book == null){
            response.sendRedirect(request.getContextPath()+"/error-1.jsp");
            return;
        }
        request.setAttribute("book",book);
        request.getRequestDispatcher("/WEB-INF/pages/book.jsp").forward(request,response);
    }



    public void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取商品的id
        String idStr = request.getParameter("id");

        boolean flag = false;
        int id = -1;
        try {
            id = Integer.parseInt(idStr);
        }catch (Exception e){

        }

        if(id>0){

            //2.获取购物车对象
            ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
            //3.调用BookService中的方法，把商品放到购物车中
            flag =  bookService.addToCart(id,sc);

            if(flag){
                //4.转发页面到getBooks
                System.out.println(sc.getBookNumber());
                getBooks(request,response);
                System.out.println("zzzzz");
                return;
            }
        }

       response.sendRedirect(request.getContextPath()+"/error-1.jsp");
    }


    public void forwardPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("page");
        request.getRequestDispatcher("/WEB-INF/pages/"+page+".jsp").forward(request,response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");

        int id = -1;
        try{
            id = Integer.parseInt(idStr);
        }catch (Exception e){}

        ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
        bookService.removeItemFromShoppingCart(sc,id);
        if(sc.isEmpty()){
            request.getRequestDispatcher("/WEB-INF/pages/emptyCart.jsp").forward(request,response);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request,response);

    }

    public void clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
        bookService.clearShoppingCart(sc);
        request.getRequestDispatcher("/WEB-INF/pages/emptyCart.jsp").forward(request,response);

    }
    public void updateItemQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String quantityStr = request.getParameter("quantity");
        ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
        int id = -1;
        int quantity = -1;
        try {
            id = Integer.parseInt(idStr);
            quantity = Integer.parseInt(quantityStr);
        }catch (Exception e){}

        if(id>0&&quantity>0){
            bookService.updateItemQuantity(sc,id,quantity);

            //传回json数据
            Map<String,Integer> result = new HashMap<>();
            result.put("bookNumber",sc.getBookNumber());
            result.put("totalMoney", (int) sc.getTotalMoney());

            Gson gson = new Gson();
            String jsonStr = gson.toJson(result);

            response.setContentType("text/javascript");
            response.getWriter().print(jsonStr);
        }
    }

    private UserService userService = new UserService();
    public void cash(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //简单验证表单域是否符合基本规范
        String username = request.getParameter("username");
        String accountId = request.getParameter("accountId");

        StringBuffer error = validateFormField(username, accountId);

        if(error.toString().equals("")){
            error = validateUser(username,accountId);
            if(error.toString().equals("")){
                error = validateBookStoreNumber(request);
                if(error.toString().equals("")){
                    error = validateBalance(request,accountId);
                }
            }
        }

        if(!error.toString().equals("")){
            request.setAttribute("errors",error);
            request.getRequestDispatcher("/WEB-INF/pages/cash.jsp").forward(request,response);
            return;
        }

        //验证通过具体的逻辑操作
        System.out.println("--------");
        bookService.cash(BookStoreWebUtils.getShoppingCart(request),username,accountId);
        System.out.println("--------");
        response.sendRedirect(request.getContextPath()+"/success.jsp");

    }

    AccountService accountService = new AccountService();

    /**
     * 验证余额是否充足
     * @param accountId
     * @return
     */
    private StringBuffer validateBalance(HttpServletRequest request,String accountId){
        StringBuffer error = new StringBuffer();
        ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);

        Account account = accountService.getAccount(Integer.parseInt(accountId));
        if(sc.getTotalMoney()>account.getBalance()){
            error.append("余额不足");
        }
        return  error;
    }

    /**
     * 验证库存是否充足
     * @param request
     * @return
     */
    private StringBuffer validateBookStoreNumber(HttpServletRequest request){
        StringBuffer error = new StringBuffer();
        ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
        for(ShoppingCartItem item:sc.getItems()){
            int quantity = item.getQuantity();
            int storeNumber = bookService.getBook(item.getBook().getId()).getStoreNumber();
            if(quantity > storeNumber){
                error.append(item.getBook().getTitle()+"库存不足<br>");
            }
        }
        return error;
    }

    /**
     * 验证用户名和账号是否匹配
     * @param username
     * @param accountId
     * @return
     */
    private StringBuffer validateUser(String username,String accountId){
        boolean flag = false;
        User user = userService.getUserByUserName(username);
        if(user!=null){
            int accountId1 = user.getAccountId();
            if(accountId.trim().equals(accountId1+"")){
                flag = true;
            }
        }
        StringBuffer error2 = new StringBuffer();
        if(!flag){
            error2.append("用户名和账号不匹配");
        }

        return error2;

    }

    /**
     * 验证表单域是否符合规则：是否为空
     * @param username
     * @param accountId
     * @return
     */
    private StringBuffer validateFormField(String username, String accountId) {
        StringBuffer error = new StringBuffer();
        if(username == null||username.trim().equals("")){
            error.append("用户名不能为空<br>");
        }
        if(accountId == null||accountId.trim().equals("")){
            error.append("账号不能为空");
        }
        return error;
    }
}
