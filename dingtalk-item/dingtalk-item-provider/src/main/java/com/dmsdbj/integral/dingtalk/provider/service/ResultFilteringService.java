package com.dmsdbj.integral.dingtalk.provider.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dingtalk.item.pojo.*;
import com.dmsdbj.integral.dingtalk.model.AlarmHandleModel;
import com.dmsdbj.integral.dingtalk.model.ErrorInfoModel;
import com.dmsdbj.integral.dingtalk.model.ErrorUserInfoModel;
import com.dmsdbj.integral.dingtalk.model.MapModel;
import com.dmsdbj.integral.dingtalk.provider.dao.AlarmInfoDao;
import com.dmsdbj.integral.dingtalk.provider.dao.ApproveDetailDao;
import com.dmsdbj.integral.dingtalk.provider.dao.DisciplineUserDao;
import com.dmsdbj.integral.dingtalk.provider.dao.ResultFilteringDao;
import com.dmsdbj.integral.dingtalk.utils.TimestampUtil;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.tfjybj.framework.json.JsonHelper;
import com.tfjybj.framework.log.LogCollectManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 曹祥铭
 * @Description: 打卡结果过滤
 * @Date: Create in 15:21 2020/6/4
 */
@Service
@RefreshScope
public class ResultFilteringService {
    private static final String INDEX = "ResultFilter";
    @Autowired
    private ResultFilteringDao resultFilteringDao;
    @Autowired
    private PunchResultService punchResultService;
    @Autowired
    private ApproveDetailService approveDetailService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private AlarmInfoDao alarmInfoDao;
    @Autowired
    private ApproveDetailDao approveDetailDao;
    @Autowired
    private DisciplineUserDao disciplineUserDao;

    @Value("${message.teachersChat}")
    private String teacherChatAddress;

    private static final String NAME="name";

    private static final String ORG="org";

    /**
     * 收集日常日志
     */
    List<ErrorInfoModel> errorList = new ArrayList<>();

