package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dmsdbj.integral.dingtalk.provider.service.SendToHighestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
/**
 * @author:董可
 * @Date: 2020/7/22 11:27
 * @Description
 */
@Api(tags = {"发送工作通知"})
@RequestMapping(value = "/sendToMi")
@RestController
public class SendToHighestController {
    @Resource
    private SendToHighestService sendToHighestService;

    /**
     * 功能描述: 调用钉钉接口发送消息--董可--2020年6月6日09:44:41
     * @Author: keer
     * @Date: 2020年6月6日09:44:41
     * @Param: []
     * @Return: boolean
     */
    @ApiOperation(value = "调用钉钉接口发送群消息")
    @GetMapping(value = {"/useDingSendToHighest"})
    public boolean sendToMi(){
        boolean message = sendToHighestService.sendToHighest();
        return message;
    }
}
