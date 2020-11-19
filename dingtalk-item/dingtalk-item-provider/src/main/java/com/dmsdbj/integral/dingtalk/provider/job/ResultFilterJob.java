package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.provider.service.ResultFilteringService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 16:34 2020/6/9
 */
@Slf4j
@Component
@JobHandler(value = "ResultFilterJob")
public class ResultFilterJob extends IJobHandler {

    @Resource
    private ResultFilteringService resultFilteringService;
    @Override
    public ReturnT<String> execute(String  baseCheckTime1){
        XxlJobLogger.log(this.getClass().getSimpleName()+"--start");
        resultFilteringService.resultFiltering();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--end");
        return ReturnT.SUCCESS;
    }
}