    public void resultFiltering() {
        List<PunchResultEntity> punchResultEntitys = null;
        try {
            LogCollectManager.common(MessageFormat.format(Constants.BEGIN + "开始查询打卡结果", ""), Constants.DING_INDEX + INDEX);
            //调用查询打卡结果的service，得到打卡结果集合
            punchResultEntitys = punchResultService.getPunchResult();
            LogCollectManager.common(MessageFormat.format(Constants.LOG + "调用查询打卡结果的接口-{0}  result <> {1}", "getPunchResult", JsonHelper.toJson(punchResultEntitys)), Constants.DING_INDEX + INDEX);
            resultFilterMaster(punchResultEntitys);
        } catch (Exception e) {
            //捕捉异常信息，打印日志
            //将异常信息发送到积分项目组群
            setErrMessage("punchResultService", Constants.NOTHING.toString(), Constants.NOTHING, e.getMessage(), Constants.INTERFACE_CALL);
            LogCollectManager.common(MessageFormat.format(Constants.ERR + "service <> {0} <> description <> 查询打卡结果，调用getPunchResult出现异常! <> message <> {1}", INDEX, e.getMessage()), Constants.DING_ERROR);
            sendMessageService.makeAndSendErrorMsg(errorList);
            errorList.clear();
            e.printStackTrace();
            return;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void resultFilterMaster(List<PunchResultEntity> punchResultEntitys) {
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN + "进入考勤异常处理", ""), Constants.DING_INDEX + INDEX);
        if (punchResultEntitys == null || punchResultEntitys.isEmpty()) {
            setErrMessage("punchResultService", Constants.NOTHING, Constants.NOTHING, Constants.NODATA, Constants.INTERFACE_CALL);
            LogCollectManager.common(MessageFormat.format(Constants.ERR + "service <> {0} <> description <> 查询打卡结果发生异常 <> message <> {1}", INDEX, "查询结果为空!"), Constants.DING_INDEX + INDEX);
            sendMessageService.makeAndSendErrorMsg(errorList);
            errorList.clear();
            return;
        }
        //更新告警表中的Is_past字段，表示之前的数据都已查过，不在查询
//        alarmInfoDao.updateAlarmIsPast();
        //拼接积分id，因为钉钉接口没有返回积分ID
        punchResultEntitys = insertJifenId(punchResultEntitys);
        //拼接完之后，判空，可能出现查不到积分Id的情况，将没有积分id的人的信息发送到积分项目组（数据错误）
        punchResultEntitys = checkNull(punchResultEntitys);
        //过滤得到打卡结果为正常的集合OnDuty
        List<PunchResultEntity> normalPunchResultEntityList = punchResultEntitys
                .stream()
                .filter(punchResultEntity -> Constants.NORMAL.equals(punchResultEntity.getTimeResult().trim()))
                .collect(Collectors.toList());
        //判空
        if (!normalPunchResultEntityList.isEmpty()) {
            //将打卡结果拼接上OnDuty和OffDuty
            List<PunchResultEntity> allNormalPunchResultEntityList = makeDutyTimeResult(normalPunchResultEntityList, Constants.NORMAL);
            //将处理后的正常考勤打卡的集合存入数据库
            //存库异常
            int insetPunchResultFlag = 0;
            try {
//                insetPunchResultFlag = resultFilteringDao.insertResultFiltering(allNormalPunchResultEntityList);
            } catch (Exception e) {
                e.printStackTrace();
                setErrMessage("insertResultFiltering", allNormalPunchResultEntityList.toString(), insetPunchResultFlag + "", e.getMessage(), Constants.INSERT);
            }
        }
        //过滤得到异常打卡结果集合
        List<PunchResultEntity> unNormalPunchResultEntityList = punchResultEntitys.stream()
                .filter(punchResultEntity -> !punchResultEntity.getTimeResult().trim().contains(Constants.NORMAL))
                .collect(Collectors.toList());
        //判空，没有任何异常的打卡，就不在执行以下检查
        if (unNormalPunchResultEntityList.isEmpty()) {
            return;
        }
        //先获得考勤异常用户的姓名，方便以后存库需要
        //查库异常
        Map<String, String> userNameMap = exchangeListToMap("name", unNormalPunchResultEntityList);
        //获得用户的组织机构
        //查库异常
        Map<String, String> orgMap = exchangeListToMap("org", unNormalPunchResultEntityList);
        //过滤出异常打卡集合中打卡结果不等于（NotSigned)的集合
        List<PunchResultEntity> unNotSignedPunchResultList = unNormalPunchResultEntityList.stream()
                .filter(unNormalPunchResultEntity -> !Constants.NOT_SIGNED.equals(unNormalPunchResultEntity.getTimeResult().trim()))
                .collect(Collectors.toList());
        LogCollectManager.common(MessageFormat.format(Constants.LOG+ "迟到早退的员工集合 <> {0}",JsonHelper.toJson(unNotSignedPunchResultList)),Constants.DING_INDEX + INDEX);
        //判断如果没有其他异常考勤的人，则不执行
        if (!unNotSignedPunchResultList.isEmpty()) {
            //存入打卡结果表
            //存库异常
            int unNotSignedFlag = 0;
            try {
//                unNotSignedFlag = resultFilteringDao.insertResultFiltering(unNotSignedPunchResultList);
            } catch (Exception e) {
                e.printStackTrace();
                setErrMessage("insertResultFiltering", unNotSignedPunchResultList.toString(), unNotSignedFlag + "", e.getMessage(), Constants.INSERT);
            }
            //将其他异常考勤的结果存入告警考勤表中
            //遍历异常考勤的集合，进行数据的合并
            List<AlarmDetailEntity> alarmDetailEntities = makeAlarmEntityList(unNotSignedPunchResultList, userNameMap, orgMap, "");
            //执行插入数据库的操作
            //存库异常
            int insertAlarmFlag = 0;
            try {
//                insertAlarmFlag = resultFilteringDao.insertAlarmDetail(alarmDetailEntities);
            } catch (Exception e) {
                e.printStackTrace();
                setErrMessage("insertResultFiltering", alarmDetailEntities.toString(), insertAlarmFlag + "", e.getMessage(), Constants.INSERT);
            }
        }
        //过滤出异常打卡集合中打卡结果为NotSigned的集合
        List<PunchResultEntity> notSignedPunchResultList = unNormalPunchResultEntityList.stream()
                .filter(punchResultEntity -> Constants.NOT_SIGNED.equals(punchResultEntity.getTimeResult().trim()))
                .collect(Collectors.toList());
        notSignedPunchResultList = notSignedPunchResultList.stream().peek(e -> e.setId(IdWorker.getIdStr())).collect(Collectors.toList());
        //判空
        if (!notSignedPunchResultList.isEmpty()) {
            //拿到userDingID数组
            List<String> userIdList = notSignedPunchResultList.stream()
                    .map(PunchResultEntity::getUserId)
                    .collect(Collectors.toList());
            List<PunchResultEntity> approvalPunchList = null;
            List<PunchResultEntity> noApprovalPunchList = null;
            try {
                //调用service
                //调用接口异常
                List<String> approveUserDingIdList = approveDetailService.getLeaveStatus(userIdList, notSignedPunchResultList.get(0).getBaseCheckTime());
                //找出未打卡已经请假的人
                approvalPunchList = notSignedPunchResultList.stream()
                        .filter(e -> approveUserDingIdList.contains(e.getUserId().trim()))
                        .peek(e -> e.setTimeResult(Constants.TIME_RESULT_LEAVE))
                        .collect(Collectors.toList());
                LogCollectManager.common(MessageFormat.format(Constants.LOG+"未打卡已经请假的员工集合 <> {0}",JsonHelper.toJson(approvalPunchList)),Constants.DING_INDEX+INDEX);
                //找出未打卡未请假的人
                noApprovalPunchList = notSignedPunchResultList.stream().filter(e -> !approveUserDingIdList.contains(e.getUserId().trim())).collect(Collectors.toList());
                LogCollectManager.common(MessageFormat.format(Constants.LOG+"未打卡未请假的的员工集合 <> {0}",JsonHelper.toJson(noApprovalPunchList)),Constants.DING_INDEX+INDEX);
            } catch (Exception e) {
                setErrMessage("approveDetailService", userIdList.toString(), Constants.NOTHING, e.getMessage(), Constants.INTERFACE_CALL);
                sendMessageService.makeAndSendErrorMsg(errorList);
                e.printStackTrace();
                return;
            }
            //拼接用户积分id
            //判空
            int notSignedFlag = 0;
            if (!approvalPunchList.isEmpty()) {
                approvalPunchList = insertJifenId(approvalPunchList);
                try {
//                    notSignedFlag = resultFilteringDao.insertResultFiltering(approvalPunchList);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrMessage("insertResultFiltering", approvalPunchList.toString(), notSignedFlag + "", e.getMessage(), Constants.INSERT);
                }
            }
            if (!noApprovalPunchList.isEmpty()) {
                noApprovalPunchList = insertJifenId(noApprovalPunchList);
                noApprovalPunchList= noApprovalPunchList.stream().peek(e->e.setTimeResult(e.getCheckType().trim()+e.getTimeResult())).collect(Collectors.toList());
                //拿到请假的人,将其存库，timeresult为Leave，存库
                //存到打卡表中
                int noApproveAndNotSigned = 0;
                try {
//                    noApproveAndNotSigned = resultFilteringDao.insertResultFiltering(noApprovalPunchList);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrMessage("insertResultFiltering", noApprovalPunchList.toString(), noApproveAndNotSigned + "", e.getMessage(), Constants.INSERT);
                }
                //将未打卡的用户信息存入告警考勤表
                //将未打卡用户的信息匹配上姓名
                List<AlarmDetailEntity> alarmDetailList = makeAlarmEntityList(noApprovalPunchList, userNameMap, orgMap, Constants.NO_APPROVE);
                //将未打卡未请假的人存入告警表中
                int alarmNotSignedFlag = 0;
                try {
//                    alarmNotSignedFlag = resultFilteringDao.insertAlarmDetail(alarmDetailList);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrMessage("insertAlarmDetail", alarmDetailList.toString(), alarmNotSignedFlag + "", e.getMessage(), Constants.INSERT);
                }
            }
        }
        //查询并发送告警信息
        queryAlarmInfo();
        //发送异常日志信息
        if (!errorList.isEmpty()) {
            sendMessageService.makeAndSendErrorMsg(errorList);
            errorList.clear();
        }
        LogCollectManager.common(MessageFormat.format(Constants.END+"考勤异常过滤结束!",""),Constants.INDEX+INDEX);
    }

