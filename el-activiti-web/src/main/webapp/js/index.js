
$(function() {
	   
	   $('.index--navi-tab').click(function() {
			var $this = $(this);
			var url = $this.attr('src');
			var jsref = $this.attr('jsref');
			var title = $this.children("span").children("ttitle").text();
		
			if ($('#tabs').tabs('exists', title)){
				$('#tabs').tabs('select', title);//选中并刷新
				var currTab = $('#tabs').tabs('getSelected');
				var url = $(currTab.panel('options').content).attr('src');
				if(url != undefined && currTab.panel('options').title != '首页') {
					$('#tabs').tabs('update',{
						tab:currTab,
						href:url
					});
				}
			} else {
				var content = '<iframe scrolling="yes" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
                $('#tabs').tabs('add',{
                    title:title,
                    content:content,
                    cache:false,
                    onLoad:function(panel){
                        $.includeFile('head',jsref+"?random="+$.getIndexTime());
                    },
                    closable:true
                });
			}
	   });
	 //添加权限刷新按钮
		$('#refreshMViewButton').click(function(){
			$('<div id="dealingDiv"></div>').dialog({
				title : '',
				width : 415,
				height : 'auto',
				top:	140,
				draggable:false,
				closed : false,
				cache : false,
				href : $.getContextPath()+ '/page/html/refresh.html',
				modal : true,
				onLoad : function() {
					$.includeFile("#dealingDiv",[ '/js/refresh.js']);
				},
				onClose:function() {
					$.closeDialog('#dealingDiv');
				}
			});
		});

		$('#logoutSystem').click(function(){
			$.messager.confirm("提示","确定删除此条记录吗?",function(flg) {
				if (flg) {

				}
			});
		});

	$("#index_statis_table").datagrid({
	});
	$("#index_statis_table_1").datagrid({
	});

});
//@ sourceURL=source.dc111

