<%@ page import="java.util.Date" %>
<%@ page import="java.util.Locale" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/24
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
  <head>
    <title>国际化</title>
  </head>
  <body>
  <%Date date = new Date();
    request.setAttribute("date",date);
    request.setAttribute("salary","12345.67");
  %>
  <%
    String code = request.getParameter("code");
    if(code!= null){
        if("en".equals(code)){
            session.setAttribute("locale", Locale.US);
        }else if("zh".equals(code)){
            session.setAttribute("locale",Locale.CHINA);
        }
    }
  %>
  <c:if test="${sessionScope.locale!=null}">
  <fmt:setLocale value="${sessionScope.locale}"></fmt:setLocale>
  </c:if>

  <%--用fmt标签设置国际化--%>

  <fmt:setBundle basename="i18n"></fmt:setBundle>
    <fmt:message key="date"></fmt:message>:
    <fmt:formatDate value="${requestScope.date}" dateStyle="FULL"></fmt:formatDate>,
    <fmt:message key="salary"></fmt:message>:
    <fmt:formatNumber value="${requestScope.salary}" type="currency"></fmt:formatNumber>

  <br><br>
  <a href="index.jsp?code=zh">中文</a>
  <a href="index.jsp?code=en">English</a>
  </body>
</html>