    /**
     * 根据PunchResultEntity 构造告警表的实体
     *
     * @param list        PunchResultEntity集合
     * @param userNameMap key:user_ding id ,value:userName
     * @param orgMap      key:
     * @return AlarmDetailEntity集合
     */
    public List<AlarmDetailEntity> makeAlarmEntityList(List<PunchResultEntity> list, Map<String, String> userNameMap, Map<String, String> orgMap, String remarks) {
        String info = null;
        String timeResult = null;
        list = list.stream().peek(e -> {
            if (e.getTimeResult().trim().equals(Constants.NOT_SIGNED)) {
                e.setTimeResult(e.getCheckType().trim() + e.getTimeResult().trim());
            }
        }).collect(Collectors.toList());
        List<AlarmDetailEntity> alarmDetailEntities = new ArrayList<>();
        for (PunchResultEntity item : list
        ) {
            AlarmDetailEntity alarmDetailEntity = new AlarmDetailEntity();
            alarmDetailEntity.setId(IdWorker.getIdStr());
            alarmDetailEntity.setUserDingId(item.getUserId());
            alarmDetailEntity.setUserJifenId(item.getUserJifenId());
            alarmDetailEntity.setUserName(userNameMap.get(item.getUserId()));
            if (Constants.ONDUTY.equals(item.getCheckType().trim())) {
                info = Constants.ON_DUTY;
            } else if (Constants.OFFDUTY.equals(item.getCheckType().trim())) {
                info = Constants.OFF_DUTY;
            }
            if (StringUtils.isNotBlank(item.getProcInstId())) {
                alarmDetailEntity.setRemark(info + Constants.LEAVE + remarks);
            } else {
                //判断打卡异常类型
                timeResult = checkTimeResultType(item.getTimeResult().trim());
                alarmDetailEntity.setRemark(timeResult + remarks);
            }
            alarmDetailEntity.setOrgazition(orgMap.get(item.getUserJifenId()));
            alarmDetailEntities.add(alarmDetailEntity);
        }
        return alarmDetailEntities;
    }

