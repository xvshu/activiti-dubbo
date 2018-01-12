$.extend(
	$.fn.form.methods, {
		getDataObj : function(jq) {
		// 获得form中的具体数值
		var dataObj = {};
		$(jq.selector).find('input').each(function(i) {
			var inputType = $(this).attr("type");
			if (inputType != "button") {
				if ($(this).attr("name")) {
					// 判断是否为空
					if (($(this).val() != "") && ($(this).val() != " ")) {
						dataObj[$(this).attr("name")] = $(this).val();
					}
				}
			}
		});
		$(jq.selector).find('textarea').each(function(i) {
			if ($(this).attr("name")) {
				if (($(this).val() != "") && ($(this).val() != " ")) {
					dataObj[$(this).attr("name")] = $(this).val();
				}
			}
		});
		return dataObj;
	},
	// form只读和隐藏边框
	readOnlyAndNoborder : function(jq) {
		$(jq.selector).find('input').each(function(i) {
			var inputType = $(this).attr("type");
			if (inputType != "button") {
				if ($(this).attr("name")) {
					$(this).css("border", "0px");
				}
				$(this).attr("readonly", "true");
			}
		});
		$(jq.selector).find('textarea').each(function(i) {
			$(this).css("border", "0px");// 去掉边框
			$(this).attr("readonly", "true");// 只读属性
		});
		$(jq.selector).find('.easyui-textbox').each(function(i) {
			$(this).combobox('disable');
		});
		$(jq.selector).find('.easyui-numberbox').each(function(i) {
			$(this).combobox('disable');
		});
		$(jq.selector).find('.easyui-combobox').each(function(i) {
			$(this).combobox('disable');
		});
		$(jq.selector).find('.easyui-datebox').each(function(i) {
			$(this).datebox('disable');
		});
		$(jq.selector).find('.easyui-combotree').each(function(i) {
			$(this).combotree('disable');
		});
		$(jq.selector).find('input[type="radio"]').each(function(i) {
			$(this).attr("disabled", "true");// 只读属性
		});
		//隐藏上传按钮
		$(jq.selector).find('.icon-upload_loan').parents('a').hide()
		//$('#LoanCheckExecuteInfoViewModel #LoanCheckExecuteInfoViewModel-fileButton').hide()
	},
	// 显示边框和非readOnly;
	noReadOnlyAndborder : function(jq) {		
		$(jq.selector).find('input').each(function(i) {			
			var inputType = $(this).attr("type");
			if (inputType != "button") {				
				if ($(this).attr("name")) {					
					$(this).css("border", "#A4BED4 solid 1px");
				}
				$(this).removeAttr("readonly");
			}
		});
	},
	// 清空form数据的事件
	clearFormData : function(jq) {
		$(jq.selector).find('input').each(function(i) {
			var inputType = $(this).attr("type");
			if (inputType != "button") {
				$(this).val('');
				if (inputType == "file") {
					$(this).after($(this).clone().val(""));
					$(this).remove();
				}
			}
		});
		$(jq.selector).find('textarea').each(function(i) {
			$(this).val('');
		});
	},
	//获取FORM所有值
	getDataJson : function(jq) {
		// 获得form中的具体数值
		var dataObj = {};
		$(jq.selector).find('input').each(function(i) {			
			var inputType = $(this).attr("type");			
			if(inputType!='button'&&inputType!='checkbox'&&inputType!='radio'){
				if ($(this).attr("name")) {
						dataObj[$(this).attr("name")] = $(this).val();
				}
			}
		});
		$(jq.selector).find('textarea').each(function(i) {
			if ($(this).attr("name")) {
					dataObj[$(this).attr("name")] = $(this).val();
			}
		});
		
		$(jq.selector).find('input[type="radio"]:checked').each(function(i) {
			var name = $(this).attr("name");
			if (name) {
				dataObj[$(this).attr("name")] =$(this).val();				 
				dataObj[$(this).attr("name")+"Name"] =$(this).next("span").html();
			}
		});
		$(jq.selector).find('input:checkbox:checked').each(function(i) {
			var name = $(this).attr("name");
			if (name) {
				dataObj[$(this).attr("name")] =$(this).val();
				dataObj[$(this).attr("name")+"Name"] =$(this).html();
			}
		});
		$(jq.selector).find('select').each(function(i) {					 
			if ($(this).attr("id")) {				
					if($(this).combobox("getValue")!=''){
						dataObj[$(this).attr("id")] = $(this).combobox("getValue");
					    dataObj[$(this).attr("id")+"Name"] =$(this).combobox("getText");
					}else{
						dataObj[$(this).attr("id")] = "";
					    dataObj[$(this).attr("id")+"Name"] ="";
					}
			}
		});
		
		return dataObj;
	},
	
	disableValidation : function (jq){
		
		$(jq.selector).find('.validatebox-text').each(function(i) {	
			var _26=$.data(this,"validatebox").options;
			_26.required=false;
			$(this).removeClass("validatebox-invalid");
			$(this).removeClass("easyui-validatebox");
			$(this).removeClass("validatebox-text");
			
		});
		
	}
});

//@ sourceURL=test