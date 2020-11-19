package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.PluginBindsUserModel;
import com.dmsdbj.integral.backstage.pojo.PluginUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PluginBindsUserDao {
    List<PluginBindsUserModel> selectPluginBindsUser(String pluginId);
    Boolean insertPluginBindsUser(@Param("pluginUserEntities") List<PluginUserEntity> pluginUserEntities);
    Boolean deletePluginBindsUser(@Param("pluginUserEntities") List<PluginUserEntity> pluginUserEntities);
}
