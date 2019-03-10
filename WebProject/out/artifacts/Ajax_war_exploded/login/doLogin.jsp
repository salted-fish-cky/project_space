<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/13
  Time: 20:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>doLogin</title>
</head>
<body>

<c:set value="${initParam.userSessionKey}" var="sessionKey"></c:set>

<c:choose>
    <c:when test="${!empty param.username}">
        <c:set value="${param.username}" var="USERSESSIONKEY" scope="session"></c:set>
        <c:redirect url="list.jsp"></c:redirect>
    </c:when>
    <c:otherwise>
        <c:redirect url="login.jsp"></c:redirect>
    </c:otherwise>
</c:choose>
</body>
</html>
