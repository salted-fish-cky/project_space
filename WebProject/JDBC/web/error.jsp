<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/24
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isErrorPage="true" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <center>
        <h2>Error</h2><br>
        <%=exception.getMessage()%>
    </center>
</body>
</html>
