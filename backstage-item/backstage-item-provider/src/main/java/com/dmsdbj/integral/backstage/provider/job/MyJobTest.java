package com.dmsdbj.integral.backstage.provider.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 齐智
 * @Date: 2020/7/26 7:47
 * @Version: 1.0
 **/

@Component
@JobHandler(value="MyJobTest")
@Configuration
public class MyJobTest  extends IJobHandler {

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        System.out.println("job执行成功");
        return ReturnT.SUCCESS;
    }
}
