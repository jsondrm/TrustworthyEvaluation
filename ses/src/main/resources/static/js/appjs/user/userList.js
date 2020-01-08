var prefix = "/user";
$(function () {
    load();
});

function load() {
    // selectLoad();
    $('#exampleTable').bootstrapTable('destroy');
    $('#exampleTable').bootstrapTable({
        method: "get",
        url: prefix + "/userList", //服务器数据加载地址
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
            return {
                limit: params.limit, //页面大小
                offset: params.offset //页码
            };
        },
        columns: [
            {
                checkbox: true
            },
            {
                field: 'userId',
                title: '工号'
                // visible: false
            },
            {
                field: 'username',
                title: '姓名'
            },
            {
                field: 'dptName',
                title: '部门'
            },
            {
                field: 'group',
                title: '分组'
            },
            {
                field: 'email',
                title: '邮箱'
            },
            {
                field: 'mobile',
                title: '手机号'
            },
            {
                field : 'status',
                title : '状态',
                align : 'center',
                formatter : function(value, row, index) {
                    if (value == '0') {
                        return '<span class="label label-danger">离职</span>';
                    } else if (value == '1') {
                        return '<span class="label label-primary">正常</span>';
                    }
                }
            },
            {
                title: '操作',
                field: 'userId',
                align: 'center',
                formatter: function (value, row, index) {
                    var e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                        + row.userId
                        + '\')"><i class="fa fa-edit"></i></a>';
                    var d = '<a class="btn btn-warning btn-sm" href="#" title="删除" mce_href="#" onclick="remove(\''
                        + row.userId
                        + '\')"><i class="fa fa-remove"></i></a>';
                    // var e = '<a  class="btn btn-primary btn-sm ' +  '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                    //     + row.userId
                    //     + '\')"><i class="fa fa-edit "></i></a> ';
                    // var d = '<a class="btn btn-warning btn-sm ' +  '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                    //     + row.userId
                    //     + '\')"><i class="fa fa-remove"></i></a> ';
                    // var f = '<a class="btn btn-success btn-sm ' +  '" href="#" title="重置密码"  mce_href="#" onclick="resetPwd(\''
                    //     + row.userId
                    //     + '\')"><i class="fa fa-key"></i></a> ';
                    return e + d;
                }
            }
        ]
    });
}

//查询
function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}

//添加
function add() {
    layer.open({
        type: 2,
        title: '增加用户',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '600px'],
        content: prefix + '/add' // iframe的url
    });
}

//编辑
function edit(id) {
    layer.open({
        type: 2,
        title: '编辑用户',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '500px'],
        content: prefix + '/edit/' + id // iframe的url
    });
}

//删除
function remove(id) {
    layer.confirm('确定删除用户？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'id': id
            },
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function resetPwd(id) {
    layer.open({
        type : 2,
        title : '重置密码',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '400px', '260px' ],
        content : prefix + '/resetPwd/' + id // iframe的url
    });
}
//批量删除
function batchRemove() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
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
            ids[i] = row['userId'];
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
