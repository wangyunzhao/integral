package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.PluginBindsUserModel;
import com.dmsdbj.integral.backstage.pojo.CustomGroupEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomGroupDao {
    boolean addCustomGroup(List<CustomGroupEntity> customGroupEntities);
    boolean updateDeleteCustomGroup(String groupName);
//    项目组处重
    List<String> selectGroupName();
    List<PluginBindsUserModel> selectGroupByGroupName(String groupName);
}
