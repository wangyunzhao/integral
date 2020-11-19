package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dingtalk.item.pojo.AlarmDetailEntity;
import com.dingtalk.item.pojo.AlarmHandleEntity;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.dingtalk.model.AlarmInfoModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.service.AlarmInfoService;
import com.tfjybj.framework.log.LogCollectManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author 王云召
 * @since 2020年6月24日09:35:42
 * 查询处理告警信息
 */
@Api(tags = {"告警信息"})
@RequestMapping(value = "/alarmInfo")
@RestController
public class AlarmInfoController {

    @Resource
    private AlarmInfoService alarmInfoService;

    private static final String INDEX = "alarmInfoService";
    /**
     * 查询告警信息返回前端给纪委处理
     *
     * @param dingId
     * @return
     * @author 王云召
     * @sinse 2020年6月4日10:09:20
     */
    @ApiOperation(value = "查询告警信息返回前端给纪委处理")
    @GetMapping(value = {"queryAlarmInfo"})
    public IntegralResult queryAlarmInfo(@ApiParam(value = "积分id",required = false)@RequestParam("dingId") String dingId) {

        LogCollectManager.common(MessageFormat.format(Constants.BEGIN+"进入查询告警信息返回前端给纪委处理",""),Constants.DING_INDEX+INDEX);
        //根据钉钉id查询userid
        String userId = alarmInfoService.getUserIdByDingId(dingId);

        //根据用户的userid查询用户所在的组织id（所在期数）
        String orgazitionId = alarmInfoService.queryOrgazitionIdByUserId(userId);
        if (orgazitionId.isEmpty()){
            LogCollectManager.common(MessageFormat.format(Constants.ERR+"无组织id",""),Constants.DING_INDEX+INDEX);
            return IntegralResult.build(IntegralResult.FAIL, "无组织id");
        }

        //根据用户的orgazitionId查询所在组织的告警消息，只能看自己所在组织的告警消息
        List<AlarmInfoModel> alarmInfos = alarmInfoService.queryAlarmInfo(orgazitionId);
        //判断是否有告警消息
        if(alarmInfos.isEmpty()){
            LogCollectManager.common(MessageFormat.format(Constants.ERR+"进入查询告警信息返回前端给纪委处理--无告警消息",""),Constants.DING_INDEX+INDEX);
            return IntegralResult.build(IntegralResult.FAIL, "无告警信息");
        }
        LogCollectManager.common(MessageFormat.format(Constants.END+"查询告警信息返回前端给纪委处理--完成",""),Constants.DING_INDEX+INDEX);
        return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", alarmInfos);
    }


    /**
     * 纪委处理异常打卡的结果更新到数据库
     * @param alarmHandleEntity
     * @return
     * @author 王云召
     * @sinse 2020年6月4日10:09:20
     */
    @ApiOperation(value = "纪委处理异常打卡的结果更新到数据库")
    @PostMapping(value = {"handleAlarmInfoByOperator"})
    public IntegralResult handleAlarmInfoByOperator(
            @ApiParam(value = "违纪人员是否联系上")@RequestParam String isSave,
            @ApiParam(value = "告警处理结果实体")@RequestBody AlarmHandleEntity alarmHandleEntity) {
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN+"进入纪委处理异常打卡的结果更新到数据库",""),Constants.DING_INDEX+INDEX);
            int isUpdate = alarmInfoService.handleAlarmInfoByOperator(isSave,alarmHandleEntity);
            if (isUpdate==Constants.ZERO){
                return IntegralResult.build(IntegralResult.FAIL, "更新失败，没有数据插入！");
            }
        return IntegralResult.build(IntegralResult.SUCCESS, "更新成功");
    }
    /**
     * 插入未销卡人员信息
     *
     * @author fjx
     * @param alarmDetailEntity
     * @return
     *
     */
    @ApiOperation(value = "插入未销卡人员信息")
    @PostMapping(value = {"insertAlarmInfo"})
    public IntegralResult insertAlarmInfo(@Param("alarmDetailEntity") List<AlarmDetailEntity> alarmDetailEntity){
       if(CollectionUtils.isEmpty(alarmDetailEntity)){
           return IntegralResult.build(IntegralResult.FAIL, "人员信息为空");
       }
        alarmInfoService.insertAlarmInfo(alarmDetailEntity);
        return IntegralResult.build(IntegralResult.SUCCESS, "插入成功");
    }
}
