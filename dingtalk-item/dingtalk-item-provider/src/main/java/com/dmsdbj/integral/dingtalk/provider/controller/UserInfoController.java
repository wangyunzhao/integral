package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dmsdbj.integral.dingtalk.provider.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调钉钉接口获取用户信息
 *
 * @author 崔晓鸿
 * @since 2020年6月15日09:00:11
 */
@Api(tags = {"用户信息"})
@RestController
@RequestMapping("userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @ApiOperation(value = "根据免登授权码获取用户信息")
    @GetMapping("getUserInfo")
    public Object getUserInfo(@ApiParam(value = "免登授权码",required = true) @RequestParam String code){
        Object userInfo = userInfoService.getUserInfo(code);
        return userInfo;
    }
}
