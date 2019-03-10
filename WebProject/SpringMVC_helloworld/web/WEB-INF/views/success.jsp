<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-12-6
  Time: 下午7:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Success</title>
</head>
<body>
    <h4>Success Page</h4>

    time:${requestScope.time}

    <br><br>

    request user:${requestScope.user}

    <br><br>

    session user:${sessionScope.user}
</body>
</html>
