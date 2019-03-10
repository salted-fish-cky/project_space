<%@ page import="com.cky.demo.bean.Customer" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/24
  Time: 19:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
  <head>
    <title>customers操作</title>
  </head>
  <body>
    <center>

      <form method="post" action="query.do">
        <table>
          <tr>
            <td>Name:</td>
            <td><input type="text" name="customerName"/></td>
          </tr>
          <tr>
            <td>Address:</td>
            <td><input type="text" name="address"/></td>
          </tr>
          <tr>
            <td>Phone:</td>
            <td><input type="text" name="phone"/></td>
          </tr>
          <tr>
            <td><input type="submit" value="query"/></td>
            <td><a href="newCustomer.jsp">add a customer</a></td>
          </tr>
        </table>
      </form>

      <br>
      <br>
      <hr>


      <c:if test="${!empty requestScope.customers}">

      <table border="1" cellspacing="0" cellpadding="10">
        <tr>
          <td>Id</td>
          <td>Name</td>
          <td>Address</td>
          <td>Phone</td>
          <td>UPDATE/DELETE</td>
        </tr>
        <c:forEach items="${requestScope.customers}" var="customer">

        <tr>
          <td>${customer.id}</td>
          <td>${customer.name}</td>
          <td>${customer.address}</td>
          <td>${customer.phone}</td>
          <td>
            <c:url value="/edit.do" var="edit">
              <c:param name="id" value="${customer.id}"></c:param>
            </c:url><c:url value="/delete.do" var="delete">
              <c:param name="id" value="${customer.id}"></c:param>
            </c:url>
            <a href="${edit}">UPDATE</a>
              <a class="delete" href="${delete}">DELETE</a>
          </td>
        </tr>
        </c:forEach>
        </c:if>
      </table>
    </center>
  <script type="text/javascript">
    var deletes = document.getElementsByClassName("delete");

    for(var i=0;i<deletes.length;i++){
        deletes[i].onclick = function () {
            var names = this.parentNode.parentNode.childNodes;
            var name = names[3].innerHTML;
            var flag = confirm("确定要删除"+name+"的信息吗？");
            return flag;
        };
    }



  </script>
  </body>
</html>
