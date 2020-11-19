//package com.dmsdbj.integral.dingtalk.config;
//
//import com.tfjybj.framework.auth.interceptor.AbstractAuthInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//@Configuration
//public class WebConfig extends WebMvcConfigurationSupport {
//
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        // 注册自定义拦截器，添加拦截路径和排除拦截路径
//        registry.addInterceptor(new AbstractAuthInterceptor()) // 添加拦截器1
//                .addPathPatterns("/**") // 添加拦截路径;
//                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
//
//        super.addInterceptors(registry);
//    }
//}