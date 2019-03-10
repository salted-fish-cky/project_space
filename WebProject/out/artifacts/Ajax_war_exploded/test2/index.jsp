<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/27
  Time: 15:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>

    <script type="text/javascript" src="../jQuery/jquery.js"></script>
    <script type="text/javascript">
        $(function () {
            var isHasCart = "${sessionScope.sc == null}";
            if(isHasCart){
                $('#cartstatus').hide();
            }else{
                $('#cartstatus').show();
                $('#bookName').html("${sessionScope.sc.bookName}");
                $('#totalBookNumber').html("${sessionScope.sc.totalBookNumber}");
                $('#totalBookMoney').html("${sessionScope.sc.totalMoney}");

            }
            $('a').click(function () {
                $('#cartstatus').show();
                var url = this.href;
                var args = {"time":new Date()};
                $.getJSON(url,args,function (data) {
                    $('#bookName').text(data.bookName);
                   alert("zzz");
                    $('#totalBookNumber').text(data.totalBookNumber);

                    $('#totalBookMoney').text(data.totalBookMoney);


                });
                return false;
            });
        })
    </script>
</head>
<body>
<div id="cartstatus">
    您已将&nbsp;<span id="bookName"></span>&nbsp;加入到购物车，
    购物车中的书有&nbsp;<span id="totalBookNumber"></span>&nbsp;本，
    总价格&nbsp; <span id="totalBookMoney"></span>&nbsp;钱
</div>
<br><br>
    Java&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/addToCart?id=Java&price=100">加入购物车</a><br><br>
    Oracle&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/addToCart?id=Oracle&price=150">加入购物车</a><br><br>
    Struts2&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/addToCart?id=Struts2&price=200">加入购物车</a><br><br>
</body>
</html>
