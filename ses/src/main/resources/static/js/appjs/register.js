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
        data : $('#registerForm').serialize(), // 你的formid
        async : false,
        error : function(request) {
            parent.layer.alert("网络超时");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            } else {
                parent.layer.alert(data.msg);
                //window.location.href = "/register";
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.open(index);
            }

        }
    });
}
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#registerForm").validate({
        rules : {
            username : {
                required : true,
                minlength : 2,
                remote : {
                    url : "/user/exit", // 后台处理程序
                    type : "post", // 数据发送方式
                    dataType : "json", // 接受数据格式
                    data : { // 要传递的数据
                        username : function() {
                            return $("#username").val();
                        }
                    }
                }
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
            username : {
                required : icon + "请输入名字",
                minlength : icon + "用户名必须两个字符以上",
                remote : icon + "用户名已经存在"
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
            email : icon + "请输入您的E-mail"
        }
    })
}