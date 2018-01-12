$.extend($.fn.validatebox.defaults.rules, {
    phoneNum: { //验证手机号
        validator: function(value, param){
            return /^1[3-8]+\d{9}$/.test(value);
        },
        message: '请输入正确的手机号码。'
    },
    telNum:{ //既验证手机号，又验证座机号
        validator: function(value, param){
            return /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d3)|(\d{3}\-))?(1[358]\d{9})$)/.test(value);
        },
        message: '请输入正确的电话号码。'
    }
});

/**
 * 初始化用户列表
 */
function initUserList(){
    $('#showDataTable').datagrid({
        loadMsg: "数据加载中...",
        title:'用户列表',
        url:'/user/loadAll',
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
                $("#searchForm").form('clear');
                $.messager.alert("操作提示", data.message);
                return ;
            }
        },
        columns:[[
            {field:'id',title:'ID',hidden:true},
            {field:'name',title:'真实姓名',width:80},
            {field:'username',title:'登录名',width:100},
            {field:'email',title:'邮箱',width:150},
            {field:'mobile',title:'手机号',width:80},
            {field:'userType',hidden:true},
            {field:'userTypeStr',title:'用户类型',width:60,formatter:userTypeFormatter},
            {field:'postId',title:'职位id',hidden:true},
            {field:'postName',title:'职位',width:100},
            {field:'depCode',title:'部门编码',hidden:true},
            {field:'depName',title:'部门',width:150},
            {field:'funCode',title:'职务编码',hidden:true},
            {field:'funName',title:'职务',width:100},
            {field:'areaCode',title:'区域编码',hidden:true},
            {field:'areaName',title:'区域',width:210},
            {field:'status',title:'状态',width:60,formatter:statusFormatter,styler:statusStyler},
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
 * 用户类型格式化
 * @param value
 * @returns {*}
 */
function userTypeFormatter(value, row, index){
    var userType = row.userType;
    if (userType == 1 || userType == '1') {
        return "总部";
    }else if(userType == 2 || userType == '2'){
        return "盟商";
    }else {
        return "其他";
    }
}

//全局查询条件
var selParam = {};

/**
 * 查询-打开对话框
 */
function showDialogSearchUser(){
    $("#selNameSimple").textbox('setValue','');
    $("#searchForm").form('clear');
    $('#search_diglog').dialog('open');
}
/**
 * 获取查询参数
 */
function getParamSearchUser(){
    var selName = $("#selName").val();
    var selPostName = $("#selPostName").val();
    var selDepName = $("#selDepName").val();
    var selFunName = $("#selFunName").val();
    var selAreaName = $("#selAreaName").val();
    var param = {};
    if (selName!=null && selName!=''){
        param.name = selName;
    }
    if (selPostName!=null && selPostName!=''){
        param.postName = selPostName;
    }
    if (selDepName!=null && selDepName!=''){
        param.depName = selDepName;
    }
    if (selFunName!=null && selFunName!=''){
        param.funName = selFunName;
    }
    if (selAreaName!=null && selAreaName!=''){
        param.areaName = selAreaName;
    }
    return {paramMap:JSON.stringify(param)};
}

/**
 * 查询
 */
function searchUser(){
    var param = getParamSearchUser();
    //if (JSON.stringify(param) != "{}"){
    //}
    $('#showDataTable').datagrid('load',param);
    $('#search_diglog').dialog('close');
    selParam = param;
}
/**
 * 简单查询
 */
function searchUserSimple(){
    var selName = $("#selNameSimple").val();
    if (selName == null || selName == ''){
        $.messager.alert("操作提示", "用户名不能为空");
        return;
    }
    var param = {paramMap:JSON.stringify({name:selName})};
    $('#showDataTable').datagrid('load',param);
    selParam = param;
}

/**
 * 初始化职位信息
 */
var post = '';
function initPost(type){
    if (post==null || post == '' || post == undefined){
        post = loadAllPost();
    }
    if (post==null || post == ''  || post == undefined){
        $.messager.alert("操作提示", "获取职位信息失败");
        return false;
    }
    $("#"+type+"_post").combobox("loadData", post);
    return true;
}

/**
 * 初始化部门信息
 * @type {string}
 */
var department = '';
function initDepartment(type){
    if (department==null || department == '' || department == undefined){
        department = getTreeNodeData('/department/loadAll');
    }
    if (department==null || department == ''  || department == undefined){
        $.messager.alert("操作提示", "获取部门信息失败");
        return false;
    }
    $("#"+type+"_department").combotree("loadData", department);
    return true;
}

/**
 * 初始化职务信息
 * @type {string}
 */
var functionData = '';
function initFunction(type){
    if (functionData==null || functionData == '' || functionData == undefined){
        functionData = getTreeNodeData('/function/loadAll');
    }
    if (functionData==null || functionData == ''  || functionData == undefined){
        $.messager.alert("操作提示", "获取职务信息失败");
        return false;
    }
    $("#"+type+"_function").combotree("loadData", functionData);
    return true;
}

/**
 * 添加-打开对话框
 */
function showDialogAddUser(){
    $("#showAreaMsgDivForEditUser").remove("#areaMsgDiv");
    $("#showAreaMsgDivForAddUser").append($("#areaMsgDiv"));
    initUserMsg('add');
}

function addUser(){
    var param = {};
    //用户名
    var name = $('#add_user_name').val();
    param.name = name;
    //登录名
    var name_login = $('#add_user_name_login').val();
    if (name_login!=null && name_login!=''){
        param.username = name_login;
    }
    //密码
    var passwd_login = $('#add_user_passwd').val();
    if (passwd_login!=null && passwd_login!=''){
        param.loginPasswd = passwd_login;
    }
    //邮箱
    var email = $('#add_user_email').val();
    if (email!=null && email!=''){
        param.email = email;
    }
    //手机号
    var mobile = $('#add_user_mobile').val();
    if (mobile!=null && mobile!=''){
        param.mobile = mobile;
    }
    //用户类型
    var userType = $('#add_user_type').combobox('getValue');
    param.userType = userType;
    //部门信息
    var department = $('#add_department').combotree('tree').tree('getSelected');
    param.depCode = department.attributes.code;
    param.depName = department.text;
    //职位信息
    param.postId = $('#add_post').combobox('getValue');
    param.postName = $('#add_post').combobox('getText');
    //职位信息
    var functionData = $('#add_function').combotree('tree').tree('getSelected');
    param.funCode = functionData.attributes.code;
    param.funName = functionData.text;
    //区域信息
    var county = getSelectCounty();
    var city = getSelectCity();
    var province = getSelectProvince();
    if (county.code != null && county.name != null){
        param.areaName = county.name;
        param.areaCode = county.code;
    }else if (city.code != null && city.name != null){
        param.areaName = city.name;
        param.areaCode = city.code;
    }else if (province.code != null && province.name != null){
        param.areaName = province.name;
        param.areaCode = province.code;
    }

    $.ajax({
        url: '/user/add',
        data: param,
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

function initUserMsg(type){
    $("#"+type+"_form").form('clear');

    //初始化职位信息
    if (!initPost(type)){
        return;
    }
    //初始化部门信息
    if (!initDepartment(type)){
        return;
    }
    //初始化职务信息
    if (!initFunction(type)){
        return;
    }

    //初始化省份信息
    initProvince();

    if (type == 'edit'){
        var row = getSelected('showDataTable');
        $("#"+type+"_user_name").textbox('setValue',row.name);
        $("#"+type+"_user_type").combobox('select',row.userType);
        $("#"+type+"_department").combotree('setValue',row.depName);
        $("#"+type+"_post").combobox('select',row.postId);
        $("#"+type+"_function").combotree('setValue',row.funName);
        var areaCode = row.areaCode;
        if (areaCode){
            var provinceCode = '';
            var cityCode = '';
            var countyCode = '';
            //省份
            if(areaCode.endWith('0000')){
                provinceCode = areaCode;
            }else if(areaCode.endWith('00')){
                provinceCode = areaCode.substring(0,areaCode.length-4)+'0000';
                cityCode = areaCode;
            }else {
                provinceCode = areaCode.substring(0,areaCode.length-4)+'0000';
                cityCode = areaCode.substring(0,areaCode.length-2)+'00';
                countyCode = areaCode;
            }
            if (provinceCode != null && provinceCode != ''){
                $("#province").combobox('select',provinceCode);
            }
            if (cityCode != null && cityCode != ''){
                $("#city").combobox('select',cityCode);
            }
            if (countyCode != null && countyCode != ''){
                $("#county").combobox('select',countyCode);
            }
        }

    }

    $("#"+type+"_diglog").dialog('open');

    //$("#selPostName").textbox("setValue", row.postId);
    //row.userType;
    //row.postId
    //row.depCode
    //row.funCode
    //row.areaCode
}

/**
 * 修改-打开对话框
 */
function showDialogEditUser(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要修改的用户");
        return;
    }
    $("#showAreaMsgDivForAddUser").remove("#areaMsgDiv");
    $("#showAreaMsgDivForEditUser").append($("#areaMsgDiv"));

    initUserMsg('edit');
}

function editUser(){
    var param = {};
    //用户ID
    var row = getSelected('showDataTable');
    param.id = row.id;
    //用户名
    var name = $('#edit_user_name').val();
    param.name = name;
    //用户类型
    var userType = $('#edit_user_type').combobox('getValue');
    if (userType != row.userType){
        param.userType = userType;
    }
    //部门信息
    var departmentText = $("#edit_department").combotree('getValue');
    if(departmentText != row.depName){
        var department = $('#edit_department').combotree('tree').tree('getSelected');
        param.depCode = department.attributes.code;
        param.depName = department.text;
    }
    //职位信息
    var postId = $('#edit_post').combobox('getValue');
    if (postId != row.postId){
        param.postId = $('#edit_post').combobox('getValue');
        param.postName = $('#edit_post').combobox('getText');
    }
    //职位信息
    var functionText = $('#edit_function').combotree('getValue');
    if (functionText != row.funName){
        var functionData = $('#edit_function').combotree('tree').tree('getSelected');
        param.funCode = functionData.attributes.code;
        param.funName = functionData.text;
    }


    //区域信息
    var county = getSelectCounty();
    var city = getSelectCity();
    var province = getSelectProvince();
    if (county.code != null && county.name != null){
        param.areaName = county.name;
        param.areaCode = county.code;
    }else if (city.code != null && city.name != null){
        param.areaName = city.name;
        param.areaCode = city.code;
    }else if (province.code != null && province.name != null){
        param.areaName = province.name;
        param.areaCode = province.code;
    }

    $.ajax({
        url: '/user/update',
        data: param,
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                $.messager.alert('提示','修改成功！','info');
                $('#showDataTable').datagrid("reload",selParam);
                $('#edit_diglog').dialog('close');
            }else{
                $.messager.alert('提示',data.message,'error');
            }
        }
    });
}

//树形结构标识
var treeMark = '';

/**
 * 打开树形结构
 * @param url
 */
function showTreeDiglog(url,mark){
    loadTreeNodeData(url);
    treeMark = mark;
    $('#showTree_diglog').dialog('open');
}
/**
 * 获取树形结构选择的数据
 */
function getTreeSelData(){
    var data = getSelectedTreeNode();
    if (!data) {
        $.messager.alert("操作提示", "尚未选择！");
        return;
    }
    if (treeMark == 'department'){
        $('#showSelDepartment').html(data.text);
        $('#selDepartmentCode').val(data.attributes.code);
    }else if(treeMark == 'function'){
        $('#showSelFunction').html(data.text);
        $('#selFunctionCode').val(data.attributes.code);
    }
    $('#showTree_diglog').dialog('close');
}

/**
 * 禁用确认
 */
function confirmNoUser(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要禁用的用户");
        return;
    }
    var configMsg = '确认禁用 "'+row.name+'" 吗？';
    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        if (r) {
            $.ajax({
                url: '/user/delete',
                data: {id:row.id,status:2},
                type: "post",
                async:false,
                success: function (data, textStatus, XMLHttpRequest) {
                    if(data != null && data.code=='0'){
                        $.messager.alert('提示','禁用成功！','info');
                        $('#showDataTable').datagrid("reload",selParam);
                    }else{
                        $.messager.alert('提示',data.message,'error');
                    }
                }
            });
        }
    });
}
/**
 * 恢复用户
 */
function confirmUndoUser(){
    var row = getSelected('showDataTable');
    if (!row){
        $.messager.alert("操作提示", "请选择要恢复的用户");
        return;
    }
    var configMsg = '确认恢复 "'+row.name+'" 吗？';
    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        if (r) {
            $.ajax({
                url: '/user/delete',
                data: {id:row.id,status:1},
                type: "post",
                async:false,
                success: function (data, textStatus, XMLHttpRequest) {
                    if(data != null && data.code=='0'){
                        $.messager.alert('提示','恢复用户成功！','info');
                        $('#showDataTable').datagrid("reload",selParam);
                    }else{
                        $.messager.alert('提示',data.message,'error');
                    }
                }
            });
        }
    });
}