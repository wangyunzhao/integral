package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.provider.service.ResultFilteringService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 17:45 2020/6/15
 */
@Slf4j
@Component
@JobHandler(value = "AlarmToTeacherJob")
public class AlarmToTeacherJob extends IJobHandler {
    @Resource
    private ResultFilteringService resultFilteringService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log(this.getClass().getSimpleName()+"--start");
        resultFilteringService.queryAlarmToTeacher();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--end");
        return ReturnT.SUCCESS;

    }
}
