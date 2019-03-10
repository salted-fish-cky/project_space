<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/29
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>信息填写</title>
</head>
<body>
    <center>
        <h4>请输入寄送地址和信用卡信息</h4><br><br>
        <form action="<%=request.getContextPath()%>/proccessStep2" method="post">
            <table border="1" cellspacing="0" cellpadding="10">
                <tr>
                    <td colspan="2">寄送信息</td>
                </tr>
                <tr>
                    <td>姓名：</td>
                    <td><input type="text" name="name"></td>
                </tr>
                <tr>
                    <td>地址：</td>
                    <td><input type="text" name="address"></td>
                </tr>
                <tr>
                    <td colspan="2">信用卡信息</td>
                </tr>
                <tr>
                    <td>种类：</td>
                    <td><input type="radio" name="cardType" value="Visa"/>Visa<br>
                    <input type="radio" name="cardType" value="Master"/>Master
                    </td>
                </tr>
                <tr>
                    <td>卡号：</td>
                    <td><input type="text" name="card"></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="submit"></td>
                </tr>
            </table>
        </form>
    </center>
</body>
</html>
