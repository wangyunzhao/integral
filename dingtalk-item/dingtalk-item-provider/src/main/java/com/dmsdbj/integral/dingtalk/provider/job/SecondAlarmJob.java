package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.provider.service.ApproveDetailService;
import com.dmsdbj.integral.dingtalk.utils.http.SecondAlarmUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Date;

/**
 * @program: integral
 * @Date: 2020-06-08 15:01
 * @Author: fjx
 * @Description: 考勤二次报警job
 */

@JobHandler(value = "SecondAlarmJob")
@Component
@Slf4j
public class SecondAlarmJob extends IJobHandler {

    @Resource
    private SecondAlarmUtil secondAlarmUtil;

    @Resource
    private ApproveDetailService approveDetailService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        // job 获取job名字，开始执行
        XxlJobLogger.log(this.getClass().getSimpleName() + "--start");

        if(secondAlarmUtil.weekNoon()){
            approveDetailService.secondAlarm(secondAlarmUtil.beginTime());
        }
        // job 结束
        XxlJobLogger.log(this.getClass().getSimpleName() + "--end");
        return ReturnT.SUCCESS;
    }
}
