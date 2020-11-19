package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dingtalk.item.pojo.TypeUserEntity;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.dingtalk.provider.service.TypeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户级别表操作
 *
 * @author 崔晓鸿
 * @since 2020年6月24日09:35:42
 */
@Api(tags = "用户级别表操作")
@RestController
@RequestMapping("typeUser")
public class TypeUserController {
    @Autowired
    private TypeUserService typeUserService;

    /**
     * 添加用户级别
     *
     * @author 崔晓鸿
     * @since 2020年6月24日10:24:42
     */
    @ApiOperation(value = "添加用户级别")
    @PostMapping("insertTypeUser")
    public IntegralResult insertTypeUser(@RequestBody List<TypeUserEntity> typeUserEntities){
        boolean insertFlag = typeUserService.insertTypeUser(typeUserEntities);
        if(insertFlag){
            return IntegralResult.build(IntegralResult.SUCCESS,"插入成功");
        }else {
            return IntegralResult.build(IntegralResult.FAIL,"插入失败，插入用户已经存在");
        }
    }

    /**
     * 更新用户级别
     *
     * @author 崔晓鸿
     * @since 2020年6月24日10:24:42
     */
    @ApiOperation(value = "更新用户级别")
    @PostMapping("updateTypeUser")
    public IntegralResult updateTypeUser(@RequestBody TypeUserEntity typeUserEntity){
        boolean updateFlag=typeUserService.updateTypeUser(typeUserEntity);
        if(updateFlag){
            return IntegralResult.build(IntegralResult.SUCCESS,"更新成功");
        }else {
            return IntegralResult.build(IntegralResult.SUCCESS,"更新失败，更新的用户不存在");
        }

    }
}
