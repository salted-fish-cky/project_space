<%@ page import="com.cky.struts2.demo.Person" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/11/7
  Time: 20:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <s:debug></s:debug>
    <br><br>
    s:property:打印值栈中的属性值的：对于对象栈，打印值栈中对应的属性值
    <br><br>
    <s:property value="productName"></s:property>
    <br><br>
    对于Map栈，打印request，session，application的某个属性值或某个请求参数
    <br><br>
    <s:property value="#session.date"></s:property>
    <br>
    <s:property value="#parameter.name"></s:property>
    <br><br>
    s:url   :创建一个url字符串
    <br><br>
    <s:url value="/testTag.action" var="url">
        <s:param name="productId" value="1001"></s:param>
    </s:url>
    ${url}
    <br><br>
    <s:url value="/testTag.action" var="url2">
        <%--对于value值会自动的解析--%>
        <s:param name="productId" value="productId"></s:param>
    </s:url>
    ${url2}

    <br><br>
    <s:url value="/testTag.action" var="url3">
        <%--对于value值会自动的解析,若不希望进行ognl解析，则使用单引号引起来--%>
        <s:param name="productId" value="'adbdg'"></s:param>
    </s:url>
    ${url3}
    <br><br>
    <%--构建一个请求action的地址--%>
    <s:url value="/testTag.action" namespace="/" method="save" var="url4">
    </s:url>
    ${url4}
    <br><br>
    <s:url value="/testTag.action" var="url5" includeParams="get"></s:url>
    ${url5}

    <br><br>
    s:set:   向page，request，session，application，域对象中加入一个属性值
    <br><br>
    <s:set var="productName" value="productName" scope="request"></s:set>
    productName:${requestScope.productName}
    <br><br>
    s:push:   把一个对象在标签开始后压入值栈中，标签结束时，弹出标签
    <%
        Person person  = new Person();
        person.setAge(10);
        person.setName("zz");
        request.setAttribute("person",person);
    %>
    <br><br>
    <s:push value="#request.person">
        ${name}
        ${age}
    </s:push>

    <br><br>
    s:if ,s:else,s:else if :
    <br><br>
    <%--可以用值栈中的属性--%>
    <s:if test="productPrice>100">aaa</s:if>
    <s:elseif test="productPrice>80">bbb</s:elseif>
    <s:else>ccc</s:else>
    <br><br>
    s:iterator : 便利集合 ，把这个可遍历对象里的每一个元素依次压入和弹出
    <br><br>
    <%
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("aa",10));
        personList.add(new Person("bb",20));
        personList.add(new Person("cc",30));
        personList.add(new Person("dd",40));
        personList.add(new Person("ee",50));
        request.setAttribute("persons",personList);
    %>

    <s:iterator value="#request.persons">
        ${name}-${age}
    </s:iterator>
    <br><br>
    <br><br>
    <br><br>
    <br><br>

</body>
</html>
