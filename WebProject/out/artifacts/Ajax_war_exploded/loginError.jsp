<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/19
  Time: 19:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <center>
        <h2>用户登录错误界面</h2>
        <div>
            <%--jsp中有9个隐藏对象
            1.request     HttpServletRequest的一个对象
            2.response    HttpServletResponse的一个对象
            3.session     HttpSession的一个对象
            4.application   ServletContext 的一个对象
            5.page          指jsp对应servlet的this
            6.config        指jsp对应servlet的ServletConfig对象
            7.pageContext      页面的上下文，可以从该对象中获取其他8个隐藏对象
            8.out           JspWriter的一个对象
            9.eception
            --%>
            <%--<%=request.getAttribute("errorMessage")%>--%>
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                out.print(errorMessage);
                System.out.println(errorMessage);
            %>
        </div>
    </center>
</body>
</html>
