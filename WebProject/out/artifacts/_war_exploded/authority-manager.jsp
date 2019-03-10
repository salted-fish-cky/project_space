<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/16
  Time: 21:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>authority-manager</title>
</head>
<body>
    <center>
        <br><br>
        <form action="AuthorityServlet?methond=getAuthorities" method="post">
            name:<input type="text" name="username">
            <input type="submit" value="submit">
        </form>
        <br><br>
        <c:if test="${requestScope.user != null}">
            ${requestScope.user.username}的权限是:
            <br><br>
            <form action="AuthorityServlet?methond=updateAuthorities" method="post">
                <input type="hidden" name="username" value="${requestScope.user.username}"/>
                <c:forEach items="${requestScope.authorities}" var="auth">
                    <c:set var="flag" value="false"></c:set>
                    <c:forEach items="${requestScope.user.authorities}" var="ua">
                        <c:if test="${ua.url == auth.url}">
                            <c:set var="flag" value="true"></c:set>
                        </c:if>
                    </c:forEach>
                    <c:if test="${flag == true}">
                        <input type="checkbox" name="authority" value="${auth.url}" checked="checked">${auth.displayName}
                    </c:if>
                    <c:if test="${flag == false}">
                        <input type="checkbox" name="authority" value="${auth.url}">${auth.displayName}
                    </c:if>
                    <br><br>
                </c:forEach>
                <input type="submit" value="update">
            </form>
        </c:if>
    </center>
</body>
</html>
