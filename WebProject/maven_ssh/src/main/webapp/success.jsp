<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-12-13
  Time: 下午8:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<s:debug></s:debug>
   <br><br>

<s:if test="user!=null">
    昵称：${user.nickname}
    账户：${user.tel}
    性别：${user.gender}
    邮箱：${user.email}
</s:if>
</body>
</html>
