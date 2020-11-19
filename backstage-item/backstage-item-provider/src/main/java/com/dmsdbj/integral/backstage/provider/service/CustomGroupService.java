package com.dmsdbj.integral.backstage.provider.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.CustomGroupModel;
import com.dmsdbj.integral.backstage.model.GroupModel;
import com.dmsdbj.integral.backstage.pojo.CustomGroupEntity;
import com.dmsdbj.integral.backstage.provider.dao.CustomGroupDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomGroupService {
    @Resource
    private CustomGroupDao customGroupDao;

    /**
     * 将从前端返回的自定义分组相关数据添加到数据库中
     * @param customGroupModels
     * @return
     */
    public IntegralResult addCustomGroup(List<CustomGroupModel> customGroupModels){

        List<CustomGroupEntity> customGroupEntities = customGroupModels.stream().map(customGroupModel -> {
            CustomGroupEntity customGroupEntity = new CustomGroupEntity();
            customGroupEntity.setDingId(customGroupModel.getDingId());
            customGroupEntity.setGroupName(customGroupModel.getGroupName());
            customGroupEntity.setName(customGroupModel.getName());
            customGroupEntity.setId(IdWorker.getIdStr());
            return customGroupEntity;
        }).collect(Collectors.toList());

        boolean flag = customGroupDao.addCustomGroup(customGroupEntities);
        if (flag){
            return IntegralResult.build(IntegralResult.SUCCESS,"自定义分组添加成功");
        }
        return IntegralResult.build(IntegralResult.FAIL,"自定义分组添加失败");
    }

    /**
     *返回的分组名和创建者姓名修改数据库中is_delete字段
     * @param groupName
     * @return
     */
    public IntegralResult updateDeleteCustomGroup(String groupName){
        boolean flag = customGroupDao.updateDeleteCustomGroup(groupName);
        if (flag){
            return IntegralResult.build(IntegralResult.SUCCESS,"自定义分组删除成功");
        }
        return IntegralResult.build(IntegralResult.FAIL,"自定义分组删除失败");
    }

    /**
     * 自定义分组查询
     * @return
     */
    public IntegralResult<GroupModel> qureyCustomGroup() {
        List<String> groupNames = customGroupDao.selectGroupName();
        List<GroupModel> groupModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupNames)){
            groupNames.stream().forEach(groupName -> {
                GroupModel groupModel = new GroupModel();
                groupModel.setName(groupName);
                groupModel.setUserList(customGroupDao.selectGroupByGroupName(groupName));
                groupModels.add(groupModel);
            });
            return IntegralResult.build(IntegralResult.SUCCESS,"自定义分组查询成功",groupModels);
        }
        return IntegralResult.build(IntegralResult.FAIL,"自定义分组查询失败");
    }
}
