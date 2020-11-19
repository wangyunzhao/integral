package com.dmsdbj.integral.backstage.provider.job;

import com.dmsdbj.integral.backstage.provider.service.SpecificPointsService;
import com.dmsdbj.integral.backstage.provider.service.UpdateSpecificPointsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 齐智
 * @Date: 2020/8/13 9:42
 * @Version: 1.0
 **/
@Slf4j
@Component
@JobHandler(value = "UpdateSpecificPointsJob")
public class UpdateSpecificPointsJob extends IJobHandler {

    @Autowired
    private UpdateSpecificPointsService updateSpecificPointsService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log(this.getClass().getSimpleName()+"--start");
        XxlJobLogger.log(this.getClass().getSimpleName()+"--根据特定人员加分表更新用户的可赠的积分");
        updateSpecificPointsService.updateGivingIntegralWithId();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--根据特定人员加分表的部门更新用户的可赠的积分");
        updateSpecificPointsService.updateGivingIntegralWithDeptId();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--end");
        return ReturnT.SUCCESS;
    }
}
