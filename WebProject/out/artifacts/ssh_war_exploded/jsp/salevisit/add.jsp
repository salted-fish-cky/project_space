<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<TITLE><s:property value="#saleVisit==null?'添加':'修改'" />拜访记录</TITLE> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="${pageContext.request.contextPath }/css/Style.css" type=text/css rel=stylesheet>
<LINK href="${pageContext.request.contextPath }/css/Manage.css" type=text/css
	rel=stylesheet>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/my.js"></script>
<!-- 使用日期控件步骤1: 导入js和css -->
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.4.4.min.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath }/js/datepicker/jquery.datepick.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/datepicker/jquery.datepick.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/datepicker/jquery.datepick-zh-CN.js"></script>
<script type="text/javascript">
	
<!-- 使用日期控件步骤2: 当页面加载完成时,调用 datepick方法指定需要应用的文本框 -->
	$(document).ready(function(){
			// yy 已经代表了4位的年份
			// mm 代表月份
			// dd 代表日期
		$('#visitTime').datepick({dateFormat: 'yy-mm-dd'});
		$('#visitNexttime').datepick({dateFormat: 'yy-mm-dd'});
	});
</script>
<META content="MSHTML 6.00.2900.3492" name=GENERATOR>
</HEAD>
<BODY>
	<FORM id=form1 name=form1
		action="${pageContext.request.contextPath }/SaleVisitAction_add"
		method="post"  onsubmit="return checkForm(['user.userId','visitNexttime']);" >
		<!-- 隐藏域回显当前编辑的拜访记录id -->
		<input  type="hidden"  name="visitId" value="<s:property value="#saleVisit.visitId" />"  />

		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
			<TBODY>
				<TR>
					<TD width=15><IMG src="${pageContext.request.contextPath }/images/new_019.jpg"
						border=0></TD>
					<TD width="100%" background="${pageContext.request.contextPath }/images/new_020.jpg"
						height=20></TD>
					<TD width=15><IMG src="${pageContext.request.contextPath }/images/new_021.jpg"
						border=0></TD>
				</TR>
			</TBODY>
		</TABLE>
		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
			<TBODY>
				<TR>
					<TD width=15 background=${pageContext.request.contextPath }/images/new_022.jpg><IMG
						src="${pageContext.request.contextPath }/images/new_022.jpg" border=0></TD>
					<TD vAlign=top width="100%" bgColor=#ffffff>
						<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0>
							<TR>
								<TD class=manageHead>当前位置：拜访记录管理 &gt; <s:property value="#saleVisit==null?'添加':'修改'" />拜访记录</TD>
							</TR>
							<TR>
								<TD height=2></TD>
							</TR>
						</TABLE>
						
						<TABLE cellSpacing=0 cellPadding=5  border=0>
						  
						    
							<TR>
								<td>所属客户：</td>
								<td >
								<input type="hidden" name="customer.custId" style="WIDTH: 180px" id="custId" value="<s:property value="#saleVisit.customer.custId" />" />
								<input type="text"  style="WIDTH: 180px" id="custName" value="<s:property value="#saleVisit.customer.custName" />"/>
									<input type="button" value="选择客户" onclick="window.open('${pageContext.request.contextPath}/CustomerAction_list?select=true','','width=600,height=300')" />
								</td>
								<td>拜访时间 ：</td>
								<td  >
									<INPUT class=textbox id="visitTime" type="text"
														style="WIDTH: 180px" maxLength=50 name="visitTime" readonly="readonly" value="<s:property value="#saleVisit.visitTime_s" />" >
								</td>
							</TR>
							
							<TR>
								
								<td>被拜访人 ：</td>
								<td >
								<INPUT class=textbox id=sChannel2 type="text"
														style="WIDTH: 180px" maxLength=50 name="visitInterviewwee" value="<s:property value="#saleVisit.visitInterviewwee" />" >
								</td>
								<td>拜访地址：</td>
								<td>
								<INPUT class=textbox id=sChannel2
														style="WIDTH: 180px" maxLength=50 name="visitAddress" value="<s:property value="#saleVisit.visitAddress" />">
								</td>
							</TR>
							
							<TR>
								
								
								<td>拜访详情 ：</td>
								<td>
								<INPUT class=textbox id="custPhone"
														style="WIDTH: 180px" maxLength=50 name="visitDetail" value="<s:property value="#saleVisit.visitDetail" />">
								</td>
								<td>下次拜访时间：</td>
								<td>
								<INPUT class=textbox id="visitNexttime" readonly="readonly"
														style="WIDTH: 180px" maxLength=50 name="visitNexttime" value="<s:property value="#saleVisit.visitNexttime_s" />">
								</td>
							</TR>
							<tr>
								<td rowspan=2>
								<INPUT class=button id=sButton2 type="submit"
														value="保存 " name=sButton2>
								</td>
							</tr>
							
						</TABLE>
						
						
					</TD>
					<TD width=15 background="${pageContext.request.contextPath }/images/new_023.jpg">
					<IMG src="${pageContext.request.contextPath }/images/new_023.jpg" border=0></TD>
				</TR>
			</TBODY>
		</TABLE>
		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
			<TBODY>
				<TR>
					<TD width=15><IMG src="${pageContext.request.contextPath }/images/new_024.jpg"
						border=0></TD>
					<TD align=middle width="100%"
						background="${pageContext.request.contextPath }/images/new_025.jpg" height=15></TD>
					<TD width=15><IMG src="${pageContext.request.contextPath }/images/new_026.jpg"
						border=0></TD>
				</TR>
			</TBODY>
		</TABLE>
	</FORM>
</BODY>
</HTML>
