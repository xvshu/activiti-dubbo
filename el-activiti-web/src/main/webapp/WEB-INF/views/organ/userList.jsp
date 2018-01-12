<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>用户列表</title>
	<%@ include file="/common/easyui1_5.jsp"%>
</head>
<body>
	<table id="showDataTable" style="width:100%;height:auto;"
		   data-options="rownumbers:true,singleSelect:true,pagination:true,toolbar:'#tb'">
	</table>

	<!-- 头部功能按钮 -->
	<div id="tb" style="padding:2px 5px;">
		真实姓名：<input id="selNameSimple" class="easyui-textbox" style="width:15%;height:25px">
		<a href="javascript:searchUserSimple();" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;&nbsp;
		<a href="javascript:showDialogSearchUser();" class="easyui-linkbutton" iconCls="icon-grant">多选项查询</a>&nbsp;&nbsp;
		<a href="javascript:showDialogAddUser();" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
		<a href="javascript:showDialogEditUser();" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
		<a href="javascript:confirmNoUser();" class="easyui-linkbutton" iconCls="icon-no" plain="true">禁用</a>
		<a href="javascript:confirmUndoUser();" class="easyui-linkbutton" iconCls="icon-undo" plain="true">恢复</a>
	</div>
	<!-- 头部功能按钮End -->

	<!-- 查询功能 -->
	<div id="search_diglog" class="easyui-dialog" title="多选项查询" style="width:400px;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#search-buttons'" closed="true">
		<form id="searchForm">
			<div style="margin:20px">
				<div>真实姓名:</div>
				<input id="selName" class="easyui-textbox" style="width:70%;height:32px">
			</div>
			<div style="margin:20px">
				<div>职位</div>
				<input id="selPostName" class="easyui-textbox" style="width:70%;height:32px">
			</div>
			<div style="margin:20px">
				<div>部门</div>
				<input id="selDepName" class="easyui-textbox" style="width:70%;height:32px">
			</div>
			<div style="margin:20px">
				<div>职务</div>
				<input id="selFunName" class="easyui-textbox" style="width:70%;height:32px">
			</div>
			<div style="margin:20px">
				<div>区域</div>
				<input id="selAreaName" class="easyui-textbox" style="width:70%;height:32px">
			</div>
		</form>
	</div>
	<div id="search-buttons">
		<a href="javascript:searchUser();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#search_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 查询功能End -->

	<!-- 添加功能 -->
	<div id="add_diglog" class="easyui-dialog" title="添加" style="width:400px;display: none;height: 600px;"
		 data-options="resizable:true,modal:true,buttons: '#add-buttons'" closed="true">
		<form id="add_form">
			<div style="margin:20px">
				<div>真实姓名:</div>
				<input id="add_user_name" class="easyui-textbox" prompt="真实姓名" style="width:70%;height:32px" data-options="required:true,missingMessage:'真实姓名不能为空'"/>
			</div>
			<div style="margin:20px">
				<div>登录名:</div>
				<input id="add_user_name_login" class="easyui-textbox" prompt="登录名" style="width:70%;height:32px"/>
			</div>
			<div style="margin:20px">
				<div>登录密码:</div>
				<input id="add_user_passwd" class="easyui-passwordbox" prompt="登录密码" style="width:70%;height:32px"/>
			</div>
			<div style="margin:20px">
				<div>邮箱:</div>
				<input id="add_user_email" class="easyui-validatebox textbox" validType="email" prompt="邮箱" style="width:70%;height:32px"/>
			</div>
			<div style="margin:20px">
				<div>手机号:</div>
				<input id="add_user_mobile" class="easyui-validatebox textbox" validType="phoneNum" prompt="手机号" style="width:70%;height:32px"/>
			</div>
			<div style="margin:20px">
				<select id="add_user_type" class="easyui-combobox" name="user_type" label="用户类型:" labelPosition="top" style="width:70%;"
						data-options="required:true,missingMessage:'用户类型不能为空',panelHeight:'200'">
					<option value="1" selected>总部</option>
					<option value="2">盟商</option>
				</select>
			</div>
			<div style="margin:20px">
				<input id="add_department" class="easyui-combotree" label="部门:" labelPosition="top"
						data-options="required:true,missingMessage:'部门不能为空',editable:'true'" style="width:70%">
			</div>
			<div style="margin:20px">
				<select id="add_post" class="easyui-combobox" name="add_post" label="职位:" labelPosition="top"
						data-options="required:true,missingMessage:'职位不能为空',valueField:'id', textField:'name', panelHeight:'200'" style="width:70%;">
				</select>
			</div>
			<div style="margin:20px">
				<input id="add_function" class="easyui-combotree" label="职务:" labelPosition="top"
					   data-options="required:true,missingMessage:'职务不能为空',editable:'true'" style="width:70%">
			</div>
			<div style="margin:20px" id="showAreaMsgDivForAddUser">
				<div>区域:</div>
				<%--<jsp:include page="areaJsp.jsp"/>--%>
			</div>
		</form>
	</div>
	<div id="add-buttons">
		<a href="javascript:addUser();" class="easyui-linkbutton" iconCls="icon-add">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#add_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 添加功能 -->

	<!-- 修改功能 -->
	<div id="edit_diglog" class="easyui-dialog" title="修改" style="width:400px;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#edit-buttons'" closed="true">
		<form id="edit_form">
			<div style="margin:20px">
				<div>真实姓名:</div>
				<input id="edit_user_name" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'真实姓名不能为空'"/>
			</div>
			<div style="margin:20px">
				<select id="edit_user_type" class="easyui-combobox" name="user_type" label="用户类型:" labelPosition="top" style="width:70%;"
						data-options="required:true,missingMessage:'用户类型不能为空',panelHeight:'200'">
					<option value="1" selected>总部</option>
					<option value="2">盟商</option>
				</select>
			</div>
			<div style="margin:20px">
				<input id="edit_department" class="easyui-combotree" label="部门:" labelPosition="top"
					   data-options="required:true,missingMessage:'部门不能为空',editable:'true'" style="width:70%">
			</div>
			<div style="margin:20px">
				<select id="edit_post" class="easyui-combobox" name="add_post" label="职位:" labelPosition="top"
						data-options="required:true,missingMessage:'职位不能为空',valueField:'id', textField:'name', panelHeight:'200'" style="width:70%;">
				</select>
			</div>
			<div style="margin:20px">
				<input id="edit_function" class="easyui-combotree" label="职务:" labelPosition="top"
					   data-options="required:true,missingMessage:'职务不能为空',editable:'true'" style="width:70%">
			</div>
			<div style="margin:20px" id="showAreaMsgDivForEditUser">
				<div>区域:</div>
				<jsp:include page="areaJsp.jsp"/>
			</div>

		</form>
		<%--<div id="showTreeNodeDiglog_department" class="easyui-dialog" title="部门列表" style="width:400px">--%>
			<%----%>
		<%--</div>--%>
	</div>
	<div id="edit-buttons">
		<a href="javascript:editUser();" class="easyui-linkbutton" iconCls="icon-add">修改</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#edit_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 修改功能 -->

	<!-- 树形数据获取 -->
	<div id="showTree_diglog" class="easyui-dialog" title="选择" style="width:400px;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#selete-buttons'" closed="true">
		<jsp:include page="treeNode.jsp"/>
	</div>
	<div id="selete-buttons">
		<a href="javascript:getTreeSelData();" class="easyui-linkbutton" iconCls="icon-ok">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#showTree_diglog').dialog('close')">关闭</a>
		<a href="javascript:expandAll();" class="easyui-linkbutton" iconCls="icon-detail">展开全部</a>
	</div>
	<!-- 树形数据获取 -->

	<script type="text/javascript" src="/js/organ/common.js"></script>
	<script type="text/javascript" src="/js/organ/userList.js"></script>
	<script type="text/javascript" src="/js/organ/postList.js"></script>
	<script>
		initUserList();
	</script>
</body>