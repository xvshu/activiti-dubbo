$.extend(
	$.fn.validatebox.defaults.rules,{
		// 验证汉字
		chinese : {
			validator : function(value, param) {
				if (/[^\u4E00-\u9FA5]/g.test(value) || value.length < param[0]) {
					return false;
				} else {
					return true;
				}
			},
			message : '只能输入汉字'
		},
		// 验证电话(区号)
		telephone : {
			validator : function(value, param) {
				var pattern = /^[0-9]{7,8}$/;
				return pattern.test(value);
			},
			message : '电话号码不合法'
		},
		// 验证电话
		phone : {
			validator : function(value, param) {
				var pattern = /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,8}$)|(^[0-9]{3,4}\-[0-9]{7,8}\-(\d{4}|\d{3}|\d{2}|\d{1})$)|(^1[0-9]{10}$)/;
				return pattern.test(value);
			},
			message : '电话号码不合法'
		},
		// 验证手机
		mobile : {
			validator : function(value, param) {
				var pattern = /^1[3|4|5|8][0-9]\d{8,8}$/;
				return pattern.test(value);
			},
			message : '手机号码不合法'
		},
		// 验证区号
		distactCode : {
			validator : function(value, param) {
				var pattern = /^[0-9]{3,4}$/;
				return pattern.test(value);
			},
			message : '区号不合法'
		},
		// 验证长度(含中文)
		length : {
			validator : function(value, param) {
				var sum = 0;    
				for (var i = 0; i < value.length; i++) {    
					if ((value.charCodeAt(i) >= 0) && (value.charCodeAt(i) <= 255)) {
						sum += 1;  
					} else if (/[^\u4E00-\u9FA5]/g.test(value.charCodeAt(i))){
						sum += 3;
					} else {
						sum += 2;
					}
				}    
				return sum <= param[0];
			},
			message : '数据过长'
		},
		// 验证长度
		length_common : {
			validator : function(value, param) {
				if (value.length > param[0]) {
					return false;
				} else {
					return true;
				}
			},
			message : '数据过长'
		},
		// 验证长度(非空)
		length_no_blank : {
			validator : function(value, param) {
				var ret = /^\s/;
				if (ret.test(value)) {
					return false;
				} else {
					if (value.length > param[0]) {
						return false;
					} else {
						return true;
					}
				}
			},
			message : '输入值不能为空或数据过长'
		},
		// 验证长度(非空+中文)
		cn_three_length_no_blank : {
			validator : function(value, param) {
				var ret = /^\s/;
				if (ret.test(value)) {
					return false;
				} else {
					var cArr = value.match(/[^\x00-\xff]/ig);
					if (value.length + (cArr == null ? 0 : 2 * cArr.length) > param[0]) {
						return false;
					} else {
						return true;
					}
				}
			},
			message : '输入值不能为空或数据过长'
		},
		// 验证数字
		number : {
			validator : function(value, param) {
				var pattern = /^((-|\+)*\d+$)|(^[-\+]?\d+(\.\d+)?$)/;
				if (param != null && param != '' && param[0] != '' && param[0] != null && param[0] != undefined) {
					if (pattern.test(value) && value.length <= param[0]) {
						return true;
					} else {
						return false;
					}
				} else {
					return pattern.test(value);
				}
			},
			message : "数据不合法"
		},
		// 验证数字和逗号（多个数字之间用逗号隔开）
		numberAndComma  : {
			validator : function(value, param) {
				var pattern = /^[\d,]+$/;
				if (param != null && param != '' && param[0] != '' && param[0] != null && param[0] != undefined) {
					if (pattern.test(value) && value.length <= param[0]) {
						return true;
					} else {
						return false;
					}
				} else {
					return pattern.test(value);
				}
			},
			message : "数据不合法"
		},
		// 验证身份证
		idcard_format : {
			validator : function(value) {
				if (isIdCardNo(value)) {
					return true;
				} else {
					return false;
				}
			},
			message : '身份证号码不正确'
		},
		// 验证字符最大长度
		minLength : {
			validator : function(value, param) {
				return $.trim(value).length >= param[0];
			},
			message : '字符长度不足.'
		},
		// 验证货币
		currency : {
			validator : function(value) {
				return /^\d+(\.\d+)?$/i.test(value);
			},
			message : '货币格式不正确'
		},
		// 验证QQ,从10000开始
		qq : {
			validator : function(value) {
				return /^[1-9]\d{4,9}$/i.test(value);
			},
			message : 'QQ号码格式不正确'
		},
		// 验证整数区间
		integer_interval : {
			validator : function(value,param) {
				var reg = /^[+]?[0-9]+\d*$/;
				if (reg.test(value)) {
					var min = param[0];
					var max = param[1];
					if (value >= min && value <= max) {
						var len = value.length;
						var firstStr = value.substring(0,1);
						if (len>=2 && firstStr == '0') {
							return false;
						} else {
							return true;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			},
			message : '请输入{0}到{1}的整数'
		},
		// 验证浮点数 区间
		float_interval : {
			validator : function(value,param) {
				var reg = /^(\d*\.)?\d+$/;
				if (reg.test(value)) {
					var min = param[0];
					var max = param[1];
					if (value >= min && value <= max) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			},
			message : '请输入{0}到{1}的数'
		},
		// 验证年龄
		age : {
			validator : function(value) {
				return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
			},
			message : '年龄必须是0到120之间的整数'
		},
		// 验证英文
		english : {
			validator : function(value, param) {
				var ret = /^\s/;
				if (ret.test(value)) {
					return false;
				} else {
					if (value.length > param[0]) {
						return false;
					} else {
						if (/[\u4E00-\u9FA5]/g.test(value)) {
							return false;
						} else {
							return true;
						}
					}
				}
			},
			message : '请输入英文或数据过长'
		},
		// 验证是否包含空格和非法字符
		unnormal : {
			validator : function(value) {
				return /.+/i.test(value);
			},
			message : '输入值不能为空和包含其他非法字符'
		},
		// 验证用户名
		username : {
			validator : function(value) {
				return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
			},
			message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
		},
		// 验证传真
		fax : {
			validator : function(value) {
				return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
			},
			message : '传真号码不正确'
		},
		// 验证邮政编码
		zip : {
			validator : function(value) {
				return /\d{6}$/i.test(value); 
			},
			message : '邮政编码格式不正确'
		},
		// 根据设置位数验证邮政编码
		zip_code : {
			validator : function(value, param) {
				var pattern = /^\d+$/;
				if (param != null && param != '' && param[0] != '' && param[0] != null && param[0] != undefined) {
					if (pattern.test(value) && value.length == param[0]) {
						return true;
					} else {
						return false;
					}
				} else {
					return pattern.test(value);
				}
			},
			message : "邮政编码格式不正确"
		},
		// 验证IP地址
		ip : {
			validator : function(value) {
				return /d+.d+.d+.d+/i.test(value);
			},
			message : 'IP地址格式不正确'
		},
		// 验证姓名，可以是中文或英文
		name : {
			validator : function(value) {
				return /^[\Α-\￥]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
			},
			message : '请输入合法姓名'
		},
		// 验证判空
		no_blank: {
			validator : function(value, param) {
				if (/^\s+$/gi.test(value)) {
					return false;
				}
				return true;
			},
			message : '输入值不能为空'
		},
		// 验证两次输入值是否相同
		equalTo: { 
	        validator: function (value, param) { 
	            return value == $(param[0]).val(); 
	        }, 
	        message: '两次输入的字符不一至' 
	    },
	    username_length:{
	    	 validator : function(value,param) { 
	    		    if(value.length>=param[0]&&value.length<=param[1]){
	    		    	return /^[a-zA-Z][a-zA-Z0-9_]*$/i.test(value); 
	    		    }else {
	    		    	return false;
	    		    }
		            
		        }, 
		        message : '用户名不合法（字母开头，允许1-50字节，允许字母数字下划线）' 
	    },
	    englishWithLength : {// 验证英语 
	        validator : function(value,param) { 
	        	if(value.length <= param[0]){
	            return /^[A-Za-z]+$/i.test(value); 
	        	}else {
	        		return false;
	        	}
	        }, 
	        message : '请输入英文' 
	    },
	    chineseAndEnglish : {// 验证姓名，可以是中文或英文 
           validator : function(value) { 
               return /^[a-z\u4E00-\u9FA5]+$/gi.test(value);
           }, 
           message : '请输入中文或字母' 
        },
         /*验证结束时间 
		  * 用法：data-options="validType:'dateScopEnd[\'endTime\']'"        endTime日历的id
		  */
	    	dateScopStart:{
				validator : function(value,param) {
					var comName=param[0];
					var beginValue=$('#'+comName).datebox('getValue');
					if(param.length>1){
						beginValue=$(' #'+comName).datebox('getValue');
					}
					if(beginValue&&value<beginValue){
						return false;
					}
					return true;
				},
				message : '时间区间不合法，结束日期不能早于开始日期'
			},
			// 验证开始时间 
			dateScopEnd:{
					validator : function(value,param) {
						var comName=param[0];
						var beginValue=$('#'+comName).datebox('getValue');
						if(param.length>1){
							beginValue=$(' #'+comName).datebox('getValue');
						}
						if(beginValue&&value>beginValue){
							return false;
						}
						return true;
					},
					message : '时间区间不合法，开始日期不能晚于结束日期'
				}
});
/**
 * 验证身份证格式上的正确性
 */
function isIdCardNo(num) {
	var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);
	var parityBit=new Array("1","0","X","9","8","7","6","5","4","3","2");
	var varArray = new Array();
	var lngProduct = 0;
	var intStrLen = num.length;
	var idNumber = num;
    if ((intStrLen != 15) && (intStrLen != 18)) {
    	return false;
    }	
    for (var i = 0; i < intStrLen; i++) {
    	varArray[i] = idNumber.charAt(i);
    	if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
    		return false;
    	} else if (i < 17) {
    		varArray[i] = varArray[i] * factorArr[i];
		}
    }
 	if (intStrLen == 18) {
	    if (isDate8(idNumber.substring(6, 14)) == false) {
	    	return false;
		}
	    for (var i = 0; i < 17; i++) {
	    	lngProduct = lngProduct + varArray[i];
    	}
     	if (varArray[17] != parityBit[lngProduct % 11]) {
     		return false;
     	}
 	} else {        
 		if (isDate6(idNumber.substring(6,12)) == false) {
 			return false;
 		}
 	}
 	return true;
}
/**
 * 判断是否为“YYYYMM”式的时期
 */
function isDate6(sDate) {
   if (!/^[0-9]{6}$/.test(sDate)) return false;
   var year = sDate.substring(0, 4);
   var month = sDate.substring(4, 6);
   if (year < 1700 || year > 2500) return false;
   if (month < 1 || month > 12) return false;
   return true;
}
/**
 * 判断是否为“YYYYMMDD”式的时期
 */
function isDate8(sDate) {
   if (!/^[0-9]{8}$/.test(sDate)) return false;
   var year = sDate.substring(0, 4);
   var month = sDate.substring(4, 6);
   var day = sDate.substring(6, 8);
   var iaMonthDays = [31,28,31,30,31,30,31,31,30,31,30,31];
   if (year < 1700 || year > 2500) return false;
   if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1]=29;
   if (month < 1 || month > 12) return false;
   if (day < 1 || day > iaMonthDays[month - 1]) return false;
   return true;
}