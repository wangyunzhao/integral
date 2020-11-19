package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.ProjectModel;
import com.dmsdbj.integral.backstage.provider.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 项目管理
 * @Author 冯瑞姣
 * @Date: 2020-08-05 16:37
 * @Version: 1.0
 **/

@Api(tags={"项目管理接口"})
@RequestMapping(value = "project")
@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "添加项目")
    @PostMapping(value = "addProject")
    public IntegralResult addProject(@RequestBody ProjectModel projectModel) {
        return projectService.addProject(projectModel);
    }

    @ApiOperation(value = "添加项目判重")
    @GetMapping(value = "projectAddIsRepeat")
    public IntegralResult projectAddIsRepeat(@RequestParam String name, String englishName){
        return projectService.projectAddIsRepeat(name,englishName);
    }

    @ApiOperation(value = "查询项目添加记录")
    @GetMapping(value = "projectAddRecord")
    public IntegralResult projectAddRecord(@RequestParam String pageNo, String pageSize){
        return projectService.projectAddRecord(pageNo,pageSize);
    }

    @ApiOperation(value = "模糊搜索项目记录")
    @GetMapping(value = "searchProjectRecord")
    public IntegralResult searchProjectRecord(@RequestParam String content,String pageNo,String pageSize){
        return projectService.searchProjectRecord(content,pageNo,pageSize);
    }

    @ApiOperation(value = "删除项目")
    @PostMapping(value = "deleteProjectRecord")
    public IntegralResult deleteProjectRecord(@RequestBody List<ProjectModel> projectModel){
        if(projectModel.size() > 0){
            return projectService.deleteProjectRecord(projectModel);
        } else {
            return IntegralResult.build(IntegralResult.FAIL, "没有要删除的项目记录！");
        }
    }

    @ApiOperation(value = "编辑项目（普通项目组长）")
    @PostMapping(value = "editProject")
    public IntegralResult editProject(@RequestParam String id, String name){
        return projectService.editProject(id, name);
    }

    @ApiOperation(value = "编辑项目（有权限）")
    @PostMapping(value = "editProjectPower")
    public IntegralResult editProjectPower(@RequestParam String id, String name, String exchangeRate){
        return projectService.editProjectPower(id, name, exchangeRate);
    }

    @ApiOperation(value = "编辑项目判重")
    @GetMapping(value = "projectEditIsRepeat")
    public IntegralResult projectEditIsRepeat(@RequestParam String name,String id){
        return projectService.projectEditIsRepeat(name,id);
    }

    @ApiOperation(value = "根据id查询项目中文名称和英文名称")
    @GetMapping(value = "projectName")
    public IntegralResult projectName(@RequestParam String id){
        return projectService.projectName(id);
    }

    @ApiOperation(value = "查询所有绑定项目称英文名称和项目Id")
    @GetMapping(value = "projectNameId")
    public IntegralResult projectNameId(){
        return projectService.projectNameId();
    }
}
