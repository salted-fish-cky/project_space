<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/28
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Page</title>
</head>
<body>
    <center>
        <h4>Book Page</h4>
    </center>

    <a href="book.jsp?book=javaWeb">JavaWeb</a><br><br>
    <a href="book.jsp?book=java">java</a><br><br>
    <a href="book.jsp?book=Oracle">Oracle</a><br><br>
    <a href="book.jsp?book=Ajax">Ajax</a><br><br>
    <a href="book.jsp?book=JavaScript">JavaScript</a><br><br>
    <a href="book.jsp?book=Android">Android</a><br><br>
    <a href="book.jsp?book=Jbpm">Jbpm</a><br><br>
    <a href="book.jsp?book=Struts">Struts</a><br><br>
    <a href="book.jsp?book=Spring">Spring</a><br><br>

    <%
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            for(Cookie c:cookies){
                String name = c.getName();
                if(name.startsWith("STUDY_BOOK_")){
                    out.write(c.getValue()+"  ");
                }
            }
        }
    %>
</body>
</html>
