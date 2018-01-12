$.extend({
	// 文件下载
	download : function(url, data, method) {
		if (url && data) {
			// 把参数组装成 form的 input
			var inputs = '';
			$.each(data, function(name, value) {
				inputs += '<input type="hidden" name="' + name + '" value="' + value + '" />';
			});
			// request发送请求
			$('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>').appendTo('body').submit().remove();
		}
	},
	/**
	 * 预览文件
	 */
	previewFile: function (objectType,businessId,fileType){
		$("<div id='previewFileDialog'></div>").dialog({
			title:"预览",
			width:800,
			height:"auto",
			top : 60,
			closed:false,
			cache : false,
			modal : true,
			shadow:false,
			content:"<iframe src='file/list/"+objectType+"/"+businessId+"/"+fileType+"' width='780' height='600' />",
			onLoad:function(){},
			onClose:function() {
				$.closeDialog("#previewFileDialog");
			}
			
		});
	},
	/**
	 * 上传文件
	 */
	uploader : function(objectType,businessId,fileType,callBack) {
		$("<div id='uploaderFileDialog'></div>").dialog({
			title:"上传",
			width:800,
			height:"auto",
			top : 60,
			closed:false,
			cache : false,
			modal : true,
			shadow:false,
			content:"<iframe src='file/batch/"+objectType+"/"+businessId+"/"+fileType+"' width='780' height='360' />",
			onLoad:function(){},
			onClose:function() {
				if (typeof callBack == "function") {
	                callBack();
				}
				$.closeDialog("#uploaderFileDialog");
			}
			
		});
	},
	/**
	 * 删除附件
	 */
	deleteFile : function(objectType,businessId,fileType,callBack){
		$.messager.confirm("删除","确定删除此附件?",function(r){
			if(r){
				$.messager.progress();
				$.ajax({
					   type: "PUT",
					   url: "file/del/"+objectType+"/"+businessId+"/"+fileType,
					   success: function(msg){
						   $.messager.progress("close");
							if(typeof callBack == 'function'){
								callBack();
							}
					   }
					});
			}
		});
	}
});