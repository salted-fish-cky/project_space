<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/13
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <center>
        <font color="red">${requestScope.message}</font>
        <form action="hello.jsp" method="post">
            <input type="text" name="username" value="${param.username}"><br><br>
            <input type="password" name="password"><br><br>
            <input type="submit" value="登陆">
        </form>
    </center>
</body>
</html>
