package com.dmsdbj.integral.dingtalk.provider.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmsdbj.integral.dingtalk.provider.service.AddIntegralService;
import com.dmsdbj.integral.dingtalk.provider.service.SendMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author:keer
 * @Date: 2020/7/22 11:27
 * @Description
 */
@Api(tags = {"发送群消息"})
@RequestMapping(value = "/sendMessage")
@RestController
public class SendMessageController {

    @Resource
    private SendMessageService sendMessageService;
    @Resource
    private AddIntegralService addIntegralService;
    /**
     * 调用钉钉接口发送消息--董可--2020年6月6日09:44:41
     * @param chatId
     * @param msg
     * @return
     */
    @ApiOperation(value = "调用钉钉接口发送群消息")
    @GetMapping(value = {"/useDingSendMessage"})
    public boolean sendMessage(String chatId,JSON msg){
        boolean message = sendMessageService.sendMessage(chatId,msg) ;
        return message;
    }

    /**
     * 调用钉钉接口发送工作通知
     * @return
     * @author 王云召
     * @since 2020年6月24日09:35:42
     */
    @ApiOperation(value = "调用钉钉接口发送工作通知")
    @GetMapping(value = {"/useDingSendWorkMessage"})
    public void useDingSendWorkMessage(){
        addIntegralService.sendWorkMessageToUser();
    }
}