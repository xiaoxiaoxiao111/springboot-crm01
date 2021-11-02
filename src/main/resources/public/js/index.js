layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    form.on('submit(login)', function(data) {
        var fieldData = data.field;

        if (fieldData.username == 'undefined' || fieldData.username.trim() == '') {
            layer.msg("用户名不能为空");
            return false;
        }

        if (fieldData.password == 'undefined' || fieldData.password.trim() == '') {
            layer.msg("密码不能为空");
            return false;
        }
        $.ajax({
            // crm/user/login
            type:"post",
            url: ctx + "/user/login",
            data: {
                "userName": fieldData.username,
                "userPwd": fieldData.password
            },
            dataType: "json",
            success: function (msg) {
                //resultInfo
                if (msg.code == 200) {
                    //成功了
                    //layer.msg("登录成功了",{icon:5});
                    //跳转
                   // window.location.href=ctx+"/main";
                    layer.msg("登录成功了", function () {
                        //将用户的数据存储到Cookie
                        $.cookie("userIdStr", msg.result.userIdStr);
                        $.cookie("userName", msg.result.userName);
                        $.cookie("trueName", msg.result.trueName);

                        //判断是否勾选了"记住我"功能
                        if($("input[type='checkbox']").is(":checked")){
                            $.cookie("userIdStr",msg.result.userIdStr,{expires:7});
                            $.cookie("userName",msg.result.userName,{expires: 7});
                            $.cookie("trueName",msg.result.trueName,{expires:7});
                        }

                        //跳转页面
                        window.location.href = ctx + "/main";
                    });
                }else{
                    //失败的提示
                    layer.msg(msg.msg);
                }
            }
        });
        //取消默认行为
        return false;
    });
})