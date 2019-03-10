<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/27
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jQuery/jquery.js"></script>
    <script type="text/javascript">

        $(function () {
            $(":input[name='username']").change(function () {
                var value = $(this).val();
                value = $.trim(value);
                if(value!=null){
                    var url = "${pageContext.request.contextPath}/valiateUserName";
                    var args = {"username":value,"time":new Date()};
                    $.post(url,args,function (data) {
                        $('#message').html(data);
                    });
                }
            });
      });
    </script>
  </head>
  <body>
  <center>
    <form action="" method="post">
      UserName:<input type="text" name="username"><br><br>
      <div id="message"></div>
      <input type="submit">
    </form>
  </center>
  </body>
</html>
