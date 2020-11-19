package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.AlarmDetailEntity;
import com.dingtalk.item.pojo.AlarmHandleEntity;
import com.dingtalk.item.pojo.GradeRuleEntity;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleModel;
import com.dmsdbj.integral.dingtalk.model.AlarmInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("alarmInfoDao")
public interface AlarmInfoDao {

    String queryOrgazitionIdByUserId(String userId);

    List<AlarmInfoModel> queryAlarmInfo(String orgazitionId);

    int handleAlarmInfoByOperator(AlarmHandleEntity alarmHandleEntity);

    String queryOperatorType(String operateType);

    int updateAlarmIsSaveById(String isSave,String alarmId);

    String getUserIdByDingId(String dingId);

    String getUserNameByDingId(String dingId);


    /**
     * 插入未销卡人员信息
     *
     * @author fjx
     * @param alarmDetailEntity
     * @return
     */
    int insertAlarmInfo(@Param("alarmDetailEntity") List<AlarmDetailEntity> alarmDetailEntity);

    /**
     *曹祥铭-查询所有考勤规则
     * @return
     */
    List<GradeRuleEntity> selectAllRule();

    int updateAlarmIsSend();

    List<AlarmDetailEntity> queryTodayAllInfoToTeacher();

    int updateAlarmIsSendToTeacher();

    /**
     * 根据群id查询期数
     * @param chatId
     * @return
     */
    String queryOrgNameByChatId(String chatId);

    //曹祥铭-查询本期本月的纪委-查询纪委姓名
    String queryOrgJiWeiName(String userId);

    //曹祥铭-根据组织机构ID查询组织机构名
    String queryOrgNameByOrgId(String orgId);

    //曹祥铭-向告警处理表中插入信息
    int insertAlarmHandel(AlarmHandleModel alarmHandleModel);

    //曹祥铭-查询告警信息表中的信息
    AlarmHandleModel queryAlarmHandleByAlarmId(String alarmId);

    //曹祥铭-更新告警表中的is_past字段
    int updateAlarmIsPast();


    /**
     * 功能描述: 董可-查询is_send=3的数据，发给米老师
     * @Author: keer
     * @Date: 2020/6/2 14:44
     * @Param: []
     * @Return: java.util.List<com.dingtalk.item.pojo.AlarmDetailEntity>
     */
    List<AlarmDetailEntity> queryToHighest();


    /**
     * 功能描述:  董可-查询第二级的处理人--目前是纪老师
     * @Author: keer
     * @Date: 2020/6/2 14:44
     * @Param: []
     * @Return: java.util.List<com.dingtalk.item.pojo.AlarmDetailEntity>
     */
    String queryLower();


    /**
     * 功能描述:  董可-发消息给最高级处理人之后，更新告警考勤表
     * @Author: keer
     * @Date: 2020/6/2 14:44
     * @Param: []
     * @Return: java.util.List<com.dingtalk.item.pojo.AlarmDetailEntity>
     */
    int updateAlarmSendToHighest();


    /**
     * 功能描述:  董可-发消息给最高级处理人之后，更新alarm——handle表
     * @Author: keer
     * @Date: 2020/6/2 14:44
     * @Param: []
     * @Return: java.util.List<com.dingtalk.item.pojo.AlarmDetailEntity>
     */
    int updateHandleSendToHighest();

    /**
     * 功能描述:  获取【发送给谁】这个人的名字
     * @Author: keer
     * @Date: 2020/6/2 14:44
     * @Param: []
     * @Return: java.util.List<com.dingtalk.item.pojo.AlarmDetailEntity>
     */
    String queryDingNameToSend();

    /**
     * 功能描述:  获取发送给三级这个人的dingID
     * @Author: keer
     * @Date: 2020/6/2 14:44
     * @Param: []
     * @Return: java.util.List<com.dingtalk.item.pojo.AlarmDetailEntity>
     */
    String queryDingIdToSend();
}
