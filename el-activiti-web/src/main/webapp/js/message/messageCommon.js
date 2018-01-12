/**
 * 根据index获取行数据
 */
function getRowDataByIndex(id,rowIndex){
    var rows = $('#'+id).datagrid('getRows');
    return rows[rowIndex];
}

/**
 * 格式化状态
 */
function statusFormatter(value, row, index) {
    var status = row.status;
    if (status == 0 || status == '0') {
        return "未读";
    } else if (status == 1 || status == '1') {
        return "已读";
    } else {
        return "其他";
    }
}
/**
 * 格式化发送类型
 */
function sendTypeFormatter(value, row, index) {
    var sendType = value;
    if (sendType == 1 || sendType == '1') {
        return "私人消息";
    } else if (sendType == 2 || sendType == '2') {
        return "公共消息";
    } else {
        return "系统消息";
    }
}
/**
 * 消息类型
 */
function typeFormatter(value, row, index){
    var type = value;
    if (type == 1 || type == '1') {
        return "站内信";
    } else if (type == 2 || type == '2') {
        return "邮件";
    } else if (type == 3 || type == '3') {
        return "短信";
    }else if (type == 4 || type == '4') {
        return "公告";
    }
}
/**
 * 发送类型
 */
function sendTypeFormatter(value, row, index){
    var sendType = value;
    if (sendType == 1 || sendType == '1') {
        return "私人消息";
    } else if (sendType == 2 || sendType == '2') {
        return "公共消息";
    } else if (sendType == 3 || sendType == '3') {
        return "系统消息";
    }
}

/**
 * 阅读消息
 */
function readMessage(id,index){
    showReadMessage(id,index);
    var row = getRowDataByIndex(id,index);
    if (row.status == 0 || row.status == '0'){
        var param = {};
        if (row.id != 0 && row.id > 0){
            param.id = row.id;
        }else{
            param.recId = row.recId;
            param.messageId = row.messageId;
        }
        param.status = 1;
        updateMessage(param,null,id);
    }
}

/**
 * 展示消息
 */
function showReadMessage(id,index){
    var row = getRowDataByIndex(id,index);
    $("#showMessageTitle").html("标题："+row.title+"<br\>发送人："+row.sendName+"<br\>发送时间：" + dateTimeFormatter(row.createTime));
    $("#showMessage").html(row.message);
    $('#showMessageRead').dialog('open');
}

/**
 * 删除消息
 */
function delMessage(id,index){
    var row = getRowDataByIndex(id,index);
    var configMsg = '确认删除该消息吗？';
    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        var param = {};
        if (row.id != 0 && row.id > 0){
            param.id = row.id;
        }else{
            param.recId = row.recId;
            param.messageId = row.messageId;
        }
        param.status = 2;
        if (r) {
            updateMessage(param,index,id);
        }
    })
}
/**
 * 更新消息
 */
function updateMessage(param,index,id){
    $.ajax({
        url: '/message/updateMessageRec',
        data: param,
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                if (index && index != null){
                    $.messager.alert('提示','操作成功！','info',function(){
                        $('#'+id).datagrid('deleteRow',index);
                    });
                }
                if (id && id == 'showDataTable'){
                    $('#'+id).datagrid("reload",{recId:2});
                }
            }else{
                $.messager.alert('提示',data.message,'error');
            }

        }
    });
}