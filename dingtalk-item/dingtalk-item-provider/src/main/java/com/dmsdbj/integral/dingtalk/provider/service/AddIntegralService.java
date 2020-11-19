package com.dmsdbj.integral.dingtalk.provider.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dingtalk.item.pojo.BreachNumberEntity;
import com.dingtalk.item.pojo.BreachResultEntity;
import com.dmsdbj.integral.dingtalk.model.AddIntegralDataModel;
import com.dmsdbj.integral.dingtalk.model.BreachNumberModel;
import com.dmsdbj.integral.dingtalk.model.CallInterfaceResultModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.AddIntegralDao;
import com.dmsdbj.integral.dingtalk.provider.dao.BreachNumberDao;
import com.dmsdbj.integral.dingtalk.provider.dao.BreachResultDao;
import com.dmsdbj.integral.dingtalk.provider.dao.PunchResultDao;
import com.dmsdbj.integral.dingtalk.utils.http.HttpUtils;
import com.dmsdbj.integral.dingtalk.utils.http.ResponseWrap;
import com.dmsdbj.integral.dingtalk.utils.http.TokenUtils;
import com.tfjybj.framework.log.LogCollectManager;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.swing.*;
import java.security.BasicPermission;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.UIManager.put;

/**
 * @author 王云召
 * @since 2020年6月24日09:35:42
 */
@Service("addIntegralService")
@RefreshScope
public class AddIntegralService {
    /**
     * 加分uri
     */
    @Value("${permission.addIntegral}")
    private String addIntegralUri;

    /**
     * 纪委处理加分分数
     */
    @Value("${score.addIntegral}")
    private Integer addIntegral;

    /**
     * 纪委未处理减分分数
     */
    @Value("${score.miunsIntegral}")
    private Integer miunsIntegral;
    @Resource
    private AddIntegralService addIntegralService;

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private AddIntegralDao addIntegralDao;
    @Resource
    private PunchResultDao punchResultDao;
    @Resource
    private BreachResultDao breachResultDao;
    @Resource
    private BreachNumberDao breachNumberDao;

    @Resource
    private SendMessageService sendMessageService;
    /**
     * 初始一个日期，用来计算date那天的加分
     */

    private LocalDate date;

    /**
     * 当前service名称
     */
    private static final String INDEX = "addIntegralService";


    /**
     * 计算考勤加分分值
     *
     * @param num
     * @return
     */
    public int caluIntegral(int num) {
        //先获取今天的日期，在减去num天，因为计算分值只能计算昨天的，故num默认值为1。
        date = LocalDate.now().minusDays(num);

        //计算分值，更新数据库，成功返回1，失败返回0
        int isCalu = addIntegralDao.caluIntegral(date);
        return isCalu;
    }


    /**
     * 查询加分数据
     *
     * @return addIntegralData
     * @author 王云召
     * @since 2020年6月24日09:35:42
     */
    public List<AddIntegralDataModel> queryAddIntegralData() {
        //查询昨天或者第前num天的考勤打卡加分数据
        List<AddIntegralDataModel> addIntegralData = addIntegralDao.queryAddIntegralData(date);
        return addIntegralData;
    }


    /**
     * 查询纪委处理告警消息的加分数据
     *
     * @return
     */
    public List<AddIntegralDataModel> queryHandleAlarmIntegralData() {
        //计算纪委处理异常打卡的分值
        int isCalu = addIntegralDao.caluHandleInfoIntegral(addIntegral, miunsIntegral);
        //查询纪委处理异常打卡的分值
        List<AddIntegralDataModel> handleAlarmIntegralData = addIntegralDao.queryHandleAlarmIntegralData();
        return handleAlarmIntegralData;

    }


