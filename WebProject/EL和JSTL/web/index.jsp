<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/1
  Time: 20:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>EL表达式使用</title>
  </head>
  <body>
  name:${param.username}<br>
  age:${sessionScope.age}<br>
  <%--用另外一种方式pageContext--%>
  name2:${pageContext.request.getParameter("username")}<br>
  age2:${pageContext.session.getAttribute("age")}
  </body>
</html>
