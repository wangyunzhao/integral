package com.dmsdbj.integral.backstage;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @Description 钉钉考勤
 * @Author 齐智
 * @Date: 2020/7/24 19:31
 * @Version: 1.0
 **/
@MapperScan({"com.dmsdbj.integral.backstage.provider.dao"})
@EnableTransactionManagement
@SpringBootApplication
@EnableEurekaClient
@Reference
public class BackStageApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackStageApplication.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
