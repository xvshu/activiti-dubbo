$(document).ready(function () {
    $("#send_message_type").combobox({
        onChange: function (newValue,oldValue) {
            if (newValue == ''){
                return;
            }
            //个人
            if (newValue == '1'){
                //发送站内信或者公告
                var valueField = "id";
                //发送邮件
                if (type == 2 || type == '2'){
                    valueField = 'email';
                }
                initUserList(valueField,'send_person');
                $("#send_person").combobox("enable");
                $("#send_department").combotree("disable");
            }else if (newValue == '2'){
                initDepartment('send_department');
                $("#send_department").combobox("enable");
                $("#send_person").combotree("disable");
            }
        }
    });
    $("#send_note_type").combobox({
        onChange: function (newValue,oldValue) {
            if (newValue == ''){
                return;
            }
            //个人
            if (newValue == '1'){
                //发送站内信或者公告
                var valueField = "id";
                //发送邮件
                if (type == 2 || type == '2'){
                    valueField = 'email';
                }
                initUserList(valueField,'send_note_person');
                $("#send_note_person").combobox("enable");
                $("#send_note_department").combotree("disable");
            }else if (newValue == '2'){
                initDepartment('send_note_department');
                $("#send_note_department").combobox("enable");
                $("#send_note_person").combotree("disable");
            }
        }
    });
});

var type = '';
//打开消息发送框
function showSendMessageDialog(type){
    $("#contentForm").form('clear');
    KindEditor.instances[0].html('');
    this.type = type;
    $("#send_message_type").combobox('clear');
    var comboboxData = [];
    comboboxData.push({ "text": "请选择", "id":''});
    if (type == 4 || type == '4'){
        comboboxData.push({ "text": "组织", "id":2 });
    }else{
        comboboxData.push({ "text": "个人", "id":1 },{ "text": "组织", "id":2 });
    }
    $("#send_message_type").combobox("loadData", comboboxData);
    $('#showSendMessageDialog').dialog('open');
}

/**
 * 发送消息
 */
function sendMessage(){
    var html = $("#v_content").val();
    var param = {};
    param.sendId = sendId;
    param.sendName = sendName;
    param.types = type;
    param.type = type;
    param.title = $("#send_message_title").val();
    param.message = html;
    var send_message_type = $("#send_message_type").combobox('getValue');
    //个人
    if (send_message_type == 1){
        param.sendType = 1;//私人消息
        //发送站内信
        if (type == 1 || type == '1'){
            var recIdsArr = $('#send_person').combobox('getValues');
            if (recIdsArr == null || recIdsArr == ''){
                alert('用户不能为空');
                return;
            }
            param.recIds = recIdsArr.join(",");
        }
        //发送邮件
        else if (type == 2 || type == '2'){
            param.recIds = 0;
            var emailsArr = $('#send_person').combobox('getValues');
            if (emailsArr == null || emailsArr == ''){
                alert('用户不能为空');
                return;
            }
            param.emails = emailsArr.join(",");
        }
    }
    //组织
    else if (send_message_type == 2){
        param.sendType = 2;
        var department = $('#send_department').combotree('tree').tree('getSelected');
        param.groupCode = department.attributes.code;
    }

    $.ajax({
        url: '/message/send',
        data: param,
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','发送成功！','info');
                $('#showSendMessageDialog').dialog('close');
            }else{
                $.messager.alert('提示',data.message,'error');
            }
        }
    });
}

/**
 * 初始化用户
 */
function initUserList(valueField,id){
    $.ajax({
        url: '/user/loadAll',
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $('#'+id).combobox({
                    data : data.data.data,
                    valueField:valueField,
                    textField:'name'
                });
            }else{
                $.messager.alert('提示','加载用户失败','error');
            }
        }
    });
}

/**
 * 初始化部门信息
 * @type {string}
 */
function initDepartment(id){
    var department = getTreeNodeData('/department/loadAll');
    if (department==null || department == ''  || department == undefined){
        $.messager.alert("操作提示", "获取部门信息失败");
        return;
    }
    $("#"+id).combotree("loadData", department);
}

//打开消息发送框
function showSendNoteDialog(type){
    $("#contentNoteForm").form('clear');
    KindEditor.instances[0].html('');
    this.type = type;
    $('#showSendNoteDialog').dialog('open');
}

/**
 * 发送短信
 */
function sendNote(){
    var html = $("#note_content").val();
    var param = {};
    param.sendId = sendId;
    param.sendName = sendName;
    param.types = type;
    param.type = type;
    param.message = html;
    var send_note_type = $("#send_note_type").combobox('getValue');
    //个人
    if (send_note_type == 1){
        param.sendType = 1;//私人消息
        var recIdsArr = $('#send_note_person').combobox('getValues');
        if (recIdsArr == null || recIdsArr == ''){
            alert('用户不能为空');
            return;
        }
        param.recIds = recIdsArr.join(",");
    }
    //组织
    else if (send_note_type == 2){
        param.sendType = 2;
        var department = $('#send_note_department').combotree('tree').tree('getSelected');
        param.groupCode = department.attributes.code;
    }

    $.ajax({
        url: '/message/send',
        data: param,
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','发送成功！','info');
                $('#showSendNoteDialog').dialog('close');
            }else{
                $.messager.alert('提示',data.message,'error');
            }
        }
    });
}