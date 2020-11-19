package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.provider.service.PluginBindsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 插件绑定人员操作
 * @return
 * @author 梁佳宝
 * @since 2020年8月12日19点00分
 */
@Api(tags = {"插件绑定人员模块"})
@RequestMapping(value = "/pluginBindsUserController")
@RestController
public class PluginBindsUserController {
    @Resource
    private PluginBindsUserService pluginBindsUserService;

    /**
     * 根据插件id查询数据库中人员数据
     * @param pluginId
     * @return
     */
    @ApiOperation(value = "根据插件id查询数据库中人员数据")
    @GetMapping("/selectPluginBindsUser")
    public IntegralResult selectPluginBindsUser(@RequestParam String pluginId){
        return pluginBindsUserService.selectPluginBindsUser(pluginId);
    }
}
