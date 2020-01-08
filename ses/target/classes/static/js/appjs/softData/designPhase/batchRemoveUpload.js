var prefix = "/softData/designPhase";

$().ready(function() {
    validateRule();
});
$(function () {
    var picker = $('#operateDate').datetimepicker({
        minView: "month",
        format: 'yyyy-mm-dd',
        // locale: moment.locale('zh-cn')
        language: "zh-CN",
        todayBtn: 1,
        autoclose: 1,
        endDate: new Date()
    });
});
$.validator.setDefaults({
    submitHandler : function() {
        submit();
    }
});
// 修改正互反矩阵，上传情况说明
function submit() {
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
                var rows = parent.$('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
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
                        async : false,
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
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signUpForm").validate({
        rules : {
            name : {
                required : true
            }
        },
        messages : {
            name : {
                required : icon + "请输入名字"
            }
        }
    })
}