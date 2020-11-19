package com.dmsdbj.integral.dingtalk.provider.service;

import com.dingtalk.item.pojo.AlarmDetailEntity;
import com.dingtalk.item.pojo.AlarmHandleEntity;
import com.dmsdbj.integral.dingtalk.model.AlarmInfoModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.AlarmInfoDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AlarmInfo接口
 * tid_alarm_detail表
 *
 * @author 王云召
 * @version ${version}
 * @since ${version} 2019-09-11 22:13:52
 */
@Service("alarmInfoService")
public class AlarmInfoService {

    @Resource
    private AlarmInfoDao alarmInfoDao;

    /**
     * 查询告警信息返回前端给纪委处理
     *
     * @param orgazitionId
     * @author 王云召
     * @sinse 2020年6月4日10:20:19
     */
    public List<AlarmInfoModel> queryAlarmInfo(String orgazitionId) {
        //根据用户的orgazitionId查询所在组织的告警消息，只能看自己所在组织的告警消息
        List<AlarmInfoModel> alarmInfos = alarmInfoDao.queryAlarmInfo(orgazitionId);
        return alarmInfos;
    }

    /**
     * 根据uerid查询所在的期数
     *
     * @param userId
     * @author 王云召
     * @sinse 2020年6月4日10:20:23
     */
    public String queryOrgazitionIdByUserId(String userId) {
        //根据用户的userid查询用户所在的组织id（所在期数）
        String orgazitionId = alarmInfoDao.queryOrgazitionIdByUserId(userId);
        return orgazitionId;
    }

    /**
     * 根据钉钉id查询积分id
     */
    public String getUserIdByDingId(String dingId) {
        //根据钉钉id查询userid
        String userId = alarmInfoDao.getUserIdByDingId(dingId);
        return userId;
    }

    /**
     * 根据钉钉id查询用户姓名
     */
    public String getUserNameByDingId(String dingId) {
        //根据钉钉id查询userid
        String UserName = alarmInfoDao.getUserNameByDingId(dingId);
        return UserName;
    }


    /**
     * 根据uerid查询所在的期数
     *
     * @param alarmHandleEntity
     * @author 王云召
     * @sinse 2020年6月4日10:20:23
     */
    public int handleAlarmInfoByOperator(String isSave, AlarmHandleEntity alarmHandleEntity) {

        //更新告警表中的是否联系上
        int updateIsSave = alarmInfoDao.updateAlarmIsSaveById(isSave, alarmHandleEntity.getAlarmId());
        if (updateIsSave == Constants.ZERO) {
            return Constants.ZERO;
        }
        //查询用户姓名
        String operatorName = alarmInfoDao.getUserNameByDingId(alarmHandleEntity.getOperatorDingId());

        //将级别插入到实体
        alarmHandleEntity.setOperatorName(operatorName);
        alarmHandleEntity.setIsHandle(1);
        //更新数据库
        int isUpdate = alarmInfoDao.handleAlarmInfoByOperator(alarmHandleEntity);
        return isUpdate;
    }



    /**
     * 插入未销卡人员信息
     *
     * @param alarmDetailEntity
     * @return
     * @author fjx
     */
    public int insertAlarmInfo(@Param("alarmDetailEntity") List<AlarmDetailEntity> alarmDetailEntity) {
        return alarmInfoDao.insertAlarmInfo(alarmDetailEntity);
    }
}
