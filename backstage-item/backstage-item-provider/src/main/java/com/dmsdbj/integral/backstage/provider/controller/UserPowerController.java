package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.TemplateUtilModel;
import com.dmsdbj.integral.backstage.model.UserPowerModel;
import com.dmsdbj.integral.backstage.provider.service.UserPowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 功能描述: 根据用户dingID判断是否有用户权限,最后返回用户的姓名，code，用户姓名，以及用户角色（项目组长，普通人员，积分组人员）
 * @Author: keer
 * @Date: 2020/8/8 18:07
 * @Param:
 * @Return:
 */
@Api(tags = "根据钉钉id获取用户信息和角色-登录公共接口")
@RequestMapping(value = "/getUserPowerByDingId")
@RestController
public class UserPowerController {

    @Autowired
    private UserPowerService userPowerService;

    @ApiOperation(value = "根据钉钉id获取用户信息和角色-公共接口")
    @GetMapping(value = "/getUserLoginPower")
    public UserPowerModel getUserPower(String dingId){
        return userPowerService.hasPower(dingId);
    }
}
