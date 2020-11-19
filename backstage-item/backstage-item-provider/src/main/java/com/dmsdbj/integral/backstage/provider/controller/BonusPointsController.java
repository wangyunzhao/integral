package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.BonusPointsModel;
import com.dmsdbj.integral.backstage.provider.service.BonusPointsService;
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
 * @describe 可赠积分控制层
 */
@RestController
@Api(tags = {"可赠积分管理"})
@RequestMapping(value = "/bonusPoints")
public class BonusPointsController {

    @Autowired
    private BonusPointsService bonusPointsService;

    /**
     * @param type
     * @param pageNo
     * @param pageSize
     * @description: 按照类型查询可赠积分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:19
     */
    @ApiOperation(value = "分页按类型查询可赠积分")
    @GetMapping(value = "/queryBonusPoints")
    public IntegralResult queryBonusPoints(
            @ApiParam(value = "类型0-组织,1-个人", required = true) @RequestParam("type") Integer type,
            @ApiParam(value = "页数", required = true) @RequestParam("pageNo") Integer pageNo, @ApiParam(value = "每页记录数", required = true) @RequestParam("pageSize") Integer pageSize
    ) {

        if (type == null) {
            return IntegralResult.build(IntegralResult.FAIL, "查询类型不能为空");
        } else {
            return bonusPointsService.queryBonusPoints(type, pageNo, pageSize);
        }
    }


    /**
     * @param content
     * @param type
     * @param pageNo
     * @param pageSize
     * @description: 分页按类型模糊查询可赠积分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:24
     */
    @ApiOperation(value = "分页按类型模糊查询可赠积分")
    @GetMapping(value = "/likeQueryBonusPoints")
    public IntegralResult likeQueryBonusPoints(
            @ApiParam(value = "查询内容", required = true) @RequestParam("content") String content,
            @ApiParam(value = "类型0-组织,1-个人", required = true) @RequestParam("type") Integer type,
            @ApiParam(value = "页数", required = true) @RequestParam("pageNo") Integer pageNo, @ApiParam(value = "每页记录数", required = true) @RequestParam("pageSize") Integer pageSize
    ) {
        if (type == null || content == null) {
            return IntegralResult.build(IntegralResult.FAIL, "查询类型和内容不能为空");
        } else {
            return bonusPointsService.likeQueryBonusPoints(content, type, pageNo, pageSize);
        }
    }


    /**
     * @param bonusPointsModelSet
     * @description: 根据id删除可赠积分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:44
     */
    @ApiOperation(value = "根据id删除可赠积分")
    @PutMapping(value = "/deleteBonusPoints")
    public IntegralResult deleteBonusPoints(
            @ApiParam(value = "需要删除的id", required = true) @RequestBody Set<BonusPointsModel> bonusPointsModelSet
    ) {
        if (bonusPointsModelSet.size() > 0) {
            return bonusPointsService.deleteBonusPoints(bonusPointsModelSet);
        } else {
            return IntegralResult.build(IntegralResult.FAIL, "没有要删除的用户！");
        }
    }

    @ApiOperation(value = "批量添加可赠积分")
    @PostMapping(value = "/addBonusPoints")
    public IntegralResult addBonusPoints(
            @ApiParam(value = "添加的可赠积分", required = true) @RequestBody Set<BonusPointsModel> bonusPointsModelSet
    ) {
        if (bonusPointsModelSet.size() > 0) {
            return bonusPointsService.addBonusPoints(bonusPointsModelSet);
        } else {
            return IntegralResult.build(IntegralResult.FAIL, "没有需要添加的可赠积分！");
        }
    }

    /**
     * @param type
     * @description: 按类型查询全部可赠积分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 13:27
     */
    @ApiOperation(value = "按类型查询全部可赠积分")
    @GetMapping(value = "/queryAllBonusPoints")
    public IntegralResult queryAllBonusPoints(
            @ApiParam(value = "类型0-组织,1-个人", required = true) @RequestParam("type") Integer type
    ) {
        if (type == null) {
            return IntegralResult.build(IntegralResult.FAIL, "查询类型不能为空");
        } else {
            return bonusPointsService.queryAllBonusPoints(type);
        }
    }


    @ApiOperation(value = "更新某条可赠积分")
    @PutMapping(value = "/updateBonusPoints")
    public IntegralResult updateBonusPoints(
            @ApiParam(value = "需要更新的可赠积分", required = true) @RequestBody BonusPointsModel bonusPointsModel
    ) {
        if (bonusPointsModel == null) {
            return IntegralResult.build(IntegralResult.FAIL, "更新内容不能为空");
        }
        if (bonusPointsModel.getReductionAuth() == null || bonusPointsModel.getWeight() == null) {
            return IntegralResult.build(IntegralResult.FAIL, "更新内容不能为空");
        }
        if(bonusPointsModel.getId()==null){
            return IntegralResult.build(IntegralResult.FAIL, "id不能为空");
        }
        if(bonusPointsModel.getOperator()==null){
            return IntegralResult.build(IntegralResult.FAIL, "操作人不能为空");
        }
        else {
            return bonusPointsService.updateBonusPoints(bonusPointsModel);
        }
    }


}
