package com.dmsdbj.integral.backstage.provider.job;

import com.dmsdbj.integral.backstage.provider.service.UpdateBonusPointsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 20:33 2020/8/12
 */
@Slf4j
@Component
@JobHandler(value = "UpdateBonusPointsJob")
public class UpdateBonusPointsJob  extends IJobHandler {

    @Autowired
    private UpdateBonusPointsService updateBonusPointsService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log(this.getClass().getSimpleName()+"--start");
        XxlJobLogger.log(this.getClass().getSimpleName()+"--更新所有人的可赠积分、权重、以及是否有减分权限");
        updateBonusPointsService.updateBonusPoints();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--更新指定人员的可赠积分、权重、以及是否有减分权限");
        updateBonusPointsService.setWeightAndIntegral();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--更新指定部门人员的可赠积分、权重、以及是否有减分权限");
        updateBonusPointsService.setWeightAndIntegralByList();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--end");
        return null;
    }
}