    /**
     * 向告警处理表中插入信息
     * @param alarmDetailEntity
     */
    public void insertAlarmHandle(AlarmDetailEntity alarmDetailEntity) {
        AlarmHandleModel alarmHandleModel = new AlarmHandleModel();
        //查询考勤异常人员所在组织的纪委信息
        DisciplineUserEntity disciplineUserEntity = disciplineUserDao.queryUserInfoByOrgId(alarmDetailEntity.getOrgazition());
       if(disciplineUserEntity==null){
           disciplineUserEntity=new DisciplineUserEntity();
           disciplineUserEntity.setUserDingId("0");
           disciplineUserEntity.setUserName("系统");
       }
        alarmHandleModel.setOperatorName(disciplineUserEntity.getUserName());
        alarmHandleModel.setOperatorDingId(disciplineUserEntity.getUserDingId());
        alarmHandleModel.setId(IdWorker.getIdStr());
        alarmHandleModel.setAlarmId(alarmDetailEntity.getId());
        alarmHandleModel.setOperateResult("未处理");
        alarmHandleModel.setOperatorType(1);
        alarmInfoDao.insertAlarmHandel(alarmHandleModel);

    }

    /**
     * 判断上下班，并将类型类型改为对应的，上班-正常打卡，下班-正常打卡，上班-未打卡，下班-未打卡
     *
     * @param list       打卡结果集合
     * @param dutyType   上班类型
     * @param timeResult 打卡结果类型
     * @return 处理后的打卡结果集合
     */
    public List<PunchResultEntity> checkDuty(List<PunchResultEntity> list, String dutyType, String timeResult) {

        return list.stream().filter(PunchResultEntity -> dutyType.equals(PunchResultEntity.getCheckType().trim())).peek(normalPunch -> normalPunch.setTimeResult(dutyType + timeResult)).collect(Collectors.toList());
    }

