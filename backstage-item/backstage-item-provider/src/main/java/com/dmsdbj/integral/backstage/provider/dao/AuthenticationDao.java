package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.AuthenticationModel;
import com.dmsdbj.integral.backstage.pojo.ProjectEntity;
import org.springframework.stereotype.Repository;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 8:26 2020/7/27
 */
@Repository("authenticationDao")
public interface AuthenticationDao {
    //根据SecretID查询SecretKey等项目信息
    ProjectEntity queryProjectBySecretId(String  secretId);
}
