<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/easyui1_5.jsp"%>
	<script type="text/javascript" src="${ctx }/js/jquery.jsonp.js"></script>
	<title>Disconf</title>

	<script type="text/javascript">

        $.ajaxSetup({
            async: false
        });

		var envList;
        /**
         * 初始化用户列表
         */
        function initList(){

            if(!signinDisconf()){
                alert("登录失败！");
                return;
			}

            getEnv();
            alert("getenv success");

            $.get("${disconfUrl}/api/app/list", function(result){
                if(result.success!="true"){
                    alert("出现错误");
                    return;
                }
                var total = data.page.totalCount;
                var data_list = data.page.result;
                var data_return = {};
                data_return.total = total;
                data_return.rows = data_list;
                alert("load data");
                $("#dg").datagrid("loadData", data_return);
            });
        }

        function signinDisconf(){
            var result = false;
            $.ajax({
                type: "POST",
                url: "${disconfUrl}/api/account/signin",
                data: {
                    "name": "admin",
                    "password": "admin",
                    "remember":0
                }
            }).done(function (data) {
                if (data.success != "true") {
                    alert("disconf系统无此用户");
                    result = false;
                }
                result =  true;
            });

            return result;
		}


        function getEnv(){

            $.get("${disconfUrl}/api/env/list", function(result){
                if(result.success!="true"){
					alert("出现错误");
					return;
				}
                var columns=new Array();
                $.each(result.page.result, function(i, field){
                    var column={};
                    column["title"]="环境"+i;
                    column["field"]=field.id;
                    column["hidden"]=true;
                    columns.push(column);
                    var column2={};
                    column2["title"]="环境"+i;
                    column2["field"]=field.name;
                    columns.push(column2);
                });
                envList=columns;
            });

            <%--$.jsonp({--%>
                <%--"url": '${disconfUrl}/api/env/list',--%>
                <%--"success": function(data) {--%>
                    <%--alert("success");--%>
                    <%--var columns=new Array();--%>
                    <%--$.each(res.result, function(i, field){--%>
                        <%--var column={};--%>
                        <%--column["title"]="环境"+i;--%>
                        <%--column["field"]=field.id;--%>
                        <%--column["hidden"]=true;--%>
                        <%--columns.push(column);--%>
                        <%--var column2={};--%>
                        <%--column2["title"]="环境"+i;--%>
                        <%--column2["field"]=field.name;--%>
                        <%--columns.push(column2);--%>
                    <%--});--%>
                    <%--envList=columns;--%>
                <%--},--%>
                <%--"error": function(date) {--%>
                    <%--if(date.success=="true"){--%>
                        <%--alert("success");--%>
                        <%--return date.page;--%>
                    <%--}--%>
                    <%--alert("error");--%>
                <%--}--%>
            <%--});--%>
		}

        $(function() {
            initList()
		});
    </script>

</head>
<body>

	<table id="dg" class="easyui-datagrid" title="App列表" style="width:100%;height:auto;"
		   data-options="rownumbers:true,singleSelect:true,method:'get',toolbar:'#tb'">
		<thead>
		<tr>
			<th data-options="field:'id'">ID</th>
			<th data-options="field:'name',align:'right'">名称</th>
		</tr>
		</thead>
	</table>

	<!-- 头部功能按钮 -->
	<div id="tb" style="padding:2px 5px;">
	</div>
	<!-- 头部功能按钮End -->
</body>
</html>