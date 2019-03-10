<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/28
  Time: 14:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>books</title>

    <script type="text/javascript" src="${pageContext.request.contextPath}/jQuery/jquery.js"></script>
    <script type="text/javascript">
        $(function () {

//           $('a').click(function () {
//               var serializeVal = $(':hidden').serialize();
//               var href = this.href+"&"+serializeVal;
//               window.location.href = href;
//               return false;
//           }); 
           
           $('#pageNo').change(function () {
               var value = $(this).val();
                value = $.trim(value);
                var flag = false;
               var pageNo = 0;
               //校验value是否为数字
                var reg = /^\d+$/g;
                if(reg.test(value)){
                    //校验value是否在合法范围
                    pageNo = parseInt(value);
                    if(pageNo>=1&&pageNo<=parseInt("${page.totalPageNumber}")){
                        flag = true;
                    }
                }

                if(!flag){
                    alert("输入的不是合法的页码！");
                    $(this).val("");
                    return;
                }
               //页面跳转
               var href = "${pageContext.request.contextPath}/bookServlet?method=getBooks&pageNo="+pageNo+"&"+$(':hidden').serialize();
               window.location.href = href;
           });
        });
    </script>
    <%@include file="/Commons/queryCondition.jsp"%>
</head>
<body>
    <%--<input type="hidden" name="minPrice" value="${param.minPrice}">--%>
    <%--<input type="hidden" name="maxPrice" value="${param.maxPrice}">--%>

    <center>
        <c:if test="${param.title!=null}">
            您已经将${param.title}放入到购物车中
        </c:if>
        <br><br>
        <c:if test="${!empty sessionScope.shoppingCart.books}">
            您的购物车中有${sessionScope.shoppingCart.bookNumber}本书，<a href="bookServlet?method=forwardPage&page=cart&pageNo=${page.pageNo}&id=${item.book.id}">查看购物车</a>
        </c:if>
        <br><br>
        <form action="${pageContext.request.contextPath}/bookServlet?method=getBooks" method="post">
            Price:<input type="text" size="1" name="minPrice">-
                  <input type="text" size="1" name="maxPrice">
                  <input type="submit" value="Submit">
        </form>
        <br><br>
        <table cellpadding="10">
            <c:forEach items="${page.list}" var="book">
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/bookServlet?method=getBook&pageNo=${page.pageNo}&id=${book.id}">${book.title}</a>
                        <br>
                        <a href="">${book.author}</a>
                    </td>
                    <td>${book.price}</td>
                    <td><a href="${pageContext.request.contextPath}/bookServlet?method=addToCart&pageNo=${page.pageNo}&id=${book.id}&title=${book.title}">加入购物车</a></td>
                </tr>
            </c:forEach>
        </table>

        <br><br>

        共${page.totalPageNumber}页
        &nbsp;&nbsp;
        当前第${page.pageNo}页
        &nbsp;&nbsp;
        <c:if test="${page.hasPrev}">
            <a href="${pageContext.request.contextPath}/bookServlet?method=getBooks&pageNo=1">首页</a>
            &nbsp;&nbsp;
            <a href="${pageContext.request.contextPath}/bookServlet?method=getBooks&pageNo=${page.prevPage}">上一页</a>
        </c:if>

        &nbsp;&nbsp;

        <c:if test="${page.hasNext}">
            <a href="${pageContext.request.contextPath}/bookServlet?method=getBooks&pageNo=${page.nextPage}">下一页</a>
            &nbsp;&nbsp;
            <a href="${pageContext.request.contextPath}/bookServlet?method=getBooks&pageNo=${page.totalPageNumber}">末页</a>
        </c:if>
        &nbsp;&nbsp;
        转到<input type="text" size="1" id="pageNo">页 
    </center>

</body>
</html>
