<%--
  Created by IntelliJ IDEA.
  User: cky/
  Date: 2017/9/29
  Time: 21:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>购物</title>
</head>
<body>
    <center>
        <h4>选择要购买的书籍：</h4><br><br>
        <form action="<%=request.getContextPath()%>/proccessStep1" method="post">
            <table border="1" cellpadding="10" cellspacing="0">
                <tr>
                    <td>书名</td>
                    <td>购买</td>
                </tr>
                <tr>
                    <td>Java</td>
                    <td><input type="checkbox" name="book" value="Java"></td>
                </tr>
                <tr>
                    <td>Oracle</td>
                    <td><input type="checkbox" name="book" value="Oracle"></td>
                </tr>

                <tr>
                    <td>JavaScript</td>
                    <td><input type="checkbox" name="book" value="JavaScript"></td>
                </tr>
                <tr>
                    <td>Android</td>
                    <td><input type="checkbox" name="book" value="Android"></td>
                </tr>
                <tr>
                    <td>Jquery</td>
                    <td><input type="checkbox" name="book" value="Jquery"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="submit">
                    </td>
                </tr>
            </table>
        </form>
    </center>
</body>
</html>
