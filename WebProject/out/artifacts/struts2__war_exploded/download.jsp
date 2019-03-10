<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-11-17
  Time: 下午7:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <s:debug></s:debug>
    <s:if test="#request.beans!=null&&#request.beans.size()>0">
        <s:iterator value="#request.beans">
            FileName:${fileName}<br>
            Desc:${fileDesc}<br>
            <a href="test_download.action?fileName=${fileName}&filePath=${filePath}">下载</a>
            <br>
            <hr>
        </s:iterator>
    </s:if>
</body>
</html>
