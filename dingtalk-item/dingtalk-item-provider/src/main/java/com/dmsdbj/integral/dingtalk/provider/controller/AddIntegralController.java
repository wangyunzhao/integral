package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.dingtalk.model.AddIntegralDataModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.service.AddIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户级别表操作
 *
 * @author 王云召
 * @since 2020年6月24日09:35:42
 *  * 此接口不用做外部调用，钉钉考勤加分是xxljob定时调用
 *  * <p>
 *  * 1.计算加分分值
 *  * 2.查询需要的考勤打卡加分数据
 *  * 3.查询需要的考勤纪委处理考勤告警信息加分数据
 *  * 4.调用加分接口
 */

@Api(tags = {"钉钉考勤加分"})
@RequestMapping(value = "/addIntegral")
@RestController
public class AddIntegralController {

    @Resource
    private AddIntegralService addIntegralService;

    private static LocalDate localDate;

    /**
     * 钉钉考勤加分
     *
     * @param num
     * @return
     * @author 王云召
     * @sinse 2020年6月4日10:09:20
     */
    @ApiOperation(value = "钉钉考勤加分")
    @GetMapping(value = {"queryAlarmInfo"})
    public IntegralResult addIntegral(@ApiParam(value = "前几天", required = false) @RequestParam("num") String num) {
        addIntegralService.addIntegral(num);
        return IntegralResult.build(IntegralResult.SUCCESS, "钉钉考勤加分成功");
    }


//    @ApiOperation(value = "测试加分")
//    @GetMapping(value = {"queryHandleAlarmIntegralData"})
//    public IntegralResult queryHandleAlarmIntegralData(){
//        List<AddIntegralDataModel> addIntegralDataModels = addIntegralService.queryHandleAlarmIntegralData();
//        return IntegralResult.build(IntegralResult.SUCCESS, "钉钉考勤加分成功");
//    }

}



