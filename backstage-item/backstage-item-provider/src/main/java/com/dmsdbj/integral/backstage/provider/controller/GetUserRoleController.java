package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.integral.backstage.model.UserCodeModel;
import com.dmsdbj.integral.backstage.model.UserPowerModel;
import com.dmsdbj.integral.backstage.model.UserRoleModel;
import com.dmsdbj.integral.backstage.provider.service.GetUserByCodeService;
import com.dmsdbj.integral.backstage.provider.service.GetUserIdByUnionid;
import com.dmsdbj.integral.backstage.provider.service.GetUserRoleService;
import com.dmsdbj.integral.backstage.provider.service.UserPowerService;
import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Api(tags = "钉钉扫码登录接口")
@RequestMapping(value = "/getUserRole")
@RestController
public class GetUserRoleController {

    @Autowired
    private GetUserRoleService getUserRoleService;
    @Autowired
    private GetUserByCodeService getUserByCodeService;
    @Autowired
    private GetUserIdByUnionid getUserIdByUnionid;

    @ApiOperation(value = "根据前端返回的Code,最终获取到用户的信息以及角色")
    @PostMapping(value = "/getUserRoleByCode")
    public UserRoleModel getUserByCode(HttpServletResponse response, @RequestBody UserCodeModel tmpAuthCode) throws ApiException {
        return getUserRoleService.getUserRole(tmpAuthCode);
    }

    @ApiOperation(value = "根据前端返回的Code,调用钉钉接口获得Unionid")
    @GetMapping(value = "/getUnionidByCode")
    public String getUnionidByCode(String code) throws ApiException {
        return getUserByCodeService.getUserByCode(code);
    }

    @ApiOperation(value = "根据UnionID调用钉钉接口获取UserId")
    @GetMapping(value = "/getUserIdByUnionId")
    public String getUserIdByUnionid(String code) throws ApiException {
        return getUserIdByUnionid.getUserIdByUnionid(code);
    }
}
