//查询未读消息
function searchMessage(){
    var messageData = null;
    //调用服务查询未读消息
    $.ajax({
        url: '/message/searchMessageRecByUidForIndex',
        data: {uid:2},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                messageData = data.data;
            }
        }
    });

    if (!messageData || messageData.totalCount<=0){
        return;
    }
    if (messageData.items == null){
        $("#messageNoticeDialog").remove("#messageNoticeTable");
        $("#messageNoticeDialog").html("您有"+messageData.totalCount+"条消息未读，请查看！")
    }else {
        initMessageNoticeTable(messageData.items);
    }
    showMessageNoticeDialog();

    //查询公告
    searchNotice();
}

//初始化公告列表
function initMessageNoticeTable(data){
    $('#messageNoticeTable').datagrid({
        data:data,
        columns:[[
            {field:'id',hidden:true},
            {field:'recId',hidden:true},
            {field:'messageId',hidden:true},
            {field:'sendName',title:'发送人',width:60},
            {field:'sendType',title:'消息类型',width:60,formatter:sendTypeFormatter},
            {field:'title',title:'消息',width:80},
            {field:'message',hidden:true},
            {field:'createTime',hidden:true},
            {field:'_operate',title:'操作',width:80,formatter:function(value,row,index){
                var html = '';
                html = html + '<a name="operateRead" href="javascript:readMessage(\'messageNoticeTable\','+index+');" class="easyui-linkbutton"></a>';
                return html;
            }}
        ]],
        onLoadSuccess:function(data){
            $("a[name='operateRead']").linkbutton({text:'阅读',plain:true,iconCls:'icon-detail'});
        },
    });
}

// 弹出公告框
function showMessageNoticeDialog(){
    var messageNoticeDialogWidth = 300;		// dialog的宽度
    var messageNoticeDialogHeight = 300;		// dialog的高度
    var topPosition = $(window).height() - messageNoticeDialogHeight;
    var leftPosition = $(window).width() - messageNoticeDialogWidth;
    $('#messageNoticeDialog').dialog({
        width: messageNoticeDialogWidth,
        height: messageNoticeDialogHeight,
        top : topPosition,
        left : leftPosition,
        title:'未读消息'
    }).dialog('open');

    $(".window-mask").remove();
}

/**
 * 展示公告
 */
function showNotice(title,createTime,message){
    $("#showMessageTitle").html("标题："+title+"<br\>发送时间：" + dateTimeFormatter(createTime));
    $("#showMessage").html(message);
    $('#showMessageRead').dialog('open');
}

function searchNotice(){
    //调用服务查询公告
    $.ajax({
        url: '/message/searchNoticeMessageForIndex',
        data: {type:4},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                var list = data.data.items;
                var noticeHtml = '<marquee onmouseout=this.start() onmouseover=this.stop() behavior="scroll" direction="left" width="1000px;" height="30px" scrolldelay="200" id="showNoticeMarquee">公告：';
                $(list).each(function(i,val) {
                    noticeHtml = noticeHtml + '<a href="javascript:showNotice(\''+val.title+'\',\''+val.createTime+'\',\''+val.message+'\')">'+val.title+'</a>&nbsp;&nbsp;';
                });
                noticeHtml = noticeHtml + '</marquee>';
                $("#showNoticeMarquee").html(noticeHtml);
            }
        }
    });
}