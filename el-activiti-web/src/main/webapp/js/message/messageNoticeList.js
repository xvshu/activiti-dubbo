/**
 * 初始化消息列表
 */
function initMessageNoticeList(){
    $('#showDataTable').datagrid({
        loadMsg: "数据加载中...",
        title:'公告列表',
        url:'/message/loadSendMessage',
        queryParams:{rows:10,type:4},
        loadFilter: function(data){
            if (data.code == '0'){
                var pageinfo = data.data;
                var total = pageinfo.totalCount;
                var data_list = pageinfo.items;
                var data_return = {};
                data_return.total = total;
                data_return.rows = data_list;
                return data_return;
            }
        },
        columns:[[
            {field:'id',hidden:true},
            {field:'sendId',hidden:true},
            {field:'sendName',title:'发送人',width:80},
            {field:'type',title:'消息类型',width:80,formatter:typeFormatter},
            {field:'title',title:'消息',width:120},
            {field:'message',hidden:true},
            {field:'createTime',title:'创建时间',width:140,formatter: dateTimeFormatter},
            {field:'_operate',title:'操作',width:120,formatter:function(value,row,index){
                var html = '<a name="operateRead" href="javascript:showReadMessage(\'showDataTable\','+index+');" class="easyui-linkbutton"></a>';
                return html;
            }}
        ]],
        onLoadSuccess:function(data){
            $("a[name='operateRead']").linkbutton({text:'阅读',plain:true,iconCls:'icon-detail'});
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

function searchTitleSimple(){
    var selTitle = $("#selTitleSimple").val();
    if (selTitle == null || selTitle == ''){
        $.messager.alert("操作提示", "标题不能为空");
        return;
    }
    var param = {title:selTitle,type:4};
    $('#showDataTable').datagrid('load',param);
}