<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/21
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>请求转发和重定向</title>
</head>
<body>
    <h3>bbb</h3>
<%
    //请求转发
    request.getRequestDispatcher("/c.jsp").forward(request,response);
%>
</body>
</html>
