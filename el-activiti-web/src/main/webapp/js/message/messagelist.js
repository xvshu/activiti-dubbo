/**
 * 初始化消息列表
 */
function initMessageList(){
    $('#showDataTable').datagrid({
        loadMsg: "数据加载中...",
        title:'消息列表',
        url:'/message/loadAll',
        queryParams:{rows:10,recId:2},
        loadFilter: function(data){
            if (data.code == '0'){
                var pageinfo = data.data;
                var total = pageinfo.totalCount;
                var data_list = pageinfo.items;
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
            {field:'id',hidden:true},
            {field:'recId',hidden:true},
            {field:'messageId',hidden:true},
            {field:'sendName',title:'发送人',width:80},
            {field:'status',hidden:true},
            {field:'statusStr',title:'状态',width:80,formatter:statusFormatter},
            {field:'sendType',title:'消息类型',width:80,formatter:sendTypeFormatter},
            {field:'title',title:'消息',width:120},
            {field:'message',hidden:true},
            {field:'createTime',title:'创建时间',width:140,formatter: dateTimeFormatter},
            {field:'_operate',title:'操作',width:120,formatter:function(value,row,index){
                var html = '';
                html = html + '<a name="operateRead" href="javascript:readMessage(\'showDataTable\','+index+');" class="easyui-linkbutton"></a>';
                html = html + '<a name="operateDel" href="javascript:delMessage(\'showDataTable\','+index+');" class="easyui-linkbutton"></a>';
                return html;
            }}
        ]],
        onLoadSuccess:function(data){
            $("a[name='operateRead']").linkbutton({text:'阅读',plain:true,iconCls:'icon-detail'});
            $("a[name='operateDel']").linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
        },
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
 * 根据发送人查询
 */
function searchSendMessageSimple(){
    var selSendName = $("#selSendNameSimple").val();
    if (selSendName == null || selSendName == ''){
        $.messager.alert("操作提示", "发送人不能为空");
        return;
    }
    var param = {sendName:selSendName,recId:2};
    $('#showDataTable').datagrid('load',param);
}

function showDialogSearchSendMessage(){
    $("#selNameSimple").textbox('setValue','');
    $("#searchForm").form('clear');
    $('#search_diglog').dialog('open');
}

function searchSendMessage(){
    var param = {};
    var selSendName = $("#selSendName").val();
    if (selSendName != null && selSendName != ''){
        param.sendName = selSendName;
    }
    var selTitle = $("#selTitle").val();
    if (selTitle != null && selTitle != ''){
        param.title = selTitle;
    }
    param.recId = 2;
    $('#showDataTable').datagrid('load',param);
    $('#search_diglog').dialog('close');
}