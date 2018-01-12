<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/easyui1_5.jsp"%>
	<script type="text/javascript" src="${ctx }/js/dateformat.js"></script>

	<title>流程列表</title>

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

        function formatOper(val,row,index){

            var resultUrl = "";
            var Psuspended =  row.suspensionState ;
            if(Psuspended==2){
                resultUrl = resultUrl + "<a  onclick=\'susWF(\"processdefinition/update/active/"+row.id+"\")\' href=\"#\" class=\"easyui-linkbutton\"  iconCls=\"icon-confirm\">激活</a>"
            }else{
                resultUrl = resultUrl + "<a onclick='susWF(\"processdefinition/update/suspend/"+row.id+"\")' href=\"#\" class=\"easyui-linkbutton\"  iconCls=\"icon-cancel-oper\" >挂起</a>"
            }

            var deleteB = '<a  onclick=\"deleteWF(\'${ctx }/workflow/process/delete?deploymentId='+row.deploymentId+'\')\" href="#" class="easyui-linkbutton" iconCls="icon-cancel">删除</a>';

			return resultUrl + "&nbsp;|&nbsp;" + deleteB;

        }

        function formatXml(val,row,index){
            return "<a onclick=\"openDialog('${ctx }/workflow/resource/read?processDefinitionId="+row.id+"&resourceType=xml')\" href='#'>"+row.resourceName+"</a>";
        }

        function formatImg(val,row,index){
            return "<a onclick=\"openDialog('${ctx }/workflow/resource/read?processDefinitionId="+row.id+"&resourceType=image')\" href='#'>"+row.diagramResourceName+"</a>";
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
            initList()
        });

    </script>
</head>
<body>



	<table id="dg" class="easyui-datagrid" title="已部署流程列表" style="width:100%;height:auto;"
		   data-options="pagination:true,rownumbers:true,singleSelect:true,url:'${ctx}/workflow/process-list/findall',method:'post'">
		<thead>
		<tr>
			<th data-options="field:'id'">ProcessDefinitionId</th>
			<th data-options="field:'deploymentId'">DeploymentId</th>
			<th data-options="field:'name',align:'right'">名称</th>
			<th data-options="field:'key'">KEY</th>
			<th data-options="field:'version'">版本号</th>
			<th data-options="field:'idXml',align:'right',formatter:formatXml">XML</th>
			<th data-options="field:'idImg',align:'right',formatter:formatImg">图片</th>
			<th data-options="field:'suspensionState',align:'right',formatter:formatSus">流程状态</th>
			<th data-options="field:'_operate',align:'left',formatter:formatOper">操作</th>
		</tr>
		</thead>
	</table>

	<div id="dd"></div>

</body>
</html>