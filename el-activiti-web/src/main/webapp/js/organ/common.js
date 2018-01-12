/**
 * 状态格式化
 * @param value
 */
function statusFormatter(value){
    if (value == 1 || value == '1') {
        return "正常";
    }else if(value == 2 || value == '2'){
        return "禁用";
    }else if (value == 3 || value == '3'){
        return "删除";
    }else {
        return "其他";
    }
}

/**
 * 格式化日期时间
 */
function dateTimeFormatter(value) {
    if (value == undefined) {
        return "";
    }
    var date = new Date();
    date.setTime(value);

    return date.Format("yyyy-MM-dd hh:mm:ss");
}

/**
 * 组织机构状态字段高亮显示
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function statusStyler(value,row,index){
    if (value > 1){
        return 'background-color:#ffee00;color:red;';
    }
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

/**
 * 获取选中的行信息
 */
function getSelected(id){
    return $('#'+id).datagrid('getSelected');
}
/**
 * 获取选中的多行信息
 */
function getSelections(){
    var ss = [];
    var rows = $('#showDataTable').datagrid('getSelections');
    for(var i=0; i<rows.length; i++){
        var row = rows[i];
        ss.push('<span>'+row.id+":"+row.name+":"+row.status+'</span>');
    }
    $.messager.alert('Info', ss.join('<br/>'));
}

/**
 * 判断字符串是否以某个字符串结尾
 */
String.prototype.endWith=function(endStr){
    var d=this.length-endStr.length;
    return (d>=0&&this.lastIndexOf(endStr)==d)
}