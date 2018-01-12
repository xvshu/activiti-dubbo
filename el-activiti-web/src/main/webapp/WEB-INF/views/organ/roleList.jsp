<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>角色列表</title>
    <%@ include file="/common/easyui1_5.jsp"%>
</head>
<body>

<table id="showDataTable" style="width:100%;height:auto;"
       data-options="rownumbers:true,singleSelect:true,pagination:true,toolbar:'#tb'">
</table>

<!-- 头部功能按钮 -->
<div id="tb" style="padding:2px 5px;">
    角色名：<input id="selName" class="easyui-textbox" style="width:15%;height:25px">
    <a href="javascript:searchRole();" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;&nbsp;
    <a href="javascript:showDialogAddRole();" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
    <a href="javascript:showDialogEditRole();" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
    <a href="javascript:confirmNoRole();" class="easyui-linkbutton" iconCls="icon-no" plain="true">禁用</a>
    <a href="javascript:confirmUndoRole();" class="easyui-linkbutton" iconCls="icon-undo" plain="true">恢复</a>

    <a href="javascript:loadRights();" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加权限</a>

</div>
<!-- 头部功能按钮End -->

<!-- 添加权限 -->
<div id="rights_diglog" class="easyui-dialog" title="添加权限" style="width:400px;display: none;"
     data-options="resizable:true,modal:true,buttons: '#rights-buttons'" closed="true">
    <jsp:include page="checkboxTreeNode.jsp"/>

</div>
<div id="rights-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:addRights()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#rights_diglog').dialog('close')">关闭</a>
</div>

<!-- 添加权限 -->

<!-- 添加功能 -->
<div id="add_diglog" class="easyui-dialog" title="添加" style="width:400px;display: none;"
     data-options="resizable:true,modal:true,buttons: '#add-buttons'" closed="true">
    <form id="add_form">
        <div style="margin:20px">
            <div>名称:</div>
            <input id="role_name" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'名称不能为空'"/>
        </div>
    </form>
</div>
<div id="add-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:addRole()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#add_diglog').dialog('close')">关闭</a>
</div>
<!-- 添加功能 -->

<!-- 修改功能 -->
<div id="edit_diglog" class="easyui-dialog" title="修改" style="width:400px;display: none;"
     data-options="resizable:true,modal:true,buttons: '#edit-buttons'" closed="true">
    <form id="edit_form">
        <div style="margin:20px">
            <div>名称:</div>
            <input id="editName" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'名称不能为空'"/>
        </div>
    </form>
</div>
<div id="edit-buttons">
    <a href="javascript:editRole();" class="easyui-linkbutton" iconCls="icon-add">修改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#edit_diglog').dialog('close')">关闭</a>
</div>
<!-- 修改功能 -->

<script type="text/javascript" src="/js/organ/common.js"></script>
<script type="text/javascript" src="/js/organ/roleList.js"></script>
<script>
    initRoleList();
    loadTreeNodeData('/right/loadAll');
</script>
</body>
</html>