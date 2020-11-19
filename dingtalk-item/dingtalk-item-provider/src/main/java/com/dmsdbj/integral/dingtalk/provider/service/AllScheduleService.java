package com.dmsdbj.integral.dingtalk.provider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.item.pojo.ScheduleDetailEntity;
import com.dmsdbj.integral.dingtalk.model.TcAllusersDateModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.AllScheduleDao;
import com.dmsdbj.integral.dingtalk.utils.StringConvertUtil;
import com.dmsdbj.integral.dingtalk.utils.http.HttpUtils;
import com.dmsdbj.integral.dingtalk.utils.http.ResponseWrap;
import com.dmsdbj.integral.dingtalk.utils.http.TokenUtils;
import com.tfjybj.framework.json.JsonHelper;
import com.tfjybj.framework.log.LogCollectManager;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.dmsdbj.integral.dingtalk.provider.common.Constants.INDEX;

/**
 * 查询企业排班详情
 *
 * @author 梁佳宝
 * @since 2020年6月4日11点56分
 */
@Slf4j
@Service("allScheduleService")
@RefreshScope
public class AllScheduleService {
    private static final String INDEX = "toDBListSchedule";
    @Resource
    private AllScheduleDao allScheduleDao;
    @Value("${AppKey}")
    private String appkey;
    @Value("${AppSecret}")
    private String appSecret;
    @Value("${permission.allschedule}")
    private String urlAllSchedule;

    private static String hasMore;

    /**
     * 根据数字，查询某一天的企业排班数据,负数表示前几天优先从数据库中查询，正数表示未来几天从钉钉接口查询
     *
     * @param day
     * @return
     */
    public List<ScheduleDetailEntity> getAllSchedule(int day) throws ParseException {
        List<ScheduleDetailEntity> scheduleDetailEntities;
        if (day > 0) {
            //将数据从钉钉接口查询出来
            scheduleDetailEntities = getAllScheduleFormDingDing(day);
        } else {
            String date = LocalDate.now().plusDays(day).toString();
            //将数据库中数据查询出来
            scheduleDetailEntities = allScheduleDao.findAllByPlanCheckTime(date);
            //如果从数据库没有拿到数据则去钉钉接口调用
            if (scheduleDetailEntities.isEmpty()) {
                scheduleDetailEntities = getAllScheduleFormDingDing(day);
            }

        }
        return scheduleDetailEntities;
    }

    /**
     * 将将排班集合存储数据库
     *
     * @param day
     */
    public void toDbListSchedule(int day) {
        //获取某一天的企业排班全部数据
        List<ScheduleDetailEntity> allScheduleDetailEntityList = getAllScheduleFormDingDing(day);
        //allScheduleDetailEntityList不为null，插入数据库
        if (!allScheduleDetailEntityList.isEmpty()) {
            try {
                allScheduleDao.addSchedule(allScheduleDetailEntityList);
            }catch (Exception e) {
                LogCollectManager.common(MessageFormat.format(Constants.ERR+"service <> {0} <> description <> 排班数据插入数据库出现异常! <> message <> {1}",INDEX,e.getMessage()),Constants.DING_ERROR);
            }

        }
    }

