package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.provider.service.UpdateBonusPointsService;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 16:29 2020/8/12
 */
@RestController
@Api(tags = {"可赠积分，以及权重和减分权限配置"})
@RequestMapping(value = "/updateBonusPoints")
public class UpdateBonusPointsController {

    @Autowired
    private UpdateBonusPointsService updateBonusPointsService;

    @GetMapping(value = "/updateBonusPoints")
    public IntegralResult updateBonusPoints(){
        int result=0;
        try {
            result= updateBonusPointsService.updateBonusPoints();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(result<0){
            return IntegralResult.build(IntegralResult.FAIL,"更新失败");
        }else{
            return IntegralResult.build(IntegralResult.SUCCESS,result+"条数据更新成功！");
        }
    }

    @GetMapping(value = "/setWeightAndIntegral")
    public IntegralResult setWeightAndIntegral(){
        int result=0;
        try {
            result=updateBonusPointsService.setWeightAndIntegral();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(result<0){
            return IntegralResult.build(IntegralResult.FAIL,"更新失败");
        }else{
            return IntegralResult.build(IntegralResult.SUCCESS,result+"条数据更新成功！");
        }
    }

}
