<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 17-12-9
  Time: 下午2:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#testJson").click(function () {

                var url = this.href;
                var args = {};
                $.post(url,args,function (data) {
                    for(var i =0;i<data.length;i++){
                        var id = data[i].id;
                        var name = data[i].name;
                        var email = data[i].email;
                        alert(id+":"+name+":"+email);
                    }
                })
                return false;
            })
        })
    </script>
  </head>
  <body>
    <a href="emps">List All Employees</a>
    <br><br>
    <a href="testJson" id="testJson">Test Json</a>
    <br><br>

    <form action="testHttpMessageConverter" method="post" enctype="multipart/form-data">
      File:<input type="file" name="file">
      Desc:<input type="text" name="desc">
      <input type="submit" value="submit">
    </form>

  <br><br>
  <a href="testResponseEntity">Test ResponseEntity</a>

  <br><br>

    <form action="testFileUpload" method="post" enctype="multipart/form-data">
      File:<input type="file" name="file">
      Desc:<input type="text" name="desc">
      <input type="submit" value="submit">
    </form>
  </body>
</html>
