<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/3
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Trades</title>
</head>
<body>
<center>
    <h4>User:${user.username}</h4>
    <br><br>
    <table>

        <c:forEach items="${user.trades}" var="trade">
            <tr>
                <td>
                    <table cellpadding="10" border="1" cellspacing="0">

                        <tr>
                            <td colspan="3">TradeTime:${trade.tradeTime}</td>
                        </tr>
                        <c:forEach items="${trade.items}" var="item">
                            <tr>
                                <td>${item.book.title}</td>
                                <td>${item.book.price}</td>
                                <td>${item.quantity}</td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br>
                </td>
            </tr>

        </c:forEach>
    </table>

</center>
</body>
</html>
