package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dingtalk.item.pojo.AlarmHandleEntity;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleModel;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleResultModel;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleSubmitModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.service.AlarmHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 报警结果处理
 *
 * @author 崔晓鸿
 * @since 2020年6月22日10:37:26
 */

@Api(tags = {"报警结果处理"})
@RestController
@RequestMapping("alarmHandle")
public class AlarmHandleController {

    @Autowired
    private AlarmHandleService alarmHandleService;


    /**
     * 查询所有人的异常报警结果
     *
     * @return
     * @author 崔晓鸿
     * @since 2020年6月22日20:06:20
     */
    @ApiOperation(value = "查看所有人的报警处理结果")
    @GetMapping("selectAlarmHandle")
    public IntegralResult selectAlarmHandle(@ApiParam(value = "钉钉id",required = true) @RequestParam String dingId){
        Object resultMap=new Object();
        try {
            resultMap = alarmHandleService.selectAlarmHandle(dingId);
            return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",resultMap);
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"查询失败",resultMap);
        }
    }

    /**
     * 一键已阅
     *根据报警id和alarmId插入已联系上的默认处理结果
     * @return
     * @author 王云召
     * @since 2020年6月23日09:03:49
     */
    @ApiOperation(value = "一键已阅")
    @PostMapping("readWithOneClickByDingId")
    public IntegralResult readWithOneClickByDingId(@ApiParam(value = "钉钉id",required = true) @RequestParam String dingId,
                                                     @ApiParam(value = "alarm集合",required = true) @RequestBody List<AlarmHandleEntity> alarmHandleEntities)
    {
        int isSuccess = alarmHandleService.readWithOneClickByDingId(dingId,alarmHandleEntities);
        if (isSuccess== Constants.ZERO){
            return IntegralResult.build(IntegralResult.FAIL,"一键已阅失败！");
        }
        return IntegralResult.build(IntegralResult.SUCCESS,"一键已阅成功！");
    }
    /**
     * 提交
     *
     * @return
     * @author 王梦瑶
     * @since 2020年6月23日09:03:49
     */
    @ApiOperation(value = "提交")
    @PostMapping("SubmitSuperior")
    public  IntegralResult submitSuperior(@ApiParam(value ="alarm集合")@RequestBody AlarmHandleSubmitModel alarmHandleSubmitModel)
    {
        int isSuccess=alarmHandleService.submitSuperior(alarmHandleSubmitModel);
        if (isSuccess==Constants.ZERO){
            return  IntegralResult.build(IntegralResult.FAIL,"提交失败!");
        }
        return  IntegralResult.build(IntegralResult.SUCCESS,"提交成功！");
    }

    /**
     * 根据alarmID更新isSafe
     * @param alarmHandleSubmitModel
     * @return
     */
    @ApiOperation(value = "根据alarmID更新isSafe")
    @PostMapping("updateIsSafeByAlarmId")
    public IntegralResult updateIsSafeByAlarmId(@RequestBody AlarmHandleSubmitModel alarmHandleSubmitModel){
        //判断
        if(alarmHandleSubmitModel.getAlarmId().isEmpty()){
            return IntegralResult.build(IntegralResult.FAIL,"更新失败，id不能为空");
        }else {
            int isUpdateSafe=alarmHandleService.updateIsSafeByAlarmId(alarmHandleSubmitModel.getAlarmId());
            return IntegralResult.build(IntegralResult.SUCCESS,"更新成功!");
        }

    }

    /**
     * 根据dingId来查询type类型
     * @param dingId
     * @return
     */
    @ApiOperation(value = "根据dingId来查询type类型")
    @GetMapping("selectUserType")
    public IntegralResult selectUserType(@ApiParam(value = "钉钉id", required = true) @RequestParam String dingId ){
        String type = alarmHandleService.selectUserType(dingId);
        if (!type.isEmpty()){
            return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",type);
        }else {
            return IntegralResult.build(IntegralResult.FAIL,"查询失败",type);
        }
    }
}
