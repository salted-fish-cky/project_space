<%@ page import="com.cky.demo.bean.Customer" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/27
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>updateCustomer</title>
</head>
<body>
<center>

   <c:if test="${requestScope.message!=null}">
       <br>
       <font color="red">${requestScope.message}</font>

   </c:if>

    <c:set value="${requestScope.customer!=null?requestScope.customer.id:param.id}" var="id"></c:set>
    <c:set value="${requestScope.customer!=null?requestScope.customer.name:param.oldName}" var="oldName"></c:set>
    <c:set value="${requestScope.customer!=null?requestScope.customer.name:param.oldName}" var="name"></c:set>
    <c:set value="${requestScope.customer!=null?requestScope.customer.address:param.address}" var="address"></c:set>
    <c:set value="${requestScope.customer!=null?requestScope.customer.phone:param.phone}" var="phone"></c:set>

    <form method="post" action="update.do">
        <input type="hidden" name="id" value="${id}"/>
        <input type="hidden" name="oldName" value="${oldName}"/>
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="customerName"
                           value="${name}"/></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><input type="text" name="address"
                           value="${address}"/></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone"
                           value="${phone}"/></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="submit"/></td>
            </tr>
        </table>
    </form>
</center>
</body>
</html>
