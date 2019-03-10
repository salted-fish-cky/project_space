<%@ page import="java.util.Date" %><%--
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
    <h3>ddd</h3>
    <%
        response.sendRedirect("c.jsp");
        Date data = new Date();
        System.out.println(data);
    %>
</body>
</html>
