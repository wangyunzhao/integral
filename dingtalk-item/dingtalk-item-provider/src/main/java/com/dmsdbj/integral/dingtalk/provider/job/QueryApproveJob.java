package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.provider.dao.ApproveDetailDao;
import com.dmsdbj.integral.dingtalk.provider.service.ApproveDetailService;
import com.dmsdbj.integral.dingtalk.provider.service.ResultFilteringService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: 曹祥铭
 * @Description: 查询审批并出入到数据库中
 * @Date: Create in 15:45 2020/6/14
 */
@Component
@JobHandler(value = "queryApproveJob")
public class QueryApproveJob extends IJobHandler {

    @Resource
    private ApproveDetailService approveDetailService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        XxlJobLogger.log(this.getClass().getSimpleName()+"--start");
        approveDetailService.approveMethods();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--end");

        return ReturnT.SUCCESS;
    }
}
