//①先将数组的值存入localstorage
var metricElementMatrixArray = document.getElementsByName("metricElementMatrixArrayInCodingPhase[]");
var currentUser = localStorage.getItem("currentUsername");

function change(){
    if(currentUser != "admin"){
        alert("请联系管理员进行修改！");
    }else{
        layer.open({
            type: 2,
            title: '原始凭证上传',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '400px'],
            content: "/operateRecord" + '/upload' // iframe的url
        });
    }
}
function saveArray() {
    var arrayNew = [];
    for (var i = 0; i < metricElementMatrixArray.length; i++) {
        if (metricElementMatrixArray[i]) {
            arrayNew.push(metricElementMatrixArray[i].value);
        }
    }
    localStorage.setItem('metricElementMatrixArrayInCodingPhase', JSON.stringify(arrayNew));
}

//点击保存数据按钮，调用保存方法
$("#saveData").click(function () {
    saveArray();
    alert("保存成功");
})

//加载页面，将保存在localstorage中的数据显示在页面上
$(document).ready(function () {
    var arrayOld = JSON.parse(localStorage.getItem('metricElementMatrixArrayInCodingPhase'));
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
        "metricElementMatrixArray": localStorage.getItem('metricElementMatrixArrayInCodingPhase'),
        "subAttributeMatrixArray": localStorage.getItem('subAttributeMatrixArrayInCodingPhase'),
        "attributeMatrixArray": localStorage.getItem('attributeMatrixArrayInCodingPhase')
    };
    $.ajax({
        type:"POST",
        async:false,
        dataType:"json",//服务端（后台）返回的数据类型
        contentType:"application/json",//客户端（前端）发送的数据类型
        url:"/softData/codingPhase/countWeightByMatrix",
        data:JSON.stringify(zhenghufanMatrix),
        success:function(data){
            if(data.code == 1){
                alert(data.msg);
            }else{
                alert("提交成功！");
                window.location.href = "/softData/codingPhase/displayWeight";
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

