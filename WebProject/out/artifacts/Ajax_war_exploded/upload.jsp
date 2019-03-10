<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/17
  Time: 19:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>upload</title>
</head>
<body>
    <center>
        <form action="upload" method="post" enctype="multipart/form-data">
            <input type="file" name="file"><br><br>
            <input type="text" name="filename"><br><br>
            <input type="submit" value="submit">
        </form>
    </center>
</body>
</html>
