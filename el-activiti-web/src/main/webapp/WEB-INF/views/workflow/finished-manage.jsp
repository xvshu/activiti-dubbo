<%@page import="com.eloancn.framework.activiti.util.ProcessDefinitionCache,org.activiti.engine.RepositoryService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>管理结束流程</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/easyui1_5.jsp"%>
	<script type="text/javascript" src="${ctx }/js/dateformat.js"></script>
    <script type="text/javascript">

        /**
         * 初始化用户列表
         */
        function initFinishedList(){
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


        function formatOperhi(val,row,index){
            return '<a href="#" onclick="historyFlow('+index+')">查看历史</a>&nbsp;|&nbsp;<a href="#" onclick="deleteFlow('+index+')">删除</a>';
        }


        function formatTime(val,row,index){
            if(val==null){
                return "";
            }
            var str=""+val;
            if(str==""){
                return "";
            }
            return dateUnixFormat(str.substring(0,10));
        }

        function historyFlow(index){
            $('#dg').datagrid('selectRow',index);// 关键在这里
            var row = $('#dg').datagrid('getSelected');
            if (row){
                url = '${ctx}/workflow/processinstance/query/history/'+row.processInstanceId;
                openDialog(url)//这里返回的类型有：json,html,xml,text
            }
        }

        function deleteFlow(index){
            $.messager.confirm("警示","您确认要删除此流程吗？",function(data){
                if(data){
                    $('#dg').datagrid('selectRow',index);// 关键在这里
                    var row = $('#dg').datagrid('getSelected');
                    if (row){
                        url = 'deletehi/'+row.processInstanceId;
                        $.post(url,null,
                            function(data){
                                $.messager.alert("提示",data);
                                $('#dg').datagrid('reload');//刷新
                            },
                            "text");//这里返回的类型有：json,html,xml,text
                    }
				}

			});

        }

        function openDialog(url){
            var content = '<iframe src="' + url + '" width="100%" height="99%" frameborder="0" scrolling="yes"></iframe>';
            $('#dd').dialog({
                title: "信息展示",
                width: 800,
                height: 500,
                closed: false,
                cache: false,
                content: content,
                modal: true
            });
            $('#dd').dialog('options').content = content;
        }

        $(function() {
            initFinishedList();
        });


	</script>


</head>

<body>
	<%
	RepositoryService repositoryService = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()).getBean(org.activiti.engine.RepositoryService.class);
	ProcessDefinitionCache.setRepositoryService(repositoryService);
	%>

	<table id="dg" class="easyui-datagrid" title="结束状态任务列表" style="width:100%;height:auto;"
		   data-options="pagination:true,rownumbers:true,singleSelect:true,url:'${ctx}/workflow/processinstance/finished/findall',method:'post'">
		<thead>
		<tr>
			<th data-options="field:'id'">执行IDssss</th>
			<th data-options="field:'processInstanceId'">流程实例ID</th>
			<th data-options="field:'processDefinitionId',align:'right'">流程定义ID</th>
			<th data-options="field:'processDefinitionKey',align:'right'">流程KEY</th>
			<th data-options="field:'processDefinitionName',align:'right'">流程名称</th>
			<th data-options="field:'processDefinitionVersion',align:'right'">流程版本</th>

			<th data-options="field:'businessKey',align:'right'">业务KEY</th>
			<th data-options="field:'startUserId',align:'right'">启动用户id</th>
			<%--<th data-options="field:'name',align:'right'">名称</th>--%>
			<th data-options="field:'deleteReason',align:'right'">结束原因</th>
			<th data-options="field:'endTime',align:'right',formatter:formatTime">结束时间</th>

			<th data-options="field:'_operate',align:'left',formatter:formatOperhi">操作</th>
		</tr>
		</thead>
	</table>

	<!-- 历史展示 -->

	<div id="dd"></div>

</body>
</html>
