<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-11-15
  Time: 下午9:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <s:form action="testValidation">
        <s:textfield name="age" label="Age"></s:textfield>
        <s:submit></s:submit>
    </s:form>
</body>
</html>
