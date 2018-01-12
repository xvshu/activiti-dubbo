<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>公告列表</title>
    <%@ include file="/common/easyui1_5.jsp"%>
</head>
<body>
<table id="showDataTable" style="width:100%;height:auto;"
       data-options="rownumbers:true,singleSelect:true,pagination:true,toolbar:'#tb'">
</table>
<!-- 头部功能按钮 -->
<div id="tb" style="padding:2px 5px;">
    公告标题：<input id="selTitleSimple" class="easyui-textbox" style="width:15%;height:25px">
    <a href="javascript:searchTitleSimple();" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;&nbsp;
</div>
<!-- 头部功能按钮End -->

<div class="easyui-dialog" id="showMessageRead" title="查看公告" style="display: none;"
     closed="true" data-options="buttons: '#showMessageRead-buttons'">
    <div class="easyui-layout" style="width:600px;height:300px;">
        <div data-options="region:'north',title:'公告'" style="background:#eee;height:100px;" id="showMessageTitle"></div>
        <div data-options="region:'center',title:'公告内容'" style="background:#eee;height:200px;" id="showMessage"></div>
    </div>
</div>

<div id="showMessageRead-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#showMessageRead').dialog('close')">关闭</a>
</div>

<script type="text/javascript" src="/js/organ/common.js"></script>
<script type="text/javascript" src="/js/message/messageCommon.js"></script>
<script type="text/javascript" src="/js/message/messageNoticeList.js"></script>
<script>
    initMessageNoticeList();
</script>
</body>