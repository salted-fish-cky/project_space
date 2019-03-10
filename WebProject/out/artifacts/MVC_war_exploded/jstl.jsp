<%@ page import="com.cky.demo.bean.Customer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/11
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>jstl</title>
</head>
<body>
    <h4>c:url 产生一个url地址，可以Cookie是否可用来进行url重写，对get请求的参数进行</h4>
    <c:url value="/test.jsp" var="testurl">
        <c:param name="name" value="cky"></c:param>
    </c:url>
    url:${testurl}
    <br><br>
    <h4>c:redirect 使当前的JSP页面重定向到指定的页面</h4>
    <%--<jsp:forward page="/test.jsp"></jsp:forward>--%>
    <%--/代表的是web应用根目录--%>
    <%--<c:redirect url="test.jsp"></c:redirect>--%>
    <br><br>

    <h4>c:import 可以包含任何页面到当前页面</h4>
    <c:import url="http://www.baidu.com"></c:import>
    <br><<br>
    <h4>
        c:forTokens,处理字符串，类似于String 的 split（）方法
    </h4>
    <c:set value="a,b,c.d.e.f.g;h;j" var="test" scope="request"></c:set>
    <c:forTokens items="${requestScope.test}" delims="," var="s">
        ${s}<br>
    </c:forTokens>
    <br><br>
    <h4>c:forEach,可以对数组，map，Collection 进行遍历,begin(对于集合begin从0开始计算) end step</h4>
    <%--对数组进行遍历--%>
    <c:forEach begin="1" end="10" step="1" var="i">
        ${i}----
    </c:forEach>

    <%List<Customer> cust = new ArrayList<>();
        cust.add(new Customer("aaa","1"));
        cust.add(new Customer("bbb","2"));
        cust.add(new Customer("ccc","3"));
        cust.add(new Customer("ddd","4"));
        request.setAttribute("customer",cust);
    %>
    <%--对集合进行遍历--%>
    <c:forEach items="${requestScope.customer}" var="cust">
        ${cust.name}---${cust.id}
    </c:forEach>
    <br><br>
    <h4>c:choose,c:when,c:otherwise,可以实现if...else
        其中c:choose 以c:when,c:otherwise的父标签出现
        c:when ,c:otherwise不能脱离c:choose单独使用
    </h4>
    <c:choose>
        <c:when test="${param.age>60}">
            老年
        </c:when>
        <c:when test="${param.age>35}">
            中年
        </c:when>
        <c:when test="${param.age>18}">
            青年
        </c:when>
        <c:otherwise>
            未成年
        </c:otherwise>
    </c:choose>
<br><br>
    <h4>c:if 不可以接else ,但可以把判断的结果储存起来</h4>
    <c:set value="20" var="age" scope="request"></c:set>
    <c:if test="${requestScope.age>18}">成年了</c:if><br>
    <c:if test="${requestScope.age>18}" var="isAdult" scope="request"></c:if>
    isAdult:${requestScope.isAdult}
    <br><br>

    <h4>c:set 可以为域对象赋属性值，其中value支持表达式，
        还可以域对象中的javaBean的熟悉赋值，target,value都指出EL 表达式</h4>
    <c:set value="cky" var="name" scope="page"></c:set>
    <%--等价于pageContext.setAttribute("name","cky")--%>
    name:${pageScope.name}
    <br><br>
    <c:set var="subject" value="${param.subject}" scope="session"></c:set>

    subject:${sessionScope.subject}

    <br><br>
    <%Customer customer = new Customer();
        customer.setId("1001");
        request.setAttribute("customer",customer);
    %>
    ID:${requestScope.customer.id}
    <%--给里面的属性赋值--%>
    <c:set target="${requestScope.customer}" property="id" value="${param.id}"></c:set>
    <br>
    ID:${requestScope.customer.id}
    <br>
    <br>
    <h4>c:remove 移除指定域对象中的指定属性值</h4>
    <c:set value="1888-546-465" var="date" scope="session"></c:set>
    data:${sessionScope.date}
    <c:remove var="date"></c:remove>
    <br>
    data:${sessionScope.date}


    <h4>c:out 可以对特殊字符进行自动转换</h4>
    <%request.setAttribute("book","<<java>>");%>
    book:${requestScope.book}
    <br><br>
    <%--jstl表达式--%>
    book:<c:out value="${requestScope.book} "></c:out>
</body>
</html>
