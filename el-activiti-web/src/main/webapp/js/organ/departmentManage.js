function showDialogAddDep(){
    $("#add_form").form('clear');
    $('#add_diglog').dialog('open')
}

/**
 * 添加节点
 */
function addDepartment(){
    var department_name = $("#department_name").val();
    if (department_name == null || department_name == ''){
        alert('部门名称不能为空');
        return;
    }
    var node = getSelectedTreeNode();
    //将新节点信息发送到后端服务入库
    $.ajax({
        url: '/department/add',
        data: {name:department_name,pid:node.id,parentCode:node.attributes.code},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','保存成功！','info',function(){
                    var data_append = [{id : data.data.id,code : data.data.code,name: data.data.name}];
                    treeNodeAppend(node,data_append);
                    $('#add_diglog').dialog('close');
                });
            }else{
                $.messager.alert('提示',data.message,'error');
            }

        }
    });
}

/**
 * 打开修改节点
 */
function updateDepartment_show(){
    var node = getSelectedTreeNode();
    $("#department_name_update").textbox('setValue',node.text);
    $('#update_diglog').dialog('open');
}

/**
 * 修改节点
 */
function updateDepartment(){
    var node = getSelectedTreeNode();
    var name_update = $("#department_name_update").val();
    var name = node.text;
    if (name == name_update) {
        alert("部门名字没有改变，不能进行修改操作！");
        return;
    }
    //将节点信息发送到后端服务入库
    $.ajax({
        url: '/department/update',
        data: {name:name_update,id:node.id},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','修改成功！','info',function(){
                    treeNodeUpdate(node,name_update);
                    $('#update_diglog').dialog('close');
                });
            }else{
                $.messager.alert('提示',data.message,'error');
            }

        }
    });
}

/**
 * 删除节点
 */
function removeDepartment(){
    var node = getSelectedTreeNode();
    var configMsg = '确认删除 "'+node.text+'" 吗？';

    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        if (r) {
            $.ajax({
                url: '/department/delete',
                data: {id:node.id,status:3},
                type: "post",
                async:false,
                success: function (data, textStatus, XMLHttpRequest) {
                    if(data != null && data.code=='0'){
                        $.messager.alert('提示','删除成功！','info',treeNodeRemove(node));
                    }else{
                        $.messager.alert('提示',data.message,'error');
                    }
                }
            });
        }
    });

}

