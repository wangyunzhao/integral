package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.provider.service.AllScheduleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * 钉钉企业排班job
 * @author 梁佳宝
 */
@Slf4j
@Component
@JobHandler("GetAllSchedule")
public class GetAllScheduleJob extends IJobHandler {

    @Resource
    private AllScheduleService allScheduleService;

    /**
     * 获取当天排班信息
     * @param s
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log(this.getClass().getSimpleName() + "--start");
        try {
            // 获取异常考勤
            XxlJobLogger.log(this.getClass().getSimpleName() + "查询企业排班详情");

            // param参数默认传0，表示取当天时间
            allScheduleService.toDbListSchedule(0);

        } catch (Exception e) {
            XxlJobLogger.log(e.getMessage(), e);
            throw e;
        }
        XxlJobLogger.log(this.getClass().getSimpleName() + "--end");
        return ReturnT.SUCCESS;
    }
}
