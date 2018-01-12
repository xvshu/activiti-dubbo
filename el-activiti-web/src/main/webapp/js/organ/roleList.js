/**
 * 初始化角色列表
 */
function initRoleList(){
    $('#showDataTable').datagrid({
        loadMsg: "数据加载中...",
        title:'角色列表',
        url:'/role/loadAll',
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
function getParamSearchRole(){
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
function searchRole(){
    var param = getParamSearchRole();
    //if (JSON.stringify(param) != "{}"){
    //}
    $('#showDataTable').datagrid('load',param);
    $('#search_diglog').dialog('close');
}


/**
 * 添加-打开对话框
 */
function showDialogAddRole(){
    $("#add_form").form('clear');
    $('#add_diglog').dialog('open');
}
function addRole(){
    var name = $('#role_name').val();
    $.ajax({
        url: '/role/add',
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
function showDialogEditRole(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要修改的角色");
        return;
    }
    $("#editName").textbox('setValue',row.name);
    $('#edit_diglog').dialog('open');
}

function editRole(){
    var row = getSelected('showDataTable');
    $.ajax({
        url: '/role/update',
        data: {id:row.id,name:$("#editName").val()},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','修改成功！','info');
                $('#showDataTable').datagrid("reload",getParamSearchRole());
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
function confirmNoRole(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要禁用的角色");
        return;
    }
    var configMsg = '确认禁用 "'+row.name+'" 吗？';
    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        if (r) {
            $.ajax({
                url: '/role/update',
                data: {id:row.id,status:2},
                type: "post",
                async:false,
                success: function (data, textStatus, XMLHttpRequest) {
                    if(data != null && data.code=='0'){
                        $.messager.alert('提示','禁用成功！','info');
                        $('#showDataTable').datagrid("reload",getParamSearchRole());
                    }else{
                        $.messager.alert('提示',data.message,'error');
                    }
                }
            });
        }
    });
}
/**
 * 恢复角色
 */
function confirmUndoRole(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要恢复的角色");
        return;
    }
    var configMsg = '确认恢复 "'+row.name+'" 吗？';
    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        if (r) {
            $.ajax({
                url: '/role/update',
                data: {id:row.id,status:1},
                type: "post",
                async:false,
                success: function (data, textStatus, XMLHttpRequest) {
                    if(data != null && data.code=='0'){
                        $.messager.alert('提示','恢复成功！','info');
                        $('#showDataTable').datagrid("reload",getParamSearchRole());
                    }else{
                        $.messager.alert('提示',data.message,'error');
                    }
                }
            });
        }
    });
}

/**
 * 获取全部角色信息
 */
function loadAllRole(){
    var returnData = '';
    $.ajax({
        url:'/role/loadAll',
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

/**
 * 加载权限
 */
function loadRights(){

    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择角色");
        return;
    }

    //默认展开
    $('#easyui_tree').tree('expandAll');

    //先将回显数据全部清除
    var roots=$("#easyui_tree").tree('getRoots');
    for(var i=0;i<roots.length;i++){
        $("#easyui_tree").tree('uncheck',roots[i].target);
    }

    var list = loadCheckRights(row.id);
    for(var i=0;i<list.length;i++){
        var node = $('#easyui_tree').tree('find', list[i].rightId);
        $('#easyui_tree').tree('check',node.target);
    }

    $('#rights_diglog').dialog('open');
}

/**
 * 获取角色的权限信息
 * @param roleId
 * @returns {string}
 */
function loadCheckRights(roleId){
    var returnData = '';
    $.ajax({
        url:'/role/right/list',
        data: {roleId:roleId},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                returnData = data.data;
            }
        }
    });
    return returnData;
}

/**
 * 添加权限
 */
function addRights(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择角色");
        return;
    }
    var nodes = $('#easyui_tree').tree('getChecked');
    if(nodes==null || nodes == 'undefined' || nodes == ''){
        $.messager.alert("操作提示", "请选择权限");
        return;
    }
    var rightIds = '';
    for(var i=0;i<nodes.length;i++){
        if (rightIds != '') rightIds += ',';
        rightIds += nodes[i].id;
    }

    var param = {};
    param.roleId = row.id;
    param.rightIds = rightIds;

    //添加权限
    updateRights(param);
}

function updateRights(param){
    $.ajax({
        url:'/role/right/update',
        data: param,
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','添加成功！','info',function(){
                    $('#rights_diglog').dialog('close');
                });
            }else{
                $.messager.alert('提示',data.message,'error');
            }
        }
    });
}