var prefix = "/softData/designPhase";
currentUser = localStorage.getItem("currentUsername");
editId = localStorage.getItem("editId");
removeId = localStorage.getItem("removeId");
rows = localStorage.getItem("rows");
$(function () {
    if(currentUser != "admin"){
        var uploadButton = document.getElementById("uploadFile");
        uploadButton.setAttribute("disabled",true);
        var buttons = document.getElementsByTagName("button");
        for(var i = 0; i < buttons.length; i++){
            buttons[i].setAttribute("disabled", true)
        }
    }
    load();
});

function load() {
    // selectLoad();
    $('#exampleTable').bootstrapTable('destroy');
    $('#exampleTable').bootstrapTable({
        method: "get",
        url: prefix + "/softDataList", //服务器数据加载地址
        iconSize: 'outline',
        toolbar: '#exampleToolbar',
        striped: true, //设置为true 会有隔行变色效果
        cache: false, //是否使用缓存，默认为true
        dataType: "json", //服务器返回的数据类型
        pagination: true, //设置为true会在底部显示分页条
        singleSelect: false, //设置为true会禁止多选
        pageSize: 5, //如果设置了分页，每页数据条数
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
            /*{
                field: 'id',
                title: '编号'
            },*/
            {
                field: 'softwareNumber',
                title: '软件编号'
            },
            {
                field: 'phase',
                title: '阶段'
            },
            {
                field: 'attribute',
                title: '属性'
            },
            {
                field: 'subAttribute',
                title: '子属性'
            },
            {
                field: 'metricElement',
                title: '度量元'
            },
            {
                field: 'target',
                title: '目标'
                //width : '200px'
            },
            {
                field: 'metricIndex',
                title: '指标名称'
            },
            {
                field: 'metricType',
                title: '类型'
            },
            {
                field: 'metricDefinition',
                title: '定义'
            },
            {
                field: 'x_y_spec',
                title: 'x和y具体含义'
            },
            {
                field: 'x_value',
                title: 'x的值'
            },
            {
                field: 'y_value',
                title: 'y的值'
            },
            {
                title: '操作',
                field: 'id',
                align: 'center',
                //width : '200px',
                formatter: function (value, row, index) {
                    var e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" id="editButton" disabled="true" title="编辑" onclick="edit(\''
                        + row.id
                        + '\')"><i class="fa fa-edit"></i></a>';
                    var d = '<a class="btn btn-warning btn-sm" href="#" title="删除" mce_href="#" id="removeButton" disabled="true" onclick="remove(\''
                        + row.id
                        + '\')"><i class="fa fa-remove"></i></a>';
                    return e + d;
                }
            }
        ]
    });

}
//对上传文件初始化
$("#designPhaseFile").fileinput({
    language: 'zh', //设置语言
    uploadUrl: prefix + "/uploadFile", //上传的地址
    showUpload: false,
    allowedFileExtensions: ['xls', 'xlsx'],
    uploadAsync: false,
    maxFileCount: 1,
    browseOnZoneClick: true,
    dropZoneEnabled: true,
    showPreview: true,
    enctype: 'multipart/form-data',
    showCaption: true,//是否显示标题
    browseClass: "btn btn-primary", //按钮样式
    previewFileIcon: "<i class='glyphicon glyphicon-king'></i>"
});

$("#uploadFile").click(function () {
    if(currentUser != "admin"){
        var uploadButton = document.getElementById("uploadFile");
        uploadButton.setAttribute("disabled",true);
    }else{
        var type = "designPhaseFile";
        var id = "designPhaseFile";
        var formData = new FormData();
        formData.append(type,$("#"+id)[0].files[0]);
        $.ajax({
            type:"POST",
            url:prefix + "/uploadFile",
            data:formData,
            processData:false,
            contentType:false,
            success:function(data){
                alert(data);
                window.location.href = "/softData/designPhase";
            }
        });
    }
});

//查询
function reLoad() {
    var opt = {
        query: {
            type: $('.chosen-select').val()
        }
    };
    $('#exampleTable').bootstrapTable('refresh', opt);
}

