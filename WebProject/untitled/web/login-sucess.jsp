<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/6
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login-success</title>
</head>
<body>
    Welcome:${sessionScope.get("username")}

    <br><br>

    Count on line:${applicationScope.count}

    <br><br>

    <a href="logout.do">logout</a>
</body>
</html>
