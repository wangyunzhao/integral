package com.dmsdbj.integral.backstage.provider.service;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.PluginBindsUserModel;
import com.dmsdbj.integral.backstage.pojo.PluginUserEntity;
import com.dmsdbj.integral.backstage.provider.dao.PluginBindsUserDao;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 插件绑定人员操作
 * @return
 * @author 梁佳宝
 * @since 2020年8月12日13点19分
 */
@Service
public class PluginBindsUserService {
    @Resource
    private PluginBindsUserDao pluginBindsUserDao;

    /**
     * 根据插件id查询数据库中人员数据
     * @param pluginId
     * @return
     */
    public IntegralResult<PluginBindsUserModel> selectPluginBindsUser(String pluginId) {
        List<PluginBindsUserModel> pluginBindsUserModels = pluginBindsUserDao.selectPluginBindsUser(pluginId);
        if (CollectionUtils.isEmpty(pluginBindsUserModels)) {
            return IntegralResult.build(IntegralResult.FAIL,"该插件还未绑定人员");
        }
        return IntegralResult.build(IntegralResult.SUCCESS,"插件绑定人员查询成功",pluginBindsUserModels);
    }

}
