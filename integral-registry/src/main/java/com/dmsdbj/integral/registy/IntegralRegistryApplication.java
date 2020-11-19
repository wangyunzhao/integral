package com.dmsdbj.integral.registy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class IntegralRegistryApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(IntegralRegistryApplication.class,args);
    }
}
