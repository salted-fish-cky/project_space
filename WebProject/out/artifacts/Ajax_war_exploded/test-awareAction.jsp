<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/5
  Time: 21:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h4>AwareAction page</h4>
    application:${applicationScope.applicationKey}<br>
    session:${sessionScope.sessionKey}<br>
    request:${requestScope.requestKey}<br>
</body>
</html>
