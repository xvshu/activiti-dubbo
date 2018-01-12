<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>权限树形结构</title>
	<%@ include file="/common/easyui1_5.jsp"%>
	<script type="text/javascript" src="/js/organ/right.js"></script>
</head>
<body>
	<div style="margin:20px 0;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-outstatement" onclick="collapseAll()">收起全部节点</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-detail" onclick="expandAll()">展开全部节点</a>
	</div>

	<!-- 引入树形结构 -->
	<jsp:include page="treeNode.jsp"/>

	<!-- 右键数据操作 -->
	<div id="menu" class="easyui-menu" style="width:120px;">
		<div onclick="showDialogAddFun()" data-options="iconCls:'icon-add'">添加权限</div>
		<div onclick="showDialogUpdateFun()" data-options="iconCls:'icon-edit'">修改权限</div>
		<div onclick="removeFunction()" data-options="iconCls:'icon-remove'">删除权限</div>
	</div>

	<!-- 添加节点 -->
	<div id="add_diglog" class="easyui-dialog" title="添加" style="width:400px;height:auto;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#add-buttons'" closed="true">
		<form id="add_form">
			<div style="margin:20px">
				<select id="pid_select" class="easyui-combobox" name="add_role_select" label="上级权限:" labelPosition="top"
						data-options="panelHeight:'auto'" style="width:70%;">

				</select>
			</div>

			<div style="margin:20px">
				<div>权限名称:</div>
				<input id="right_name" class="easyui-textbox" style="width:70%;height:32px"
					   data-options="required:true,missingMessage:'权限名称不能为空'"/>
			</div>

			<div style="margin:20px">
				<div>权限编码:</div>
				<input id="right_code" class="easyui-textbox" style="width:70%;height:32px"
					   data-options="required:true,missingMessage:'权限编码不能为空'"/>
			</div>

			<div style="margin:20px">
				<div>系统编码:</div>
				<input id="bus_code" class="easyui-textbox" style="width:70%;height:32px"/>
			</div>

		</form>
	</div>
	<div id="add-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:addFunction()">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#add_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 添加节点End -->

	<!-- 修改节点 -->
	<div id="update_diglog" class="easyui-dialog" title="修改" style="width:400px;height:auto;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#update-buttons'" closed="true">
		<form id="update_form">
			<div style="margin:20px">
				<div>权限名称:</div>
				<input id="right_name_update" class="easyui-textbox" style="width:70%;height:32px"
					   data-options="required:true,missingMessage:'权限名称不能为空'"/>
			</div>

			<div style="margin:20px">
				<div>权限编码:</div>
				<input id="right_code_update" class="easyui-textbox" style="width:70%;height:32px"
					   data-options="required:true,missingMessage:'权限编码不能为空'"/>
			</div>
		</form>
	</div>
	<div id="update-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:updateFunction()">修改</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#update_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 修改节点End -->

	<!-- 展示职务角色列表 -->
	<div id="showFunRoles_diglog" class="easyui-dialog" title="权限角色列表" style="width:400px;height:auto;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#showFunRoles-buttons'" closed="true">
		<table id="showFunRolesTable"></table>
	</div>
	<div id="showFunRoles-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#showFunRoles_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 展示职务角色列表 -->

	<script>
		loadTreeNodeData('/right/loadAll');
	</script>
</body>
</html>