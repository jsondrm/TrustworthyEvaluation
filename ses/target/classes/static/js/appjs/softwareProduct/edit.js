// 以下为官方示例
$().ready(function() {
    validateRule();
    // $("#signupForm").validate();
});

$.validator.setDefaults({
    submitHandler : function() {
        update();
    }
});
function update() {
    //$("#roleIds").val(getCheckedRoles());
    $.ajax({
        cache : true,
        type : "POST",
        url : "/softwareProduct/update",
        data : $('#signUpForm').serialize(),// 你的formid
        async : false,
        error : function(request) {
            alert("Connection error");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg(data.msg);
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);

            } else {
                parent.layer.msg(data.msg);
            }

        }
    });

}
/*function getCheckedRoles() {
    var adIds = "";
    $("input:checkbox[name=role]:checked").each(function(i) {
        if (0 == i) {
            adIds = $(this).val();
        } else {
            adIds += ("," + $(this).val());
        }
    });
    return adIds;
}
function setCheckedRoles() {
    var roleIds = $("#roleIds").val();
    alert(roleIds);
    var adIds = "";
    $("input:checkbox[name=role]:checked").each(function(i) {
        if (0 == i) {
            adIds = $(this).val();
        } else {
            adIds += ("," + $(this).val());
        }
    });
    return adIds;
}*/
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signUpForm").validate({
        rules : {
            number : {
                required : true,
            }
        },
        messages : {
            number : {
                required : icon + "请输入编号"
            }
        }
    })
}
/*
var openDept = function(){
    layer.open({
        type:2,
        title:"选择部门",
        area : [ '300px', '450px' ],
        content:"/system/sysDept/treeView"
    })
}
function loadDept( deptId,deptName){
    $("#deptId").val(deptId);
    $("#deptName").val(deptName);
}*/