    /**
     * 根据上下班的类型，设置对应的打卡结果
     * 例如上班正常打卡：OnDutyNormal，下班正常打卡：OffDutyNormal，上班未打卡：OnDutyNotSigned ……
     *
     * @param list       需要设置的打卡结果集
     * @param timeResult 打卡结果类型（Normal，NotSigned……
     * @return 打卡结果集合
     */
    public List<PunchResultEntity> makeDutyTimeResult(List<PunchResultEntity> list, String timeResult) {
        //判断打卡结果Normal的结果CheckType是否为OnDuty，如果是，则将timeResult改为 OnDutyNormal，反之则改为OffDutyNormal
        List<PunchResultEntity> allPunchResultEntityList = checkDuty(list, Constants.ONDUTY, timeResult);
        List<PunchResultEntity> offDutyPunchResultEntityList = checkDuty(list, Constants.OFFDUTY, timeResult);
        //打印日志需要的参数map
        Map<String, Object> map = new HashMap<>();
        map.put("allNormalPunchResultEntityList", allPunchResultEntityList);
        map.put("offDutyNormalPunchResultEntityList", offDutyPunchResultEntityList);
        allPunchResultEntityList.addAll(offDutyPunchResultEntityList);
        return allPunchResultEntityList;
    }

    /**
     * 查询告警表中未发送告警信息的集合，并进行分组，根据组织id准备发送告警信息
     *
     * @return 是否发送成功
     */
    public boolean queryAlarmInfo() {
        //记录群组ID
        String Cid = "chat9d7d597dbb7fa6fbcf92f98411d158b3 ";
        //查询今日未发送的告警信息
        List<AlarmDetailEntity> alarmDetailEntitiy = resultFilteringDao.queryNotSendMessage();
        //筛选出没有组织机构的人
        List<AlarmDetailEntity> alarmDetailEntities = alarmDetailEntitiy.stream()
                .filter(p -> p.getOrgazition() != null)
                .collect(Collectors.toList());
        List<AlarmDetailEntity> noOrgAlarmDetailEntities = alarmDetailEntitiy.stream()
                .filter(p -> p.getOrgazition() == null)
                .collect(Collectors.toList());
        if (!noOrgAlarmDetailEntities.isEmpty()) {
            //发送到积分考勤维护群
            List<ErrorUserInfoModel> errList = new ArrayList<>();
            for (AlarmDetailEntity model : noOrgAlarmDetailEntities
            ) {
                setErrMessage("【" + model.getUserName() + model.getRemark() + "】", Constants.DING_ID + model.getUserDingId() + "\n" + Constants.INTEGRAL_ID + model.getUserJifenId(), Constants.NOTHING, "组织ID为空", Constants.DATA);
            }
        }
        //根据组织机构id进行分组
        Map<String, List<AlarmDetailEntity>> alarmGroupMap = alarmDetailEntities.stream()
                .collect(Collectors.groupingBy(AlarmDetailEntity::getOrgazition));
        //存储所有异常考勤人员的姓名以及异常原因
        for (String groupId : alarmGroupMap.keySet()
        ) {
            StringBuffer alarmNames = new StringBuffer();
            List<AlarmDetailEntity> alarmList = alarmGroupMap.get(groupId);
            //该组织下异常考勤人员的数量
            String nums = String.valueOf(alarmList.size());
            //根据groupId，查询cid
//            Cid = resultFilteringDao.queryOrgChatId(groupId);
            //拼接姓名
            for (AlarmDetailEntity alarmEntity : alarmList
            ) {
                if (StringUtils.isNotEmpty(alarmEntity.getUserName())) {
                    String nameRemark = alarmEntity.getUserName() + "【" + alarmEntity.getRemark() + "】\n";
                    alarmNames = alarmNames.append(nameRemark);
                }
                //向告警处理表中插入需要纪委去处理的信息
                insertAlarmHandle(alarmEntity);
            }
            String strNames = alarmNames.toString();
            try {
                //开始发送消息
                sendMessageService.sendDingMessage(strNames, nums, "chat9d7d597dbb7fa6fbcf92f98411d158b3");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateAlarmInfo();
        return true;
    }

    /**
     * 向老师发送纪委处理的告警信息
     *
     * @return 是否发送成功
     */
    public boolean queryAlarmToTeacher() {
        boolean isSucessed = true;
        //查询今日所有的告警信息，将要发送给老师
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN + "发送告警信息给老师的方法", ""), INDEX);
        List<AlarmDetailEntity> alarmDetailEntities = alarmInfoDao.queryTodayAllInfoToTeacher();
        List<AlarmDetailEntity> noOrgAlarmDetailEntities = alarmDetailEntities.stream()
                .filter(p -> p.getOrgazition() == null)
                .collect(Collectors.toList());
        if (!noOrgAlarmDetailEntities.isEmpty()) {
            //发送到积分考勤维护群
            List<ErrorUserInfoModel> errList = new ArrayList<>();
            for (AlarmDetailEntity model : noOrgAlarmDetailEntities
            ) {
                setErrMessage("【" + model.getUserName() + model.getRemark() + "】", Constants.DING_ID + model.getUserDingId() + "\n" + Constants.INTEGRAL_ID + model.getUserJifenId(), Constants.NOTHING, "给老师发送告警信息时，组织ID为空", Constants.DATA);
            }
        }
        alarmDetailEntities = alarmDetailEntities.stream()
                .filter(p -> p.getOrgazition() != null)
                .collect(Collectors.toList());
        Map<String, List<AlarmDetailEntity>> alarmGroupMap = alarmDetailEntities.stream()
                .collect(Collectors.groupingBy(AlarmDetailEntity::getOrgazition));
        //存储所有异常考勤人员的姓名以及异常原因

        for (String groupId : alarmGroupMap.keySet()
        ) {

            StringBuffer alarmNames = new StringBuffer();
            List<AlarmDetailEntity> alarmList = alarmGroupMap.get(groupId);
            //该组织下异常考勤人员的数量
            String nums = String.valueOf(alarmList.size());
            String Cid = teacherChatAddress;
            String chatId = resultFilteringDao.queryOrgChatId(groupId);
//            updateAlarmInfoBySystem(alarmList);
            //拼接姓名
            for (AlarmDetailEntity alarmEntity : alarmList
            ) {
                //查询告警处理表中的信息
                AlarmHandleModel alarmHandleModel = queryAlarmHandleInfo(alarmEntity.getId());
                if (StringUtils.isNotEmpty(alarmEntity.getUserName())) {
                    String oper = alarmHandleModel.getOperatorName();
                    String nameRemark = alarmEntity.getUserName() + "\n" + "【异常类型】 " + alarmEntity.getRemark() + "\n" + "【状态反馈】 " + alarmHandleModel.getOperateResult() + "\n" + "【纪委姓名】 " + oper + "\n" + "\n";
                    alarmNames = alarmNames.append(nameRemark);
                }
            }
            String strNames = alarmNames.toString();
            try {
                //开始发送消息
                //获得期数
                String orgName = alarmInfoDao.queryOrgNameByChatId(chatId);
                String substring = orgName.substring(0, 3);
                sendMessageService.sendDingTeacherMessage(strNames, nums, Cid, substring);
            } catch (Exception e) {
                e.printStackTrace();
                isSucessed = false;
            }
        }
        if (isSucessed) {
            updateAlarmInfoTeacher();
        }
        return true;
    }


