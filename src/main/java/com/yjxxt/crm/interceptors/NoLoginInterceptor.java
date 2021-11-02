package com.yjxxt.crm.interceptors;

import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.yjxxt.crm.utils.LoginUserUtil.releaseUserIdFromCookie;

public class NoLoginInterceptor extends HandlerInterceptorAdapter{
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从cookies中获取用户信息
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //判断用户ID是否不为空，且数据库中存在对应的用户信息
        if(userId == null || userService.selectByPrimaryKey(userId) == null){
            //表示未登录，抛出异常
            throw new NoLoginException("用户未登录");
        }
        //放行
        return true;
    }
}
