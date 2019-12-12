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
function submit() {
    $.ajax({
        cache : true,
        type : "POST",
        url : "/operateRecord/designPhase/submit",
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
                var table = window.parent.document.getElementById("designAttributeTable");
                var tableInput = table.getElementsByTagName("input");
                for(var i = 0; i < tableInput.length; i++){
                    tableInput[i].removeAttribute("disabled")
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