<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>消息发送</title>
    <link rel="stylesheet" href="/js/kindeditor/themes/default/default.css" />
    <link rel="stylesheet" href="/js/kindeditor/plugins/code/prettify.css" />
    <script charset="utf-8" src="/js/kindeditor/kindeditor-all-min.js"></script>
    <script charset="utf-8" src="/js/kindeditor/lang/zh-CN.js"></script>
    <script charset="utf-8" src="/js/kindeditor/plugins/code/prettify.js"></script>
    <script type="text/javascript" src="/js/message/sendMessage.js"></script>
    <script>
        var editor;
        $(function() {
            editor = KindEditor.create('textarea[name="v_content"]',{
                resizeType : 1,
                width:"100%",
                height:"200px",
                uploadJson : 'http://upload.eloancn.com:8080/fileUpload.action',
                fileManagerJson : 'http://imglocal.eloancn.com/',
                allowFileManager : true,
                allowImageUpload : true,
                afterChange:function(){
                    this.sync();
                },
                afterBlur:function(){
                    this.sync();
                }
            });
        });
    </script>
</head>
<body>
    <div class="easyui-dialog" id="showSendMessageDialog" title="发送消息" style="display: none;width: 800px;height: auto;"
         closed="true" data-options="buttons: '#showSendMessageDialog-buttons'">
        <form name="contentForm" id="contentForm">
            <div style="margin:20px">
                <div>标题:</div>
                <input id="send_message_title" class="easyui-textbox" prompt="标题" style="width:70%;height:32px" data-options="required:true,missingMessage:'标题不能为空'"/>
            </div>
            <div style="margin:20px">
                <select id="send_message_type" class="easyui-combobox" name="user_type" label="消息发送对象:" labelPosition="top" style="width:70%;"
                        data-options="required:true,missingMessage:'消息发送对象不能为空',textField:'text',valueField:'id',panelHeight:'200'">
                </select>
            </div>
            <div style="margin:20px;" id="sendPersonDiv">
                <select id="send_person" class="easyui-combobox" label="用户:" labelPosition="top"
                        data-options="required:true,panelHeight:'200',multiple:true" style="width:70%;">
                </select>
            </div>
            <div style="margin:20px;" id="sendGroupDiv">
                <input id="send_department" class="easyui-combotree" label="部门:" labelPosition="top"
                       data-options="required:true,editable:'false'" style="width:70%">
                <div style="display: none;">
                <jsp:include page="../organ/treeNode.jsp"/>
                </div>
            </div>
            <div style="margin:20px">
                内容：
                <textarea rows="8" cols="100" style="width:800px;height: auto;" id="v_content" name="v_content" class="easyui-validatebox"
                          data-options="required:true,validType:'length[1,1000000]'" invalidMessage="最大长度不能超过1000000">
                </textarea>
            </div>
        </form>
    </div>
    <div id="showSendMessageDialog-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="sendMessage()">提交</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#showSendMessageDialog').dialog('close')">关闭</a>
    </div>

    <div class="easyui-dialog" id="showSendNoteDialog" title="发送短信" style="display: none;width: 800px;height: auto;"
         closed="true" data-options="buttons: '#showSendNoteDialog-buttons'">
        <form name="contentNoteForm" id="contentNoteForm">
            <div style="margin:20px">
                <select id="send_note_type" class="easyui-combobox" name="user_type" label="消息发送对象:" labelPosition="top" style="width:70%;"
                        data-options="required:true,missingMessage:'消息发送对象不能为空',panelHeight:'200'">
                    <option value="">请选择</option>
                    <option value="1">个人</option>
                    <option value="2">组织</option>
                </select>
            </div>
            <div style="margin:20px;">
                <select id="send_note_person" class="easyui-combobox" label="用户:" labelPosition="top"
                        data-options="required:true,panelHeight:'200',multiple:true" style="width:70%;">
                </select>
            </div>
            <div style="margin:20px;">
                <input id="send_note_department" class="easyui-combotree" label="部门:" labelPosition="top"
                       data-options="required:true,editable:'false'" style="width:70%">
                <div style="display: none;">
                    <jsp:include page="../organ/treeNode.jsp"/>
                </div>
            </div>
            <div style="margin:20px">
                短信内容：
                <input class="easyui-textbox" id="note_content" data-options="multiline:true,required:true,missingMessage:'内容不能为空'" style="width:300px;height:100px"/>
            </div>
        </form>
    </div>
    <div id="showSendNoteDialog-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="sendNote()">提交</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#showSendMessageDialog').dialog('close')">关闭</a>
    </div>

</body>
<%!
    private String htmlspecialchars(String str) {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }
%>