<%@ page import="com.cky.demo.bean.Customer" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/27
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>updateCustomer</title>
</head>
<body>
<center>

    <%String message = (String) request.getAttribute("message");
        if(message!=null){
    %>

    <br>
    <font color="red"><%=message%></font>
    <%    }
        String id = null;
        String name = null;
        String oldName = null;
        String address = null;
        String phone = null;
        Customer customer = (Customer) request.getAttribute("customer");
        if(customer!=null){
            id = customer.getId() + "";
            name = customer.getName();
            oldName = customer.getName();
            address = customer.getAddress();
            phone = customer.getPhone();
        }else{
            id = request.getParameter("id");
            name = request.getParameter("oldName");
            oldName = request.getParameter("oldName");
            address = request.getParameter("address");
            phone = request.getParameter("phone");
        }
    %>
    <form method="post" action="update.do">
        <input type="hidden" name="id" value="<%=id%>"/>
        <input type="hidden" name="oldName" value="<%=oldName%>"/>
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="customerName"
                           value="<%=name%>"/></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><input type="text" name="address"
                           value="<%=address%>"/></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone"
                           value="<%=phone%>"/></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="submit"/></td>
            </tr>
        </table>
    </form>
</center>
</body>
</html>