    /**
     * 查询告警处理表中的信息
     * @param alarmId
     * @return
     */
    public AlarmHandleModel queryAlarmHandleInfo(String alarmId) {

        return alarmInfoDao.queryAlarmHandleByAlarmId(alarmId);
    }

    /**
     * 向打卡结果中插入用户的积分id
     *
     * @param list 需要插入积分ID的集合
     * @return 插入积分ID后的集合
     */
    public List<PunchResultEntity> insertJifenId(List<PunchResultEntity> list) {
        //查库异常
        List<MapModel> jifenIdMap = resultFilteringDao.queryUserJiFenIdByDing(list);
        System.out.println(jifenIdMap);

        //将list转换为map
        Map<String, String> maps = jifenIdMap.stream()
                .collect(Collectors.toMap(MapModel::getKey, MapModel::getValue));
        return list.stream()
                .peek(punch -> punch.setUserJifenId(maps.get(punch.getUserId().trim())))
                .collect(Collectors.toList());
    }

    /**
     * 将list转为map
     *
     * @param type
     * @param punchList
     * @return
     */
    public Map<String, String> exchangeListToMap(String type, List<PunchResultEntity> punchList) {
        //查库异常
        List<MapModel> mapList = new ArrayList<>();
        //判断要生成map的类型，type=name,获得用户dingID和username的映射，type=org,生成用户dingID和用户组织机构的映射
        if (NAME.equals(type) ) {
            mapList = resultFilteringDao.queryUserNameByUserDingId(punchList);
        } else if (ORG.equals(type) ) {
            mapList = resultFilteringDao.queryOrganizationIdByUserId(punchList);
        }
        return mapList.stream().collect(Collectors.toMap(MapModel::getKey, MapModel::getValue));
    }

