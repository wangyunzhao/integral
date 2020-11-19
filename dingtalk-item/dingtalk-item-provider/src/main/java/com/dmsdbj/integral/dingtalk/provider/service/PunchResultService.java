package com.dmsdbj.integral.dingtalk.provider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.item.pojo.PunchResultEntity;
import com.dmsdbj.integral.dingtalk.model.PunchResultTimeModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.PunchResultDao;
import com.dmsdbj.integral.dingtalk.utils.http.HttpUtils;
import com.dmsdbj.integral.dingtalk.utils.http.ResponseWrap;
import com.dmsdbj.integral.dingtalk.utils.http.TokenUtils;
import com.dmsdbj.itoo.tool.business.ItooResult;
import com.tfjybj.framework.json.JsonHelper;
import com.tfjybj.framework.log.LogCollectManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObject;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.dmsdbj.integral.dingtalk.provider.common.Constants.*;
import java.text.MessageFormat;


@Service
@Slf4j
@RefreshScope
/**
 * 功能描述: 查询打卡结果，处理完返回，两种打卡结果：1.根据planIds查询，2.根据时间段查询
 * @Author: keer + 王梦瑶
 * @Date: 2020/6/6 9:15
 */
public class PunchResultService {

    @Resource
    private PunchResultDao punchResultDao;

    /**
     * 钉钉唯一标识
     */
    @Value("${AppKey}")
    private String appkey;
    @Value("${AppSecret}")
    private String appsecret;


    /**
     * 查询排班打卡结果的钉钉url
     */
    @Value("${permission.qiyepunchresult}")
    private String qiYePunchResultAddress;


    /**
     * 根据时间查询打卡结果的钉钉url
     */
    @Value("${permission.punchresult}")
    private String punchResultAddress;

    /**
     * 读取操作员id
     */
    @Value("${punchResultOpUserId.opUserId}")
    private String opUserIdAddress;


