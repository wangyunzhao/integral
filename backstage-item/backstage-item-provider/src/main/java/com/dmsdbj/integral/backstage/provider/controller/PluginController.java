package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.PluginModel;
import com.dmsdbj.integral.backstage.pojo.PluginEntity;
import com.dmsdbj.integral.backstage.provider.service.PluginService;
import com.dmsdbj.itoo.sso.utils.UserUtil;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 插件管理操作
 * @return
 * @author 梁佳宝
 * @since 2020年8月12日13点19分
 */
@Api(tags = {"插件模块"})
@RequestMapping(value = "/pluginController")
@RestController
public class PluginController {
    @Resource
    private PluginService pluginService;
    /**
     * 根据绑定项目的id，判断数据库中是否已存在该数据命名
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日13点19分
     */
    @ApiOperation(value = "根据绑定项目的id，判断数据库中是否已存在该数据命名")
    @GetMapping(value = {"/queryPluginId"})
    public IntegralResult queryPluginId(@RequestParam("项目id") String projectId) {
        return pluginService.queryPluginId(projectId);
    }

    /**
     * 查询数据库中所有的未删除插件
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日17点55分
     */
    @ApiOperation(value = "查询数据库中所有的未删除插件")
    @GetMapping(value = {"/queryAllPlugin"})
    public IntegralResult queryAllPlugin() {
        return pluginService.queryAllPlugin();
    }
    /**
     * 将从前端返回的插件相关数据添加到数据库中
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日15点30分
     */
    @ApiOperation(value = "插件相关数据添加到数据库中")
    @PostMapping(value = {"/addPlugin"})
    public IntegralResult addPlugin(@RequestBody PluginModel pluginModel){
        return pluginService.addPlugin(pluginModel);
    }
    /**
     *插件相关数据更新数据库中相关数据
     * @param pluginModel
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日16点25分
     */
    @ApiOperation(value = "插件相关数据更新数据库中相关数据")
    @PostMapping(value = {"/updatePlugin"})
    public IntegralResult updatePlugin(@RequestBody PluginModel pluginModel){
        return pluginService.updatePlugin(pluginModel);
    }
    /**
     *根据从前端返回的插件id删除数据库中数据
     * @param projectId
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日16点25分
     */
    @ApiOperation(value = "根据从前端返回的插件id删除数据库中数据")
    @GetMapping(value = {"/updateDeletePlugin"})
    public IntegralResult updateDeletePlugin(String projectId){
        return pluginService.updateDeletePlugin(projectId);
    }
}
