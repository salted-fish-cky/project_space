<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>layout</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
		$(function () {
//			alert提示框
//			$.messager.alert("标题","内容","info");
			//confirm 确认框
//			$.messager.confirm("标题","你确定要删除当前记录吗？",function (r) {
//				alert(r);
//            })

			//show方法 ----欢迎框
			$.messager.show({
				title:'欢迎信息',
				msg:'欢迎admin登陆系统',
				timeout:3000,
				showType:'slide'
			})
        })
	</script>
</head>
<body>

</body>
</html>