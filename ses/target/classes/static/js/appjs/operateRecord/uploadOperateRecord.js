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
// 执行添加、批量删除、编辑和删除四个操作时，填写操作记录
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
                parent.layer.msg("保存成功");
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
                var buttons = parent.document.getElementsByTagName("button");
                for(var i = 0; i < buttons.length; i++){
                    buttons[i].removeAttribute("disabled");
                }
                var editButtons = parent.document.getElementById("editButton");
                for(var i = 0; i < editButtons.length; i++){
                    editButtons[i].removeAttribute("disabled");
                }
                var removeButtons = parent.document.getElementById("removeButton");
                for(var i = 0; i < removeButtons.length; i++){
                    removeButtons[i].removeAttribute("disabled");
                }
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
            operator : {
                required : true
            },
            operateTime : {
                required : true
            },
            operateTarget : {
                required : true
            },
            description : {
                required : true
            }
        },
        messages : {
            operator : {
                required : icon + "请输入名字"
            },
            operateTime : {
                required : icon + "请输入操作时间"
            },
            operateTarget : {
                required : icon + "请输入操作对象"
            },
            description : {
                required : icon + "请输入说明"
            }

        }
    })
}