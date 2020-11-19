package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.pojo.SpecificPointsEntity;
import com.dmsdbj.integral.backstage.pojo.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author 齐智
 * @Date: 2020/8/13 9:55
 * @Version: 1.0
 **/
@Repository
public interface UpdateSpecificPointsDao {


    /**
     * 根据特定人员，更新可赠积分
     */
    int updateGivingIntegralWithId();

    /**
     * 查询部门要加分的特定的部门
     * @return
     */
    List<SpecificPointsEntity> selectDept();

    /**
     * 根据特定人员，更新可赠积分
     */
    int updateGivingIntegralWithDeptId(@Param("list") List<UserEntity> userEntities);
}
