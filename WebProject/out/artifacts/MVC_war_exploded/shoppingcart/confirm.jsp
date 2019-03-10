<%@ page import="com.cky.demo.bean.Customer" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/29
  Time: 21:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>确认订单</title>
</head>
<body>
    <%Customer customer = (Customer) session.getAttribute("customer");
        String[] books = (String[]) session.getAttribute("book");
    %>
    <center>
        <h4>确认收货订单</h4><br><br>
        <table border="1" cellpadding="10" cellspacing="0">
            <tr>
                <td>顾客姓名：</td><td><%=customer.getName()%></td>
            </tr>
            <tr>
                <td>地址：</td>
                <td><%=customer.getAddress()%></td>
            </tr><tr>
                <td>卡号：</td>
                <td><%=customer.getCard()%></td>
            </tr><tr>
                <td>卡的种类：</td>
                <td><%=customer.getCardType()%></td>
            </tr><tr>
                <td>购买的书籍：</td>
                <td><%
                 for (String book:books){
                     out.write(book);
                     out.write("<br>");
                 }
                %></td>
            </tr>
        </table>
    </center>

</body>
</html>
