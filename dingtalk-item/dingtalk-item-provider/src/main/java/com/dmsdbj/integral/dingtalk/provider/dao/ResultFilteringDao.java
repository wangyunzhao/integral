package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.AlarmDetailEntity;
import com.dingtalk.item.pojo.PunchResultEntity;
import com.dmsdbj.integral.dingtalk.model.MapModel;
import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 17:27 2020/6/4
 */
@Repository("ResultFilteringDao")
public interface ResultFilteringDao {

    //将过滤后的打卡结果插入数据库
    int insertResultFiltering(@Param("punchResultEntityList") List<PunchResultEntity> punchResultEntityList);

    //根据user_ding_id查询用户的姓名
    List<MapModel> queryUserNameByUserDingId(@Param("punchResultEntityList") List<PunchResultEntity> punchResultEntityList);

    //根据user_id查询用户的组织机构id
    List<MapModel> queryOrganizationIdByUserId(@Param("punchResultEntityList") List<PunchResultEntity> punchResultEntityList);

    //将异常考勤的数据插入异常考勤表中
    Integer insertAlarmDetail(@Param("alarmDetailEntityList") List<AlarmDetailEntity> alarmDetailEntityList);

    //查询今日未发送的告警信息

    List<AlarmDetailEntity> queryNotSendMessage();

    //根据组织机构id查询群组id
    String  queryOrgChatId(String orgId);

    //根据user_ding_id查询user_jifen_id
  List<MapModel> queryUserJiFenIdByDing(@Param("punchResultEntityList") List<PunchResultEntity> punchResultEntityList);
}
