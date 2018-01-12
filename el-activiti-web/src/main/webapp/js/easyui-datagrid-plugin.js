/**
 * Created by xvshu on 2017/11/28.
 */
/**
 * 扩展方法 使datagrid的列中能显示row中的对象里的属性
 * 无需调用自动执行 Field：Staff.JoinDate
 **/
$.fn.datagrid.defaults.view = $.extend({}, $.fn.datagrid.defaults.view, {
    renderRow: function (target, fields, frozen, rowIndex, rowData) {
        var opts = $.data(target, 'datagrid').options;
        var cc = [];
        if (frozen && opts.rownumbers) {
            var rownumber = rowIndex + 1;
            if (opts.pagination) {
                rownumber += (opts.pageNumber - 1) * opts.pageSize;
            }
            cc.push('<td class="datagrid-td-rownumber"><div class="datagrid-cell-rownumber">' + rownumber + '</div></td>');
        }
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            var col = $(target).datagrid('getColumnOption', field);
            var fieldSp = field.split(".");
            var dta = rowData[fieldSp[0]];
            for (var j = 1; j < fieldSp.length; j++) {
                dta = dta[fieldSp[j]];
            }
            if (col) {
                // get the cell style attribute
                var styleValue = col.styler ? (col.styler(dta, rowData, rowIndex) || '') : '';
                var style = col.hidden ? 'style="display:none;' + styleValue + '"' : (styleValue ? 'style="' + styleValue + '"' : '');

                cc.push('<td field="' + field + '" ' + style + '>');

                var style = 'width:' + (col.boxWidth) + 'px;';
                style += 'text-align:' + (col.align || 'left') + ';';
                style += opts.nowrap == false ? 'white-space:normal;' : '';

                cc.push('<div style="' + style + '" ');
                if (col.checkbox) {
                    cc.push('class="datagrid-cell-check ');
                } else {
                    cc.push('class="datagrid-cell ');
                }
                cc.push('">');

                if (col.checkbox) {
                    cc.push('<input type="checkbox"/>');
                } else if (col.formatter) {
                    cc.push(col.formatter(dta, rowData, rowIndex));
                } else {
                    cc.push(dta);
                }

                cc.push('</div>');
                cc.push('</td>');
            }
        }
        return cc.join('');
    }
});
