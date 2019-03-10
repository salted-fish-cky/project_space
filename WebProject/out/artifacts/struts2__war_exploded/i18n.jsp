<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-11-15
  Time: 下午7:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>i18n</title>
</head>
<body>
    <s:debug></s:debug>
    <br><br>
    <%--中文和英文q切换   在超链接后面加请求参数 request_locale--%>
    <a href="testI18n.action?request_locale=zh_CN">中文</a>
    <a href="testI18n.action?request_locale=en_US">English</a>
    <br><br>
    <s:text name="time">
        <s:param value="date"></s:param>
    </s:text>
    <br><br>
    <s:form action="">
        <%--若label标签使用%{getText('username')}的方式就也可以从国际化资源文件中获取value值--%>
        <s:textfield name="username" label="%{getText('username')}"></s:textfield>
        <s:textfield name="username" key="username"></s:textfield>
        <s:password name="password" key="password"></s:password>

        <s:submit key="submit"></s:submit>
    </s:form>
</body>
</html>
