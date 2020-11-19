package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.provider.service.ProjectApproveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/8/4
 * Time: 19:52
 * Description:${DESCRIPTION}
 */
@Api(tags="审批管理接口")
@RequestMapping(value = "/approve")
@RestController
public class ProjectApproveController {

    @Autowired
    private ProjectApproveService projectApproveService;

    @ApiOperation(value = "查询待审批项目")
    @GetMapping(value = "projectApproveRecord")
    public IntegralResult projectApproveRecord(@RequestParam String pageNo,String pageSize){
        return projectApproveService.queryProjectApprove(pageNo,pageSize);
    }

    @ApiOperation(value = "模糊查询待审批项目")
    @GetMapping(value = "searchRecord")
    public IntegralResult searchRecord(@RequestParam String content,String pageNo,String pageSize){
        return projectApproveService.queryProjectApproveLike(pageNo,pageSize,content);
    }

    @ApiOperation(value = "通过审批")
    @PostMapping(value = "agreeApproved")
    public IntegralResult agreeApproved(@RequestParam String id,String exchangeRate){
        try {
        return projectApproveService.agreeApproved(id,exchangeRate);
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"操作失败");
        }

    }

    @ApiOperation(value = "拒绝审批")
    @PostMapping(value = "rejectApproved")
    public IntegralResult rejectApproved(@RequestParam String id,String remark){
        try {
            return projectApproveService.rejectApproved(id,remark);
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"操作失败");
        }
    }
}
