<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-12-9
  Time: 下午2:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
    <%--
     SpringMVC处理静态资源：
     在SpringMVC的配置文件中配置<mvc:default-servlet-handler/>
    --%>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.js"></script>
    <script type="text/javascript">
        $(function () {
            $(".delete").click(function () {

                var href  = $(this).attr("href");
                $("form").attr("action",href).submit();
                return false;
            })
        })
    </script>
</head>
<body>
    <form method="post" action="">
        <input type="hidden" name="_method" value="DELETE">
    </form>

    <c:if test="${empty requestScope.employees}">
        没有任何员工信息
    </c:if>

    <c:if test="${!empty employees}">
        <table border="1" cellspacing="0" cellpadding="10">
            <tr>
                <th>ID</th>
                <th>LastName</th>
                <th>Email</th>
                <th>Gender</th>
                <th>Department</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
            <c:forEach items="${employees}" var="emp">
                <tr>
                    <td>${emp.id}</td>
                    <td>${emp.name}</td>
                    <td>${emp.email}</td>
                    <td>${emp.sex==0?'Female':'Male'}</td>
                    <td>${emp.department.dept_name}</td>
                    <td><a href="emp/${emp.id}">Edit</a></td>
                    <td><a class="delete" href="emp/${emp.id}">Delete</a></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>


<br><br>
    <a href="emp">Add New Employee</a>
</body>
</html>
