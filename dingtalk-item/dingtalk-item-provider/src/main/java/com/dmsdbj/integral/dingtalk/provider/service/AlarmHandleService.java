package com.dmsdbj.integral.dingtalk.provider.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dingtalk.item.pojo.AlarmHandleEntity;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleResultModel;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleSubmitModel;
import com.dmsdbj.integral.dingtalk.model.HandleResultModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.AlarmHandleDao;
import com.dmsdbj.integral.dingtalk.provider.dao.AlarmInfoDao;
import com.tfjybj.framework.json.JsonHelper;
import com.tfjybj.framework.log.LogCollectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报警结果处理
 *
 * @author 崔晓鸿
 * @since 2020年6月22日10:35:49
 */
@Service
public class AlarmHandleService {

    @Autowired
    private AlarmHandleDao alarmHandleDao;

    @Autowired
    private AlarmInfoDao alarmInfoDao;

    private static final String INDEX = "alarmHandle";

    /**
     * 查询所有人的异常报警结果
     *
     * @return
     * @author 崔晓鸿
     * @since 2020年6月22日20:04:33
     */
    public Object selectAlarmHandle(String dingId) {
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN + " 进入查询所有人的异常报警结果方法", ""), Constants.DING_INDEX + INDEX);
        // 存放考勤异常的人
        List<AlarmHandleResultModel> alarmHandleResultModels = new ArrayList<>();
        // 根据dingId查询这个人的级别，级别是纪委只能查看本期，老师可看所有
        String userType = alarmHandleDao.selectUserType(dingId);
        if(userType==null){
            return new HashMap<>(16);
        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + " 进入查询考勤异常的人的方法", ""), Constants.DING_INDEX + INDEX);
        // 1 代表纪委，纪委只能看本期数据
        if (Constants.USER_TYPE.equals(userType)) {
            // 根据dingId查询userId
            String userId = alarmInfoDao.getUserIdByDingId(dingId);
            // 根据dingId查询这个人所在组织
            String organizationId = alarmInfoDao.queryOrgazitionIdByUserId(userId);
            // 查询相应期数的考勤异常的人
            alarmHandleResultModels = alarmHandleDao.selectAlarmHandle(organizationId);
        } else {
            // 查询所有考勤异常的人
            alarmHandleResultModels = alarmHandleDao.selectAlarmHandle(null);
        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + " 查询所有考勤异常的人方法结束 result <> {0}", JsonHelper.toJson(alarmHandleResultModels)), Constants.DING_INDEX + INDEX);
        // 存放处理结果的集合
        List<HandleResultModel> handleResultModels = new ArrayList<>();
        // 存放违纪次数
        AlarmHandleResultModel alarmHandleResultModelDis = new AlarmHandleResultModel();
        // 存放已处理的集合
        List<AlarmHandleResultModel> alarmHandleResultModelIsHandle = new ArrayList<>();
        // 存放未处理的集合
        List<AlarmHandleResultModel> alarmHandleResultModelNoHandle = new ArrayList<>();
        LogCollectManager.common(MessageFormat.format(Constants.LOG + " 进入对每个考勤异常的人的进行处理 - 增加处理结果和违纪次数 ", ""), Constants.DING_INDEX + INDEX);
        for (AlarmHandleResultModel alarmHandleResultModel : alarmHandleResultModels) {
            // 通过AlarmId查询考勤异常的人的处理结果
            handleResultModels = alarmHandleDao.selectHandleResultByAlarmId(alarmHandleResultModel.getAlarmId());
            // 通过userId查询考勤异常的人本月的违纪次数
            alarmHandleResultModelDis = alarmHandleDao.selectDisobedientByUserId(alarmHandleResultModel.getUserId());
            // 放违纪次数
            alarmHandleResultModel.setDisobedientNum(alarmHandleResultModelDis.getDisobedientNum());
            // 放处理结果
            alarmHandleResultModel.setHandleResult(handleResultModels);

            // 分类，将已处理的和未处理分开，1代表已处理 判断当下处理人的处理结果
             Optional<HandleResultModel> handleResultModelsCollect = handleResultModels.stream().filter(result -> dingId.equals(result.getOperatorDingId())).findFirst();
            if(handleResultModelsCollect.isPresent() && Constants.IS_HANDLE.equals(handleResultModelsCollect.get().getIsHandle())){
                alarmHandleResultModelIsHandle.add(alarmHandleResultModel);
            } else {
                alarmHandleResultModelNoHandle.add(alarmHandleResultModel);
            }

        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + " 对每个考勤异常的人的进行处理 - 增加处理结果和违纪次数 结束", ""), Constants.DING_INDEX + INDEX);
        // 排序，不安全的排在前边也就是isSafe是0的排在前边
        List<AlarmHandleResultModel> alarmHandleResultModelIsHandleCollect = alarmHandleResultModelIsHandle.stream().sorted(Comparator.comparing(AlarmHandleResultModel::getIsSafe)).collect(Collectors.toList());
        List<AlarmHandleResultModel> alarmHandleResultModelNoHandleCollect = alarmHandleResultModelNoHandle.stream().sorted(Comparator.comparing(AlarmHandleResultModel::getIsSafe)).collect(Collectors.toList());
        // 构造分类后的map，返回
        Map<String, List<AlarmHandleResultModel>> resultMap = new HashMap<>(16);
        resultMap.put("isHandle", alarmHandleResultModelIsHandleCollect);
        resultMap.put("noHandle", alarmHandleResultModelNoHandleCollect);
        LogCollectManager.common(MessageFormat.format(Constants.END + " 查询所有人的异常报警结果方法结束 result <> {0}", JsonHelper.toJson(resultMap)), Constants.DING_INDEX + INDEX);
        return resultMap;
    }

    /**
     * 一键已阅
     *
     * @param dingId
     * @param alarmHandleEntities
     * @return int
     * @author 王云召
     * @since 2020年6月24日09:35:42
     */
    public int readWithOneClickByDingId(String dingId, List<AlarmHandleEntity> alarmHandleEntities) {
        String operatorType = alarmInfoDao.queryOperatorType(dingId);
        if (operatorType.isEmpty()) {
            //没有级别，失败
            return Constants.ZERO;
        }
        String userName = alarmInfoDao.getUserNameByDingId(dingId);
        alarmHandleEntities.stream().forEach(alarmHandleEntity ->
        {
            alarmHandleEntity.setId(IdWorker.getIdStr());
            alarmHandleEntity.setOperatorType(operatorType);
            alarmHandleEntity.setOperatorDingId(dingId);
            alarmHandleEntity.setOperatorName(userName);
            alarmHandleEntity.setOperateResult("已知悉");
            alarmHandleEntity.setIsHandle(1);
        });
        return alarmHandleDao.readWithOneClickByDingId(alarmHandleEntities);
    }

    /**
     * 提交
     *
     * @param
     * @param alarmHandleSubmitModel
     * @return int
     * @author 王梦瑶
     * @since 2020年6月24日09:35:42
     */
    public int submitSuperior(AlarmHandleSubmitModel alarmHandleSubmitModel) {
        AlarmHandleEntity alarmHandleEntity = new AlarmHandleEntity();
        //查询当前提交人的等级
        String grade = alarmHandleDao.selectSubmitGrade(alarmHandleSubmitModel.getOperatorDingId());
        //判断是否提交到上级
        if (alarmHandleSubmitModel.getIsSubmitSuperior().equals("1")) {
            //更新tid_alarm_detail表中is_send字段
            alarmHandleDao.updateIsSend(alarmHandleSubmitModel.getAlarmId());

        }
        alarmHandleDao.updateIsSend(alarmHandleSubmitModel.getAlarmId());
        //将处理结果插入数据库
        //报警考勤id
        alarmHandleEntity.setAlarmId(alarmHandleSubmitModel.getAlarmId());
        //处理人钉钉id
        alarmHandleEntity.setOperatorDingId(alarmHandleSubmitModel.getOperatorDingId());
        //根据钉id查询姓名
        String userName = alarmHandleDao.selectUserName(alarmHandleSubmitModel.getOperatorDingId());
        //处理人姓名
        alarmHandleEntity.setOperatorName(userName);
        //处理人级别
        alarmHandleEntity.setOperatorType(grade);
        //处理结果
        alarmHandleEntity.setOperateResult(alarmHandleSubmitModel.getOperateResult());
        alarmHandleDao.updateIsSafeByAlarmId(alarmHandleSubmitModel.getAlarmId(), alarmHandleSubmitModel.getIsSafe());
        return alarmHandleDao.insertAlarmHandle(alarmHandleEntity);
    }

    /**
     * 根据alarmID更新isSafe
     *
     * @param alarmId
     * @return
     */
    public int updateIsSafeByAlarmId(String alarmId) {
        return alarmHandleDao.updateIsSafeByAlarmId(alarmId, "0");

    }

    /**
     * 根据dingId来查询type类型
     *
     * @param dingId
     * @return
     */
    public String selectUserType(String dingId) {
        return alarmHandleDao.selectUserType(dingId);
    }
}