    /**
     * 根据参数数字，获取某一天的企业排班全部数据
     *
     * @param day
     * @return
     */
    public List<ScheduleDetailEntity> getAllScheduleFormDingDing(int day)  {
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN+"进入获取企业排班信息",""),Constants.DING_INDEX+INDEX);
        int offset = Constants.offset;
        int count = Constants.count;
        List<ScheduleDetailEntity> allScheduleDetailEntityList = new ArrayList<>();
        List<ScheduleDetailEntity> partScheduleDetailEntityList;
        hasMore = "true";
        //从钉钉接口获取数据
        while ((Constants.hasMoreTrue).equals(hasMore)) {
            partScheduleDetailEntityList = getPartScheduleFormDingDing(offset, day);
            allScheduleDetailEntityList.addAll(allScheduleDetailEntityList.size(), partScheduleDetailEntityList);
            offset += count;
        }
        LogCollectManager.common(MessageFormat.format(Constants.END+"根据日期获取企业排班结束", ""),Constants.DING_INDEX+INDEX);
        return allScheduleDetailEntityList;
    }

    /**
     * 根据数字，从钉钉接口获取某一天的企业排班部分数据
     *
     * @param offset
     * @param day
     * @return
     */
    public List<ScheduleDetailEntity> getPartScheduleFormDingDing(Integer offset, int day) {
        List<ScheduleDetailEntity> scheduleDetailEntityList = new ArrayList<>();
        String accessToken = TokenUtils.getAccessToken(appkey,appSecret);
        String url = urlAllSchedule + accessToken;
        String nowDate = LocalDate.now().plusDays(day).toString() + " 06:00:00";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workDate", nowDate);
        jsonObject.put("offset", offset);
        if (!StringUtils.isEmpty(accessToken)) {
            try {
                //调用钉钉接口，取接口数据
                HttpUtils post = HttpUtils.post(url);
                post.setParameter(JSON.toJSONString(jsonObject));
                post.addHeader("Content-Type", "application/json;charset=UTF-8");
                //获取返回数据
                ResponseWrap execute = post.execute();
                String resultStr = execute.getString();
                JSONObject resultJson = JSONObject.parseObject(resultStr);
                //提取Json串中数据
                Object resultObject = resultJson.get("result");
                JSONObject resultJsonObject = JSONObject.parseObject(JSONObject.toJSONString(resultObject));
                JSONArray schedules = JSONObject.parseArray(JSONObject.toJSONString(resultJsonObject.get("schedules")));
                hasMore = JSONObject.toJSONString(resultJsonObject.get("has_more"));
                if (schedules.isEmpty()) {
                    //增添错误日志，getAllScheduleFormDingDing()方法，获取schedules没有数据
                    LogCollectManager.common(MessageFormat.format(Constants.ERR+"service <> {0} <> description <> 获取钉钉schedules排班集出现异常! ",INDEX),Constants.DING_ERROR);
                    //返回数据结果集
                    hasMore = "false";
                    return scheduleDetailEntityList;
                }
                //处理数据，转换为实体类型，并筛选出没有排版的数据
                scheduleDetailEntityList = schedules.stream().map(item -> {
                    String object = item.toString();
                    String res = StringConvertUtil.camelCaseName(object);
                    return JSON.parseObject(res, ScheduleDetailEntity.class);
                }).filter(item -> item.getCheckType() != null).collect(Collectors.toList());

                if (scheduleDetailEntityList.isEmpty()) {
                    //添加警告日志，当天没有排班信息
                    if(offset == Constants.offset) {
                        LogCollectManager.common(MessageFormat.format(Constants.LOG+"service <> {0} <> description <> 今天没有CheckType排班信息 ",INDEX),Constants.DING_ERROR);
                    }
                    //返回数据结果集
                    hasMore = "false";
                    return scheduleDetailEntityList;
                } else {
                    //遍历数据，加工存库
                    Set<String> useridset = new HashSet<>();
                    scheduleDetailEntityList.stream().map(item -> useridset.add(item.getUserId())).collect(Collectors.toList());

                    List<TcAllusersDateModel> list = allScheduleDao.findByDingId(useridset);
                    List<String> noInfolist = new ArrayList<>();
                    List<ScheduleDetailEntity> partScheduleDetailEntityList = scheduleDetailEntityList.stream().peek(item -> {
                        String dingId = item.getUserId();
                        TcAllusersDateModel tc = list.stream().filter(child -> dingId.equals(child.getDingid())).findAny().orElse(null);
                        if (tc != null){
                            item.setUserJifenId(tc.getId());
                            item.setUserCode(tc.getUsercode());
                            item.setUserName(tc.getUsername());
                        }
                        else {
                            noInfolist.add(dingId);
                        }
                    }).filter(item -> item.getUserName() != null).collect(Collectors.toList());
                    if (!noInfolist.isEmpty()) {
                        //打印日志警告
                        LogCollectManager.common(MessageFormat.format(Constants.ERR+"tc_alluser中没有此id相关信息", JsonHelper.toJson(noInfolist)),INDEX);
                    }
                    return partScheduleDetailEntityList;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        //添加错误日志，token为空
        LogCollectManager.common(MessageFormat.format(Constants.ERR+"service <> {0} <> description <> 查询企业排班信息结果，token为空!",INDEX),Constants.DING_ERROR);
        hasMore = "false";
        return scheduleDetailEntityList;
    }
}
