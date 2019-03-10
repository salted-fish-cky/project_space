<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/29
  Time: 19:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>cart</title>

    <script type="text/javascript" src="${pageContext.request.contextPath}/jQuery/jquery.js"></script>
    <script type="text/javascript">
        $(function () {

            $(".delete").click(function () {

                var $tr = $(this).parent().parent();
                var title = $.trim($tr.find("td:first").text());
                var flag = confirm("您确定要删除"+title+"的信息吗？");
                if(flag){
                    return true;
                }
                return false;
            });

            //ajax修改商品数量
            $(":text").change(function () {

                var $tr = $(this).parent().parent();
                var title = $.trim($tr.find("td:first").text());

                var quantityVal = $.trim(this.value);
                var reg = /^\d+$/g;
                var quantity = -1;
                var flag1 = false;
                if(reg.test(quantityVal)){
                    quantity = parseInt(quantityVal);
                    if(quantity>=0){
                        flag1 = true;
                    }
                }

                if(!flag1){
                    alert("输入的数量不合格！");
                    $(this).val($(this).attr("class"));
                    return;
                }

                if(quantity==0){
                    var flag2 = confirm("确定要删除"+title+"的信息吗？");
                    if(flag2){
                        var $a = $tr.find("td:last").find("a");
                        $a[0].onclick();
                        return;
                    }
                }


                var flag = confirm("确定要修改"+title+"的数量吗？");
                if(!flag){
                    $(this).val($(this).attr("class"));
                }

                var url = "bookServlet";
                var idVal =$.trim(this.name);

                var args = {"method":"updateItemQuantity","id":idVal,"quantity":quantityVal,"date":new Date()};
                $.post(url,args,function (data) {
                    var bookNumber = data.bookNumber;
                    var totalMoney = data.totalMoney;
                    $("#totalMoney").text("总金额：￥"+totalMoney);
                    $("#bookNumber").text("您的购物车中共有:"+bookNumber);
                },"JSON");
            });
        })
    </script>
</head>
<body>
<%@include file="/Commons/queryCondition.jsp"%>
    <center>
        <br><br>
        <div id="bookNumber">您的购物车中共有:${sessionScope.shoppingCart.bookNumber}</div><br><br>

        <table>
            <tr>
                <td>Title</td>
                <td>Quantity</td>
                <td>Price</td>
                <td>&nbsp;</td>
            </tr>
            <c:forEach items="${sessionScope.shoppingCart.items}" var="item">
                <tr>
                    <td>${item.book.title}</td>
                    <td><input class="${item.quantity}" type="text" name="${item.book.id}" value="${item.quantity}" size="1"></td>
                    <td>${item.book.price}</td>
                    <td><a href="bookServlet?method=remove&pageNo=${param.pageNo}&id=${item.book.id}" class="delete">删除</a></td>

                </tr>
            </c:forEach>
                <tr>
                    <td id="totalMoney" colspan="4">总金额：￥${sessionScope.shoppingCart.totalMoney}</td>
                </tr>

                <tr>
                    <td colspan="4">
                        <a href="bookServlet?method=getBooks&pageNo=${param.pageNo}">继续购物</a>&nbsp;&nbsp;
                        <a href="bookServlet?method=clear">清空购物车</a>&nbsp;&nbsp;
                        <a href="bookServlet?method=forwardPage&page=cash">结账</a>
                    </td>
                </tr>
                <br><br>

        </table>


    </center>
</body>
</html>
