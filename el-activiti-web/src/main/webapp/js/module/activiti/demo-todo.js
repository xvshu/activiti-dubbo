/**
 * 请假流程任务办理
 */
$(function() {

    // 签收
    $('.claim').button({
        icons: {
            primary: 'ui-icon-person'
        }
    });
    
    // 办理
    $('.handle').button({
        icons: {
            primary: 'ui-icon-comment'
        }
    }).click(handle);
    
    // 跟踪
    $('.trace').click(graphTrace);
    
});


// 用于保存加载的详细信息
var detail = {};





/**
 * 完成任务
 * @param {Object} taskId
 */
function complete(taskId, variables) {
    var dialog = this;
    
	// 转换JSON为字符串
    var keys = "", values = "", types = "";
	if (variables) {
		$.each(variables, function() {
			if (keys != "") {
				keys += ",";
				values += ",";
				types += ",";
			}
			keys += this.key;
			values += this.value;
			types += this.type;
		});
	}
	
	$.blockUI({
        message: '<h2><img src="' + ctx + '/images/ajax/loading.gif" align="absmiddle"/>正在提交请求……</h2>'
    });
	
	// 发送任务完成请求
    $.post(ctx + '/demo/activiti/complete/' + taskId, {
        keys: keys,
        values: values,
        types: types
    }, function(resp) {
		$.unblockUI();
        if (resp == 'success') {
            alert('任务完成');
            location.reload();
        } else {
            alert('操作失败!');
        }
    });
}


/*
 * 使用json方式定义每个节点的按钮
 * 以及按钮的功能
 * 
 * open:打开对话框的时候需要处理的任务
 * btns:对话框显示的按钮
 */
var handleOpts = {
    zongjian: {
        width: 300,
        height: 300,
        btns: [{
            text: '同意',
            click: function() {
                var taskId = $(this).data('taskId');

                // 设置流程变量
                complete(taskId, [{
                    key: 'pass',
                    value: true,
                    type: 'B'
                }]);
            }
        }, {
            text: '驳回',
            click: function() {
                var taskId = $(this).data('taskId');

                $('<div/>', {
                    title: '填写驳回理由',
                    html: "<textarea id='leaderBackReason' style='width: 250px; height: 60px;'></textarea>"
                }).dialog({
                    modal: true,
                    open: function() {

                    },
                    buttons: [{
                        text: '驳回',
                        click: function() {
                            var leaderBackReason = $('#leaderBackReason').val();
                            if (leaderBackReason == '') {
                                alert('请输入驳回理由！');
                                return;
                            }

                            // 设置流程变量
                            complete(taskId, [{
                                key: 'pass',
                                value: false,
                                type: 'B'
                            }, {
                                key: 'leaderBackReason',
                                value: leaderBackReason,
                                type: 'S'
                            }]);
                        }
                    }, {
                        text: '取消',
                        click: function() {
                            $(this).dialog('close');
                            $('#deptLeaderAudit').dialog('close');
                        }
                    }]
                });
            }
        }, {
            text: '取消',
            click: function() {
                $(this).dialog('close');
            }
        }]
    },
    jingli: {
        width: 300,
        height: 300,
        btns: [{
            text: '同意',
            click: function() {
                var taskId = $(this).data('taskId');

                if(!$("#nextUsers").val()){
                    alert("请填写下一级处理用户！");
                    return;
                }

                // 设置流程变量
                complete(taskId, [{
                    key: 'pass',
                    value: true,
                    type: 'B'
                },{
                    key: 'nextUserId',
                    value: $("#nextUsers").val(),
                    type: 'S'
                }]);
            }
        }, {
            text: '驳回',
            click: function() {
                var taskId = $(this).data('taskId');

                $('<div/>', {
                    title: '填写驳回理由',
                    html: "<textarea id='leaderBackReason' style='width: 250px; height: 60px;'></textarea>请用,号分割"
                }).dialog({
                    modal: true,
                    open: function() {

                    },
                    buttons: [{
                        text: '驳回',
                        click: function() {
                            var leaderBackReason = $('#leaderBackReason').val();
                            if (leaderBackReason == '') {
                                alert('请输入驳回理由！');
                                return;
                            }

                            // 设置流程变量
                            complete(taskId, [{
                                key: 'pass',
                                value: false,
                                type: 'B'
                            }, {
                                key: 'leaderBackReason',
                                value: leaderBackReason,
                                type: 'S'
                            }]);
                        }
                    }, {
                        text: '取消',
                        click: function() {
                            $(this).dialog('close');
                            $('#deptLeaderAudit').dialog('close');
                        }
                    }]
                });
            }
        }, {
            text: '取消',
            click: function() {
                $(this).dialog('close');
            }
        }]
    }
};




/**
 * 办理流程
 */
function handle() {
	// 当前节点的英文名称
	var tkey = $(this).attr('tkey');
	
	// 当前节点的中文名称
	var tname = $(this).attr('tname');
	
	// 请假记录ID
	var rowId = $(this).parents('tr').attr('id');
	
	// 任务ID
	var taskId = $(this).parents('tr').attr('tid');
	
	// 使用对应的模板
	$('#' + tkey).data({
		taskId: taskId
	}).dialog({
		title: '流程办理[' + tname + ']',
		modal: true,
		width: handleOpts[tkey].width,
		height: handleOpts[tkey].height,
		buttons: handleOpts[tkey].btns
	});
}

Date.prototype.format = function(format) {
    var o = {
        "M+": this.getMonth() + 1, //month 
        "d+": this.getDate(), //day 
        "h+": this.getHours(), //hour 
        "m+": this.getMinutes(), //minute 
        "s+": this.getSeconds(), //second 
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter 
        "S": this.getMilliseconds() //millisecond 
    }
    if (/(y+)/.test(format)) 
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) 
        if (new RegExp("(" + k + ")").test(format)) 
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}