package com.xydl.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @类功能说明：拦截器注册
 * @公司名称：南京星源动力信息技术有限公司
 * @作者：chenchen
 * @创建时间：2020/7/28 14:44
 * @版本：V1.0
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Bean
    public AppInterceptor appInterceptor() {
        return new AppInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册用时拦截器
        registry.addInterceptor(new ExcuteTimeInterceptor());

        //注册加解密拦截器  (拦截所有已api开头的拦截器)
        InterceptorRegistration ir = registry.addInterceptor(appInterceptor()).addPathPatterns("/**");

        //不拦截请求
        ir.excludePathPatterns("/test/test");
        ir.excludePathPatterns("/*.html");
        ir.excludePathPatterns("/*.js");
        ir.excludePathPatterns("/js/*.js");
        //ir.excludePathPatterns("/error");
    }
}
