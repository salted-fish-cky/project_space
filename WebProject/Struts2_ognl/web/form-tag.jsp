<%@ page import="com.cky.struts2.demo.City" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/8
  Time: 19:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <%
        List<City> cities = new ArrayList<>();
        cities.add(new City(1001,"aa"));
        cities.add(new City(1002,"bb"));
        cities.add(new City(1003,"cc"));
        cities.add(new City(1004,"ee"));
        cities.add(new City(1005,"dd"));
        request.setAttribute("cities",cities);
    %>

<%--
    表单标签：
    `1.使用和html的form 标签差不多
    2.Struts2的form标签会生成一个table以进行自动排版
    3.可以对表单提交的值进行回显：从栈顶对象开始匹配属性
--%>
 <s:form action="save">
     <s:textfield name="userName" label="UserName"></s:textfield>
     <s:password name="password" label="password"></s:password>
     <s:textarea name="desc" label="desc"></s:textarea>

     <s:checkbox name="married" label="married"></s:checkbox>

     <s:radio name="gender" list="#{'1':'Male','0':'FeMale'}" label="Gender"></s:radio>
     <s:checkboxlist name="city" list="#request.cities" listKey="cityId" listValue="cityName" label="City"></s:checkboxlist>

     <s:select list="{11,12,13,14,15,16,17,18,19}"
        headerKey=""
        headerValue="请选择"
        name="age"
        label="Age">

         <s:optgroup label="21-30" list="#{21:21}"></s:optgroup>
         <s:optgroup label="31-40" list="#{31:31}"></s:optgroup>
     </s:select>

     <s:submit>submit</s:submit>
 </s:form>
</body>
</html>
