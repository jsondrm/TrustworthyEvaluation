//①先将数组的值存入localstorage
var subAttributeMatrixArray = document.getElementsByName("subAttributeMatrixArrayInTestPhase[]");
var currentUser = localStorage.getItem("currentUsername");

function change(){
    if(currentUser != "admin"){
        alert("请联系管理员进行修改！");
    }else{
        layer.open({
            type: 2,
            title: '操作记录添加',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '400px'],
            content: "/operateRecord" + '/upload' // iframe的url
        });
    }
}
function saveArray() {
    var arrayNew = [];
    for (var i = 0; i < subAttributeMatrixArray.length; i++) {
        if (subAttributeMatrixArray[i]) {
            arrayNew.push(subAttributeMatrixArray[i].value);
        }
    }
    localStorage.setItem('subAttributeMatrixArrayInTestPhase', JSON.stringify(arrayNew));
}

//点击保存数据按钮，调用保存方法
$("#saveData").click(function () {
    saveArray();
    alert("保存成功");
})

//加载页面，将保存在localstorage中的数据显示在页面上
$(document).ready(function () {
    var arrayOld = JSON.parse(localStorage.getItem('subAttributeMatrixArrayInTestPhase'));
    if(arrayOld != null) {
        for (var i = 0; i < arrayOld.length; i++) {
            subAttributeMatrixArray[i].value = arrayOld[i];
        }
    }
});

//点击下一步按钮，跳转到属性正互反矩阵输入页面
$("#nextStep").click(function () {
    window.location.href = "/softData/testPhase/inputMetricElementMatrix";
})