<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-12-9
  Time: 下午3:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%--
        使用SpringMVC的表单标签
        可以通过modelAttribute属性指定绑定的模型属性，
        若没有指定该属性，则默认从request域对象中读取command的表单bean，如果该属性也不存在，则会发生错误
    --%>
    <form:form method="post" action="${pageContext.request.contextPath}/emp" modelAttribute="employee">
       <c:if test="${employee.id==null}">
           <%--path对应html表单标签的name属性--%>
           LastName:<form:input path="name"></form:input>
       </c:if>
        <c:if test="${employee.id!=null}">
            <form:hidden path="id"></form:hidden>
            <input type="hidden" name="_method" value="PUT">
        </c:if>
        <br>
        Email:<form:input path="email"></form:input>
        <br>
            <%
                Map<String,String> genders = new HashMap<>();
                genders.put("1","Male");
                genders.put("0","Female");
                request.setAttribute("genders",genders);
            %>
        Gender:<form:radiobuttons path="sex" items="${genders}"></form:radiobuttons>
        <br><br>
        Department:<form:select path="department.id" items="${departments}" itemLabel="dept_name" itemValue="id"></form:select>
        <br>
        <input type="submit" value="submit">
    </form:form>
</body>
</html>
