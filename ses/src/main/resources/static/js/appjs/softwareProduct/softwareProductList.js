var prefix = "/softwareProduct";
$(function () {
    load();
});

function load() {
    // selectLoad();
    $('#exampleTable').bootstrapTable('destroy');
    $('#exampleTable').bootstrapTable({
        method: "get",
        url: prefix + "/softwareProductList", //服务器数据加载地址
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
                checkbox: true
            },
            {
                field: 'number',
                title: '编号'
            },
            {
                field: 'softwareName',
                title: '名称'
            },
            {
                field : 'status',
                title : '状态',
                align : 'center',
                formatter : function(value, row, index) {
                    if (value == '0') {
                        return '<span class="label label-danger">待评测</span>';
                    } else if (value == '1') {
                        return '<span class="label label-primary">评测中</span>';
                    }else if (value == '2') {
                        return '<span class="label label-primary">评测结束</span>';
                    }
                }
            },
            {
                field: 'keXinDu',
                title: '软件可信值'
            },
            {
                field: 'leader',
                title: '项目组长'
            },
            {
                field: 'developManager',
                title: '开发负责人'
            },
            {
                field: 'testManager',
                title: '测试负责人'
            },
            // {
            //     field: 'QAManager',
            //     title: '质量管理负责人'
            // },
            {
                field : 'securityLevel',
                title : '保密等级'
            },
            {
                field : 'description',
                title : '描述'
            },
            {
                title: '操作',
                field: 'status',
                align: 'center',
                formatter: function (value, row, index) {

                    var e = '<a  class="btn btn-primary btn-sm ' +  '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                        + row.number
                        + '\')"><i class="fa fa-edit "></i></a> ';
                    var d = '<a class="btn btn-warning btn-sm ' +  '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                        + row.number
                        + '\')"><i class="fa fa-remove"></i></a> ';
                    var s = '<a class="btn btn-info btn-sm ' +  '" href="#" title="报告"  mce_href="#" onclick="showReport(\''
                        + row.number
                        + '\')"><i class="fa fa-search"></i></a> ';
                    var t = '<a class="btn btn-info btn-sm ' +  '" href="#" title="报告"  mce_href="#" onclick="showReport(\''
                        + row.number
                        + '\')" disabled="false"><i class="fa fa-search"></i></a> ';
                    if (value == '0' || value== '1') {
                        return e + d + t;
                    } else if (value == '2') {
                        return e + d + s;
                    }
                }
            },{
                field: 'status',
                align: 'center',
                formatter: function (value, row, index) {
                    var m = '<button type="button" class="btn btn-primary  btn-sm" style="margin-right:15px;" onclick="goToEvaluate(\''
                        + row.number
                        + '\')">开始测评</button>';
                    var n = '<button type="button" class="btn btn-primary  btn-sm" style="margin-right:15px;" onclick="goToEvaluate(\''
                        + row.number
                        + '\')" disabled="true">测评中</button>';
                    var p = '<button type="button" class="btn btn-primary  btn-sm" style="margin-right:15px;" onclick="goToEvaluate(\''
                        + row.number
                        + '\')" disabled="true">结束测评</button>';
                    if (value == '0') {
                        return m;
                    } else if (value == '1') {
                        return n;
                    }else{
                        return p;
                    }
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
        title: '增加测评软件',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/add' // iframe的url
    });
}

//编辑
function edit(id) {
    layer.open({
        type: 2,
        title: '编辑测评软件',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '500px'],
        content: prefix + '/edit/' + id // iframe的url
    });
}

//删除
function remove(number) {
    layer.confirm('确定删除测评软件？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'number': number
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

function showReport(number) {
    window.location.href = '/measurementData/showReport/' + number;
}

function goToEvaluate(number){
    // $('button').addClass('disabled');
    localStorage.setItem("curSoftwareNumberEvaluating",number);
    $.ajax({
        url: prefix + "/goToEvaluate",
        type: "post",
        data: {
            'number': number
        },
        success: function (r) {
            if (r.code == 0) {
                layer.msg(r.msg);
                reLoad();
                location.reload();
                // window.location.href = '/main';
            } else {
                layer.msg(r.msg);
            }
        }
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
            ids[i] = row['number'];
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
