<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/js/organ/common.js"></script>
<script type="text/javascript" src="/js/message/messageCommon.js"></script>
<script type="text/javascript" src="/js/message/messageNoticeDialog.js"></script>

<div id="messageNoticeDialog" class="easyui-dialog" title="未读消息" style="display: none;"
     data-options="iconCls:'icon-tip',modal:true,resizable:true" closed="true">
    <table id="messageNoticeTable" style="width:100%;height:auto;"
           data-options="rownumbers:false,singleSelect:true">
    </table>
</div>

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

<script>
    var messageNoticeTimer = 0;
    $(function() {
        //五分钟请求一次
        messageNoticeTimer = setInterval("searchMessage()", 30*30*1000);
    })
</script>