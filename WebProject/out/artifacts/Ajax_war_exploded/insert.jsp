<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/24
  Time: 12:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>插入用户</title>
</head>
<body>
<center>
    <h4>插入</h4>
    <form action="insert">
        account:<input type="number" name="account"><br>
        password:<input type="password" name="password"><br>
        nickName:<input type="text" name="nickName"><br>
        gender:<select name="gender">
        <option value ="男">男</option>
        <option value ="女">女</option>

    </select><br>
        tel:<input type="tel" name="tel"><br>
        email:<input type="email" name="email"><br>
        <input type="submit" value="提交">
    </form>
</center>
</body>
</html>
