package com.dmsdbj.integral.dingtalk.provider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.api.R;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceGetleavestatusRequest;
import com.dingtalk.api.response.OapiAttendanceGetleavestatusResponse;
import com.dingtalk.api.response.OapiUserListResponse;
import com.dingtalk.item.pojo.AlarmDetailEntity;
import com.dingtalk.item.pojo.ApproveDetailEntity;
import com.dingtalk.item.pojo.PunchResultEntity;
import com.dmsdbj.integral.dingtalk.model.*;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.ApproveDetailDao;
import com.dmsdbj.integral.dingtalk.provider.dao.ResultFilteringDao;
import com.dmsdbj.integral.dingtalk.utils.TimestampUtil;
import com.dmsdbj.integral.dingtalk.utils.http.HttpUtils;
import com.dmsdbj.integral.dingtalk.utils.http.ResponseWrap;
import com.dmsdbj.integral.dingtalk.utils.http.SecondAlarmUtil;
import com.dmsdbj.integral.dingtalk.utils.http.TokenUtils;
import com.taobao.api.ApiException;

import com.tfjybj.framework.log.LogCollectManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ApproveDetailController接口
 * 根据审批实例id调用钉钉接口获取所有审批存库并返回值
 *
 * @author 王梦瑶
 * @version 0.0.1
 * @since 2020年6月2日08:18:11
 */
@Service
@Slf4j
@RefreshScope
public class ApproveDetailService {

    @Value("${permission.approvaldetails}")
    private String approvalDetailsAddres;
    @Value("${AppKey}")
    private String appkey;
    @Value("${AppSecret}")
    private String appsecret;
    @Autowired
    private ApproveDetailDao approveDetailDao;

    @Resource
    private PunchResultService punchResultService;

    @Resource
    private ResultFilteringService resultFilteringService;

    @Resource
    private AlarmInfoService alarmInfoService;

    @Resource
    private ResultFilteringDao resultFilteringDao;

    @Resource
    private SecondAlarmUtil secondAlarmUtil;

    @Autowired
    private SendMessageService sendMessageService;

    private static final String INDEX = "ApproveDetailService";

