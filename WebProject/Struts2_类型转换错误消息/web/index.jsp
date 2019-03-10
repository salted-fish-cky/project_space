<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-11-14
  Time: 下午8:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    <title>index</title>
  </head>
  <body>
  <s:debug></s:debug>
    <s:form action="testConversion">
      <s:textfield name="age" label="Age"></s:textfield>
      <s:textfield name="birth" label="Birth"></s:textfield>
      <s:submit value="submit"></s:submit>
    </s:form>
  </body>
</html>
