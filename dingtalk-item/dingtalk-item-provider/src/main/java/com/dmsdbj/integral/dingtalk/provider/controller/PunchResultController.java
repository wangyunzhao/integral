package com.dmsdbj.integral.dingtalk.provider.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.item.pojo.PunchResultEntity;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.dingtalk.provider.service.PunchResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:wmy+keer
 * @Date: 2020/7/22 11:25
 * @Description 调用钉钉接口查询打卡结果
 */
@Api(tags = {"查询打卡结果"})
@RequestMapping(value = "/punchResult")
@RestController
public class PunchResultController {

    @Resource
    private PunchResultService punchResultService;


    /**
     * 功能描述: 查询某次的排班打卡结果（给别人调用）
     * @Author: keer
     * @Date: 2020/6/8 09:44
     * @Return: java.util.List<com.dingtalk.item.pojo.PunchResultEntity>
     */
    @ApiOperation(value = "查询排班打卡结果--查询所有人某次的打卡结果")
    @GetMapping(value = {"/getSchedulePunchResult"})
    public List<PunchResultEntity> getPunchResult(){
        List<PunchResultEntity> punchResultEntityList = punchResultService.getPunchResult();
        return punchResultEntityList;
    }

    /**
     * 根据传入的打卡时间，和人员调用钉钉获取打卡结果查询打卡情况
     *
     * @return
     * @author王梦瑶
     * @since 2020年6月7日17:11:46
     */
    @ApiOperation(value = "根据传入的打卡时间，和人员查询打卡情况")
    @GetMapping(value = {"queryRcordResult"})
    public List<PunchResultEntity> queryRcordResult(String workDateFrom, String workDateTo,  String[] userIdList) throws ParseException {
        return punchResultService.queryRecordResult(workDateFrom, workDateTo, userIdList);
    }
}
