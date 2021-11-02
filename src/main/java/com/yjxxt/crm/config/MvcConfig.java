package com.yjxxt.crm.config;

import com.yjxxt.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截路径
        //配置拦截器，设置拦截页面与放行页面
        registry.addInterceptor(noLoginInterceptor())
                //拦截页面 添加拦截路径
                //注意不能写 ***
                .addPathPatterns("/**")
                //放行页面 放行路径
                .excludePathPatterns("/index","/user/login","/css/**","/js/**","/images/**","/lib/**");
    }
}
