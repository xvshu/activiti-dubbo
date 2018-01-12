<%@page import="com.eloancn.framework.activiti.util.ProcessDefinitionCache,org.activiti.engine.RepositoryService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>待办任务</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/easyui1_5.jsp"%>
	<script type="text/javascript" src="${ctx }/js/easyui-datagrid-plugin.js"></script>



	<script type="text/javascript" src="${ctx }/js/dateformat.js"></script>

	<script type="text/javascript">

        /**
         * 初始化用户列表
         */
        function initList(){
            $('#dg').datagrid({
                loadMsg: "数据加载中...",
                loadFilter: function(data){
                    var total = data.totalCount;
                    var data_list = data.result;
                    var data_return = {};
                    data_return.total = total;
                    data_return.rows = data_list;
                    return data_return;
                }
            });

            //设置分页控件
            var pager = $('#dg').datagrid('getPager');
            $(pager).pagination({
                pageSize: 10,//每页显示的记录条数，默认为10
                pageList: [10,20],//可以设置每页记录条数的列表
                beforePageText: '第',//页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
            });
        }

        //格式化操作按钮单元格
        function formatOper(val,row,index){
			var assigneeWF = row.task.assignee;
			var taskId = row.task.id;
			var operHtml = "";
			if(assigneeWF==null || assigneeWF==""){
                operHtml = "<a onclick=\"climeWF('${ctx }/demo/activiti/task/claimWF/"+taskId+"')\" href=\"#\">签收</a>";
			}else{
                operHtml = "<a onclick=\"opendiglogDo('"+taskId+"')\" href=\"#\">办理</a>";
            }
			return operHtml;
        }

        function climeWF(url){
            $.messager.confirm("警示","您确认要执行此操作吗？",function(data){
                if(data){
                    $.post(url,null,
                        function(data){
                            $.messager.alert("提示",data);
                        },
                        "text");//这里返回的类型有：json,html,xml,text
                }
            });
		}

		function opendiglogDo(taskId){
            $("#workFlowDoForm").form('clear');
            $('#oper_diglog').dialog('open');
            $("#taskId").textbox("setValue",taskId);
		}

		function wfPass(ispass){
		    var url="/demo/activiti/completeWf";
		    $("#isPass").val(ispass);
            $.ajax({
                cache: true,
                type: "POST",
                url:url,
                data:$('#workFlowDoForm').serialize(),
                async: false,
                error: function(request) {
                    $.messager.alert("Connection error","操作失败，请联系管理员！");
                },
                success: function(data) {
                    $.messager.alert("提示",data);
                    $('#dg').datagrid("reload",{});
                    $('#oper_diglog').dialog('close');
                }
            });
		}


		$(function() {
		    //初始化表格
			initList();
		});


	</script>
</head>

<body>


	<table id="dg" class="easyui-datagrid" title="所有待办任务列表" style="width:100%;height:auto;"
		   data-options="pagination:true,rownumbers:true,singleSelect:true,url:'${ctx }/demo/activiti/list/taskall/findall',method:'post'">
		<thead>
		<tr>
			<th data-options="field:'task.id'">任务id</th>
			<th data-options="field:'task.name'">当前任务名称</th>
			<th data-options="field:'elProcessInstance.id'">流程实例id</th>
			<th data-options="field:'elProcessInstance.processDefinitionId',align:'right'">流程定义ID</th>
			<th data-options="field:'elProcessInstance.processDefinitionKey',align:'right'">流程定义KEY</th>
			<th data-options="field:'elProcessInstance.processDefinitionName',align:'right'">流程名称</th>
			<th data-options="field:'_operate',align:'left',formatter:formatOper">操作</th>
		</tr>
		</thead>
	</table>

	<!-- 办理 -->
	<div id="oper_diglog" class="easyui-dialog" title="添加候选人" style="width:400px"
		 data-options="resizable:true,modal:true,buttons: '#add-buttons'" closed="true">
		<form id="workFlowDoForm">
			<div style="margin:20px">
				<table>
					<tr>
						<td>任务id:</td>
						<td><input id="taskId" name="taskId" class="easyui-textbox" style="width:100px;height:32px" /></td>
					</tr>
				</table>
				<input type=text style="display:none" id="isPass" name="isPass">

			</div>
		</form>
	</div>
	<div id="add-buttons">
		<a href="javascript:wfPass(true);" class="easyui-linkbutton" iconCls="icon-add">同意</a>
		<a href="javascript:wfPass(false);" class="easyui-linkbutton" iconCls="icon-add">拒绝</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#oper_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 添加候选人功能 -->

</body>
</html>
