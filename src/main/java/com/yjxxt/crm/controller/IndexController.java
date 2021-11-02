package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;
    //登录页面
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    //后台资源页面
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //通过工具类，从cookie中获取userid
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //根据查询用户的信息
        User user = userService.selectByPrimaryKey(userId);
        //存储
        request.setAttribute("user",user);
        //转发到目标页面
        return "main";
    }

    //欢迎页面
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

}
