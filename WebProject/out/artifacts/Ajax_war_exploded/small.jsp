<%@ page import="java.util.List" %>
<%@ page import="com.cky.demo.User" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/24
  Time: 10:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>small</title>
</head>
<body>
<center>
    <%List<User> users = (List<User>) request.getAttribute("user"); %>
    <table border="1" cellpadding="10" cellspacing="0">
        <tr>
            <th>
                id
            </th>
            <th>
                account
            </th>
            <th>
                password
            </th>
            <th>
                nickname
            </th>
            <th>
                gender
            </th>
            <th>
                tel
            </th>
            <th>
                email
            </th>
            <th>
                DELETE
            </th>

        </tr>
        <%for (User user:users){
        %>
        <tr>
            <td>
                <%=user.getId()%>
            </td>
            <td>
                <%=user.getAccount()%>
            </td>
            <td>
                <%=user.getPassword()%>
            </td>
            <td>
                <%=user.getNickName()%>
            </td>
            <td>
                <%=user.getGender()%>
            </td>
            <td>
                <%=user.getTel()%>
            </td>
            <td>
                <%=user.getEmail()%>
            </td>
            <td>
               <a href="delete?id=<%=user.getId()%>">DELETE</a>
            </td>
        </tr>
        <%
            }%>

    </table>
</center>


</body>
</html>
