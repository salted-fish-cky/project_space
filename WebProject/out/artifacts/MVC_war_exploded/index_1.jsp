<%@ page import="com.cky.demo.bean.Customer" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/24
  Time: 19:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

      <%List<Customer> customers = (List<Customer>) request.getAttribute("customers");
        if(customers!=null&&customers.size()>0){
      %>
      <table border="1" cellspacing="0" cellpadding="10">
        <tr>
          <td>Id</td>
          <td>Name</td>
          <td>Address</td>
          <td>Phone</td>
          <td>UPDATE/DELETE</td>
        </tr>
      <%
            for(Customer customer:customers) {

      %>

        <tr>
          <td><%=customer.getId()%></td>
          <td><%=customer.getName()%></td>
          <td><%=customer.getAddress()%></td>
          <td><%=customer.getPhone()%></td>
          <td><a href="edit.do?id=<%=customer.getId()%>">UPDATE</a>
              <a class="delete" href="delete.do?id=<%=customer.getId()%>">DELETE</a>
          </td>
        </tr>
      <%
            }
        }
      %>
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
