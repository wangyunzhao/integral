package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.DingOrganizationModel;
import com.dmsdbj.integral.backstage.model.OrganizationUserModel;
import com.dmsdbj.integral.backstage.provider.service.OrganizationUsersService;
import com.dmsdbj.itoo.tool.business.ItooResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author:wmy
 * @Date: 2020/8/11 11:46
 * @Description 获取部门列表,然后根据部门列表的id 查询出所有部门下的人员
 */
@Api(tags={"查询组织机构的人员"})
@RequestMapping(value = "/OrganizationUsers")
@RestController
public class OrganizationUsersController {
/**
* @Description 获取部门列表(组织机构),* 然后根据部门列表的id 查询出所有部门下的人员
* @Author  wmy
* @Date   2020/8/11 11:47
* @Param
* @Return
* @Exception
*
*/
    @Resource
    private OrganizationUsersService organizationUsersService;
@GetMapping("/OrganizationUsers/queryOrganizationUsers")
@ApiOperation("查询组织机构的人员")
public IntegralResult<OrganizationUserModel> queryOrganizationUsers(){
    List<OrganizationUserModel> organizationUserModels =organizationUsersService.selectOrganizationUser();
    return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",organizationUserModels);
}
}
