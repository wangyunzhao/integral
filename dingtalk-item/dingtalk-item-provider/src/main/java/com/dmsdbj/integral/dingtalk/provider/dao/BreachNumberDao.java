package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.BreachNumberEntity;
import com.dmsdbj.integral.dingtalk.model.BreachNumberModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BreachNumberDao {

    /**
     * @description: 插入多条违纪次数记录
     * @param breachNumberEntityList
     * @return: int
     * @author: 马珂
     * @time: 2020/8/25 19:51
     */
    int insertBreachNumber(@Param("breachNumberEntityList") List<BreachNumberEntity> breachNumberEntityList);

    /**
     * @description: 更新删除字段根据id
     * @param breachNumberEntityList
     * @return: int
     * @author: 马珂
     * @time: 2020/8/26 9:53
     */
    int updateDeleteBreachNumber(@Param("breachNumberEntityList") List<BreachNumberEntity> breachNumberEntityList);

    int updateNumberBreachNumber(@Param("breachNumberModelList") List<BreachNumberModel> breachNumberModelList);

    int updateDeleteAllBreachNumber();
}
