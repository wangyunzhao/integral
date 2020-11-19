package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.provider.service.AddIntegralService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/23 15:33
 * @describe 多次违纪减分
 */
@Slf4j
@Component
@JobHandler(value = "BreachDeductPointsJob")
public class BreachDeductPointsJob extends IJobHandler implements Serializable {
    @Resource
    private AddIntegralService addIntegralService;

    @Override
    public ReturnT<String> execute(String num) {

        //执行加分方法，num代表执行第前num天的加分数据
        return addIntegralService.BreachDeductPoints();
    }
}
