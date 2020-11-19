package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.AlarmHandleEntity;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleResultModel;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleSubmitModel;
import com.dmsdbj.integral.dingtalk.model.HandleResultModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报警结果处理
 *
 * @author 崔晓鸿
 * @since 2020年6月22日10:38:43
 */
@Repository("alarmHandleDao")
public interface AlarmHandleDao {
    /**
     * 查询报警异常的人（传参数organizationId代表按期查，不传参数organizationId，也就是为null，代表查所有）
     *
     * @param organizationId
     * @return List<AlarmHandleResultModel>
     * @author 崔晓鸿
     * @since 2020年6月8日09:45:15
     */
    List<AlarmHandleResultModel> selectAlarmHandle(@Param("organizationId") String organizationId);
    /**
     * 根据alarmId查询报警处理结果
     *
     * @param alarmId
     * @return List<AlarmHandleResultModel>
     * @author 崔晓鸿
     * @since 2020年6月22日16:49:28
     */
    List<HandleResultModel> selectHandleResultByAlarmId(String alarmId);
    /**
     * 根据用户id查本月违纪次数
     *
     * @param userId
     * @return List<AlarmHandleResultModel>
     * @author 崔晓鸿
     * @since 2020年6月22日16:49:34
     */
    AlarmHandleResultModel selectDisobedientByUserId(String userId);

    /**
     * 一键已阅
     * @author 王云召
     * @since 2020年6月23日09:24:12
     * @param alarmHandleEntities
     * @return int
     */
    int readWithOneClickByDingId(@Param("alarmHandleEntities")List<AlarmHandleEntity> alarmHandleEntities);
    /**
     * 查询当前提交人的等级
     *
     * @return String
     * @author 王梦瑶
     * @param   dingId
     * @since 2020年6月23日09:24:12
     */
    String selectSubmitGrade(String dingId);
    /**
     * 查询当前提交人的姓名
     * @param   dingId
     * @return String
     * @author 王梦瑶
     * @since 2020年6月23日09:24:12
     */
    String selectUserName(String dingId);
    /**
     * 更新is_send字段
     *@param   alarmId
     * @return String
     * @author 王梦瑶
     * @since 2020年6月23日09:24:12
     */
    int updateIsSend(String alarmId);
//    /**
//     * 更新is_send字段
//     *
//     * @return String
//     * @author 王梦瑶
//     * @since 2020年6月23日09:24:12
//     */
//    int updateIsSafe(String alarmId,String isSafe);
    /**
     *将提交的数据插入数据库
     *
     * @return String
     * @author 王梦瑶
     * @param alarmHandleEntity
     * @since 2020年6月23日09:24:12
     */
    int insertAlarmHandle(AlarmHandleEntity alarmHandleEntity);
    /**
     *根据alarmId更新安全状态
     *
     * @return alarmId,isSafe
     * @author 王梦瑶
     * @param alarmId
     * @param isSafe
     * @since 2020年6月23日09:24:12
     */
    int updateIsSafeByAlarmId(String alarmId,String isSafe);

    /**
     * 根据用户钉钉id查用于级别
     *
     * @param dingId
     * @return type
     * @author 崔晓鸿
     * @since 2020年6月22日16:49:34
     */
    String selectUserType(String dingId);
}
