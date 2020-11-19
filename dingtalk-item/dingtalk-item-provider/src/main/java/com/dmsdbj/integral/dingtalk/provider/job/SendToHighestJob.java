package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.provider.service.SendMessageService;
import com.dmsdbj.integral.dingtalk.provider.service.SendToHighestService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@JobHandler(value = "SendToHighestJob")
public class SendToHighestJob extends IJobHandler {
    @Resource
    private SendToHighestService sendToHighestService;
    @Resource
    private SendMessageService sendMessageService;

    @Override
    public ReturnT<String> execute(String s) throws Exception{
        sendToHighestService.sendToHighest();
        return ReturnT.SUCCESS;
    }
}