    /**
     * 循环审批实例id获得审批结果
     *
     * @param processInstanceId
     * @return
     */
    public List<ApproveDetailEntity> queryApproveDetail(String... processInstanceId) throws ParseException {
        if (processInstanceId != null && processInstanceId.length > 0) {
            //请求钉钉地址
            String url = approvalDetailsAddres + TokenUtils.getAccessToken(appkey, appsecret);
            List<ApproveDetailEntity> approveDetailEntities = new ArrayList<>();
            for (String processId : processInstanceId) {
                ApproveDetailEntity approveDetailEntity = new ApproveDetailEntity();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("process_instance_id", processId);
                HttpUtils post = HttpUtils.post(url);
                post.setParameter(JSON.toJSONString(jsonObject));
                post.addHeader("Content-Type", "application/json; charset=utf-8");
                ResponseWrap execute = post.execute();
                //得到钉钉返回的数据
                String resultStr = execute.getString();
                Object parse = JSONObject.parse(resultStr);
                //得到钉钉返回的数据转成JSONObject类型
                JSONObject object = (JSONObject) parse;
                //得到form_component_values
                String approveModelStr = object.getJSONObject("process_instance").getString("form_component_values");
                //得到的form_component_values赋给ApproveModel集合
                List<ApproveModel> approveModels = JSONObject.parseArray(approveModelStr, ApproveModel.class);
                //过滤component_type=TextareaField
                List<ApproveModel> textField = approveModels.stream().filter(item -> "TextareaField".equals(item.getComponent_type()
                )).collect(Collectors.toList());
                if (null != textField && textField.size() > 0)
                    approveDetailEntity.setRemark(textField.get(0).getValue());
                //过滤component_type=DDHolidayField
                List<ApproveModel> tExtValue =
                        approveModels.stream().filter(item -> "DDHolidayField".equals(item.getComponent_type())).collect(Collectors.toList());
                // 得到钉钉后台返回的value
                String values = tExtValue.get(0).getValue();
                String[] split = values.split(",");
                List<String> strList = new ArrayList<>();
                //以逗号切割 value": "[\"2020-06-11 08:00\",\"2020-07-01 21:30\",158.94,\"hour\",\"学校上课\",\"请假类型\"]"
                for (String s : split) {
                    String s1 = s.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，: \\- ,。？“”]+", "");
                    strList.add(s1);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date startTime = sdf.parse(strList.get(0));
                approveDetailEntity.setStartTime(startTime);
                Date endTime = sdf.parse(strList.get(1));
                approveDetailEntity.setEndTime(endTime);
                approveDetailEntity.setLeaveType(strList.get(4));

                //得到钉钉后台的创建时间
                String operateCreateTime = object.getJSONObject("process_instance").getString("create_time");
                //得到钉钉后台的完成时间
                String operateFinishTime = object.getJSONObject("process_instance").getString("finish_time");
                //得到钉钉后台的结果
                String result = object.getJSONObject("process_instance").getString("result");
                //得到钉钉定后台的组织id
                String organizationId = object.getJSONObject("process_instance").getString("originator_dept_id");
                //得到钉钉后台的状态
                String status = object.getJSONObject("process_instance").getString("status");
                String title = object.getJSONObject("process_instance").getString("title");
                //得到钉钉后台的operation_recordslist

                List<ApproveModels> approveModelList = null;
                try {
                    String operationRecords = object.getJSONObject("process_instance").getString("operation_records");
                    approveModelList = JSONObject.parseArray(operationRecords, ApproveModels.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("-------------queryApproveDetail,调用钉钉接口operationRecords报错");
                }
                //从operation_recordslist得到userid
                approveDetailEntity.setOriginatorUserid(approveModelList.get(0).getUserid());
                approveDetailEntity.setId(IdWorker.getIdStr());
                approveDetailEntity.setOperateCreateTime(sdf.parse(operateCreateTime));
                approveDetailEntity.setOperateFinishTime(sdf.parse(operateFinishTime));
                approveDetailEntity.setResult(result);
                approveDetailEntity.setTitle(title);
                approveDetailEntity.setStatus(status);
                approveDetailEntity.setProcessInstanceId(processId);
                approveDetailEntity.setOrganizationId(organizationId);
                List<String> approveDetailflag = approveDetailDao.queryProcessInstanceId(processId);

                if (approveDetailflag != null && approveDetailflag.size() > 0) {
                    approveDetailDao.deleteProcessInstanceId(approveDetailflag.get(0));
                }
                approveDetailEntities.add(approveDetailEntity);
            }
            approveDetailDao.insertApproval(approveDetailEntities);
            return approveDetailEntities;
        }

        return null;
    }

    /**
     * 查询当天已同意，且请假结束时间小于当前时间（job执行的时间）审批
     *
     * @param beginTime 获取配置文件中的时间（上午默认是当天 8点  ，下午默认是 当天 13点）
     * @return
     * @author fjx
     */
    public List<ApproveDetailEntity> selectAllApproveDetailAgree(String beginTime) {
        return approveDetailDao.selectAllApproveDetailAgree(beginTime);
    }

    /**
     * 根据钉钉id查询请假状态
     *
     * @return
     */
    public List<String> getLeaveStatus(List<String> userList, LocalDateTime baseCheckTime) {
        List<String> leaveDingIds = new ArrayList<>();
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN + "进入考根据钉钉id查询请假状态", ""),
                Constants.DING_INDEX + INDEX);
        //获取钉钉token
        String token = TokenUtils.getAccessToken(appkey, appsecret);
        if (token.isEmpty()) {
            LogCollectManager.common(MessageFormat.format(Constants.ERR + "获取钉钉token为空", ""),
                    Constants.DING_INDEX + INDEX);
            return null;
        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "开始调用钉钉第三方接口，查询到的请假人放到一个集合中！", ""),
                Constants.DING_INDEX + INDEX);
        //循环调用询请加状态的接口，将每次的返回结果合并到一个结果集
        for (int i = 0; i < userList.size(); i = i + Constants.TWENTYI) {
            //将int类型的i转为long
//            Long offSet = Long.parseLong(Integer.toBinaryString(i));
            Long offSet = Long.parseLong(Integer.toString(i));
            //调用查询请加状态的接口
            List<String> leaveDingId = getLeave(token, userList, offSet, baseCheckTime);
            //将结果加入到总的集合
            leaveDingIds.addAll(leaveDingId);
        }
        LogCollectManager.common(MessageFormat.format(Constants.LOG + "调用钉钉第三方接口结束，所有请假人已经放到一个集合中！" + leaveDingIds, ""),
                Constants.DING_INDEX + INDEX);
        LogCollectManager.common(MessageFormat.format(Constants.END + "考根据钉钉id查询请假状态--完成", ""),
                Constants.DING_INDEX + INDEX);
        return leaveDingIds;
    }

    /**
     * 调用查询请假的得钉钉接口
     *
     * @author 王云召
     * @since 2020年6月24日09:35:42
     */
    private List<String> getLeave(String token, List<String> dingIdlist, Long offset, LocalDateTime baseCheckTime) {
        //初始化返回值的结果集
        List<String> userDingIds = new ArrayList<>();
        // 1.准备钉钉接口需要的参数
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getleavestatus");
        OapiAttendanceGetleavestatusRequest req = new OapiAttendanceGetleavestatusRequest();
        String userId = String.join(",", dingIdlist);
        //使用基准时间计算查询请假状态的开始时间和结束时间，开始时间和结束时间与请假时间有交集就可以
        Long startTime =
                LocalDateTime.from(baseCheckTime.minusMinutes(Constants.FIVE)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long endTime =
                LocalDateTime.from(baseCheckTime.plusMinutes(Constants.FIVE)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        req.setUseridList(userId);
        //开始时间和结束时间与请假时间有交集就可以
        req.setStartTime(startTime);
        req.setEndTime(endTime);
        //设置偏移量，offset为0，本次查询的就是0-19。返回集合中油20个请假实例
        req.setOffset(offset);
        //分页，一次最多查20个
        req.setSize(Constants.TWENTY);
        OapiAttendanceGetleavestatusResponse rsp = null;
        try {
            //执行查询请假状态的接口
            rsp = client.execute(req, token);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        if (rsp.getErrcode() == Constants.ZERO) {
            //反序列化返回值，返回值为请假人的钉钉他其他信息
            DingLeavesStatusModel dingLeavesStatusModel = JSON.parseObject(rsp.getBody(), DingLeavesStatusModel.class);
            //重新生成新的集合，集合中只是请假人的钉钉id
            userDingIds = dingLeavesStatusModel.getResult().getLeaves_tatus().stream()
                    .map(leaveStatusModel -> leaveStatusModel.getUserid())
                    .collect(Collectors.toList());
        }
        return userDingIds;

    }

    /**
     * @return java.util.List<com.dingtalk.item.pojo.PunchResultEntity>
     * @Description // 构造打卡结果表
     * @Param [list]
     * @Author fjx
     * @Date 2020-06-29 10:51
     **/

    public List<PunchResultEntity> makePunchResultEntityList(List<PunchResultEntity> list) {
        // 获取应该销卡时间
        LocalDateTime baseChecktime = secondAlarmUtil.toChange();
        List<PunchResultEntity> punchResultEntities = new ArrayList<>();
        for (PunchResultEntity item : list) {
            PunchResultEntity punchResultEntity = new PunchResultEntity();
            punchResultEntity.setId(IdWorker.getIdStr());
            punchResultEntity.setUserId(item.getUserId());
            punchResultEntity.setUserJifenId(item.getUserJifenId());
            punchResultEntity.setBaseCheckTime(baseChecktime);
            punchResultEntity.setTimeResult("LeaveNotSigned");
            punchResultEntities.add(punchResultEntity);
        }
        return punchResultEntities;
    }

    /**
     * @return void
     * @Description //二次报警处理逻辑
     * @Param [beginTime] 获取配置文件中的时间（上午默认是当天 8点  ，下午默认是 当天 13点）
     * @Author fjx
     * @Date 2020-06-13 11:23
     **/
    public void secondAlarm(String beginTime) throws ParseException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 1. 获取纪委已经同意的两小时审批信息
        List<ApproveDetailEntity> listApproveDetail = selectAllApproveDetailAgree(beginTime);

        // 1.1. 筛选请假人员的ding_id,放入数组中
        String[] userIdList = listApproveDetail.stream()
                .map(approveDetailEntity -> approveDetailEntity.getOriginatorUserid())
                .collect(Collectors.toList()).toArray(new String[listApproveDetail.size()]);

        if (userIdList.length == 0) {
            log.info("没有人请假，或有人请假未经过纪委同意");
            return;
        } else {
            // 2. 调用打卡结果接口，查询未销卡人员信息，插入报警表

            // 2.1 调用打卡结果接口，获取打卡结果，只要begin_time 和 workDateTo 是当天时间，返回的就是当天信息
            String workDateTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            List<PunchResultEntity> punchResultEntities = punchResultService.queryRecordResult(beginTime, workDateTo,
                    userIdList);

            //2.2 筛选未打卡人员信息（上午只会筛选 10：15之前的 ， 下午筛选 4点45之前的）
            List<PunchResultEntity> punchResultCollect ;

            //将未打卡集合按照UserID分组，然后将每组的打卡结果按照时间倒序排序，再取出第一条
            Map<String, PunchResultEntity> collects =
                    punchResultEntities.stream()
                            .sorted(Comparator.comparing(PunchResultEntity::getBaseCheckTime).reversed())
                            .collect(Collectors.groupingBy(PunchResultEntity::getUserId, Collectors.collectingAndThen(Collectors.toList(), value -> value.get(0))));
            //将map里面的value，转换成list
            List<PunchResultEntity> notSignedLists = new ArrayList<>(collects.values());
            punchResultCollect =
                    notSignedLists.stream()
                            .filter(punchResultEntity -> "NotSigned".equals(punchResultEntity.getTimeResult().trim()))
                            .collect(Collectors.toList());
            if (punchResultCollect.size() == 0) {
                log.info("全部销卡");
            } else {
                // 根据钉钉id 查询积分id
                List<MapModel> mapJiFenId = resultFilteringDao.queryUserJiFenIdByDing(punchResultCollect);
                Map<String, String> maps = mapJiFenId.stream().collect(Collectors.toMap(MapModel::getKey,
                        MapModel::getValue));

                // 得到一个带有积分id的打卡结果集合
                List<PunchResultEntity> collect = punchResultCollect.stream()
                        .peek(punchResultEntity -> punchResultEntity.setUserJifenId(maps.get(punchResultEntity.getUserId())))
                        .collect(Collectors.toList());

                // 根据钉钉id查询用户姓名
                String name = "name";
                Map<String, String> mapName = resultFilteringService.exchangeListToMap(name, collect);

                // 根据钉钉id查询组织id
                String org = "org";
                Map<String, String> mapOrg = resultFilteringService.exchangeListToMap(org, collect);

                String remarks = "未销卡";
                // 构造报警表实体
                List<AlarmDetailEntity> alarmDetailEntities =
                        resultFilteringService.makeAlarmEntityList(collect, mapName, mapOrg, remarks);

                // 构造打开结果表
                List<PunchResultEntity> punchResultEntities1 = makePunchResultEntityList(collect);
                // 插入打开结果表
                punchResultService.insertPunchResult(punchResultEntities1);
                // 插入报警表
                resultFilteringDao.insertAlarmDetail(alarmDetailEntities);
                // 3. 推送报警信息（调用的祥铭的接口）
                resultFilteringService.queryAlarmInfo();
            }
        }
    }

    /**
     * 查询今日请假的人，并将审批详情插入数据库
     */
    public void approveMethods() {
        //去打卡结果表中，查询今天请假的人，timeResult=Leave
        List<String> leavePeople = approveDetailDao.queryLeavePeople();
        //判空
        if (leavePeople.isEmpty() || leavePeople == null) {
            //没有查到请假的人
            resultFilteringService.setErrMessage("queryLeavePeople", Constants.NOTHING, leavePeople.toString(),
                    "未查到请假的人", Constants.QUERY);
            return;
        }
        String[] leaves = leavePeople.toArray(new String[leavePeople.size()]);
        //调用查询打卡结果的service
        List<PunchResultEntity> punchResultEntityList = null;
        try {
            punchResultEntityList = punchResultService.queryRecordResult(TimestampUtil.getTodayDate(),
                    TimestampUtil.getTodayDate(), leaves);
        } catch (ParseException e) {
            e.printStackTrace();
            resultFilteringService.setErrMessage("queryRecordResult", leaves.toString(), e.getMessage(),
                    "调用钉钉打卡结果接口失败！", Constants.INTERFACE_CALL);
            sendMessageService.makeAndSendErrorMsg(resultFilteringService.errorList);
            resultFilteringService.errorList.clear();
            return;
        }//将未打卡集合按照UserID分组，然后将每组的打卡结果按照时间倒序排序，再取出第一条
        Map<String, PunchResultEntity> collect =
                punchResultEntityList.stream().sorted(Comparator.comparing(PunchResultEntity::getBaseCheckTime).reversed()).collect(Collectors.groupingBy(PunchResultEntity::getUserId, Collectors.collectingAndThen(Collectors.toList(), value -> value.get(0))));
        //将map里面的value，转换成list
        List<PunchResultEntity> notSignedLists = new ArrayList<>(collect.values());
        notSignedLists =
                notSignedLists.stream().filter(punchResultEntity -> "NotSigned".equals(punchResultEntity.getTimeResult().trim())).collect(Collectors.toList());
        //筛选出未打卡有审批实例id的用户集合
        punchResultEntityList =
                notSignedLists.stream().filter(punchResultEntity -> punchResultEntity.getProcInstId() != "" || punchResultEntity.getProcInstId() != null).collect(Collectors.toList());
        //判空
        String[] procInstIds = punchResultEntityList.stream()
                .map(approvalList -> approvalList.getProcInstId())
                .collect(Collectors.toList()).toArray(new String[punchResultEntityList.size()]);
        //调用查询审批详情的接口
        List<ApproveDetailEntity> approveDetailEntities = null;
        try {
            //调用接口异常
            approveDetailEntities = queryApproveDetail(procInstIds);
        } catch (ParseException e) {
            resultFilteringService.setErrMessage("queryApproveDetail", procInstIds.toString(),
                    approveDetailEntities.toString(), e.getMessage(), Constants.INTERFACE_CALL);
            sendMessageService.makeAndSendErrorMsg(resultFilteringService.errorList);
            e.printStackTrace();
            return;
        }

    }

}