function uploadOperateRecord(){
    if(currentUser != "admin"){
        alert("请联系管理员进行操作！");
    }else{
        layer.open({
            type: 2,
            title: '操作记录说明',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '400px'],
            content: "/operateRecord/uploadOperateRecord" // iframe的url
        });
    }
}

//添加
function add() {
    if(currentUser != "admin"){
        alert("请联系管理员进行修改！");
    }else{
        layer.open({
            type: 2,
            title: '增加度量指标',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '600px'],
            content: prefix + '/add' // iframe的url
        });
    }
}

//编辑
function edit(id) {
    if(currentUser != "admin"){
        alert("请联系管理员进行编辑！");
    }else{
        /*localStorage.setItem("editId",id);
        layer.open({
            type: 2,
            title: '操作记录说明',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '400px'],
            content: "/operateRecord/designPhase/editUpload" // iframe的url
        });*/
        layer.open({
            type: 2,
            title: '编辑度量指标',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '600px'],
            content: prefix + '/edit/' + id // iframe的url
        });
    }

}

function editUploadSubmit(){
    $.ajax({
        cache : true,
        type : "POST",
        url : "/operateRecord/submit",
        data : $('#signUpForm').serialize(), // 你的formid
        async : false,
        error : function(request) {
            parent.layer.alert("网络超时");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
                parent.layer.open({
                    type: 2,
                    title: '编辑度量指标',
                    maxmin: true,
                    shadeClose: false, // 点击遮罩关闭层
                    area: ['800px', '600px'],
                    content: prefix + '/edit/' + editId // iframe的url
                });
            } else {
                parent.layer.alert(data.msg)
            }

        }
    });
}

//删除
function remove(id) {
    if(currentUser != "admin"){
        alert("请联系管理员进行删除！");
    }else{
        /*localStorage.setItem("removeId",id);
        layer.open({
            type: 2,
            title: '操作记录说明',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '400px'],
            content: "/operateRecord/designPhase/removeUpload" // iframe的url
        });*/
        layer.confirm('确定要删除选中的记录？', {
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
}

function removeUploadSubmit(){
    $.ajax({
        cache : true,
        type : "POST",
        url : "/operateRecord/submit",
        data : $('#signUpForm').serialize(), // 你的formid
        async : false,
        error : function(request) {
            parent.layer.alert("网络超时");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
                top.layer.confirm('确定要删除选中的记录？', {
                    btn: ['确定', '取消']
                }, function () {
                    console.log(removeId);
                    $.ajax({
                        url: prefix + "/remove",
                        type: "post",
                        data: {
                            'id': removeId
                        },
                        async: false,
                        success: function (r) {
                            if (r.code == 0) {
                                top.layer.msg(r.msg);
                                top.reLoad();
                            } else {
                                top.layer.msg(r.msg);
                            }
                        }
                    });
                })
            } else {
                parent.layer.alert(data.msg)
            }

        }
    });
}


//批量删除
function batchRemove() {
    if(currentUser != "admin"){
        alert("请联系管理员进行修改！");
    }else{
        /*var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
        // localStorage.setItem("rows",rows);
        console.log(rows.length);
        if (rows.length == 0) {
            layer.msg("请选择要删除的数据");
            return;
        }
        layer.open({
            type: 2,
            title: '操作记录说明',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '400px'],
            content: "/operateRecord/designPhase/batchRemoveUpload" // iframe的url
        });*/
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

}

function batchRemoveUploadSubmit() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/operateRecord/submit",
        data: $('#signUpForm').serialize(), // 你的formid
        async: false,
        error: function (request) {
            parent.layer.alert("网络超时");
        },
        success: function (data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
                var rows = parent.$('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
                console.log(rows.length);
                parent.layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
                    btn: ['确定', '取消']
                    // 按钮
                }, function () {
                    var ids = new Array();
                    // 遍历所有选择的行数据，取每条数据对应的ID
                    $.each(rows, function (i, row) {
                        ids[i] = row['id'];
                    });
                    $.ajax({
                        async: false,
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
            } else {
                parent.layer.alert(data.msg)
            }

        }
    });
}

