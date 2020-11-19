package com.dmsdbj.integral.backstage.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Author 齐智
 * @Date: 2020/7/24 19:28
 * @Version: 1.0
 **/
@RequestMapping
public interface ApiControllerTest {

    @GetMapping("getTest")
    public String myTest(Integer id);
}
