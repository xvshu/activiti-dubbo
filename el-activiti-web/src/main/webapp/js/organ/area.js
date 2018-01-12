$(document).ready(function () {
    $("#province").combobox({
        onChange: function (newValue,oldValue) {
            initArea(newValue,'city');
        }
    });
    $("#city").combobox({
        onChange: function (newValue,oldValue) {
            initArea(newValue,'county');
        }
    });
});

/**
 * 加载区域信息
 */
function loadAreaByCode(pcode) {
    var areaList = '';
    $.ajax({
        url:'/area/loadRecord',
        data: {pcode:pcode},
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                areaList = data.data;
            }
        }
    });
    return areaList;
}

/**
 * 展示区域信息
 */
function showArea(id,areaList){
    $("#county").combobox("clear");
    $("#"+id).combobox("clear");
    $("#"+id).combobox("loadData", areaList);
}

function getSelectData(id){
    var code = $('#'+id).combobox('getValue');
    if (code == null || code == '' || code == undefined){
        return {};
    }
    var areaName = $('#'+id).combobox('getText');
    return {name:areaName,code:code};
}

/**
 * 获取选中的省份ID和名字
 */
function getSelectProvince(){
    return getSelectData('province');
}
/**
 * 获取选中的城市ID和名字
 */
function getSelectCity(){
    return getSelectData('city');
}
/**
 * 获取选中的区县ID和名字
 */
function getSelectCounty(){
    return getSelectData('county');
}

/**
 * 初始化省份信息
 */
function initProvince(){
    initArea('0','province');
}

/**
 * 初始化区域信息
 */
function initArea(pcode,id){
    var areaList = loadAreaByCode(pcode);
    if (areaList == null || areaList == '' || areaList == undefined){
        $.messager.alert('提示','加载区域数据失败','error');
        return ;
    }
    showArea(id,areaList);
}