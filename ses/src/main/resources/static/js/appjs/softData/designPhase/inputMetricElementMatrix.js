//①先将数组的值存入localstorage
var metricElementMatrixArray = document.getElementsByName("metricElementMatrixArrayInDesignPhase[]");

function saveArray() {
    var arrayNew = [];
    for (var i = 0; i < metricElementMatrixArray.length; i++) {
        if (metricElementMatrixArray[i]) {
            arrayNew.push(metricElementMatrixArray[i].value);
        }
    }
    localStorage.setItem('metricElementMatrixArrayInDesignPhase', JSON.stringify(arrayNew));
}

//点击保存数据按钮，调用保存方法
$("#saveData").click(function () {
    saveArray();
    alert("保存成功");
})

//加载页面，将保存在localstorage中的数据显示在页面上
$(document).ready(function () {
    var arrayOld = JSON.parse(localStorage.getItem('metricElementMatrixArrayInDesignPhase'));
    if(arrayOld != null) {
        for (var i = 0; i < arrayOld.length; i++) {
            metricElementMatrixArray[i].value = arrayOld[i];
        }
    }
});

//提交数据
$("#submitData").click(function () {
    //console.log("准备计算权重");
    var zhenghufanMatrix;
    zhenghufanMatrix = {
        "metricElementMatrixArray": localStorage.getItem('metricElementMatrixArrayInDesignPhase'),
        "subAttributeMatrixArray": localStorage.getItem('subAttributeMatrixArrayInDesignPhase'),
        "attributeMatrixArray": localStorage.getItem('attributeMatrixArrayInDesignPhase')
    };
    $.ajax({
        type:"POST",
        async:false,
        dataType:"json",//服务端（后台）返回的数据类型
        contentType:"application/json",//客户端（前端）发送的数据类型
        url:"/softData/designPhase/countWeightByMatrix",
        data:JSON.stringify(zhenghufanMatrix),
        success:function(data){
            if(data.code == 1){
                alert(data.msg);
            }else{
                alert("提交成功！");
                window.location.href = "/softData/designPhase/displayWeight";
            }
        }
    });
})

//点击下一步按钮，跳转到子属性正互反矩阵输入页面
/*$("#nextStep").click(function () {
    var metricElementMatrixArray = localStorage.getItem('metricElementMatrixArray');
    //var metricElementMatrixArray = document.getElementsByName("metricElementMatrixArray[]");
    $.ajax({
        type:"POST",
        async:false,
        //traditional:true,
        dataType:"json",//服务端（后台）返回的数据类型
        contentType:"application/json",//客户端（前端）发送的数据类型
        url:"/softData/requirePhase/checkCRMetricElement",
        data:JSON.stringify(metricElementMatrixArray),
        success:function(data){
            if(data.code == 1){
                alert(data.msg);
            }else{
                window.location.href = "/softData/requirePhase/inputSubAttributeMatrix";
            }
        }
    });
})*/