    /**
     * 功能描述: 查询某次的排班打卡结果(给别人调用)
     * @Author: keer
     * @Date: 2020/6/9 10:47
     * @Return: java.util.List<com.dingtalk.item.pojo.PunchResultEntity>
     */
    public List<PunchResultEntity> getPunchResult(){
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN+"进入[根据planIds查询打卡结果]",""),Constants.DING_INDEX+INDEX);
        String opUserId = opUserIdAddress;
        //查询排班详情表中今日所有人的排班id，根据排班id获取打卡结果

        // 1.根据今天日期和时间，获取本次小于现在的时间以及只有isquery=0的用户的plan_id
        List<String> planIdLists = punchResultDao.queryPunchResult();
        //获取本次需要打卡人的钉钉id
        List<String> noNormalDingIds = punchResultDao.queryPunchResultDingId();
        // 如果planId为空
        if (planIdLists.size() == ZERO) {
            return null;
        }

        // 2.for循环--分组查询打卡结果（钉钉接口限制了每次的planID数=100）
        List<PunchResultEntity> punchResultEntityListAll = new ArrayList<>();
        //以100为一组，一共有几组
        Integer planIdNum = planIdLists.size();
        Integer groupNum = planIdNum/HUNDRED+1;
        for (int i = 1; i <= groupNum ; i++) {
            List<String> planIdGroupnew = planIdLists.stream().skip((i-1)*HUNDRED).limit(HUNDRED).collect(Collectors.toList());
            String planIdStringGroup = String.join(",",planIdGroupnew);

            // 3.调用钉钉考勤接口,获取用户打卡结果
            List<PunchResultEntity> punchResultEntityList = new ArrayList<>();
            punchResultEntityList = useDingGetPunchResultGroup(planIdStringGroup, opUserId);
            if (punchResultEntityList == null){
                // 如果调用钉钉接口失败，没有数据的话，直接停止
                break;
            }
            LogCollectManager.common(MessageFormat.format(Constants.LOG+"调用[根据planIds查询打卡结果]的钉钉接口-{0}  result <> {1}","getPunchResult", JsonHelper.toJson(punchResultEntityList)),Constants.DING_INDEX+INDEX);
            for (PunchResultEntity punchResultOne : punchResultEntityList) {
                punchResultEntityListAll.add(punchResultOne);
            }
        }
        //本次已经打卡人的钉钉id
        List<String> normalDingId = punchResultEntityListAll.stream().map(punchResultEntity -> punchResultEntity.getUserId()).collect(Collectors.toList());

        //筛选出未打卡的人
        List<String> notSignedDingIds = noNormalDingIds.stream().filter(noNormalDingId -> !normalDingId.contains(noNormalDingId)).collect(Collectors.toList());

        //创建未打卡人的数据，并且加入打开结果集

        notSignedDingIds.stream().forEach(noNormalDingId -> {
            PunchResultEntity punchResultEntity = new PunchResultEntity();
            punchResultEntity.setBaseCheckTime(punchResultEntityListAll.get(ZERO).getBaseCheckTime());

            punchResultEntity.setCheckType(punchResultEntityListAll.get(ZERO).getCheckType());
            punchResultEntity.setTimeResult("NotSigned");

            punchResultEntity.setUserId(noNormalDingId);
            punchResultEntityListAll.add(punchResultEntity);
        });

        // 4.更新查过的is_query字段，设置为1
        punchResultDao.updateQueryIdNew(planIdLists);

        LogCollectManager.common(MessageFormat.format(Constants.END+"[根据planIds查询打卡结果]结束", JsonHelper.toJson("punchResultEntityListAll")),INDEX);
        return punchResultEntityListAll;
    }


    /**
     * 功能描述: 调用钉钉接口查询排班打卡结果--在此类内部使用
     * @Author: keer
     * @Date: 2020/6/9 10:47
     * @Param: [planIds, opUserId]
     * @Return: java.util.List<com.dingtalk.item.pojo.PunchResultEntity>
     */
    public List<PunchResultEntity> useDingGetPunchResultGroup(String planIds ,String opUserId){
        //获取钉钉token
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN+"进入[根据planIds调用钉钉接口获取打卡结果]",""),Constants.DING_INDEX+INDEX);
        String token = TokenUtils.getAccessToken(appkey, appsecret);

        //调用钉钉接口获取用户打卡结果

        // 1.准备钉钉接口需要的参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("op_user_id", opUserId);
        jsonObject.put("schedule_ids", planIds);

        // 2.调用钉钉接口，获取打卡结果
        String url = qiYePunchResultAddress + token;
        HttpUtils punchResultDingUrl = HttpUtils.post(url);
        punchResultDingUrl.setParameter(JSON.toJSONString(jsonObject));
        punchResultDingUrl.addHeader("Content-Type", "application/json; charset=utf-8");
        ResponseWrap responseWrap = punchResultDingUrl.execute();

        // 3.处理钉钉接口返回的responseWrap
        String punchResultStr = responseWrap.getString();
        Object parse = JSONObject.parse(punchResultStr);
        JSONObject object = (JSONObject) parse;
        String resultstr = object.getString("result");
        List<PunchResultEntity> punchResultEntityList = JSONObject.parseArray(resultstr,PunchResultEntity.class);

        //   判断钉钉接口是否有数据，有就最后返回，没有就打印日志
        Object errCode = ((JSONObject) parse).get("errcode");
        String errorCode = errCode.toString();
        if (!ZERO_STRING.equals(errorCode)){
            //否则查询不成功，返回null
            LogCollectManager.common(MessageFormat.format(Constants.LOG+"调用[根据planIds查询打卡结果]的钉钉接口异常-{0}  result <> {1}"
                    ,"errorCode:"+errCode, JsonHelper.toJson(punchResultEntityList)),Constants.DING_INDEX+INDEX);
        }
        // 如果钉钉接口返回状态码为0说明查询成功
        LogCollectManager.common(MessageFormat.format(Constants.END+"[根据planIds调用钉钉接口获取打卡结果]结束", JsonHelper.toJson("punchResultEntityListAll")),INDEX);
        return punchResultEntityList;
    }


    /**
     * 循环userid调用钉钉打卡结果接口--王梦瑶--2020年6月3日09:37:20
     *
     * @param workDateFrom,workDateTo,userIdList
     * @return
     */
    public List<PunchResultEntity> queryRecordResult(String workDateFrom, String workDateTo, String[] userIdList) throws ParseException {

        List<PunchResultEntity> punchResultlist=new ArrayList<>();
        //循环userIdList
        for (String userId:userIdList
             ) {
           String[] userIdArr={userId};
        //调用getPunchResultTime方法
            List<PunchResultEntity> punchResultEntity = getPunchResultTime(workDateFrom, workDateTo, ZERO, TEN, userIdArr);
            punchResultlist.addAll(punchResultEntity);
        }


        return punchResultlist;
    }
    /**
     * 根据userIdList查询打卡结果方法
     *
     * @param workDateFrom        开始时间
     * @param workDateTo          结束时间
     * @param userIdList          查询人userid
     * @return punchResultEntities集合
     */
    private List<PunchResultEntity> getPunchResultTime(String workDateFrom, String workDateTo, int offset, int limit, String[] userIdList) throws ParseException {
        String url = punchResultAddress + TokenUtils.getAccessToken(appkey, appsecret);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workDateFrom", workDateFrom);
        jsonObject.put("workDateTo", workDateTo);
        jsonObject.put("userIdList", userIdList);
        jsonObject.put("offset", offset);
        jsonObject.put("limit", limit);
        HttpUtils post = HttpUtils.post(url);
        post.setParameter(JSON.toJSONString(jsonObject));
        post.addHeader("Content-Type", "application/json; charset=utf-8");
        ResponseWrap execute = post.execute();
        String resultStr = execute.getString();
        Object parse = JSONObject.parse(resultStr);
        JSONObject object = (JSONObject) parse;
        String recordresultModelStr = object.getString("recordresult");
        //从钉钉后台取出的数据放入punchResultTimeModel
        List<PunchResultEntity> punchResultEntities = JSONObject.parseArray(recordresultModelStr, PunchResultEntity.class);
        return punchResultEntities;
    }

  /**
   * @Description //将未销卡人员信息插入到打卡结果表
   * @Param [list]
   * @return java.util.List<com.dingtalk.item.pojo.PunchResultEntity>
   * @Author fjx
   * @Date 2020-06-29 10:27
   **/
    public int insertPunchResult(List<PunchResultEntity> list){
      return punchResultDao.insertPunchResult(list);
    }
}
