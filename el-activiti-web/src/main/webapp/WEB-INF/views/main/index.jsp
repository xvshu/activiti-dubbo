<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>配置中心</title>
    <%@ include file="/common/global.jsp"%>


    <%@ include file="/common/easyui1_5.jsp"%>

    <link rel="stylesheet" type="text/css" href="/css/treeNode.css">

    <script type="text/javascript" src="${ctx }/js/index.js"></script>
    <%--<script type="text/javascript" src="${ctx }/js/nano.js"></script>--%>

    <script type="text/javascript" >
        function addUrlTab(url,title){
            if ($('#tabs').tabs('exists', title)){
                $('#tabs').tabs('select', title);
            } else {
                var content = '<iframe scrolling="yes" frameborder="0"  src="' + url + '" style="width:100%;height: 98%;"></iframe>';
                $('#tabs').tabs('add', {
                    title: title,
                    content: content,
                    cache: false,
                    closable: true
                });
            }
        }

        function addPanel(title,content){
            $('#menu_aa').accordion('add',{
                title:title,
                content:content
            });
        }


        /**
         * 加载树形结构数据
         */
        function loadMenuTreeNodeData(url){

            $.ajax({
                url:url
            }).done(function (data){
                var tree_munu=[];
                if (data.data){
                    tree_munu = myLoadFilter_tree(data.data,'closed');
                }else{
                    tree_munu =myLoadFilter_tree(data,'closed');
                }

                for(var i=0; i<tree_munu.length; i++){
                    var menu_one = tree_munu[i];
                    var menu_children = menu_one.children;
                    for(var c=0; c<menu_children.length; c++){
                        var menu_one_n = menu_children[c];
                        var menu_children_n = menu_one_n.children;
                        addPanel(menu_one_n.text,"<ul id='easyui_tree_menu_"+menu_one_n.id+"' ></ul>");
                    }

                }

                for(var i=0; i<tree_munu.length; i++){
                    var menu_one = tree_munu[i];
                    var menu_children = menu_one.children;
                    for(var c=0; c<menu_children.length; c++){
                        var menu_one_n = menu_children[c];
                        var menu_children_n = menu_one_n.children;
                        loadTreeInId("easyui_tree_menu_"+menu_one_n.id,menu_children_n);
                    }

                }

            });

        }

        function loadTreeInId(id,treeData){
            $("#"+id).tree({
                data:treeData,
                onClick: function(node){
                    if(node.url && node.url.length >0){
//                        alert(node.url);
                        addUrlTab('${ctx }'+node.url,node.text);
                    }

                }
            });

        }


        /**
         * 解析树形结构数据
         * @param data
         * @returns {Array}
         */
        function myLoadFilter_tree(data,treeNodeState){

            function disTreeNode(node){
                if (node==null){
                    return null;
                }

                var node_msg = {id:node.id,text:node.name,attributes:{code:node.code},url:node.url};

                if (node.childNodes == null || node.childNodes == ''){
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
                if (treeNodeState){
                    node_msg.state = treeNodeState;
                }
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

        /**
         * 格式化节点信息
         * @param node
         * @returns {*}
         */
        function format_node(node){
            var s = node.text;
            if (node.children){
                s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
            }
            return s;
        }

        function modifyPwd(){
            if(!$("#ModifyPwdForm").form("validate")){
                return ;
            }
            var newPwdStr = $("#newPwd").textbox("getValue");
            var newPwdCheck = $("#newPwdCheck").textbox("getValue");
            var oldPwd = $("#oldPwd").textbox("getValue");
            if(newPwdStr!=newPwdCheck){
                $.messager.alert("提示","两次输入密码不一致，请重新输入");
                return ;
            }
            var modifyPwdUrl="/user/modifyPwd";
            $.post(modifyPwdUrl,{newPwd:newPwdStr,oldPwd:oldPwd}, function(data) {
                if(data=='success'){
                    var logoutUrl ="/user/logout";
                    alert("修改成功，请重新登录！");
                    window.location.href=logoutUrl;
                }else if(data=='passWrang'){
                    $.messager.alert("提示","请输入正确原始密码！");
                }else{
                    $.messager.alert("提示","修改失败，请联系管理员！");
                }

            });

        }

    </script>

    <link rel="Shortcut Icon" href="${ctx }/images/logo.icon" type="image/x-icon">

    <style>

    </style>


</head>

<body class="easyui-layout">

    <!-- 消息弹框及公告 -->
    <div data-options="region:'north',border:false" style="height: 52px;overflow:hidden;">
        <div style="position:relative;float:left;width:150px;line-height: 30px;"><img style="height: ;" src="${ctx }/img/logo.jpg"></div>

        <div style="position: relative; float: right;">
            <div align="right" style="float: right; padding: 4px;vertical-align:middle">
                <div> <a href="${ctx }/user/logout" >退出</a>&nbsp;|
                    &nbsp;<a href="#" onclick="javascript:$('#modifyPwd_diglog').dialog('open')" >修改密码</a></div>
            </div>
        </div>
    </div>
    <div data-options="region:'west',split:true,title:'菜单'" style="width: 200px">

        <div id="menu_aa" class="easyui-accordion" data-options="fit:false,border:false,selected:-1" style="padding: 0px !important;">
            <div title="工作流配置" class="icon_lists" style="padding: 10px;">
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/workflow/model/list"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exportAll'"></i><ttitle>流程定义</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/workflow/process-list"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-filter'"></i><ttitle>流程部署</ttitle></span>
                    </a>
                </div>

                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/workflow/processinstance/running"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>全部运行中流程</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/workflow/processinstance/finished"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-addCallBack'"></i><ttitle>全部结束流程</ttitle></span>
                    </a>
                </div>

            </div>


            <div title="工作流引擎-示例项目" class="icon_lists" style="padding: 10px;">
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activiti/demouser"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-切换用户</ttitle></span>
                    </a>
                </div>

                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activiti/list/taskall"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>待办任务</ttitle></span>
                    </a>
                </div>

                <HR >

                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activiti/apply"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-流程启动</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activiti/list/task"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-流程办理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activiti/list/running"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-运行中</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activiti/list/finished"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-已结束流程</ttitle></span>
                    </a>
                </div>

                <HR >


                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitimul/apply"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-会签流程启动</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitimul/list/task"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-会签流程办理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitimul/list/running"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-会签运行中</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitimul/list/finished"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-会签已结束流程</ttitle></span>
                    </a>
                </div>

                <HR >


                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiauto/apply"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-自动用户启动</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiauto/list/task"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-自动用户办理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiauto/list/running"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-自动用户运行中</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiauto/list/finished"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i>;<ttitle>demo-自动用户已结束</ttitle></span>
                    </a>
                </div>

                <HR >


                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiweb/apply"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-web启动</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiweb/list/task"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-web办理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiweb/list/running"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-web运行中</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiweb/list/finished"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i>;<ttitle>demo-web已结束</ttitle></span>
                    </a>
                </div>

                <HR >


                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitigroup/apply"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-group启动</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitigroup/list/task"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-group办理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitigroup/list/running"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-group运行中</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitigroup/list/finished"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i>;<ttitle>demo-group已结束</ttitle></span>
                    </a>
                </div>

                <HR >


                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiPF/apply"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo产品工厂启动</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiPF/list/task"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo产品工厂办理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiPF/list/running"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo产品工厂运行中</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/demo/activitiPF/list/finished"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i>;<ttitle>demo产品工厂已结束</ttitle></span>
                    </a>
                </div>

                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/img/activiti/runningImg?pi_id=245116&pi_processDefinitionId='ProductFactoryNewProduct:8:225016'"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i>;<ttitle>demo展示图片</ttitle></span>
                    </a>
                </div>


            </div>

        </div>
    </div>
    <div data-options="region:'center'">
        <div id="tabs" class="easyui-tabs" data-options="fit:true">
            <div title="首页">
                <div style="width:100%;height:98%;background-image:url('${ctx }/img/bg.jpg');background-size: 100% 100%;text-align: center;">

                    <div style="width:500px;height:100px;margin: auto;  position: absolute;  top: 0; left: 0; bottom: 0; right: 0;font-size: 46px;text-align: center">
                        翼龙贷欢迎您！
                    </div>
                </div>


            </div>
	    </div>
    </div>

    <!-- 修改功能 -->
    <div id="modifyPwd_diglog" class="easyui-dialog" title="修改密码" style="width:250px;height:300px;"
         data-options="resizable:true,modal:true,buttons: '#edit-buttons'" closed="true">
        <form id="ModifyPwdForm">
            <div style="margin:20px">
                <div>原密码:</div>
                <input id="oldPwd" name="oldPwd" type="password" class="easyui-textbox" data-options="validType:'length[1,20]'" invalidMessage="最大长度不能超过20位" style="width:100%;height:32px"/>
            </div>
            <div style="margin:20px">
                <div>新密码:</div>
                <input id="newPwd" name="newPwd" type="password" class="easyui-textbox" data-options="validType:'length[1,20]'" invalidMessage="最大长度不能超过20位" style="width:100%;height:32px"/>
            </div>
            <div style="margin:20px">
                <div>确认新密码:</div>
                <input id="newPwdCheck" name="newPwdCheck" type="password" class="easyui-textbox" data-options="validType:'length[1,20]'" invalidMessage="最大长度不能超过20位" style="width:100%;height:32px"/>
            </div>
            <input type="text" id="edit_id" name="id" style="display: none;">
        </form>
    </div>
    <div id="edit-buttons">
        <a href="javascript:modifyPwd();" class="easyui-linkbutton" iconCls="icon-add">确认</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#modifyPwd_diglog').dialog('close')">关闭</a>
    </div>
    <!-- 修改功能 -->

</body>

<script>
    loadMenuTreeNodeData('/right/loadMenu');
</script>

</html>


