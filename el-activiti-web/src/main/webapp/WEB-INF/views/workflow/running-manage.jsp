<%@page import="com.eloancn.framework.activiti.util.ProcessDefinitionCache,org.activiti.engine.RepositoryService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>管理运行中流程</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/easyui1_5.jsp"%>
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

            var resultUrl = "";
            var Psuspended =  row.suspensionState ;
            if(Psuspended==2){
                resultUrl = resultUrl + "<a  onclick=\'susWF(\"update/active/"+row.processInstanceId+"\")\' href=\"#\" class=\"easyui-linkbutton\"  iconCls=\"icon-confirm\">激活</a>"
            }else{
                resultUrl = resultUrl + "<a onclick='susWF(\"update/suspend/"+row.processInstanceId+"\")' href=\"#\" class=\"easyui-linkbutton\"  iconCls=\"icon-cancel-oper\" >挂起</a>"
            }

            var addUsers='<a  onclick=\"showAddUsersDiglog(\''+row.processInstanceId+'\')\" href="#" >添加候选人</a>';
            var modifyUsers='<a  onclick=\"showModifyUsersDiglog(\''+row.processInstanceId+'\')\" href="#" >更换处理人</a>';

            var deleteB = '<a  onclick=\"deleteWF(\'update/delete/'+row.processInstanceId+'\')\" href="#" >删除</a>';

            return resultUrl + "&nbsp;|&nbsp;"+ modifyUsers + "&nbsp;|&nbsp;" + addUsers + "&nbsp;|&nbsp;"+  deleteB;

        }

        //格式化挂起状态单元格
        function formatSus(val,row,index){

            var PsuspendedS= "";
            var Psuspended =  row.suspensionState;
            if(Psuspended==2){
                PsuspendedS = "已挂起";
            }else{
                PsuspendedS = "已激活";
            }

            return PsuspendedS;
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

        function formatAvti(val,row,index){
            return row.nowActiviNoteName;
        }

        /**
         * 删除流程
         * @param url
         */
        function deleteWF(url){
            $.messager.confirm("警示","您确认要删除吗？",function(data){
                if(data){
                    postUrl(url);
                }
            });
        }

        /**
         * 激活或者挂起流程
         * @param url
         */
        function susWF(url){
            $.messager.confirm("警示","您确认要执行此操作吗？",function(data){
                if(data){
                    postUrl(url);
                }
            });
        }

        function postUrl(url){
            $.post(url, null,
                function (data) {
                    $.messager.alert("提示", data);
                    $('#dg').datagrid('reload');//刷新
                },
                "text");//这里返回的类型有：json,html,xml,text
        }

        //展示增加候选人对话框
        function showAddUsersDiglog(flowID){
            $("#addUserForm").form('clear');
            $("#folwIDAdd").val(flowID);
            var urldepartment="${ctx}/workflow/user/department/loadAll";
            loadTreeNodeData(urldepartment,$("#department"));
            $('#add_diglog').dialog('open');

		}

		//增加候选人后台交互
		function addUsers(){

            loadAddUsersId();

            if($("#addUsers").val()==null ||$("#addUsers").val()==""){
                $.messager.alert("您必须选择一个用户");
                return ;
			}

            if(!$('#addUserForm').form("validate")){
                return;
			}
            var addurl ="${ctx}/workflow/processinstance/addusers";
            $.ajax({
                cache: true,
                type: "POST",
                url:addurl,
                data:$('#addUserForm').serialize(),
                async: false,
                error: function(request) {
                    $.messager.alert("Connection error","添加失败，请联系管理员！");
                },
                success: function(data) {
                    $.messager.alert('提示',data,'info');
                    $('#dg').datagrid("reload",{});
                    $('#add_diglog').dialog('close');
                }
            });
		}

		//选择部门后，联动查询候选人名单
        function loadAddUsers(node){
		    var depId = node.attributes.code;
            var urlUsersLoad="${ctx}/workflow/user/user/loadAll?depId="+depId;
            loadListNodeData(encodeURI(encodeURI(urlUsersLoad)),$("#SaddUsers"));
		}

		//选择部门后，联动查询处理人名单
        function loadModifyUsers(node){
            var depId = node.attributes.code;
            var urlUsersLoad="${ctx}/workflow/user/user/loadAll?depId="+depId;
            loadListNodeData(encodeURI(encodeURI(urlUsersLoad)),$("#MaddUsers"));
        }

		//将管理人选择的候选人添加进隐藏框
        function loadAddUsersId(){
            var selectUsers = $("#SaddUsers").combobox('getValues');
            var inputUsers = "";
            for(var ik=0;ik<selectUsers.length;ik++){
                if(ik==0){
                    inputUsers=selectUsers[ik];
				}else{
                    inputUsers= inputUsers +"|"+selectUsers[ik];
				}
			}
			alert(inputUsers);
			$("#addUsers").val(inputUsers);
        }

        //将管理人选择的办理人添加进隐藏框
        function loadModifyUsersId(){
            var selectUsers = $("#MaddUsers").combobox('getValue');
            alert(selectUsers);
            $("#modifyUsers").val(selectUsers);
        }

		//展示修改处理人对话框
        function showModifyUsersDiglog(flowID){
            $("#modifyUserForm").form('clear');
            $("#folwIDModify").val(flowID);

            var urldepartment="${ctx}/workflow/user/department/loadAll";
            loadTreeNodeData(urldepartment,$("#Mdepartment"));

            $('#modify_user_diglog').dialog('open');

        }

        //修改处理人后台交互
        function modifyUsers(){

            loadModifyUsersId();
            if($("#modifyUsers").val()==null ||$("#modifyUsers").val()==""){
                $.messager.alert("提示","您必须选择一个用户");
                return ;
            }

            if(!$('#modifyUserForm；').form("validate")){
                return;
            }
            var addurl ="${ctx}/workflow/processinstance/modifyuser";
            $.ajax({
                cache: true,
                type: "POST",
                url:addurl,
                data:$('#modifyUserForm').serialize(),
                async: false,
                error: function(request) {
                    $.messager.alert("Connection error","添加失败，请联系管理员！");
                },
                success: function(data) {
                    $.messager.alert('提示',data,'info');
                    $('#dg').datagrid("reload",{});
                    $('#modify_user_diglog').dialog('close');
                }
            });
        }

        /**
         * 加载树形结构数据
         */
        function loadTreeNodeData(url,treeNote){

            treeNote.combotree("tree").tree({
                url:url,
                loadFilter: function(data){
                    if (data.data){
                        return myLoadFilter_tree(data.data);
                    }else{
                        return myLoadFilter_tree(data);
                    }
                }
            });
        }

        function loadListNodeData(url,treeNote){
            treeNote.combobox({
                url:url,
                loadFilter: function(data){
                    if (data.data){
                        return data.data;
                    }else{
                        return data;
                    }
                },
                valueField:'id',
                textField:'name'
            });
		}

        /**
         * 解析树形结构数据
         * @param data
         * @returns {Array}
         */
        function myLoadFilter_tree(data){

            function disTreeNode(node){
                if (node==null){
                    return null;
                }

                var node_msg = {id:node.id,text:node.name,attributes:{code:node.code}};

                if (node.childNodes == null){
                    return node_msg;
                }
                var childNodes = node.childNodes;
                for(var i=0; i<childNodes.length; i++){
                    var childNode = childNodes[i];
                    var childNode_ex = disTreeNode(childNode);
                    if (childNode_ex != null){
                        if (node_msg.children){
                            node_msg.children.push(childNode_ex);
                        } else {
                            node_msg.children = [childNode_ex];
                        }
                    }
                }
                node_msg.state = 'closed';
                return node_msg;
            }

            var nodes = [];
            if (data){
                // get the root nodes
                for(var i=0; i<data.length; i++){
                    var node = data[i];
                    var nodeMsg = disTreeNode(node);
                    nodes.push(nodeMsg);
                }
            }
            return nodes;
        }


		$(function() {
		    //初始化表格
			initList();
		});


	</script>
</head>

<body>


	<table id="dg" class="easyui-datagrid" title="运行中流程列表" style="width:100%;height:auto;"
		   data-options="pagination:true,rownumbers:true,singleSelect:true,url:'${ctx }/workflow/processinstance/running/findall',method:'post'">
		<thead>
		<tr>
			<th data-options="field:'id'">执行IDssss</th>
			<th data-options="field:'processInstanceId'">流程实例ID</th>

			<th data-options="field:'processDefinitionId',align:'right'">流程定义ID</th>
			<th data-options="field:'processDefinitionKey',align:'right'">流程定义KEY</th>
			<th data-options="field:'processDefinitionName',align:'right'">流程名称</th>

			<th data-options="field:'startUserId',align:'right'">启动人ID</th>

			<th data-options="field:'nowActiviNoteName'" formatter:formatAvti>当前节点</th>
			<th data-options="field:'suspensionState',align:'right',formatter:formatSus">流程状态</th>
			<th data-options="field:'_operate',align:'left',formatter:formatOper">操作</th>
		</tr>
		</thead>
	</table>


	<!-- 添加候选人功能 -->
	<div id="add_diglog" class="easyui-dialog" title="添加候选人" style="width:400px"
		 data-options="resizable:true,modal:true,buttons: '#add-buttons'" closed="true">
		<form id="addUserForm">
			<div style="margin:20px">
				<table>
					<tr>
						<td>部门:</td>
						<td><select id="department"  name="department" class="easyui-combotree"   style="width:156px;"/></td>
					</tr>
					<tr>
						<td>人员:</td>
						<td><select id="SaddUsers"  class="easyui-combobox" data-options="multiple:true"   style="width:156px;"/></td>
					</tr>
				</table>

				<input type=text style="display:none" id="addUsers" name="addUsers">
				<input type=text style="display:none" id="folwIDAdd" name="folwID">
			</div>
		</form>
	</div>
	<div id="add-buttons">
		<a href="javascript:addUsers();" class="easyui-linkbutton" iconCls="icon-add">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#add_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 添加候选人功能 -->

	<!-- 更改处理人功能 -->
	<div id="modify_user_diglog" class="easyui-dialog" title="更改处理人" style="width:400px"
		 data-options="resizable:true,modal:true,buttons: '#modify-buttons'" closed="true">
		<form id="modifyUserForm">
			<div style="margin:20px">
				<table>
					<tr>
						<td>部门:</td>
						<td><select id="Mdepartment"  name="Mdepartment" class="easyui-combotree"   style="width:156px;"/></td>
					</tr>
					<tr>
						<td>人员:</td>
						<td><select id="MaddUsers"  class="easyui-combobox"    style="width:156px;"/></td>
					</tr>
				</table>
				<input type=text style="display:none" id="modifyUsers" name="modifyUsers" >
				<input type=text style="display:none" id="folwIDModify" name="folwID">
			</div>
		</form>
	</div>
	<div id="modify-buttons">
		<a href="javascript:modifyUsers();" class="easyui-linkbutton" iconCls="icon-add">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#modify_user_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 更改处理人功能 -->

<script type="application/javascript">
    $('#department').combotree({
        onSelect:function(node) {
            loadAddUsers(node);
        }
    })

    $('#Mdepartment').combotree({
        onSelect:function(node) {
            loadModifyUsers(node);
        }
    })

</script>

</body>
</html>
