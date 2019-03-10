<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/17
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

    <center>
        <form action="LoginServlet?methond=login" method="post">
            name:<input type="text" name="username">
            <input type="submit" value="submit">
        </form>
    </center>

</body>
</html>
