package com.dmsdbj.integral.dingtalk.provider.service;

import com.dingtalk.item.pojo.AlarmDetailEntity;
import com.dmsdbj.integral.dingtalk.model.ErrorUserInfoModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.AlarmInfoDao;
import com.dmsdbj.integral.dingtalk.provider.dao.ResultFilteringDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 功能描述: 调用钉钉接口发送工作通知
 * @Author: keer
 * @Date: 2020/7/22 14:20
 */
@Service
public class SendToHighestService {

    @Resource
    private AlarmInfoDao alarmInfoDao;
    @Resource
    private ResultFilteringService resultFilteringService;
    @Resource
    private ResultFilteringDao resultFilteringDao;
    @Resource
    private SendMessageService sendMessageService;
    @Value("${message.teachersChat}")
    private String teacherChatAddress;

    /**
     * 功能描述: 调用钉钉接口发送工作通知
     * @Author: keer
     * @Date: 2020年6月6日09:44:41
     * @Param: []
     * @Return: java.lang.Boolean
     */
    public Boolean sendToHighest(){
        boolean isSucceed = true;
        // 1.从数据库中查询is_send=3的数据
        List<AlarmDetailEntity> needToHighestEntity = alarmInfoDao.queryToHighest();
        // 2.判断是否有操作人，没有操作人就发送到【钉钉考勤测试群】
        List<AlarmDetailEntity> noOrgazitionEntity = needToHighestEntity.stream()
                .filter(noOperator -> noOperator.getOrgazition()== null).collect(Collectors.toList());
        if (!noOrgazitionEntity.isEmpty()){
            // 3.如果没有组织ID就发送到【钉钉考勤测试群】
            List<ErrorUserInfoModel> errList = new ArrayList<>();
            for (AlarmDetailEntity needToSendErrmodel : noOrgazitionEntity
            ) {
                resultFilteringService.setErrMessage("【" + needToSendErrmodel.getUserName()
                        + needToSendErrmodel.getRemark() + "】", Constants.DING_ID
                        + needToSendErrmodel.getUserDingId() + "\n" + Constants.INTEGRAL_ID
                        + needToSendErrmodel .getUserJifenId(), Constants.NOTHING, "给老师发送告警信息时，组织ID为空", Constants.DATA);
            }
        }

        // 4.去除掉组织ID为空的人（剩下就是可以发送异常消息的人）
        needToHighestEntity = needToHighestEntity.stream()
                .filter(noOperator -> noOperator.getOrgazition() != null).collect(Collectors.toList());

        // 获取【发送给谁】这个人的dingID--使用这个钉钉ID来发送工作通知
        String sendToDingId = alarmInfoDao.queryDingIdToSend();
        // 该组织下异常考勤人员的数量
        String nums = String.valueOf(needToHighestEntity.size());
        // 查询出上一级处理人(目前是纪老师)
        String lowerName  = alarmInfoDao.queryLower();
        // 拼接姓名
        StringBuffer alarmNames = new StringBuffer();

        for (AlarmDetailEntity alarmEntity : needToHighestEntity){
            if (StringUtils.isNotEmpty(alarmEntity.getUserName())){
                String newRemark;
                if (alarmEntity.getResult() != null && alarmEntity.getResult() != ""){
                    newRemark = alarmEntity.getResult();
                }else {
                    newRemark = "未处理";
                }
                String operator = lowerName;
                if (alarmEntity.getOperator() != null && alarmEntity.getOperator() != ""){
                    operator = alarmEntity.getOperator();
                }
                String nameRemark = alarmEntity.getUserName() + "\n" + "【异常类型】 " + alarmEntity.getRemark()
                        + "\n" + "【状态反馈】 " + newRemark + "\n" + "【上一级处理人】 " + operator + "\n"+"\n";
                alarmNames = alarmNames.append(nameRemark);
            }
            // 向告警信息表中插入信息
            resultFilteringService.insertAlarmHandle(alarmEntity);
        }
        String strNames = alarmNames.toString();
        try {

            String subString = "钉钉考勤";
            // 发送工作通知
            sendMessageService.sendWorkMessage(strNames, nums, sendToDingId, subString);

        }catch (Exception e) {
            e.printStackTrace();
            isSucceed = false;
        }

        if (isSucceed) {
            // 发送告警消息成功后，更新告警考勤表
             alarmInfoDao.updateAlarmSendToHighest();

        }

        return true;
    }
}
