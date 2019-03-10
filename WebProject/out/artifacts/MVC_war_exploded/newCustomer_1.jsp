<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/27
  Time: 19:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>addCustomer</title>
</head>
<body>
<center>

    <%String message = (String) request.getAttribute("message");
        if(message!=null){
    %>
    <br>
    <font color="red"><%=message%></font>
    <%    }
    %>
    <form method="post" action="addCustomer.do">
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="customerName"
                value="<%=request.getParameter("customerName")==null?"":request.getParameter("customerName")%>"/></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><input type="text" name="address"
                value="<%=request.getParameter("address")==null?"":request.getParameter("address")%>"/></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone"
                value="<%=request.getParameter("phone")==null?"":request.getParameter("phone")%>"/></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="submit"/></td>
            </tr>
        </table>
    </form>
</center>
</body>
</html>
