package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.CustomGroupModel;
import com.dmsdbj.integral.backstage.pojo.CustomGroupEntity;
import com.dmsdbj.integral.backstage.provider.service.CustomGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = {"自定义分组管理"})
@RequestMapping(value = "/customGroupController")
@RestController
public class CustomGroupController {
    @Resource
    private CustomGroupService customGroupService;

    /**
     * 将从前端返回的插件相关数据添加到数据库中
     * @param customGroupModels
     * @return
     */
    @ApiOperation(value = "将从前端返回的插件相关数据添加到数据库中")
    @PostMapping("/addCustomGroup")
    public IntegralResult addCustomGroup(@RequestBody List<CustomGroupModel> customGroupModels){
        return customGroupService.addCustomGroup(customGroupModels);
    }

    /**
     * 根据从前端返回的分组名和创建者姓名修改数据库中is_delete字段
     * @param groupName
     * @return
     */
    @ApiOperation(value = "删除分组")
    @GetMapping("/updateDeleteCustomGroup")
    public IntegralResult updateDeleteCustomGroup(@RequestParam String groupName) {
        return customGroupService.updateDeleteCustomGroup(groupName);
    }

    /**
     * 查询分组
     * @return
     */
    @ApiOperation(value = "查询分组")
    @GetMapping("/qureyCustomGroup")
    public IntegralResult qureyCustomGroup(){
        return customGroupService.qureyCustomGroup();
    }
}
