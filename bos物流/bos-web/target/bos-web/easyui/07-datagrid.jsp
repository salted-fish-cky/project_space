<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>datagrid</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
<%--方式二 发送ajax请求获取json数据创建datagrid--%>
	<%--<table data-options="url:'${pageContext.request.contextPath}/json/datagrid-data.json'" class="easyui-datagrid">--%>
		<%--<thead>--%>
			<%--<tr>--%>
				<%--<th data-options="field:'id'">编号</th>--%>
				<%--<th data-options="field:'name'">姓名</th>--%>
				<%--<th data-options="field:'age'">年龄</th>--%>
			<%--</tr>--%>
		<%--</thead>--%>
	<%--</table>--%>

<table id="mytable"></table>
<%--方式三：使用easyUI提供的api创建datagrid--%>
<script type="text/javascript">
	$(function () {
//		创建数据表格
		$("#mytable").datagrid({
			//定义标题所有的列
			columns:[[
				{title:'编号',field:'id',checkbox:true},
				{title:'姓名',field:'name'},
				{title:'年龄',field:'age'}
			]],
			//指定数据表格发送ajax请求的地址
            url:'${pageContext.request.contextPath}/json/datagrid-data.json',
			rownumbers:true,
//			定义工具栏
			toolbar:[
				{text:'添加',iconCls:'icon-add',
//					为按钮绑定单击事件
					handler:function () {
						alert("添加")
                    }
				},
				{text:'修改',iconCls:'icon-edit'},
				{text:'删除',iconCls:'icon-remove'},
				{text:'查询',iconCls:'icon-search'}
			],

//			分页条
			pagination:true

		})
    })
</script>
</body>
</html>