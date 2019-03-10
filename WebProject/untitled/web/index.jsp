<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/4
  Time: 20:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <a href="TestActionContext.action?name=cky">TestActionContext page</a><br><br>
  <a href="TestAware.action?name=cky">TestAware page</a><br><br>
  <a href="TestServletAction.action">TestServletAction page</a><br><br>
  <a href="TestServletAware.action">TestServletAware page</a><br><br>
  <a href="login-ui.do">loginUI</a>
  <%Date date = new Date();
    if(date!=null)
      application.setAttribute("date",date);
  %>
  </body>
</html>
