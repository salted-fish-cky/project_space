<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-11-18
  Time: 下午3:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <s:form action="testToken">
        <s:token></s:token>
        <s:textfield name="username" label="UserName"></s:textfield>
        <s:submit></s:submit>
    </s:form>
</body>
</html>
