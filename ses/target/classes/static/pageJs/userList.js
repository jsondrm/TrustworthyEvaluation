layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#userList',
        url : '/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits: [20, 50, 100],
        limit: 20,
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {align: 'center', type: 'numbers', title: '编号'},
            {field: 'userId', title: '用户ID',  align:"center"},
            {field: 'loginName', title: '登录名',  align:'center'},
            {field: 'userName', title: '用户名',  align:'center'},
            {field: 'email', title: '邮箱', align:'center'},
            {field: 'phoneNumber', title: '手机',  align:'center'},
            {field: 'qq', title: 'QQ',  align:'center',templet: function (d) {
                if (d.qq == null) {
                    return "-";
                }else{
                    return d.qq;
                }
            }},
            {field: 'sex', title: '性别',  align:'center',templet: function (d) {
                if (d.sex == 0) {
                    return "女";
                } else if(d.sex == 1) {
                    return "男";
                }else{
                    return "保密"
                }
            }},
            {field: 'creator', title: '创建者', align:'center'},
            {field: 'createTime', title: '创建时间',  align:'center'},
            {title: '操作', minWidth:150, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        table.reload("userListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                loginName: $(".searchVal").val()  //搜索的关键字
            }
        })
    });
    $(window).keydown( function(e) {
        var key = window.event?e.keyCode:e.which;
        if(key.toString() == "13"){
            return false;
        }
    });

    //添加用户
    function addUser(){
        var index = layui.layer.open({
            title : "添加用户",
            type : 2,
            content : "/pageTag/userAdd",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    }


    function editUser(edit){
        var index = layui.layer.open({
            title : "编辑用户",
            type : 2,
            content : "/pageTag/userUpdate",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find(".userName").val(edit.userName);
                body.find(".email").val(edit.email);
                body.find(".phoneNumber").val(edit.phoneNumber);
                body.find(".loginName").val(edit.loginName);
                body.find(".sex").val(edit.sex);
                body.find(".administrator").val(edit.administrator);
                body.find(".qq").val(edit.qq);
                form.render();
                setTimeout(function(){
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    }

    $(".addNews_btn").click(function(){
        addUser();
    })

    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            editUser(data);
        }
    });

})