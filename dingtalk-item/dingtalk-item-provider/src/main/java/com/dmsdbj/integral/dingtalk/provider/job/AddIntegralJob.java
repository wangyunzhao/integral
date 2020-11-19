package com.dmsdbj.integral.dingtalk.provider.job;

import com.dmsdbj.integral.dingtalk.model.AddIntegralDataModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.service.AddIntegralService;
import com.tfjybj.framework.log.LogCollectManager;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王云召
 * @since 2020年6月24日09:35:42
 * 加分job
 */
@Slf4j
@Component
@JobHandler(value = "AddIntegralJob")
public class AddIntegralJob extends IJobHandler implements Serializable {
    @Resource
    private AddIntegralService addIntegralService;

    /**
     * 钉钉考勤加分job
     *
     * @param num
     * @return
     * @author 王云召
     * @sinse 2020年6月4日10:09:20
     */
    @Override
    public ReturnT<String> execute(String num) {

        try {
            //执行加分方法，num代表执行第前num天的加分数据
            return addIntegralService.addIntegral(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnT.FAIL;
    }


}
