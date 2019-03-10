<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/28
  Time: 21:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>book</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jQuery/jquery.js"></script>
    <%@include file="/Commons/queryCondition.jsp"%>
</head>
<body>
    <center>
        <br><br>
        Title:${book.title}
        <br><br>
        Author:${book.author}
        <br><br>
        Price:${book.price}
        <br><br>
        PublishingDate:${book.publishingDate}
        <br><br>
        Remark:${book.remark}
        <br><br>
        <a href="${pageContext.request.contextPath}/bookServlet?method=getBooks&pageNo=${param.pageNo}">继续购物</a>
    </center>
</body>
</html>
