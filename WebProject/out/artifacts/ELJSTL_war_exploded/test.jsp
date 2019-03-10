<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/1
  Time: 20:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--导入标签库（描述文件）--%>
<%@taglib uri="http://baidu.com" prefix="cky"%>
<html>
<head>
    <title>自定义标签测试</title>
</head>
<body>
    <cky:hello/>


    <%--El表达式测试--%>
    <%session.setAttribute("age",18);%>
    <form action="index.jsp" method="post">
        <input type="text" name="username">
        <input type="submit" value="submit">
    </form>
</body>
</html>
