<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>配置中心</title>
    <%@ include file="/common/global.jsp"%>
    <script>
        var notLogon = ${empty user};
        if (notLogon) {
            location.href = '${ctx}/login?timeout=true';
        }
    </script>

    <%@ include file="/common/easyui1_5.jsp"%>

    <script type="text/javascript" src="${ctx }/js/index.js"></script>
    <script type="text/javascript" src="${ctx }/js/nano.js"></script>

    <script type="text/javascript" >
        function addUrlTab(url,title){
            var content = '<iframe scrolling="yes" frameborder="0"  src="'+url+'" style="width:100%;height: 98%;"></iframe>';
            $('#tabs').tabs('add',{
                title:title,
                content:content,
                cache:false,
                closable:true
            });
        }
    </script>

    <link rel="Shortcut Icon" href="${ctx }/images/logo.icon" type="image/x-icon">



</head>

<body class="easyui-layout">
    <!-- 消息弹框及公告 -->
    <jsp:include page="../message/messageNoticeDialog.jsp"/>
    <!-- 消息弹框及公告 -->
    <div data-options="region:'north',border:false" style="height: 52px">
        <div style="position:relative;float:left;width:150px;line-height: 30px;"><img style="height: ;" src="${ctx }/img/logo.jpg"></div>

        <!-- 头部公告滚动条 -->
        <div style="position: absolute;margin: auto;left: 400px;top: 15px;" id="showNoticeMarquee"></div>
        <!-- 头部公告滚动条 -->

        <div style="position: relative; float: right;">
            <div align="right" style="float: right; padding: 4px;vertical-align:middle">
                <div>当前用户：<span id="nowUser">${user.firstName } ${user.lastName }</span> <a href="${ctx }/user/logout" >退出</a></div>
            </div>
        </div>
    </div>
    <div data-options="region:'west',split:true,title:'菜单'" style="width: 200px">
        <div class="easyui-accordion" data-options="fit:false,border:false,selected:-1" style="padding: 0px !important;">
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


            <%--<div title="工作流引擎-示例项目" class="icon_lists" style="padding: 10px;">--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activiti/demouser"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-切换用户</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>

                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activiti/list/taskall"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>待办任务</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>

                <%--<HR >--%>

                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activiti/apply"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-流程启动</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activiti/list/task"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-流程办理</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activiti/list/running"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-运行中</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activiti/list/finished"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-已结束流程</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>

                <%--<HR >--%>


                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitimul/apply"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-会签流程启动</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitimul/list/task"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-会签流程办理</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitimul/list/running"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-会签运行中</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitimul/list/finished"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-会签已结束流程</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>

                <%--<HR >--%>


                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitiauto/apply"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-自动用户启动</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitiauto/list/task"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-自动用户办理</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitiauto/list/running"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-自动用户运行中</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitiauto/list/finished"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i>;<ttitle>demo-自动用户已结束</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>

                <%--<HR >--%>


                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitiweb/apply"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-web启动</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitiweb/list/task"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-web办理</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitiweb/list/running"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>demo-web运行中</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/demo/activitiweb/list/finished"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i>;<ttitle>demo-web已结束</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>



            <%--</div>--%>


            <div title="组织机构管理" class="icon_lists" style="padding: 10px;">
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/user/view/loadAll"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-man'"></i><ttitle>用户管理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/department/view/loadAll"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-check'"></i><ttitle>部门管理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/post/view/loadAll"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-lock'"></i><ttitle>职位管理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/role/view/loadAll"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-man'"></i><ttitle>角色管理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/function/view/loadAll"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-receive'"></i><ttitle>职务管理</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/right/view/loadAll"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-receive'"></i><ttitle>权限管理</ttitle></span>
                    </a>
                </div>
            </div>

            <div title="消息管理" class="icon_lists" style="padding: 10px;">
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/message/view/loadAll"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>消息列表</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="${ctx }/message/view/loadAllNotice"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>公告列表</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/message/view/sendMessageListJsp"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'"></i><ttitle>消息发送记录</ttitle></span>
                    </a>
                </div>


            </div>

            <%--<div title="架构基础" class="icon_lists" style="padding: 10px;">--%>
                <%--<div class="index-nav-item">--%>
                    <%--<a href="javascript:void(0);" src="http://192.168.2.16:8989/"  class="index--navi-tab">--%>
                        <%--<span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>EloancnGenerator</ttitle></span>--%>
                    <%--</a>--%>
                <%--</div>--%>

            <%--</div>--%>

            <div title="统一配置" class="icon_lists" style="padding: 10px;">
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/configmap/list"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>配置项管理</ttitle></span>
                    </a>
                </div>

                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx }/disconf/index"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>Disconf</ttitle></span>
                    </a>
                </div>
            </div>

            <div title="监控集成" class="icon_lists" style="padding: 10px;">
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="http://192.168.2.16:2281/cat/r"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>CAT监控</ttitle></span>
                    </a>
                </div>
            </div>

        </div>
    </div>
    <div data-options="region:'center'">
        <div id="tabs" class="easyui-tabs" data-options="fit:true">
            <div title="首页">
                <img src="${ctx }/img/bg.jpg" style="width:100%;height:98%" alt="翼龙贷配置中心欢迎您" />
            </div>
	    </div>
    </div>
</body>

</html>


