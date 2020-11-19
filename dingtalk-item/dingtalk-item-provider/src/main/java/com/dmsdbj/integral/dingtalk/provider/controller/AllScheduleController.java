package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dingtalk.item.pojo.ScheduleDetailEntity;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.dingtalk.provider.service.AllScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * 查询企业排班详情
 * @author 梁佳宝
 * @since 2020年6月4日11点56分
 */
@Api(tags = {"公司所有人每天每人上下班打卡的排班详情接口"})
@RequestMapping(value = "/AllSchedule")
@RestController
public class AllScheduleController {
    @Autowired
    private AllScheduleService allScheduleService;

    /**
     * 根据数字，查询前面某一天的企业排班数据
     * @param day
     * @return
     */
    @ApiOperation(value = "查询企业某天排班信息")
    @PostMapping(value = {"/getAllSchedule/{day}"})
    public IntegralResult getAllSchedule (@ApiParam(value = "获取某天的排班详情（当天的传0即可）",required = true) @PathVariable int day) throws ParseException {
        List<ScheduleDetailEntity> allSchedulelist = allScheduleService.getAllSchedule(day);
        return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", allSchedulelist);
    }
}
