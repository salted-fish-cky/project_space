<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/7
  Time: 20:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>index</title>
  </head>
  <body>
  <a href="testTag.action?name=cky">testTag</a>
  <a href="emp-input.action">Emp Input Page</a>
  <%
    session.setAttribute("date",new Date());
  %>
  <form action="testTag.action">
    <input type="text" name="username">
    <input type="submit" value="submit">
  </form>
  </body>
</html>
