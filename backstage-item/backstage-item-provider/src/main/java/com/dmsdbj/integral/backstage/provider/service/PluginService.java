package com.dmsdbj.integral.backstage.provider.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dmsdbj.cloud.tool.business.IntegralResult;

import com.dmsdbj.integral.backstage.model.PluginModel;
import com.dmsdbj.integral.backstage.pojo.PluginEntity;
import com.dmsdbj.integral.backstage.pojo.PluginUserEntity;
import com.dmsdbj.integral.backstage.provider.dao.PluginBindsUserDao;
import com.dmsdbj.integral.backstage.provider.dao.PluginDao;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 插件管理操作
 * @return
 * @author 梁佳宝
 * @since 2020年8月12日13点19分
 */
@Service
public class PluginService {

    @Resource
    private PluginDao pluginDao;
    @Resource
    private PluginBindsUserDao pluginBindsUserDao;

    /**
     *根据绑定项目的id，判断数据库中是否已存在该数据命名
     * @param projectId
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日13点55分
     */
    public IntegralResult queryPluginId(String projectId){
        List<PluginEntity> pluginEntities = pluginDao.queryProjectId(projectId);
        if (CollectionUtils.isEmpty(pluginEntities)) {
            return IntegralResult.build(IntegralResult.SUCCESS, "项目未绑定插件");

        }
        return IntegralResult.build(IntegralResult.FAIL, "项目已经绑定插件");
    }
    /**
     *查询数据库中所有的未删除插件
     * @param
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日13点55分
     */
    public IntegralResult queryAllPlugin() {
        List<PluginEntity> pluginEntities = pluginDao.queryAllPlugin();
        if (CollectionUtils.isEmpty(pluginEntities)) {
            return IntegralResult.build(IntegralResult.FAIL, "还没有项目绑定插件");
        }
        return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", pluginEntities);
    }
    /**
     *将从前端返回的插件相关数据添加到数据库中
     * @param pluginModel
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日13点55分
     */
    public IntegralResult addPlugin(PluginModel pluginModel){
        PluginEntity pluginEntity = new PluginEntity();
        //实体数值转换
        pluginEntity.setDescription(pluginModel.getDescription());
        pluginEntity.setIconUrl(pluginModel.getIconUrl());
        pluginEntity.setIsEverybody(pluginModel.getIsEverybody());
        pluginEntity.setName(pluginModel.getName());
        pluginEntity.setProjectId(pluginModel.getProjectId());
        pluginEntity.setPageUrl(pluginModel.getPageUrl());
        pluginEntity.setOperator(pluginModel.getUserName());
        pluginEntity.setId("pluginId_"+pluginModel.getEnglishName());

        List<PluginEntity> pluginEntities = pluginDao.queryProjectId(pluginModel.getProjectId());
        if(!CollectionUtils.isEmpty(pluginEntities)){
            return IntegralResult.build(IntegralResult.FAIL, "该项目已存在插件");
        }

        Boolean flagInsert = pluginDao.addPlugin(pluginEntity);

        List<PluginUserEntity> pluginUserEntities = new ArrayList<>();
        if( pluginEntity.getIsEverybody() ==0){
            pluginModel.getPersonList().stream().forEach(child->{
                PluginUserEntity pluginUserEntity = new PluginUserEntity();
                pluginUserEntity.setId(IdWorker.getIdStr());
                pluginUserEntity.setPluginId(pluginEntity.getId());
                pluginUserEntity.setUserId(child);
                pluginUserEntity.setOperator(pluginEntity.getOperator());
                pluginUserEntities.add(pluginUserEntity);
            });
            Boolean flag =pluginBindsUserDao.insertPluginBindsUser(pluginUserEntities);
        }


        if (flagInsert){
            return IntegralResult.build(IntegralResult.SUCCESS, "添加插件成功");
        }
        return IntegralResult.build(IntegralResult.FAIL, "项目插件失败");
    }
    /**
     *插件相关数据更新数据库中相关数据
     * @param pluginModel
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日16点25分
     */
    public IntegralResult
    updatePlugin(PluginModel pluginModel){
        PluginEntity pluginEntity = new PluginEntity();
        //实体数值转换
        pluginEntity.setDescription(pluginModel.getDescription());
        pluginEntity.setIconUrl(pluginModel.getIconUrl());
        pluginEntity.setIsEverybody(pluginModel.getIsEverybody());
        pluginEntity.setPageUrl(pluginModel.getPageUrl());
        pluginEntity.setName(pluginModel.getName());
        pluginEntity.setProjectId(pluginModel.getProjectId());
        pluginEntity.setOperator(pluginModel.getUserName());

        //添加人员
        if(!CollectionUtils.isEmpty(pluginModel.getAddPersonList())){

            List<PluginUserEntity> pluginUserEntities = new ArrayList<>();
            pluginModel.getAddPersonList().stream().forEach(child->{
                PluginUserEntity pluginUserEntity = new PluginUserEntity();
                pluginUserEntity.setId(IdWorker.getIdStr());
                pluginUserEntity.setPluginId(pluginModel.getId());
                pluginUserEntity.setUserId(child);
                pluginUserEntity.setOperator(pluginModel.getUserName());
                pluginUserEntities.add(pluginUserEntity);
            });
            Boolean flag =pluginBindsUserDao.insertPluginBindsUser(pluginUserEntities);

        }

        //删除人员
        if(!CollectionUtils.isEmpty(pluginModel.getDeletePersonList())){

            List<PluginUserEntity> pluginUserEntities = new ArrayList<>();
            pluginModel.getDeletePersonList().stream().forEach(child->{

                PluginUserEntity pluginUserEntity = new PluginUserEntity();
                pluginUserEntity.setPluginId(pluginModel.getId());
                pluginUserEntity.setUserId(child);
                pluginUserEntities.add(pluginUserEntity);
            });
            Boolean flagDelete =pluginBindsUserDao.deletePluginBindsUser(pluginUserEntities);
        }


        Boolean flag = pluginDao.updatePlugin(pluginEntity);
        if (flag){
            return IntegralResult.build(IntegralResult.SUCCESS, "插件更新成功");
        }
        return IntegralResult.build(IntegralResult.FAIL, "插件更新失败");
    }
    /**
     *根据从前端返回的插件id删除数据库中数据
     * @param projectId
     * @return
     * @author 梁佳宝
     * @since 2020年8月12日16点25分
     */
    public IntegralResult updateDeletePlugin(String projectId){
        Boolean flag = pluginDao.updateDeletePlugin(projectId);
        if (flag) {
            return IntegralResult.build(IntegralResult.SUCCESS, "插件删除成功");
        }
        return IntegralResult.build(IntegralResult.FAIL, "插件删除失败");
    }

}
