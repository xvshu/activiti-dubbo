/**
 * 用于渲染数据到页面上的工具。
 * 注意：
 *      1、此工具中的数学运算没有进行精度处理，此处方法只能用于页面数据展现，禁止用于存储数据库和业务逻辑计算、判断。
 *
 * */
$.extend({
    //将输入的数字转换成XX万
    toWan : function(d1){
        return (d1/10000).toFixed(4)+'万';
    },
    //将boolean类型转换为中文汉字
    booleanToHanzi:function(boolean1){
      if(boolean1==false||boolean1=='false'){
          return '否';
      }else if(boolean1==true||boolean1=='true'){
          return '是';
      }else{
          return '未知';
      }
    },
    /**
     * 绑定checkbox和input
     * */
    checkboxInput:function(isChecked,inputIds){
        if(isChecked){
            console.log(isChecked);
            console.log(inputIds);
            //如果选中,将inputids设置为可编辑，且进行数据校验
            $(inputIds).validatebox({required:true,disabled:false});
        }else{
            console.log(isChecked);
            console.log(inputIds);
            //如果未选中，则将inputids清空、设为不可编辑、取消数据校验
            $(inputIds).num.val("");
            $(inputIds).validatebox({required:false,disabled:true});
        }
    },
    /**
     * 序列表表单
     * */
    serializeArray:function($form){
        var dataNew=$form.serializeArray();
        console.log(dataNew);
        var rowToAdd = {};
        $.each(dataNew, function() {
            if (rowToAdd[this.name]) {
                if (!rowToAdd[this.name].push) {
                    rowToAdd[this.name] = [rowToAdd[this.name]];
                }
                rowToAdd[this.name].push(this.value || '');
            } else {
                rowToAdd[this.name] = this.value || '';
            }
        });
        return rowToAdd;
    },
    /**
     * 获取checkbox的选中状态，如果选中，返回true，如果未选中则返回false
     * */
    isCheck:function(checkboxId){
        return $('#'+checkboxId).is(':checked')
    },
    /**
     * 校验用户录入的发布时间：
     * 规则：
     *  1、不能有数字之外的其他字符
     *  2、每行前两位范围为：0~24，后两位范围为0~60
     * */
    checkPublishRule:function(singleRule){
        if(singleRule==null||singleRule==''||singleRule.length!=4){
            return "发布规则中不能有空白行，或每行长度不等于4个数字";
        }else {
            var f2=singleRule.substring(0,2);
            var f4=singleRule.substring(2,4);
            if(parseInt(f2)>24||parseInt(f2)<0){
                return "发布规则中小时不规范";
            }
            if(parseInt(f4)>60||parseInt(f4)<0){
                return "发布规则中分钟不规范";
            }
            return null;
        }
    },
    /**
     * 根据给定的数量，生成颜色代码表（随机）
     * */
    genColorsRandom:function(size){
        var colors=[];
        for(var i=0;i<size;i++){
            colors.push('#'+('00000'+(Math.random()*0x1000000<<0).toString(16)).slice(-6));
        }
        return colors;

    },
    /**
     * 根据给定的数量，生成颜色代码表（预置颜色代码）
     * */
    genColors:function(size){
        var colors=[
            '#FFEBCD'
            ,'#8A2BE2'
            ,'#A52A2A'
            ,'#DEB887'
            ,'#7FFF00'
            ,'#D2691E'
            ,'#FF7F50'
            ,'#6495ED'
            ,'#DC143C'
            ,'#00FFFF'
            ,'#008B8B'
            ,'#B8860B'
            ,'#A9A9A9'
            ,'#006400'
            ,'#BDB76B'
            ,'#8B008B'
            ,'#556B2F'
            ,'#FF8C00'
            ,'#9932CC'
            ,'#8B0000'
            ,'#E9967A'
            ,'#8FBC8F'
            ,'#483D8B'
            ,'#2F4F4F'
            ,'#00CED1'
            ,'#9400D3'
            ,'#FF1493'
            ,'#00BFFF'
            ,'#696969'
            ,'#1E90FF'
            ,'#FFEBCD'
            ,'#0000FF'
            ,'#8A2BE2'
            ,'#A52A2A'
            ,'#DEB887'
            ,'#5F9EA0'
            ,'#7FFF00'
            ,'#D2691E'
            ,'#FF7F50'
            ,'#6495ED'
            ,'#FFF8DC'
            ,'#DC143C'
            ,'#00FFFF'
            ,'#00008B'
            ,'#008B8B'
            ,'#B8860B'
            ,'#A9A9A9'
            ,'#006400'
            ,'#BDB76B'
            ,'#8B008B'
            ,'#556B2F'
            ,'#FF8C00'
            ,'#9932CC'
            ,'#8B0000'
            ,'#E9967A'
            ,'#8FBC8F'
            ,'#483D8B'
            ,'#2F4F4F'
            ,'#00CED1'
            ,'#9400D3'
            ,'#FF1493'
            ,'#00BFFF'
            ,'#696969'
            ,'#1E90FF'
        ];
        if(size>colors.length){
            var cha=size-colors.length;
            for(var i=0;i<cha;i++){
                colors.push('#'+('00000'+(Math.random()*0x1000000<<0).toString(16)).slice(-6));
            }
        }
        return colors.slice(0,size);
    },
    /**
     * 日期格式化
     * */
    dateFormat:function(date,fmt){
        var o = {
            "M+": date.getMonth() + 1, //月份
            "d+": date.getDate(), //日
            "h+": date.getHours(), //小时
            "m+": date.getMinutes(), //分
            "s+": date.getSeconds(), //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    },

    dateParse:function(dateStr){
        var endLogTimeDate = new Date(Date.parse(dateStr.replace(/-/g, "/")));
        console.log(endLogTimeDate);
        return endLogTimeDate;
    },
    /**
     * 判断给定对象是否为null或undefined
     * */
    notNllOrUndefined:function(obj){
        if(obj==undefined||obj==null||obj==''){
            return false;
        }else{
            return true;
        }
    }
});