    /**
     * 调用加分第三方接口
     *
     * @author 王云召
     * @since 2020年6月24日09:35:42
     */
    public boolean callKernelAddIntegralInterface(List<AddIntegralDataModel> addIntegralData) {
        //获得权限的token
        String token = TokenUtils.authToken();

        //没有获取到权限，直接返回，不做调用加分接口
        if (token.isEmpty()) {
            LogCollectManager.common(MessageFormat.format(Constants.ERR + "权限token获取失败！", ""), Constants.DING_INDEX + INDEX);
            return false;
        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "权限token获得成功！", ""), Constants.DING_INDEX + INDEX);
        //调用第三方接口，成功返回true，失败返回false
        boolean addflag = false;
        //得到加分数据
        //开始调用加分减分接口
        try {
            addflag = addIntegral(token, addIntegralData, addIntegralUri);
        } catch (Exception e) {
            e.printStackTrace();
            LogCollectManager.common(MessageFormat.format(Constants.ERR + "调用核心加分服务的加分接口调用失败！", ""), Constants.DING_INDEX + INDEX);

        } finally {
            //判断是否加分成功
            if (addflag) {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "加分成功", ""), Constants.DING_INDEX + INDEX);
            }

        }
        return addflag;
    }


    /**
     * 加分成功后更新打卡加分数据的是否加分成功的状态
     *
     * @return addIntegralData
     * @author 王云召
     * @since 2020年6月24日09:35:42
     */
    public int updateIsSuccess(List<AddIntegralDataModel> addIntegralData) {
        //加分成功后更新数据的是否加分成功的状态
        int isSuccess = addIntegralDao.updateIsSuccess(addIntegralData);
        return isSuccess;
    }


    /**
     * 加分成功后，更改纪委处理告警信息表的状态为已经加分
     *
     * @param handleAlarmIntegralData
     * @return
     */
    public int updateIsHandSuccess(List<AddIntegralDataModel> handleAlarmIntegralData) {
        //加分成功后更新数据的是否加分成功的状态
        int isSuccess = addIntegralDao.updateIsHandSuccess(handleAlarmIntegralData);
        return isSuccess;
    }


    /**
     * 调用积分核心服务的的加分接口
     *
     * @return addIntegralData
     * @author 王云召
     * @since 2020年6月24日09:35:42
     */
    public boolean addIntegral(String token, List<AddIntegralDataModel> addIntegralData, String uri) {

        boolean isAddIntegralFlag = false;
        try {
            //调用积分核心服务的加分接口
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json; charset=utf-8");
            httpHeaders.add("Authorization", token);
            HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(addIntegralData), httpHeaders);
            //发送请求
            String responseWrap = restTemplate.postForObject(uri, requestEntity, String.class);
            CallInterfaceResultModel callInterfaceResultModel = JSON.parseObject(responseWrap, CallInterfaceResultModel.class);
            //判断第三方接口是否调用成功
            if (callInterfaceResultModel.getCode().equals(Constants.SUCCESS_COOD) || callInterfaceResultModel.getCode() == Constants.SUCCESS_COOD) {
                isAddIntegralFlag = true;
                return isAddIntegralFlag;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAddIntegralFlag;
    }


    /**
     * 查询考勤告警信息处理之后的加分信息，然后发送消息给纪委
     */
    public void sendWorkMessageToUser() {
        date = LocalDate.now().minusDays(0);
        //查询加分成功后的加分信息
        List<AddIntegralDataModel> addIntegralAfterSuccess = addIntegralDao.queryAddIntegralAfterSuccess(date);
        addIntegralAfterSuccess.forEach(addIntegralDataModel -> {
            //调用发送消息接口
            boolean isSendWorkMessage = sendMessageService.useDingSendWorkMessage(addIntegralDataModel.getUserId(), addIntegralDataModel.getIntegral());
            if (isSendWorkMessage == false) {
                LogCollectManager.common(MessageFormat.format(Constants.ERR + "钉钉id为" + addIntegralDataModel.getUserId() + "的纪委，他的告警信息加分消息发送工作通知失败！", ""), Constants.DING_INDEX + INDEX);
            }
        });

    }

    /**
     * 加分job调用的加分方法
     *
     * @author 王云召
     * @since 2020年6月24日09:35:42
     */
    public ReturnT addIntegral(String num) {

        LogCollectManager.common(MessageFormat.format(Constants.BEGIN + "进入考勤加分", ""), Constants.DING_INDEX + INDEX);

        //1.计算分值
        int number = Integer.valueOf(num);
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "开始计算加分分值", ""), Constants.DING_INDEX + INDEX);
        int isCalu = addIntegralService.caluIntegral(number);

        if (isCalu == Constants.ZERO) {
            LogCollectManager.common(MessageFormat.format(Constants.LOG + "没有考勤数据需要计算加分", ""), Constants.DING_INDEX + INDEX);
        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "第一步---计算加分分值结束", ""), Constants.DING_INDEX + INDEX);


        //2.查询加分数据
        List<AddIntegralDataModel> addIntegralData = addIntegralService.queryAddIntegralData();
        if (addIntegralData.isEmpty()) {
            LogCollectManager.common(MessageFormat.format(Constants.LOG + "考勤打卡加分全部完成，无加分数据,不用调用加分接口", ""), Constants.DING_INDEX + INDEX);
        }

        //3.查询之前纪委处理考勤告警消息的加分数据
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "查询之前纪委处理考勤告警消息的加分数据", ""), Constants.DING_INDEX + INDEX);
        List<AddIntegralDataModel> handleAlarmIntegralData = addIntegralService.queryHandleAlarmIntegralData();
        if (handleAlarmIntegralData.isEmpty()) {
            LogCollectManager.common(MessageFormat.format(Constants.LOG + "纪委处理告警信息加分全部完成，无加分数据,不用调用加分接口", ""), Constants.DING_INDEX + INDEX);

        }
        //合并两次加分数据
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "开始合并两次加分数据", ""), Constants.DING_INDEX + INDEX);

        List<AddIntegralDataModel> allAddIntegralData = new ArrayList<>();
        allAddIntegralData.addAll(addIntegralData);
        allAddIntegralData.addAll(handleAlarmIntegralData);

        LogCollectManager.common(MessageFormat.format(Constants.LOG + " 合并完成---第二步，查询加分数据完成！", ""), Constants.DING_INDEX + INDEX);

        if (allAddIntegralData.isEmpty()) {
            LogCollectManager.common(MessageFormat.format(Constants.LOG + "加分全部完成，无加分数据,不用调用加分接口", ""), Constants.DING_INDEX + INDEX);
            return ReturnT.SUCCESS;
        }

        //4.调用加分接口
        boolean callInterfaceFlag = addIntegralService.callKernelAddIntegralInterface(allAddIntegralData);
        if (callInterfaceFlag) {
            // 修改数据库打卡结果状态为加分成功
            int isCheckSuccess = 0;
            int isHandleSuccess = 0;
            if (!addIntegralData.isEmpty()) {
                isCheckSuccess = addIntegralService.updateIsSuccess(addIntegralData);
            }
            //修改处理告警信息表的加分状态
            if (!handleAlarmIntegralData.isEmpty()) {
                isHandleSuccess = addIntegralService.updateIsHandSuccess(handleAlarmIntegralData);
            }

            //返回值为零代表没有更新成功
            if (isCheckSuccess + isHandleSuccess == Constants.ZERO) {
                LogCollectManager.common(MessageFormat.format(Constants.ERR + "调用加分接口成功，更新加分成功状态失败了。", ""), Constants.DING_INDEX + INDEX);
                return ReturnT.FAIL;
            }
        } else {
            LogCollectManager.common(MessageFormat.format(Constants.ERR + "调用加分接口失败", ""), Constants.DING_INDEX + INDEX);
            return ReturnT.FAIL;
        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "第三步，调用加分接口完成！", ""), Constants.DING_INDEX + INDEX);

        LogCollectManager.common(MessageFormat.format(Constants.END + "第三步，钉钉考勤加分成功！", ""), Constants.DING_INDEX + INDEX);
        //给纪委发送工作通知，通知其昨天的加分情况，
        this.sendWorkMessageToUser();
        return ReturnT.SUCCESS;
    }


    /**
     * @param number
     * @description: 斐波那契数列
     * @return: int
     * @author: 马珂
     * @time: 2020/8/23 16:18
     */
    private int fibonacci(int number) {
        if (number == 2) {
            return -1;
        } else if (number == 3) {
            return -1;
        } else if(number>14){
            return -300;
        }else{
            return fibonacci(number - 1) + fibonacci(number - 2);
        }
    }

    /**
     * @param
     * @description: 多次违纪减分job
     * @return: com.xxl.job.core.biz.model.ReturnT
     * @author: 马珂
     * @time: 2020/8/23 15:38
     */
    public ReturnT BreachDeductPoints() {

        //判断今天是否是一号
        if(new java.util.Date().getDay()==2)
        {
            int updateDeleteAllBreachNumberCount = breachNumberDao.updateDeleteAllBreachNumber();
            //判断删除操作是否成功
            if(updateDeleteAllBreachNumberCount>0)
            {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "月初删除记录成功", ""), Constants.DING_INDEX + INDEX);
            }
            else {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "月初删除记录失败", ""), Constants.DING_INDEX + INDEX);
                return ReturnT.FAIL;
            }

        }



        // 1、获取当天违纪人员以及本机总计的违纪次数
        List<BreachNumberModel> breachResults = punchResultDao.getBreachNumberModel();
        //判断今天是否有违纪情况
        if (breachResults == null || breachResults.size() == 0) {
            LogCollectManager.common(MessageFormat.format(Constants.LOG + "今日没有违纪情况", ""), Constants.DING_INDEX + INDEX);
            return ReturnT.SUCCESS;
        }
        // 创建一个加分model集合
        List<AddIntegralDataModel> addIntegralDataModels = new ArrayList<>();
        //创建一个违纪记录集合，用于添加
        List<BreachNumberEntity> addBreachNumberEntitys = new ArrayList<>();
        //创建一个违纪记录集合，用于删除
        List<BreachNumberEntity> deleteBreachNumberEntities = new ArrayList<>();
        //创建一个违纪记录集合，用于更新
        List<BreachNumberModel> updateBreachNumberModels = new ArrayList<>();
        //违纪扣分记录
        List<BreachResultEntity> breachResultEntities = new ArrayList<>();

        for (BreachNumberModel boundedRangeModel : breachResults) {
            //判断违纪记录表中是否有记录
            //如果没有记录
            if (boundedRangeModel.getBreachNumberEntity() == null) {
                //该记录添加到添加集合中
                addBreachNumberEntitys.add(getBreachNumberEntity(boundedRangeModel));

                //减分记录和加分接口需要的实体
                Map<AddIntegralDataModel, BreachResultEntity> breachResultEntity = getBreachResultEntity(boundedRangeModel, 0);
                //加分接口的实体
                addIntegralDataModels.addAll(breachResultEntity.keySet());
                //减分记录的实体
                breachResultEntities.addAll(breachResultEntity.values());
            }
            //如果有记录
            else {
                //判断两次时间是否差七天
                //是的，大于七天：604800000=一星期的毫秒数
                if (new java.util.Date().getTime() - boundedRangeModel.getBreachNumberEntity().getUpdateTime().getTime() >= 604800000L) {
                    //删除违纪记录表
                    deleteBreachNumberEntities.add(boundedRangeModel.getBreachNumberEntity());
                    //该记录添加到添加集合中
                    addBreachNumberEntitys.add(getBreachNumberEntity(boundedRangeModel));

                    //减分记录和加分接口需要的实体
                    Map<AddIntegralDataModel, BreachResultEntity> breachResultEntity = getBreachResultEntity(boundedRangeModel, 0);
                    //加分接口的实体
                    addIntegralDataModels.addAll(breachResultEntity.keySet());
                    //减分记录的实体
                    breachResultEntities.addAll(breachResultEntity.values());
                } else {
                    //历史的违纪次数
                    int historyBreachNumberSum = boundedRangeModel.getBreachNumberEntity().getBreachNumberSum();

                    //减分记录和加分接口需要的实体
                    Map<AddIntegralDataModel, BreachResultEntity> breachResultEntity = getBreachResultEntity(boundedRangeModel, historyBreachNumberSum);
                    //加分接口的实体
                    addIntegralDataModels.addAll(breachResultEntity.keySet());
                    //减分记录的实体
                    breachResultEntities.addAll(breachResultEntity.values());

                    //更新次数
                    updateBreachNumberModels.add(boundedRangeModel);

                }
            }
        }


        if (addBreachNumberEntitys.size() != 0) {
            //插入违纪记录表
            int insertBreachNumberCount = breachNumberDao.insertBreachNumber(addBreachNumberEntitys);
            //判断上面增加操作是否成功
            if(insertBreachNumberCount>0)
            {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "对于违纪记录表的增加成功", ""), Constants.DING_INDEX + INDEX);
            }
            else {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "对于违纪记录表的增加失败", ""), Constants.DING_INDEX + INDEX);
                return ReturnT.FAIL;
            }
        }

        if (updateBreachNumberModels.size() != 0) {
            int updateNumberBreachNumberCount=breachNumberDao.updateNumberBreachNumber(updateBreachNumberModels);
            //判断上面修改操作是否成功
            if(updateNumberBreachNumberCount>0)
            {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "对于违纪记录表的修改成功", ""), Constants.DING_INDEX + INDEX);
            }
            else {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "对于违纪记录表的修改失败", ""), Constants.DING_INDEX + INDEX);
                return ReturnT.FAIL;
            }
        }

        if (deleteBreachNumberEntities.size() != 0) {
            int updateDeleteBreachNumberCount = breachNumberDao.updateDeleteBreachNumber(deleteBreachNumberEntities);
            //判断上面删除操作是否成功
            if(updateDeleteBreachNumberCount>0)
            {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "对于违纪记录表的删除成功", ""), Constants.DING_INDEX + INDEX);
            }
            else {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "对于违纪记录表的删除失败", ""), Constants.DING_INDEX + INDEX);
                return ReturnT.FAIL;
            }
        }




        //插入扣分记录表
        int insertBreachResultCount = breachResultDao.insertBreachResult(breachResultEntities);
        if(insertBreachResultCount>0) {
            LogCollectManager.common(MessageFormat.format(Constants.LOG + "对于违纪扣分表的增加成功", ""), Constants.DING_INDEX + INDEX);
        }
        else {
            LogCollectManager.common(MessageFormat.format(Constants.LOG + "对于违纪扣分表的增加失败", ""), Constants.DING_INDEX + INDEX);
            return ReturnT.FAIL;
        }




        //3、调用加分接口
        boolean callInterfaceFlag = addIntegralService.callKernelAddIntegralInterface(addIntegralDataModels);
        if (callInterfaceFlag) {
            // 4、更新违纪扣分结果表
            int updateBreachResult = breachResultDao.updateBreachResultIsSuccess(breachResultEntities);
            if (updateBreachResult > 0) {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "加分接口调用成功，更新多次违纪扣分结果成功", ""), Constants.DING_INDEX + INDEX);
            } else {
                LogCollectManager.common(MessageFormat.format(Constants.LOG + "加分接口调用成功更新多次违纪扣分结果失败", ""), Constants.DING_INDEX + INDEX);
                return ReturnT.FAIL;
            }
        } else {
            LogCollectManager.common(MessageFormat.format(Constants.ERR + "调用加分接口失败", ""), Constants.DING_INDEX + INDEX);
            return ReturnT.FAIL;
        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "调用加分接口完成！", ""), Constants.DING_INDEX + INDEX);

        LogCollectManager.common(MessageFormat.format(Constants.END + "修改违纪扣分结果表成功！", ""), Constants.DING_INDEX + INDEX);
        //给纪委发送工作通知，通知其昨天的加分情况，
        return ReturnT.SUCCESS;
    }


    /**
     * @param boundedRangeModel
     * @return
     */
    private BreachNumberEntity getBreachNumberEntity(BreachNumberModel boundedRangeModel) {

        BreachNumberEntity breachNumberEntity = new BreachNumberEntity();
        breachNumberEntity.setId(IdWorker.getIdStr());
        breachNumberEntity.setUserJifenId(boundedRangeModel.getUserJifenId());
        breachNumberEntity.setBreachNumberSum(boundedRangeModel.getBreachNumberSum());
        return breachNumberEntity;
    }


    /**
     * 减分记录和加分接口需要的实体
     * @param boundedRangeModel
     * @param breachCount
     * @return
     */
    private Map<AddIntegralDataModel,BreachResultEntity> getBreachResultEntity(BreachNumberModel boundedRangeModel, int breachCount) {
        HashMap<AddIntegralDataModel, BreachResultEntity> map = new HashMap<>();
        for (int i = breachCount+1; i <=boundedRangeModel.getBreachNumberSum()+breachCount ; i++) {
            if(i==1)
            {
                continue;
            }
            AddIntegralDataModel addIntegralDataModel = new AddIntegralDataModel();
            addIntegralDataModel.setId(IdWorker.getIdStr());
            addIntegralDataModel.setUserId(boundedRangeModel.getUserJifenId());
            addIntegralDataModel.setIntegral(fibonacci(i));
            addIntegralDataModel.setReason("由于总计第" + i + "次违纪，额外扣" + (-addIntegralDataModel.getIntegral() + "分"));

            BreachResultEntity resultEntity = new BreachResultEntity();
            resultEntity.setId(IdWorker.getIdStr());
            resultEntity.setUserJifenId(boundedRangeModel.getUserJifenId());
            resultEntity.setBreachNumber(i);
            resultEntity.setIntegral(addIntegralDataModel.getIntegral());
            resultEntity.setReason("由于总计第" + i + "次违纪，额外扣" + (-addIntegralDataModel.getIntegral() + "分"));
            map.put(addIntegralDataModel,resultEntity);
        }
        return map;
    }


}


