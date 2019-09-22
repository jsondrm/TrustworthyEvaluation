//①先将数组的值存入localstorage
var subAttributeMatrixArray = document.getElementsByName("subAttributeMatrixArrayInDesignPhase[]");

function saveArray() {
    var arrayNew = [];
    for (var i = 0; i < subAttributeMatrixArray.length; i++) {
        if (subAttributeMatrixArray[i]) {
            arrayNew.push(subAttributeMatrixArray[i].value);
        }
    }
    localStorage.setItem('subAttributeMatrixArrayInDesignPhase', JSON.stringify(arrayNew));
}

//点击保存数据按钮，调用保存方法
$("#saveData").click(function () {
    saveArray();
    alert("保存成功");
})

//加载页面，将保存在localstorage中的数据显示在页面上
$(document).ready(function () {
    var arrayOld = JSON.parse(localStorage.getItem('subAttributeMatrixArrayInDesignPhase'));
    if(arrayOld != null) {
        for (var i = 0; i < arrayOld.length; i++) {
            subAttributeMatrixArray[i].value = arrayOld[i];
        }
    }
});

//点击下一步按钮，跳转到属性正互反矩阵输入页面
$("#nextStep").click(function () {
    window.location.href = "/softData/designPhase/inputMetricElementMatrix";
})