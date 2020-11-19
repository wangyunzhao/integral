package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dingtalk.item.pojo.ApproveDetailEntity;
import com.dingtalk.item.pojo.DisciplineUserEntity;
import com.dmsdbj.integral.dingtalk.provider.service.DisciplineUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * DisciplineUserController
 * 1.调用钉钉获取部门列表获取部门（纪律委员会）id和name
 * 2.根据部门id获得当月当值纪委
 * @author 王梦瑶
 * @version 0.0.1
 * @since 2020年6月2日08:18:11
 */
@Api(tags = {"获取当月当值纪委"})
@RequestMapping(value = "/disciplineUser")
@RestController
public class DisciplineUserController {
    @Resource
    private  DisciplineUserService disciplineUserService;
/**
 * 1.调用钉钉获取部门列表获取部门（纪律委员会）id和name
 * 2.根据部门id获得当月当值纪委
 *
 * @return
 * @author
 * @since 2019年9月12日17:11:46
 */

    @ApiOperation(value = "调用钉钉接口查询当月当值纪委")
    @GetMapping(value = {"queryDisciplineUser"})
    public List<DisciplineUserEntity> queryDisciplineUser(){
        List<DisciplineUserEntity> disciplineUserEntities = disciplineUserService.queryDisciplineUser();

        return disciplineUserEntities;
    }
}
