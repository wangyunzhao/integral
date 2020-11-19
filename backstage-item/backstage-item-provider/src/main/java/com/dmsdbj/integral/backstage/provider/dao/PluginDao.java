package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.pojo.PluginEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 插件管理操作
 * @return
 * @author 梁佳宝
 * @since 2020年8月12日13点19分
 */
@Repository
public interface PluginDao {
    List<PluginEntity> queryProjectId(String projectId);
    List<PluginEntity> queryAllPlugin();
    Boolean addPlugin(PluginEntity pluginEntity);
    Boolean updatePlugin(PluginEntity pluginEntity);
    Boolean updateDeletePlugin(String projectId);

}
