var prefix = "/operateRecord";
$(function () {
    load();
});

function load() {
    // selectLoad();
    $('#operateRecordTable').bootstrapTable('destroy');
    $('#operateRecordTable').bootstrapTable({
        method: "get",
        url: prefix + "/operateRecordList", //服务器数据加载地址
        iconSize: 'outline',
        toolbar: '#exampleToolbar',
        striped: true, //设置为true 会有隔行变色效果
        cache: false, //是否使用缓存，默认为true
        dataType: "json", //服务器返回的数据类型
        pagination: true, //设置为true会在底部显示分页条
        singleSelect: false, //设置为true会禁止多选
        pageSize: 10, //如果设置了分页，每页数据条数
        pageNumber: 1, //如果设置了分页，首页页码
        pageList: [10, 25, 50, 100], //可供选择的每页行数
        showColumns: false, //是否显示内容下拉框
        sidePagination: "server", //设置在哪里进行分页 可选择 服务器端或者客户端
        queryParams: function (params) {
            //传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc，以及所有列的键值对
            //这里键的名字和控制器的变量名必须一致，这边改动，控制器也要改成一样的
            return {
                limit: params.limit, //页面大小
                offset: params.offset //页码
            };
        },
        ////请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
        // 				// queryParamsType = 'limit' ,返回参数必须包含
        // 				// limit, offset, search, sort, order 否则, 需要包含:
        // 				// pageSize, pageNumber, searchText, sortName,
        // 				// sortOrder.
        // 				// 返回false将会终止请求
        columns: [
            {
                checkbox: true,
                title:"全选"
            },
            {
                field: 'id',
                title: '编号'
            },
            {
                field: 'operateTime',
                title: '操作时间'
            },
            {
                field: 'operator',
                title: '操作人'
            },
            {
                field: 'operateTarget',
                title: '操作对象'
            },
            {
                field: 'description',
                title: '情况说明'
            }
        ]
    });
}

//刷新
function reLoad() {
    $('#operateRecordTable').bootstrapTable('refresh');
}
//批量删除
function batchRemove() {
    var rows = $('#operateRecordTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {
    });
}