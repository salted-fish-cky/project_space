<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/29
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logout</title>
</head>
<body>
    <center>
        SessionId:<%=session.getId()%>
        <br><br>
        isNew:<%=session.isNew()%>
        <br><br>
        MaxInactiveInterval:<%=session.getMaxInactiveInterval()%>
        <br><br>
        createTime:<%=session.getCreationTime()%>
        <br><br>
        lastAccessTime:<%=session.getLastAccessedTime()%>
        <br><br>

        Bye:<%=session.getAttribute("username")%>
        <br><br>
        <a href="login.jsp">重新登陆</a>

        <%session.invalidate();%>
    </center>
</body>
</html>
