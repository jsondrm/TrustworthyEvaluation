var prefix = "/softData/designPhase";


//①先将数组的值存入localstorage
var attributeMatrixArray = document.getElementsByName("attributeMatrixArrayInDesignPhase[]");
var currentUser = localStorage.getItem("currentUsername");

function change(){
    if(currentUser != "hewenxuan"){
        alert("请联系管理员进行修改！");
    }else{
        layer.open({
            type: 2,
            title: '原始凭证上传',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['800px', '400px'],
            content: "/operateRecord/designPhase" + '/upload' // iframe的url
        });
    }

}
function saveArray() {
    var arrayNew = [];
    for (var i = 0; i < attributeMatrixArray.length; i++) {
        if (attributeMatrixArray[i]) {
            arrayNew.push(attributeMatrixArray[i].value);
        }
    }
    localStorage.setItem('attributeMatrixArrayInDesignPhase', JSON.stringify(arrayNew));
}

//点击保存数据按钮，调用保存方法
$("#saveData").click(function () {
    saveArray();
    alert("保存成功");
})

//加载页面，将保存在localstorage中的数据显示在页面上
$(document).ready(function () {
    var arrayOld = JSON.parse(localStorage.getItem('attributeMatrixArrayInDesignPhase'));
    if(arrayOld != null){
        for (var i = 0; i < arrayOld.length; i++) {
            attributeMatrixArray[i].value = arrayOld[i];
        }
    }
});

$("#nextStep").click(function () {
    window.location.href = "/softData/designPhase/inputSubAttributeMatrix";
})