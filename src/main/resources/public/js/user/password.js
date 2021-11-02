layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /*绑定表单提交函数*/
    form.on("submit(saveBtn)",function(data){
        //获取表单域内容
       var fieldData= data.field;
       //发送ajax修改密码
        $.ajax({
            type:"post",
            url:ctx+"/user/updateUserPwd",
            data:{
               "oldPassword":fieldData.old_password,
                "newPassword":fieldData.new_password,
                "confirmPwd":fieldData.again_password
            },
            dataType:"json",
            success:function(msg){
                //判断是否成功
                if(msg.code==200){
                    //修改成功了
                    layer.msg("修改成功了",function(){
                        //退出系统移除Cookie
                        $.removeCookie("userIdStr",{"domain":"localhost","path":"/crm"});
                        $.removeCookie("userName",{"domain":"localhost","path":"/crm"});
                        $.removeCookie("trueName",{"domain":"localhost","path":"/crm"});
                        //页面跳转到登录页面
                        window.parent.location.href=ctx+"/index";
                    });

                }else{
                    //修改失败提示一下
                    layer.msg(msg.msg);
                }
            }
        });
        //取消默认跳转页面
        return false;
    });

});