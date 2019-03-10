<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/21
  Time: 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>请求转发和重定向</title>
</head>
<body>
    <h3>aaaa</h3>
    <%--
    请求转发和重定向：
    本质区别： 请求转发只发出一次请求，而重定向发出多次请求
    1.   请求转发：地址栏是初次发出请求的地址，
         请求的重定向：地址栏不再是初次的那个地址，而是最后响应那个地址

    2.   请求转发：在最终的Servlet中request对象和中转的那个相同
         重定向： 最终的Servlet中request对象和中转不同

    3.   请求转发：只能转发当前web资源
         重定向： 可以定向到任何资源
    --%>
    <a href="/zz/b.jsp">请求转发</a><br>
    <a href="/zz/d.jsp">重定向</a><br>
</body>
</html>
