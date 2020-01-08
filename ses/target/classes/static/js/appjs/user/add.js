$().ready(function() {
    validateRule();
});

$.validator.setDefaults({
    submitHandler : function() {
        save();
    }
});
function save() {
    $.ajax({
        cache : true,
        type : "POST",
        url : "/user/save",
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
            userId : {
                required : true
            },
            username : {
                required : true
            },
            password : {
                required : true,
                minlength : 5
            },
            confirm_password : {
                required : true,
                minlength : 5,
                equalTo : "#password"
            },
            dptName : {
                required : true
            },
            email : {
                required : true,
                email : true
            }
        },
        messages : {
            userId : {
                required : icon + "请输入您的工号"
            },
            username : {
                required : icon + "请输入您的用户名"
            },
            password : {
                required : icon + "请输入您的密码",
                minlength : icon + "密码必须5个字符以上"
            },
            confirm_password : {
                required : icon + "请再次输入密码",
                minlength : icon + "密码必须5个字符以上",
                equalTo : icon + "两次输入的密码不一致"
            },
            dptName : {
                required : icon + "请输入您的部门"
            },
            email : icon + "请输入您的邮箱"
        }
    })
}