package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.provider.service.DisciplineUserService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: 王梦瑶
 * @Description: 查询当月纪委并存入到数据库中
 * @Date: Create in 15:45 2020/6/14
 */
@Component
@JobHandler(value = "queryDisciplineUserJob")
public class QueryDisciplineUserJob extends IJobHandler {
    @Resource
    private DisciplineUserService disciplineUserService;
    /**
     * 获取当月当值纪委one
     * @param s
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log(this.getClass().getSimpleName()+"--start");
        disciplineUserService.queryDisciplineUser();
        XxlJobLogger.log(this.getClass().getSimpleName()+"--end");

        return ReturnT.SUCCESS;
 }
}
