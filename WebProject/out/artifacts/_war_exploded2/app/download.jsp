<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/23
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Download</title>
</head>
<body>

    <c:if test="${requestScope.beans!=null&&requestScope.beans.size()>0}">
        <c:forEach items="${requestScope.beans}" var="bean">
            <c:if test="bean "></c:if>
            FileName:${bean.file_name}<br>
            Desc:${bean.file_desc}<br>
            <a href="${pageContext.servletContext.contextPath}/download?filename=${bean.file_name}&filepath=${bean.file_path}">下载</a><br>
            <hr>
        </c:forEach>
    </c:if>

</body>
</html>
