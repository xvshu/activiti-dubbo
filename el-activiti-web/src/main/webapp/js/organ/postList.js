/**
 * 初始化职位列表
 */
function initPostList(){
    $('#showDataTable').datagrid({
        loadMsg: "数据加载中...",
        title:'职位列表',
        url:'/post/loadAll',
        queryParams:{rows:10},
        loadFilter: function(data){
            if (data.code == '0'){
                var pageinfo = data.data;
                var total = pageinfo.total;
                var data_list = pageinfo.data;
                var data_return = {};
                data_return.total = total;
                data_return.rows = data_list;
                return data_return;
            } else {
                $.messager.alert("操作提示", data.message);
                return ;
            }
        },
        columns:[[
            {field:'id',title:'ID',hidden:true},
            {field:'name',title:'名字',width:100},
            {field:'status',title:'状态',width:100,formatter:statusFormatter,styler:statusStyler},
            {field:'createTime',title:'创建时间',width:140,formatter: dateTimeFormatter}
        ]]
    });

    //设置分页控件
    var pager = $('#showDataTable').datagrid('getPager');
    $(pager).pagination({
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10,20],//可以设置每页记录条数的列表
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
    });
}

/**
 * 获取查询参数
 */
function getParamSearchPost(){
    var selName = $("#selName").val();
    var param = {};
    if (selName!=null && selName!=''){
        param.name = selName;
    }
    return {paramMap:JSON.stringify(param)};;
}

/**
 * 查询
 */
function searchPost(){
    var param = getParamSearchPost();
    //if (JSON.stringify(param) != "{}"){
    //}
    $('#showDataTable').datagrid('load',param);
    $('#search_diglog').dialog('close');
}


/**
 * 添加-打开对话框
 */
function showDialogAddPost(){
    $("#add_form").form('clear');
    $('#add_diglog').dialog('open');
}
function addPost(){
    var name = $('#post_name').val();
    $.ajax({
        url: '/post/add',
        data: {name:name},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','添加成功！','info');
                $('#showDataTable').datagrid("reload",{});
                $('#add_diglog').dialog('close');
            }else{
                $.messager.alert('提示',data.message,'error');
            }
        }
    });
}

/**
 * 修改-打开对话框
 */
function showDialogEditPost(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要修改的职位");
        return;
    }
    $("#editName").textbox('setValue',row.name);
    $('#edit_diglog').dialog('open');
}

function editPost(){
    var row = getSelected('showDataTable');
    $.ajax({
        url: '/post/update',
        data: {id:row.id,name:$("#editName").val()},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','修改成功！','info');
                $('#showDataTable').datagrid("reload",getParamSearchPost());
                $('#edit_diglog').dialog('close');
            }else{
                $.messager.alert('提示',data.message,'error');
            }
        }
    });
}

/**
 * 禁用确认
 */
function confirmNoPost(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要禁用的职位");
        return;
    }
    var configMsg = '确认禁用 "'+row.name+'" 吗？';
    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        if (r) {
            $.ajax({
                url: '/post/update',
                data: {id:row.id,status:2},
                type: "post",
                async:false,
                success: function (data, textStatus, XMLHttpRequest) {
                    if(data != null && data.code=='0'){
                        $.messager.alert('提示','禁用成功！','info');
                        $('#showDataTable').datagrid("reload",getParamSearchPost());
                    }else{
                        $.messager.alert('提示',data.message,'error');
                    }
                }
            });
        }
    });
}
/**
 * 恢复职位
 */
function confirmUndoPost(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要恢复的职位");
        return;
    }
    var configMsg = '确认恢复 "'+row.name+'" 吗？';
    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        if (r) {
            $.ajax({
                url: '/post/update',
                data: {id:row.id,status:1},
                type: "post",
                async:false,
                success: function (data, textStatus, XMLHttpRequest) {
                    if(data != null && data.code=='0'){
                        $.messager.alert('提示','恢复成功！','info');
                        $('#showDataTable').datagrid("reload",getParamSearchPost());
                    }else{
                        $.messager.alert('提示',data.message,'error');
                    }
                }
            });
        }
    });
}

/**
 * 获取全部职位信息
 */
function loadAllPost(){
    var returnData = '';
    $.ajax({
        url:'/post/loadAll',
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                returnData = data.data.data;
            }
        }
    });
    return returnData;
}