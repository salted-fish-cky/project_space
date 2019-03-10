<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/29
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello</title>
</head>
<body>
    <CENTER>
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
        Hello:<%=request.getParameter("username")%>
        <%session.setAttribute("username",request.getParameter("username"));%>
        <br><br>
        <a href="login.jsp">重新登陆</a>
        &nbsp;&nbsp;&nbsp;
        <a href="logout.jsp">注销</a>
    </CENTER>
</body>
</html>
