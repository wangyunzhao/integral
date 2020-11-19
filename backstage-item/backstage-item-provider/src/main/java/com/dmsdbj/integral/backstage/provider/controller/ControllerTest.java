package com.dmsdbj.integral.backstage.provider.controller;



import com.dmsdbj.integral.backstage.provider.service.ServiceTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author 齐智
 * @Date: 2020/7/24 19:34
 * @Version: 1.0
 **/
@Api(tags = {"测试"})
@RequestMapping(value = "/test")
@RestController
public class ControllerTest implements ApiControllerTest{


    @Autowired
    private ServiceTest serviceTest;

    @ApiOperation(value = "测试")
    @Override
    public String myTest(Integer id) {
        String s = serviceTest.myTest(id);
        return s+id;
    }
}
