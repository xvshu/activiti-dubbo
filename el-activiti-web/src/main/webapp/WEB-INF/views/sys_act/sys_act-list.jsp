<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/easyui1_5.jsp"%>
	<script type="text/javascript" src="${ctx }/js/dateformat.js"></script>
	<title>映射项管理列表</title>

	<script type="text/javascript">

        /**
         * 初始化用户列表
         */
        function initList(){
            $('#dg').datagrid({
                url:'${ctx}/sys_act/findall',
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

        /**
         * 简单查询
         */
        function searchNameSimple(){
            var sysName = $("#systemCodeSimple").val();
            if (sysName == null || sysName == ''){
                $.messager.alert("操作提示", "搜索文字不能为空");
                return;
            }
            var param = {systemCode:sysName};
            $('#dg').datagrid('load',param);
        }

        /**
         * 获取选中的行信息
         */
        function getSelected(){
            return $('#dg').datagrid('getSelected');
        }

        function formatOper(val,row,index){
            return '<a href="#" onclick="deleteConfig(\''+row.id+'\')">删除</a>';
        }

        function deleteConfig(id){
            $.messager.confirm("警示","您确认要删除此数据吗？",function(data){
                if(data){
					url = '${ctx}/sys_act/delete/'+id;
					$.post(url,null,
						function(data){
							$.messager.alert("提示",data);
							$('#dg').datagrid('reload');//刷新
						},
						"text");//这里返回的类型有：json,html,xml,text
                }

            });

        }

        function showDialogAddConfig(){
            $("#modelForm").form('clear');
            $('#add_diglog').dialog('open');
		}

		function addConfig(){
            if(!$("#modelForm").form("validate")){
                return ;
            }
            var addurl ="${ctx}/sys_act/insert";
            $.ajax({
                cache: true,
                type: "POST",
                url:addurl,
                data:$('#modelForm').serialize(),
                async: false,
                error: function(request) {
                    $.messager.alert("提示","添加失败，请联系管理员！");
                },
                success: function(data) {
                    if(data.indexOf("请重新添加")>=0){
                        $.messager.alert("提示",data);
					}else{
                        $.messager.alert("提示",data);
                        $('#dg').datagrid("reload",{});
                        $('#add_diglog').dialog('close');
					}
                }
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

        $.extend($.fn.validatebox.defaults.rules, {
            idcard: {// 验证身份证
                validator: function (value) {
                    return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
                },
                message: '身份证号码格式不正确'
            },
            phone: {// 验证电话号码
                validator: function (value) {
                    return /^((\d2,3)|(\d{3}\-))?(0\d2,3|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
                },
                message: '格式不正确,请使用下面格式:020-88888888'
            },
            mobile: {// 验证手机号码
                validator: function (value) {
                    return /^(13|15|18)\d{9}$/i.test(value);
                },
                message: '手机号码格式不正确'
            },
            integer: {// 验证整数 可正负数
                validator: function (value) {
                    //return /^[+]?[1-9]+\d*$/i.test(value);

                    return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
                },
                message: '请输入整数'
            },
            age: {// 验证年龄
                validator: function (value) {
                    return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
                },
                message: '年龄必须是0到120之间的整数'
            },
            english: {// 验证英语
                validator: function (value) {
                    return /^[A-Za-z]+$/i.test(value);
                },
                message: '请输入英文'
            },
            english_num: {// 验证英语
                validator: function (value) {
                    return /^[A-Za-z0-9-]+$/i.test(value);
                },
                message: '请输入英文,数字-组合'
            },
            zip: {// 验证邮政编码
                validator: function (value) {
                    return /^[1-9]\d{5}$/i.test(value);
                },
                message: '邮政编码格式不正确'
            },
            ip: {// 验证IP地址
                validator: function (value) {
                    return /d+.d+.d+.d+/i.test(value);
                },
                message: 'IP地址格式不正确'
            }
        });


        $(function() {
            initList()
		});
    </script>

</head>
<body>

	<table id="dg" class="easyui-datagrid" title="系统映射列表" style="width:100%;height:auto;"
		   data-options="pagination:true,rownumbers:true,singleSelect:true,method:'post',toolbar:'#tb'">
		<thead>
		<tr>
			<th data-options="field:'id'">ID</th>
			<th data-options="field:'systemCode'">系统Code</th>
			<th data-options="field:'activitiKey',align:'right'">工作流key</th>
			<th data-options="field:'configRemark'">映射描述</th>
			<th data-options="field:'createTime',formatter:formatTime">创建时间</th>
			<th data-options="field:'modifyTime',formatter:formatTime">更新时间</th>

			<th data-options="field:'_opear',formatter:formatOper">操作</th>

		</tr>
		</thead>
	</table>

	<!-- 头部功能按钮 -->
	<div id="tb" style="padding:2px 5px;">
		系统code：<input id="systemCodeSimple" class="easyui-textbox" style="width:15%;height:25px">

		<a href="javascript:searchNameSimple();" class="easyui-linkbutton" iconCls="icon-search">搜索</a>&nbsp;&nbsp;

		<a href="javascript:showDialogAddConfig();" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
	</div>
	<!-- 头部功能按钮End -->

	<!-- 添加功能 -->
	<div id="add_diglog" class="easyui-dialog" title="添加流程" style="width:400px;height:400px;display: none;"
		 data-options="resizable:true,modal:true,buttons: '#add-buttons'" closed="true">
		<form id="modelForm">
			<div style="margin:20px">
				<div>系统Code:</div>
				<input id="add_systemCode" name="systemCode" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'名称不能为空'"/>
			</div>
			<div style="margin:20px">
				<div>工作流key:</div>
				<input id="add_activitiKey" name="activitiKey" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'映射值不能为空'"/>
			</div>
			<div style="margin:20px">
				<div>映射描述:</div>
				<input class="easyui-textbox" data-options="multiline:true" id="add_configRemark" name="configRemark" style="width:300px;height: 50px;"></textarea>
			</div>

		</form>
	</div>
	<div id="add-buttons">
		<a href="javascript:addConfig();" class="easyui-linkbutton" iconCls="icon-add">确认</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#add_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 添加功能 -->


</body>
</html>