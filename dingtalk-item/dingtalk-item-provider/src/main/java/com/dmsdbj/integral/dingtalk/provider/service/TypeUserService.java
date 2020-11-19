package com.dmsdbj.integral.dingtalk.provider.service;

import com.dingtalk.item.pojo.TypeUserEntity;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.TypeUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户级别表操作
 *
 * @author 崔晓鸿
 * @since 2020年6月24日09:20:45
 */

@Service
public class TypeUserService {
    @Autowired
    private TypeUserDao typeUserDao;

    /**
     * 添加用户级别
     *
     * @author 崔晓鸿
     * @since 2020年6月24日10:26:16
     */
    public boolean insertTypeUser(List<TypeUserEntity> typeUserEntities) {
        // 判断要添加的用户中是否已经存在 用户中有已经存在的 则不添加
        boolean existFlag =false;
        for(TypeUserEntity typeUserEntity : typeUserEntities){
            existFlag = selectTypeUserByUserDingId(typeUserEntity.getUserDingId());
            if(existFlag){
                return false;
            }
        }
        // 用户中没有已经存在的 则添加
        int insertFlag = typeUserDao.insertTypeUser(typeUserEntities);
        if(insertFlag>Constants.ZERO){
            return true;
        }
        return false;
    }

    /**
     * 更新用户级别
     *
     * @author 崔晓鸿
     * @since 2020年6月24日10:46:12
     */
    public boolean updateTypeUser(TypeUserEntity typeUserEntity){
        // 查询要更新的用户是否存在
         boolean existFlag = selectTypeUserByUserDingId(typeUserEntity.getUserDingId());
         // 存在则更新 不存在则不更新
        int updateFlag=0;
        if(existFlag){
            // 更新用户级别
            updateFlag=typeUserDao.updateTypeUser(typeUserEntity);
        }
        if(updateFlag>Constants.ZERO){
            return true;
        }
        return false;
    }

    /**
     * 根据用户钉钉id查询当前用户是否已存在
     *
     * @author 崔晓鸿
     * @since 2020年6月24日10:33:00
     */
    public boolean selectTypeUserByUserDingId(String userDingId){
        // 根据用户钉钉id查询当前用户是否已存在
         TypeUserEntity typeUserEntity = typeUserDao.selectTypeUserByUserDingId(userDingId);
         // 若查不到，代表不存在
         if(typeUserEntity==null){
             return false;
         }else {
             return true;
         }
    }
}
