/**
 * 遍历表单，从json中取数据填充
 * @param $form
 * @param json
 */
var fillSpan = function ($Div, json,fun) {
    var jsonObj = json;
    if (typeof json === 'string') {
        jsonObj = $.parseJSON(json);
    }

    var objtype = jsonObjType(jsonObj);
    if(objtype === "array"){
        if(jsonObj.length==0){
            $Div.html("查无记录!");
            return;
        }else{

            var newHtml="";
            for(var i=0;i<jsonObj.length;i++){
                var trNew=$Div.clone();
                fillVal(trNew,jsonObj[i],fun);
                newHtml+=trNew.html();
            }
            $Div.html(newHtml);
        }

    }else{
        fillVal($Div,jsonObj,fun);
    }


}
var fillVal=function ($Div,jsonObj,selfFun) {
    for (var key in jsonObj) {  //遍历json字符串
        var objtype = jsonObjType(jsonObj[key]); // 获取值类型
        if (objtype === "string" || objtype === "number") { //如果是字符串
            var str = jsonObj[key];
            var $span=$("span[name='" + key+"']", $Div);
            var from = $span.data("from");
            if(from){//如果定义了取数据字典
                var type = $span.data("type");
                //此处没有在往后台发送请求，因为在另一个js里面有实现构建数据字典了
                commonUtils.sysLookupItemName(type,str,function(data){
                    autoFillValue(key,data,$span,selfFun);
                });
            }else{
                autoFillValue(key,str,$span,selfFun)


            }
        }

    }
}

function autoFillValue(key,value,$obj,selfFun) {
    if(!selfFun){
        $obj.text(value);
    }else{
        var selfFun2 = selfFun(key,value);
        if(selfFun2){
            $obj.text(selfFun2);
        }else{
            $obj.text(value);
        }
    }
}

/**
 * 遍历json 填充表单
 * @param $form
 * @param json
 */
var fillForm = function ($form, json,readOnly) {
    var jsonObj = json;
    if (typeof json === 'string') {
        jsonObj = $.parseJSON(json);
    }

    for (var key in jsonObj) {  //遍历json字符串
        var objtype = jsonObjType(jsonObj[key]); // 获取值类型

        if (objtype === "array") { //如果是数组，一般都是数据库中多对多关系

            var obj1 = jsonObj[key];
            for (var arraykey in obj1) {
                //alert(arraykey + jsonObj[arraykey]);
                var arrayobj = obj1[arraykey];
                for (var smallkey in arrayobj) {
                    setCkb(key, arrayobj[smallkey]);
                    break;
                }
            }
        } else if (objtype === "object") { //如果是对象，啥都不错，大多数情况下，会有 xxxId 这样的字段作为外键表的id

        } else if (objtype === "string" || objtype === "number") { //如果是字符串
            var str = jsonObj[key];
            /*var date = new Date(str);
            if (date.getDay()) {  //这种判断日期是本人懒，不想写代码了，大家慎用。
                $("[name=" + key + "]", $form).val(date.format("yyyy-MM-dd"));
                continue;
            }*/

            var tagobjs = $("[name=" + key + "]", $form);
            if ($(tagobjs[0]).attr("type") == "radio") {//如果是radio控件
                $.each(tagobjs, function (keyobj,value) {
                    if ($(value).val() == jsonObj[key]) {
                        value.checked = true;
                    }
                });
                continue;
            }

            // $("[name=" + key + "]", $form).textbox("setValue",jsonObj[key]);
            if(!$form.find("[name=" + key + "]")[0]){
                continue;
            }
            $form.find("[name=" + key + "]").textbox().textbox("setValue",jsonObj[key]);

            // form.find("[id=\"" + name + "\"]").textbox("setValue",val);
            if(readOnly){
                $("[name=" + key + "]", $form).attr("readonly",true);
            }

        } else { //其他的直接赋值
            // $("[name=" + key + "]", $form).val(jsonObj[key]);
            // $("[name=" + key + "]", $form).textbox("setValue",jsonObj[key]);
            $form.find("[name=" + key + "]").textbox().textbox("setValue",jsonObj[key]);
            if(readOnly){
                $("[name=" + key + "]", $form).attr("readonly",true);
            }
        }

    }
}

var setCkb = function (name, value) {
    //alert(name + " " + value);
    //$("[name=" + name + "][value=" + value + "]").attr("checked", "checked");  不知为何找不到具体标签;
    $("[name=" + name + "][val=" + value + "]").attr("checked", "checked");
}

var fillckb = function (name, json) {
    var jsonObj = json;
    if (typeof json === 'string') {
        jsonObj = $.parseJSON(json);
    }
    var str = jsonObj[name];
    if (typeof str === "string") {
        var array = str.split(",");
        $.each(array, function (key, value) {
            setCkb(name, value);
        });
    }
}

var jsonObjType = function (obj) {
    if (typeof obj === "object") {
        var teststr = JSON.stringify(obj);
        if (teststr[0] == '{' && teststr[teststr.length - 1] == '}') return "class";
        if (teststr[0] == '[' && teststr[teststr.length - 1] == ']') return "array";
    }
    return typeof obj;
}

$.extend($.fn.form.methods, {
    setValues: function (myself, data) {
        var form = $(myself);

        var opts = $.data(form[0], "form").options;

        var cols = "," + data.items + ",";
        for (var name in data.row) {
            if (cols.indexOf(name) >= 0) {
                var val = data.row[name];
                form.find("[id=\"" + name + "\"]").textbox("setValue",val);
            }
        }

        opts.onLoadSuccess.call(form, data);
        form.form("validate");
    }
});