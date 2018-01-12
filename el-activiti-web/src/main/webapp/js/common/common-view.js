/**
 * 页面通用组件
 * */
$.extend({
    /**
     * 还款方式-多选-页面组件
     * @param viewId多选组件id，该组件必须是easyui-combobox类型
     * @param selected 选中的还款方式
     * @param calback 初始化完成之后的回调函数
     * */
    repaymentTypeSelect:function(viewId,selectedArray,calback){
        if(selectedArray==undefined||selectedArray==null){
            selectedArray=[];
        }
        var $repaymentTypeList=$('#'+viewId);
        $repaymentTypeList.combobox("clear");
        $.ajax({
            contentType: "application/json; charset=utf-8",
            url: 'dictionary/repaymentType/listRepaymentTypeOptions',
            type: 'post',
            dataType:'json',
            data:JSON.stringify(selectedArray),
            async:false,
            success: function (data) {
                if (data.status==0) {
                    $repaymentTypeList.combobox("loadData", data.data);
                } else {
                    $.messager.alert("警告", "系统内部错误!", "warning");
                }
                if(calback!=undefined){
                    calback(data);
                }
            }
        })
    }
});