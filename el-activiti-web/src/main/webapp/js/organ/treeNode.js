/**
 * 加载树形结构数据
 */
function loadTreeNodeData(url){

    $('#easyui_tree').tree({
        url:url,
        loadFilter: function(data){
            if (data.data){
                return myLoadFilter_tree(data.data,'closed');
            }else{
                return myLoadFilter_tree(data,'closed');
            }
        },
        // 展示每一级拥有的节点数量
        formatter:function(node){
            return format_node(node);
        },
        //右键节点操作
        animate: true,
        onContextMenu: function(e,node){
            e.preventDefault();
            $(this).tree('select',node.target);
            $('#menu').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        }
    });
}

/**
 * 获取树形结构数据
 */
function getTreeNodeData(url){
    var treeNodeData = '';
    $.ajax({
        url:url,
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                if (data.data){
                    treeNodeData = myLoadFilter_tree(data.data);
                }else{
                    treeNodeData = myLoadFilter_tree(data);
                }
            }
        }
    });
    return treeNodeData;
}

/**
 * 解析树形结构数据
 * @param data
 * @returns {Array}
 */
function myLoadFilter_tree(data,treeNodeState){

    function disTreeNode(node){
        if (node==null){
            return null;
        }

        var node_msg = {id:node.id,text:node.name,attributes:{code:node.code}};

        if (node.childNodes == null || node.childNodes == ''){
            return node_msg;
        }
        var childNodes = node.childNodes;
        for(var i=0; i<childNodes.length; i++){
            var childNode = childNodes[i];
            var childNode_ex = disTreeNode(childNode);
            if (childNode_ex != null){
                if (node_msg.children){
                    node_msg.children.push(childNode_ex);
                } else {
                    node_msg.children = [childNode_ex];
                }
            }
        }
        if (treeNodeState){
            node_msg.state = treeNodeState;
        }
        return node_msg;
    }

    var nodes = [];
    if (data){
        // get the root nodes
        for(var i=0; i<data.length; i++){
            var node = data[i];
            var nodeMsg = disTreeNode(node);
            nodes.push(nodeMsg);
        }
    }
    return nodes;
}

/**
 * 收起全部节点
 */
function collapseAll(){
    $('#easyui_tree').tree('collapseAll');
}
/**
 * 展开全部节点
 */
function expandAll(){
    $('#easyui_tree').tree('expandAll');
}
function expandTo(){
    var node = $('#easyui_tree').tree('find',113);
    $('#easyui_tree').tree('expandTo', node.target).tree('select', node.target);
}
/**
 * 获取选中的节点信息
 */
function getSelectedTreeNode(){
    return $('#easyui_tree').tree('getSelected');
}
/**
 * 格式化节点信息
 * @param node
 * @returns {*}
 */
function format_node(node){
    var s = node.text;
    if (node.children){
        s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
    }
    return s;
}

/**
 * 获取树
 */
function getTree(){
    return $('#easyui_tree');
}

/**
 * 追加节点
 * @param node
 */
function treeNodeAppend(node,data_append){
    $('#easyui_tree').tree('append', {
        parent: (node?node.target:null),
        data: data_append
    });
}

/**
 * 更改节点
 */
function treeNodeUpdate(node,text_update){
    $('#easyui_tree').tree("update",{
        target : node.target,
        text : text_update
    });
}

/**
 * 移除节点
 */
function treeNodeRemove(node){
    $('#easyui_tree').tree('remove', node.target);
}