package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.ScheduleDetailEntity;
import com.dmsdbj.integral.dingtalk.model.TcAllusersDateModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 查询企业排班详情
 * @author 梁佳宝
 * @since 2020年6月4日11点56分
 */
@Mapper
@Repository("allScheduleDao")
public interface AllScheduleDao {
    /**
     * 根据时间，查询企业排班信息
     * @param createTime
     * @return
     */
    List<ScheduleDetailEntity> findAllByPlanCheckTime(@Param(value = "createTime") String createTime);

    /**
     * 添加企业排班信息
     * @param scheduleDetailEntityList
     * @return
     */
    int addSchedule(@Param(value = "scheduleDetailEntity") List<ScheduleDetailEntity> scheduleDetailEntityList);

    /**
     * 根据dingid获取用户名手机号积分id
     * @param useridset
     * @return
     */
    List<TcAllusersDateModel> findByDingId(@Param(value = "useridset") Set<String> useridset);

//    /**
//     * 用sql语句直接插入排班表
//     * @param scheduleDetailEntityList
//     * @return
//     */
//    int directAddSchedule(@Param(value = "scheduleDetailEntityList") List<ScheduleDetailEntity> scheduleDetailEntityList);
}
