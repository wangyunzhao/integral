package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.provider.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织结构树
 *
 * @author 崔晓鸿
 * @since 22020年8月13日08:20:17
 */

@Api(tags={"组织结构"})
@RequestMapping("organization")
@RestController
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    /**
     * 查询组织结构
     *
     * @author 崔晓鸿
     * @since 2020年8月13日08:20:38
     */
    @ApiOperation(value = "查询组织结构")
    @GetMapping("selectOrganization")
    public IntegralResult selectOrganization(){
        try {
             Object organization = organizationService.selectOrganization();
            return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",organization);
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"查询失败");
        }
    }
}
