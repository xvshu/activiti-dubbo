<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<%@ include file="/common/easyui1_5.jsp"%>
	<meta charset="UTF-8">
	<title>部门信息</title>
	<script type="text/javascript" src="/js/organ/departmentManage.js"></script>
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
		<div onclick="showDialogAddDep()" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="updateDepartment_show()" data-options="iconCls:'icon-edit'">修改</div>
		<div onclick="removeDepartment()" data-options="iconCls:'icon-remove'">删除</div>
	</div>

	<!-- 添加节点 -->
	<div id="add_diglog" class="easyui-dialog" title="添加" style="width:400px;height:200px;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#add-buttons'" closed="true">
		<form id="add_form">
			<div style="margin:20px">
				<div>部门名称:</div>
				<input id="department_name" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'部门名称不能为空'"/>
			</div>
		</form>
	</div>
	<div id="add-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:addDepartment()">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#add_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 添加节点End -->

	<!-- 修改节点 -->
	<div id="update_diglog" class="easyui-dialog" title="修改" style="width:400px;height:200px;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#update-buttons'" closed="true">
		<div style="margin:20px">
			<div>部门名称:</div>
			<input id="department_name_update" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'部门名称不能为空'"/>
		</div>
	</div>
	<div id="update-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:updateDepartment()">修改</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#update_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 修改节点End -->

	<script>
		loadTreeNodeData('/department/loadAll');
	</script>
</body>
</html>