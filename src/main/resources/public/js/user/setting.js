layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /*绑定表单提交函数*/
    form.on("submit(saveBtn)",function(data) {

        //发送ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/setting",
            data:{
                userName: data.field.username,
                phone:data.field.phone,
                email:data.field.email,
                trueName:data.field.trueName,
                id:data.field.id
            },
            dataType:"json",
            success:function (msg) {
                if(msg.code == 200){
                    layer.msg("保存成功了",function () {
                        //清空cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"})
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"})
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"})

                        //页面跳转
                        window.parent.location.href=ctx+"/index";
                    })
                }else{
                    //修改失败提示
                    layer.msg(msg,msg);
                }
            }
        })
        //放置页面跳转
        return false;
    })

});