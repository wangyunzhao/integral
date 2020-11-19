package com.dmsdbj.integral.backstage.provider.job;

import com.dmsdbj.integral.backstage.model.OrganizationUserModel;
import com.dmsdbj.integral.backstage.provider.service.OrganizationService;
import com.dmsdbj.integral.backstage.provider.service.OrganizationUsersService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wmy
 * @Description: 查询组织id下的所有人员
 * @Date: Create in 15:45 2020/6/14
 */
@Component
@JobHandler(value = "queryOrganizationUserJob")
public class QueryOrganizationUserJob extends IJobHandler {
    @Resource
    private OrganizationUsersService organizationUsersService;
    @Resource
    private  OrganizationService organizationService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        XxlJobLogger.log(this.getClass().getSimpleName()+"--start");
        organizationUsersService.selectOrganizationUser();
        organizationService.selectOrganization();

        XxlJobLogger.log(this.getClass().getSimpleName()+"--end");

        return ReturnT.SUCCESS;
    }
}
