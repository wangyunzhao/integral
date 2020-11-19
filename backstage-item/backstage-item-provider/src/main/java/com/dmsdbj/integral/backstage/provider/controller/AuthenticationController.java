package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.AuthenticationModel;
import com.dmsdbj.integral.backstage.provider.service.AuthenticationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 17:29 2020/8/4
 */
@RestController
@Api(tags = {"接口鉴权"})
@RequestMapping(value = "/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping(value = "/getSignature")
    public IntegralResult getSignature(@RequestBody AuthenticationModel authenticationModel){
        IntegralResult signature = authenticationService.getSignature(authenticationModel);
        return signature;
    }
}
