<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/27
  Time: 19:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>addCustomer</title>
</head>
<body>
<center>


    <c:if test="${requestScope.message!=null}">
        <font color="red">${requestScope.message}</font>
    </c:if>
    <br>

    <form method="post" action="addCustomer.do">
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="customerName"
                value="${param.name}"/></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><input type="text" name="address"
                value="${param.address}"/></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone"
                value="${param.phone}"/></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="submit"/></td>
            </tr>
        </table>
    </form>
</center>
</body>
</html>
