<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/31
  Time: 21:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <center>
        <br><br>
            您一共买了${sessionScope.shoppingCart.bookNumber}本书
            <br>
            应付：￥${sessionScope.shoppingCart.totalMoney}

        <br><br>

        <c:if test="${requestScope.errors != null}">
            <font color="red">${requestScope.errors}</font>
        </c:if>
        <form action="bookServlet?method=cash" method="post">
            <table cellpadding="10">
                <tr>
                    <td>信用卡姓名：</td>
                    <td><input type="text" name="username"  ></td>
                </tr>
                <tr>
                    <td>信用卡账号：</td>
                    <td><input type="text" name="accountId"  ></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="submit"></td>
                </tr>
            </table>
        </form>
    </center>
</body>
</html>
