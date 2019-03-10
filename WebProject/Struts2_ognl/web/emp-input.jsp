<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/9
  Time: 18:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Example</title>
</head>
<body>
    <s:form action="emp-save">
            <s:textfield name="name" label="Name"></s:textfield>
            <s:password name="password" label="Password"></s:password>

            <s:radio list="#{'1':'Male','0':'Female'}" name="gender" label="Gender"></s:radio>

            <s:select list="#request.depts" listKey="deptId" listValue="deptName" name="dept" label="Department">
            </s:select>

            <s:checkboxlist list="#request.roles" listKey="roleId" listValue="roleName" label="Roles" name="roles"></s:checkboxlist>

            <s:textarea name="desc" label="Desc"></s:textarea>

            <s:submit value="submit"></s:submit>
    </s:form>
</body>
</html>
