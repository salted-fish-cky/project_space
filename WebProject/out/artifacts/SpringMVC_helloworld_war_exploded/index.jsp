<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-12-6
  Time: 下午6:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>


  <a href="testRedirect">testRedirect</a>
  <br><br>
  <a href="testView">TestView</a>
  <br><br>
<%--
  模拟修改操作:
  1.原始数据为：1.Tom ，2.123456 ，3.tom@qq.com
  2.密码不能被修改
  3.表单回显，模拟操作直接在表单对应的属性值



--%>

<form action="testModelAttribute" method="post">
  <input type="hidden" name="id" value="1">
  username:<input type="text" name="username" value="Tom"><br>
  email:<input type="text" name="email" value="tom@qq.com"><br>
  age:<input type="text" name="age" value="13"><br>
  <input type="submit" value="submit">
</form>
  <br><br>
  <a href="testSessionAttributes">testSessionAttributes</a>
  <br><br>
  <a href="testMap">testMap</a>
  <br><br>
  <a href="testModelAndView">testModelAndView</a>
  <br><br>
    <a href="testServletAPI">testServletAPI</a>
  <br><br>
  <form action="testPojo" method="post">
    username:<input type="text" name="username">
    <br>
    password:<input type="password" name="password">
    <br>
    email:<input type="text" name="email">
    <br>
    age:<input type="text" name="age">
    <br>
    city:<input type="text" name="address.city">
    <br>
    province:<input type="text" name="address.province">
    <br>
    <input type="submit" value="submit">
  </form>


  <br><br>
  <a href="testRequestParams?username=cky&age=11">TestRequestParams</a>
  <br><br>
  <form action="testRest/1" method="post">
    <input type="hidden" name="_method" value="PUT">
    <input type="submit" value="TestRest Put" >
  </form>
  <br><br>
  <form action="testRest/1" method="post">
    <input type="hidden" name="_method" value="DELETE">
    <input type="submit" value="TestRest Delete" >
  </form>
  <br><br>
  <form action="testRest" method="post">
    <input type="submit" value="TestRest Post" >
  </form>
  <br><br>

  <a href="testRest/1">Test Rest Get</a>
  <br><br>

  <a href="hello">hello world</a>
  <br><br>
  <a href="testParamsAndHeader?username=cky&age=12">TestParamsAndHeader</a>
  <br><br>
  <a href="testAntPath/ckyzzz">TestAntPath</a>
  <br><br>
  <a href="testPathVariable/1">TestPathVariable</a>
  </body>
</html>
