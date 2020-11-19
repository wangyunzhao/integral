//package com.dmsdbj.integral.dingtalk.config;
//
//import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
//import com.tfjybj.framework.mybatis.interceptor.SqlMonitorInterceptor;
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
////import tk.mybatis.spring.annotation.MapperScan;
//
//import java.util.Properties;
//
///**
// * @Author: 曹祥铭
// * @Description:
// * @Date: Create in 9:23 2020/5/30
// */
//@Configuration
//@MapperScan({"com.dmsdbj.integral.dingtalk.provider.dao"})
//public class MyBatisConfig {
//    //将插件加入到mybatis插件拦截链中
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return new ConfigurationCustomizer() {
//            @Override
//            public void customize(org.apache.ibatis.session.Configuration configuration) {
//                //插件拦截链采用了责任链模式，执行顺序和加入连接链的顺序有关
//                SqlMonitorInterceptor myPlugin = new SqlMonitorInterceptor();
//                //设置参数，比如阈值等，可以在配置文件中配置，这里直接写死便于测试
//                Properties properties = new Properties();
//                //这里设置慢查询阈值为1毫秒，便于测试
//                properties.setProperty("time", "1000");
//                myPlugin.setProperties(properties);
//                configuration.addInterceptor(myPlugin);
//            }
//        };
//    }
//}
