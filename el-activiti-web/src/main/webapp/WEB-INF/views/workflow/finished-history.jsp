<%@page import="com.eloancn.framework.activiti.util.ProcessDefinitionCache,org.activiti.engine.RepositoryService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>结束流程历史记录</title>
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
		   data-options="pagination:true,rownumbers:true,singleSelect:true,url:'${ctx}/workflow/processinstance/query/history/findall/${processInstanceId}',method:'post'">
		<thead>
		<tr>
			<th data-options="field:'id'">任务ID</th>
			<th data-options="field:'name'">任务名称</th>
			<th data-options="field:'assignee'">处理人ID</th>
			<th data-options="field:'createTime',formatter:formatTime">创建时间</th>
			<th data-options="field:'endTime',formatter:formatTime">结束时间</th>
		</tr>
		</thead>
	</table>

	<!-- 历史展示 -->



</body>
</html>
