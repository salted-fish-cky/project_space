<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/28
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Detail Page</title>
</head>
<body>

    <center>
        <h4>Book Detail Page</h4><br>
        Book:<%=request.getParameter("book")%>
        <br><br>
        <a href="books.jsp">return</a>
        <%
            String book = request.getParameter("book");

            Cookie[] cookies = request.getCookies();
            List<Cookie> cookieList = new ArrayList<>();
            Cookie tempCookie = null;
            if(cookies!=null&&cookies.length>0){
                for(Cookie c:cookies){
                    String name = c.getName();
                    if(name.startsWith("STUDY_BOOK_")){
                        cookieList.add(c);
                        if(c.getValue().equals(book)){
                            tempCookie = c;
                        }
                    }
                }
            }

            if(cookieList.size()>=5&&tempCookie==null){
                tempCookie = cookieList.get(0);
            }
            if(tempCookie!=null){
                tempCookie.setMaxAge(0);
                response.addCookie(tempCookie);
            }

            Cookie cookie = new Cookie("STUDY_BOOK_" + book, book);
            response.addCookie(cookie);

        %>
    </center>
</body>
</html>
