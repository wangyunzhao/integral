package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.SpecificPointsModel;
import com.dmsdbj.integral.backstage.provider.service.SpecificPointsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/12 10:24
 * @describe 特定人员加分控制层
 */

@RestController
@Api(tags={"特定人员加分管理"})
@RequestMapping(value = "/specificPoints")
public class SpecificPointsController {

    @Autowired
    private SpecificPointsService specificPointsService;

    /**
     * @param type
     * @param pageNo
     * @param pageSize
     * @description: 按照类型查询特定人员加分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:19
     */
    @ApiOperation(value = "分页按类型查询特定人员加分")
    @GetMapping(value = "/querySpecificPoints")
    public IntegralResult querySpecificPoints(
            @ApiParam(value = "类型0-组织,1-个人", required = true) @RequestParam("type") Integer type,
            @ApiParam(value = "页数", required = true) @RequestParam("pageNo") Integer pageNo, @ApiParam(value = "每页记录数", required = true) @RequestParam("pageSize") Integer pageSize
    ) {

        if (type == null) {
            return IntegralResult.build(IntegralResult.FAIL, "查询类型不能为空");
        } else {
            return specificPointsService.querySpecificPoints(type, pageNo, pageSize);
        }
    }


    /**
     * @param content
     * @param type
     * @param pageNo
     * @param pageSize
     * @description: 分页按类型模糊查询特定人员加分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:24
     */
    @ApiOperation(value = "分页按类型模糊查询特定人员加分")
    @GetMapping(value = "/likeQuerySpecificPoints")
    public IntegralResult likeQuerySpecificPoints(
            @ApiParam(value = "查询内容", required = true) @RequestParam("content") String content,
            @ApiParam(value = "类型0-组织,1-个人", required = true) @RequestParam("type") Integer type,
            @ApiParam(value = "页数", required = true) @RequestParam("pageNo") Integer pageNo, @ApiParam(value = "每页记录数", required = true) @RequestParam("pageSize") Integer pageSize
    ) {
        if (type == null || content == null) {
            return IntegralResult.build(IntegralResult.FAIL, "查询类型和内容不能为空");
        } else {
            return specificPointsService.likeQuerySpecificPoints(content, type, pageNo, pageSize);
        }
    }


    /**
     * @param specificPointsModelSet
     * @description: 根据id删除特定人员加分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:44
     */
    @ApiOperation(value = "根据id删除特定人员加分")
    @PutMapping(value = "/deleteSpecificPoints")
    public IntegralResult deleteSpecificPoints(
            @ApiParam(value = "需要删除的id", required = true) @RequestBody Set<SpecificPointsModel> specificPointsModelSet
    ) {
        if (specificPointsModelSet.size() > 0) {
            return specificPointsService.deleteSpecificPoints(specificPointsModelSet);
        } else {
            return IntegralResult.build(IntegralResult.FAIL, "没有要删除的用户！");
        }
    }

    @ApiOperation(value = "批量添加特定人员加分")
    @PostMapping(value = "/addSpecificPoints")
    public IntegralResult addSpecificPoints(
            @ApiParam(value = "添加的特定人员加分", required = true) @RequestBody Set<SpecificPointsModel> SpecificPointsModelSet
    ) {
        if (SpecificPointsModelSet.size() > 0) {
            return specificPointsService.addSpecificPoints(SpecificPointsModelSet);
        } else {
            return IntegralResult.build(IntegralResult.FAIL, "没有需要添加的特定人员加分！");
        }
    }

    /**
     * @param type
     * @description: 按类型全部特定人员加分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 13:27
     */
    @ApiOperation(value = "按类型查询全部特定人员加分")
    @GetMapping(value = "/queryAllSpecificPoints")
    public IntegralResult queryAllSpecificPoints(
            @ApiParam(value = "类型0-组织,1-个人", required = true) @RequestParam("type") Integer type
    ) {
        if (type == null) {
            return IntegralResult.build(IntegralResult.FAIL, "查询类型不能为空");
        } else {
            return specificPointsService.queryAllSpecificPoints(type);
        }
    }


    @ApiOperation(value = "更新某条特定人员加分")
    @PutMapping(value = "/updateSpecificPoints")
    public IntegralResult updateSpecificPoints(
            @ApiParam(value = "需要更新的特定人员加分", required = true) @RequestBody SpecificPointsModel SpecificPointsModel
    ) {
        if (SpecificPointsModel == null) {
            return IntegralResult.build(IntegralResult.FAIL, "更新内容不能为空");
        }
        if (SpecificPointsModel.getIntegral() == null ) {
            return IntegralResult.build(IntegralResult.FAIL, "更新内容不能为空");
        }
        if(SpecificPointsModel.getId()==null){
            return IntegralResult.build(IntegralResult.FAIL, "id不能为空");
        }
        if(SpecificPointsModel.getOperator()==null){
            return IntegralResult.build(IntegralResult.FAIL, "operator不能为空");
        }
        else {
            return specificPointsService.updateSpecificPoints(SpecificPointsModel);
        }
    }

}