<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>发送消息列表</title>
    <%@ include file="/common/easyui1_5.jsp"%>
</head>
<body>
<jsp:include page="sendMessage.jsp"/>

<table id="showDataTable" style="width:100%;height:auto;"
       data-options="rownumbers:true,singleSelect:true,pagination:true,toolbar:'#tb'">
</table>
<!-- 头部功能按钮 -->
<div id="tb" style="padding:2px 5px;">
    发送人：<input id="selSendNameSimple" class="easyui-textbox" style="width:15%;height:25px">
    <a href="javascript:searchSendMessageSimple();" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;&nbsp;
    <a href="javascript:showDialogSearchSendMessage();" class="easyui-linkbutton" iconCls="icon-grant">多选项查询</a>&nbsp;&nbsp;
    <a href="javascript:sendSmsShow();" class="easyui-linkbutton" iconCls="icon-add" plain="true">发送短信</a>
    <a href="javascript:sendMailShow();" class="easyui-linkbutton" iconCls="icon-add" plain="true">发送邮件</a>
    <a href="javascript:sendMessageShow();" class="easyui-linkbutton" iconCls="icon-add" plain="true">发送站内信</a>
    <a href="javascript:sendNoticeShow();" class="easyui-linkbutton" iconCls="icon-add" plain="true">发送公告</a>
</div>
<!-- 头部功能按钮End -->

<div class="easyui-dialog" id="showMessageRead" title="查看消息" style="display: none;"
     closed="true" data-options="buttons: '#showMessageRead-buttons'">
    <div class="easyui-layout" style="width:600px;height:300px;">
        <div data-options="region:'north',title:'消息'" style="background:#eee;height:100px;" id="showMessageTitle"></div>
        <div data-options="region:'center',title:'消息内容'" style="background:#eee;height:200px;" id="showMessage"></div>
    </div>
</div>
<div id="showMessageRead-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#showMessageRead').dialog('close')">关闭</a>
</div>

<!-- 查询功能 -->
<div id="search_diglog" class="easyui-dialog" title="多选项查询" style="width:400px;display: none;"
     data-options="resizable:true,modal:true,buttons: '#search-buttons'" closed="true">
    <form id="searchForm">
        <div style="margin:20px">
            <div>发送人:</div>
            <input id="selSendName" class="easyui-textbox" style="width:70%;height:32px">
        </div>
        <div style="margin:20px">
            <div>消息标题：</div>
            <input id="selTitle" class="easyui-textbox" style="width:70%;height:32px">
        </div>
    </form>
</div>
<div id="search-buttons">
    <a href="javascript:searchSendMessage();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#search_diglog').dialog('close')">关闭</a>
</div>
<!-- 查询功能End -->



<script type="text/javascript" src="/js/organ/common.js"></script>
<script type="text/javascript" src="/js/message/messageCommon.js"></script>
<script type="text/javascript" src="/js/message/sendMessageList.js"></script>
<script>
    initSendMessageList();
</script>
</body>