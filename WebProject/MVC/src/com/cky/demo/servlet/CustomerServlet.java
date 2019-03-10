package com.cky.demo.servlet;

import com.cky.demo.bean.CriteriaCustomer;
import com.cky.demo.bean.Customer;
import com.cky.demo.dao.CustomerDAO;
import com.cky.demo.impl.CustomerDAOJdbcImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

@javax.servlet.annotation.WebServlet(name = "CustomerServlet",urlPatterns = "*.do")
public class CustomerServlet extends javax.servlet.http.HttpServlet {
    private CustomerDAO customerDAO = new CustomerDAOJdbcImpl();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String servletPath = request.getServletPath();
        String methondName = servletPath.substring(1,servletPath.indexOf("."));
        /**
         * 通过反射利用methondName调用对应的方法
         */
        try {
            Method methond = getClass().getDeclaredMethod(methondName, HttpServletRequest.class, HttpServletResponse.class);
            methond.invoke(this,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }

    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forwardPath = "/error.jsp";
        String idStr = request.getParameter("id");
        try{
            Customer customer = customerDAO.get(Integer.parseInt(idStr));
            if(customer!=null){
                forwardPath = "/updateCustomer.jsp";
                request.setAttribute("customer",customer);
            }
        }catch (Exception e){

        }
        request.getRequestDispatcher(forwardPath).forward(request,response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id= request.getParameter("id");
        String name = request.getParameter("customerName");
        String oldName = request.getParameter("oldName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        System.out.println(name);

        if(name == null||name.equals("")){
            String message = "姓名不能为空！";
            request.setAttribute("message",message);
            request.getRequestDispatcher("/updateCustomer.jsp").forward(request,response);
            return;
        }
        if(!name.equalsIgnoreCase(oldName)){
            long count = customerDAO.getCountByName(name);
            if(count>0){
                String message = "用户名"+name+"已被占用，请重新选择！";
                request.setAttribute("message",message);
                request.getRequestDispatcher("/updateCustomer.jsp").forward(request,response);
                return;
            }
        }

        Customer customer = new Customer(name, address, phone);
        customer.setId(Integer.parseInt(id));
        customerDAO.update(customer);
        response.sendRedirect("success.jsp");


    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String idStr = request.getParameter("id");
        int id = 0;
        try{
            id = Integer.parseInt(idStr);
            customerDAO.delete(id);
            response.sendRedirect("query.do");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void query(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String customerName = request.getParameter("customerName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        CriteriaCustomer criteriaCustomer = new CriteriaCustomer(customerName, address, phone);
        /**
         * 模糊查询
         */
        List<Customer> customers = customerDAO.getForListWithCriteriaCustomer(criteriaCustomer);

        request.setAttribute("customers",customers);
        request.getRequestDispatcher("/index.jsp").forward(request,response);
    }

    /**
     * 添加用户
     * @param request
     * @param response
     */
    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("customerName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        if(name == null){
            String message = "姓名不能为空！";
            request.setAttribute("message",message);
            request.getRequestDispatcher("/newCustomer.jsp").forward(request,response);
            return;
        }
        long countByName = customerDAO.getCountByName(name);
        if(countByName>0){
            String message = "用户名"+name+"已被占用，请重新选择！";
            request.setAttribute("message",message);
            request.getRequestDispatcher("/newCustomer.jsp").forward(request,response);
            return;
        }
        Customer customer = new Customer(name, address, phone);
        customerDAO.save(customer);
        response.sendRedirect("success.jsp");
    }


    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request,response);
    }


}