    /**
     * 检查积分ID是否存在null
     *
     * @param list 需要检查的打卡结果集合
     * @return 积分ID不为null的集合
     */
    public List<PunchResultEntity> checkNull(List<PunchResultEntity> list) {
        //判断是否出现null
        List<PunchResultEntity> emptyUserIdList = list.stream()
                .filter(map -> map.getUserJifenId() == null)
                .collect(Collectors.toList());
        if (!emptyUserIdList.isEmpty()) {
            //打印出没有查询到UserJifenID的人,并发送异常信息到钉钉组
            for (PunchResultEntity m : emptyUserIdList) {
                setErrMessage("【积分ID为null】", Constants.DING_ID + m.getUserId(), Constants.NOTHING, "拼接积分ID出错", Constants.DATA);
            }
        }
        return list.stream().filter(pun -> pun.getUserJifenId() != null).collect(Collectors.toList());
    }

    /**
     * 转换打卡结果，将英文转换为中文
     *
     * @param timeResult 应为打卡结果
     * @return 中文打卡结果
     */
    public String checkTimeResultType(String timeResult) {
        //查询数据库打卡规则设置
        List<GradeRuleEntity> gradeRuleEntities = alarmInfoDao.selectAllRule();
        //映射为map
        Map<String, String> allRules = gradeRuleEntities.stream()
                .collect(Collectors.toMap(GradeRuleEntity::getTimeResult, GradeRuleEntity::getRuleName));
        return allRules.get(timeResult);
    }

    /**
     * 设置错误信息内容
     *
     * @param name   出现错误对象名
     * @param param  参数
     * @param result 结果
     * @param info   错误详细信息
     * @param type   错误类型（接口调用，数据，存库，查库，更新）
     */
    public void setErrMessage(String name, String param, String result, String info, String type) {
        ErrorInfoModel interfaceModel = new ErrorInfoModel();
        interfaceModel.setName(name).setParam(param).setResult(result).setInfo(info).setType(type);
        errorList.add(interfaceModel);
    }

    //发送完告警信息后，更新告警表

    /**
     * 更新告警表
     *
     * @return 受影响的行数
     */
    public int updateAlarmInfo() {
        int flag = 0;
        try {
            flag = alarmInfoDao.updateAlarmIsSend();
        } catch (Exception e) {
            //更新异常
            setErrMessage("updateAlarmIsSend", Constants.NOTHING, flag + "", e.getMessage(), Constants.UPDATE);
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * ]
     * 发送告警信息给老师后，更新告警表
     *
     * @return 受影响的行数
     */
    public int updateAlarmInfoTeacher() {
        int flag = 0;

        try {
            flag = alarmInfoDao.updateAlarmIsSendToTeacher();
        } catch (Exception e) {
            //更新异常
            setErrMessage("updateAlarmIsSendToTeacher", Constants.NOTHING, flag + "", e.getMessage(), Constants.UPDATE);
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 清空异常日志的方法
     */
    public void clearErrList() {
        errorList.clear();
    }
}


