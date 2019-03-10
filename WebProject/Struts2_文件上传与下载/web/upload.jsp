<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-11-16
  Time: 下午8:43
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
    <s:actionerror></s:actionerror>
    <br><br>
    <s:fielderror></s:fielderror>
    <br><br>
    <s:form action="testUpload" method="POST" theme="simple"  enctype="multipart/form-data">

        File1:<s:file name="file" label="File1"></s:file>
        Desc1:<s:textfield name="desc[0]" label="Desc1"></s:textfield>
        <br><br>
        File2:<s:file name="file" label="File2"></s:file>
        Desc2:<s:textfield name="desc[1]" label="Desc2"></s:textfield>
        <br><br>
        File3:<s:file name="file" label="File3"></s:file>
        Desc3:<s:textfield name="desc[2]" label="Desc3"></s:textfield>
        <br><br>
        <s:submit></s:submit>
    </s:form>
</body>
</html>
