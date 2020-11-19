package com.dmsdbj.integral.dingtalk.provider.dao;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.item.pojo.BreachResultEntity;
import com.dingtalk.item.pojo.PunchResultEntity;
import com.dingtalk.item.pojo.ScheduleDetailEntity;
import com.dmsdbj.integral.dingtalk.model.BreachNumberModel;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 功能描述: 查询打卡结果
 * @Author: keer
 * @Date: 2020/6/2 14:33
 * @Param:
 * @Return:
 */
@SuppressWarnings("ALL")
@Mapper
@Repository("punchResultDao")
public interface PunchResultDao {


    /**
     * 功能描述: 在排班详情表中查询比当前时间小的所有用户的plan_id和dingdingID--董可
     * @Author: keer
     * @Date: 2020/6/3 14:28
     * @Param: []
     * @Return: 无
     */
    List<String> queryPunchResult();

    /**
     * 功能描述: 在排班详情表中查询比当前时间小的所有用户的dingdingID--董可
     * @Author: keer
     * @Date: 2020/6/3 14:28
     * @Param: []
     * @Return: java.util.List<java.lang.String>
     */
    List<String> queryPunchResultDingId();



    /**
     * 功能描述: 方法一：使用for循环更新已经查过的数据的is_query字段--董可
     * @Author: keer
     * @Date: 2020/7/22 14:28
     * @Param: [planid]
     * @Return: void
     */
    void updateQueryId(String planid);

    /**
     * 功能描述: 方法二：使用动态SQL更新已经查过的数据的is_query字段--董可
     * @Author: keer
     * @Date: 2020/7/22 14:29
     * @Param: planIdList排班ID集合
     * @Return: 受影响的行数
     */
    void updateQueryIdNew(@Param("PlanIDList") List<String> planIdList);


    /**
     * @Description 二次报警——插入未销卡人员到打开结果表
     * @Param [list]
     * @return java.util.List<com.dingtalk.item.pojo.PunchResultEntity>
     * @Author fjx
     * @Date 2020-06-29 10:10
     **/
    int insertPunchResult(@Param("list") List<PunchResultEntity> list);

    /**
     * 获取违纪人员与违纪次数
     * @return
     * @Author 梁佳宝
     * @Date  2020年8月23日16点06分
     */
    List<BreachNumberModel> getBreachNumberModel();
}
