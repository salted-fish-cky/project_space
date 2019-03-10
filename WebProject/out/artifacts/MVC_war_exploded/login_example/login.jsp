<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/29
  Time: 19:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
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

        <%String username = (String) session.getAttribute("username");
            if(username == null){
                username = "";
            }
        %>
        <form action="hello.jsp" method="post">
            username:<input type="text" name="username" value="<%=username%>">
            <input type="submit" value="submit">
        </form>
    </center>
</body>
</html>
