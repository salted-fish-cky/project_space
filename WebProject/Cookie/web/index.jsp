<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/9/28
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>首页</title>
  </head>
  <body>
  <center>
    <%String name = request.getParameter("name");
      if(name!=null&&!name.equals("")){
        Cookie cookie = new Cookie("name", name);
        cookie.setMaxAge(30);
        response.addCookie(cookie);
      }else{
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
          for (Cookie cookie:
               cookies) {
            String cookieName = cookie.getName();
            if(cookieName.equals("name")){
                String value = cookie.getValue();
                name = value;

            }
          }
        }
      }

      if(name!=null&& !name.equals("")){
          out.print("hello:"+name);
      }else{
          response.sendRedirect("login.jsp");
      }
    %>
  </center>

  </body>
</html>
