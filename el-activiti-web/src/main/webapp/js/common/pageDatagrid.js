function PageDatagrid(url,title,tableId,param) {

    this.tableId=!tableId?"showDataTable":tableId;
    this.url=url;
    this.param=param;
    this.title=title;
    this.$table=$('#'+this.tableId);
    /**
     * 初始化表格
     */
    this.initTable=function(rowNames,quaryParam){
        var quaryParams={rows:10};
        $.extend(quaryParams,quaryParam);
        var initTableParam={
            loadMsg: "数据加载中...",
            title:this.title,
            url:this.url,
            queryParams:quaryParams,
            loadFilter: function(data){
                var total = data.totalCount;
                var data_list = data.items;
                var data_return = {};
                data_return.total = total;
                data_return.rows = data_list;
                return data_return;
            },
            onBeforeLoad:function(param){//添加分页自定义参数
                //console.log(param);
                param.limit=param.rows;
            },
            columns:[rowNames]
        };

        $.extend(initTableParam,param)

        this.$table.datagrid(initTableParam);
    };



    /**
     * 初始化方法,渲染列表
     */
    this.init=function(rowNames,quaryParam){

        //初始化列表
        this.initTable(rowNames,quaryParam);



        var pager = this.$table.datagrid('getPager');
        $(pager).pagination({
            pageSize: 10,//每页显示的记录条数，默认为10
            pageList: [10,20],//可以设置每页记录条数的列表
            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
        });

    };

    /**
     * 重新加载
     * @param param
     */
    this.reload=function(param){
        this.$table.datagrid("reload",param);
    };

    /**
     * 设置表格是否单选
     */
    this.singleSelect=function (singleSelect) {
        this.$table.datagrid({singleSelect:!singleSelect});
    };

    /**
     * 设定加载url
     * @param url
     */
    this.setUrl=function (url) {
      this.url=url;
        this.$table.datagrid("options").url=url;
    };

    /**
     * 加载列表
     * @param param
     */
    this.reload=function (param) {
        this.$table.datagrid("load",param);
    }

    /**
     * 追加查询参数
     * @param parma
     */
    this.addQueryParam=function (param) {
        var gridOpts = this.$table. datagrid('options').queryParams;
        $.extend(gridOpts,param);
    }


    
}