package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.BreachResultEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreachResultDao {
    /**
     * 插入违纪数据
     * @param breachResultEntityList
     * @author 梁佳宝
     * @return
     */
    int insertBreachResult(@Param("breachResultEntityList") List<BreachResultEntity> breachResultEntityList);
    /**
     * 更新违纪数据
     * @param breachResultEntityList
     * @author 梁佳宝
     * @return
     */
    int updateBreachResultIsSuccess(@Param("breachResultEntityList") List<BreachResultEntity> breachResultEntityList);
}
