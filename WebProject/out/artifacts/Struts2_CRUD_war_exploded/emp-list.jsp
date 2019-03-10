<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/9
  Time: 20:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>list</title>
</head>
<body>
    <table cellspacing="0" cellpadding="10" border="1">
        <thead>
            <tr>
                <td>ID</td>
                <td>FirstName</td>
                <td>LastName</td>
                <td>Email</td>
                <td>Edit</td>
                <td>Delete</td>
            </tr>
        </thead>
        <tbody>
            <s:iterator value="#request.employees">
                <tr>
                    <td>${employeeId}</td>
                    <td>${firstName}</td>
                    <td>${lastName}</td>
                    <td>${email}</td>
                    <td><a href="">Edit</a></td>
                    <td><a href="emp-delete?employeeId=${employeeId}">Delete</a></td>
                </tr>
            </s:iterator>
        </tbody>
    </table>
</body>
</html>
