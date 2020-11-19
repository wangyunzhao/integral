package com.dmsdbj.integral.dingtalk.provider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.item.pojo.AlarmDetailEntity;
import com.dingtalk.item.pojo.ApproveDetailEntity;
import com.dingtalk.item.pojo.PunchResultEntity;

import com.dmsdbj.integral.dingtalk.model.ErrorInfoModel;
import com.dmsdbj.integral.dingtalk.utils.http.HttpUtils;
import com.dmsdbj.integral.dingtalk.utils.http.ResponseWrap;
import com.dmsdbj.integral.dingtalk.utils.http.TokenUtils;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 功能描述: 发送消息
 * @Author: keer
 * @Date: 2020/7/22 14:22
 * @Param:
 * @Return:
 */
@Service
@Slf4j
@RefreshScope
public class SendMessageService {

    @Value("${permission.sendmessage}")
    private String sendMessageAddress;

    @Value("${AppKey}")
    private String appKey;
    @Value("${AppSecret}")
    private String appSecret;

    @Value("${message.jiwei}")
    private String alarmFrontAddress;

    @Value("${permission.dingtalk}")
    private String dingTalkAddress;
    @Value("${message.attendanceMaintain}")
    private String attendanceMaintain;

    @Autowired
    private ResultFilteringService resultFilteringService;

    /**
     * 调用钉钉接口发送群消息
     *
     * @param chatId
     * @param msg
     * @return
     * @author dongke
     * @time 020年6月8日09:45:46
     */
    public boolean sendMessage(String chatId, JSON msg) {
        //获取钉钉token
        String token = TokenUtils.getAccessToken(appKey, appSecret);
        // 读取url
        String url = sendMessageAddress + token;
        // 有待优化
        HttpUtils sendMessageUrl = HttpUtils.post(url);
        JSONObject jsonObject = new JSONObject();
        String para = JSON.toJSONString(jsonObject);
        sendMessageUrl.setParameter(para);
        sendMessageUrl.addHeader("Content-Type", "application/json; charset=utf-8");
        ResponseWrap responseWrap = sendMessageUrl.execute();
        String punchResultStr = responseWrap.getString();
        Object parse = JSONObject.parse(punchResultStr);
        JSONObject object = (JSONObject) parse;
        return true;
    }

    /**
     * 曹祥铭-发送钉钉考勤异常消息
     *
     * @param names 拼接后考勤异常人员名字【张三-未打卡-未发审批】
     * @param nums  异常考勤人员的数量
     * @param cid   群ID
     * @return true
     * @throws Exception 异常
     */
    public boolean sendDingMessage(String names, String nums, String cid) throws Exception {

        //获得AccessToken
        DingTalkClient client = new DefaultDingTalkClient(dingTalkAddress);
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid("chat9d7d597dbb7fa6fbcf92f98411d158b3");
        OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
        OapiChatSendRequest.Oa oA = new OapiChatSendRequest.Oa();
        OapiChatSendRequest.Head head = new OapiChatSendRequest.Head();
        OapiChatSendRequest.Body body = new OapiChatSendRequest.Body();
        List<OapiChatSendRequest.Form> formList = new ArrayList<>();
        OapiChatSendRequest.Form form = new OapiChatSendRequest.Form();
        OapiChatSendRequest.Rich rich = new OapiChatSendRequest.Rich();
        form.setKey("本次考勤异常人员数量：");
        form.setValue((nums + "人"));
        formList.add(form);
        body.setForm(formList);
        body.setTitle(names);
        body.setAuthor("天赋吉运 · 积分项目组 · 技术支持");
        body.setContent("经系统检测 ，以上员工考勤异常，请及时处理！");
        rich.setNum("☞ 立即处理");
        body.setRich(rich);
        head.setText("考勤异常通知");
        head.setBgcolor("FFFF0000");
        oA.setHead(head);
        oA.setBody(body);
        //   oA.setPcMessageUrl(alarmFrontAddress);
        oA.setMessageUrl(alarmFrontAddress);
        msg.setMsgtype("oa");
        msg.setOa(oA);
        request.setMsg(msg);
        System.out.println(request);
        OapiChatSendResponse response = client.execute(request, TokenUtils.getAccessToken(appKey, appSecret));
        return true;
    }

    /**
     * 后续会删掉，重新抽取发送消息的方法，在测试的时候就先这样写，避免其他人也修改
     *
     * @param names
     * @param nums
     * @param cid
     * @param orgName
     * @return
     * @throws Exception
     */
    public boolean sendDingTeacherMessage(String names, String nums, String cid, String orgName) throws Exception {
        //获得AccessToken
        DingTalkClient client = new DefaultDingTalkClient(dingTalkAddress);
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(cid);
        OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
        OapiChatSendRequest.Oa oA = new OapiChatSendRequest.Oa();
        OapiChatSendRequest.Head head = new OapiChatSendRequest.Head();
        OapiChatSendRequest.Body body = new OapiChatSendRequest.Body();
        List<OapiChatSendRequest.Form> formList = new ArrayList<>();
        OapiChatSendRequest.Form form = new OapiChatSendRequest.Form();
        form.setKey("本次考勤异常人员数量：");
        form.setValue((nums + "人"));
        formList.add(form);
        body.setForm(formList);
        body.setTitle(names);
        body.setAuthor("天赋吉运 · 积分项目组 · 技术支持");
        body.setContent("经系统检测 ，以上员工考勤异常，请及时处理！");
        head.setText(orgName + "考勤异常通知");
        head.setBgcolor("FFFF0000");
        oA.setHead(head);
        oA.setBody(body);
        //   oA.setPcMessageUrl(alarmFrontAddress);
        oA.setMessageUrl(alarmFrontAddress);
        msg.setMsgtype("oa");
        msg.setOa(oA);
        request.setMsg(msg);
        System.out.println(request);
        OapiChatSendResponse response = client.execute(request, TokenUtils.getAccessToken(appKey, appSecret));
        return true;
    }

    /**
     * 曹祥铭-发送异常日志到积分项目组
     *
     * @param errInfo 处理后的异常日志
     * @return true
     */
    public boolean sendErrorMessage(String errInfo) {

        DingTalkClient client = new DefaultDingTalkClient(dingTalkAddress);
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(attendanceMaintain);
        OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
        OapiChatSendRequest.Oa oA = new OapiChatSendRequest.Oa();
        OapiChatSendRequest.Head head = new OapiChatSendRequest.Head();
        OapiChatSendRequest.Body body = new OapiChatSendRequest.Body();
        List<OapiChatSendRequest.Form> formList = new ArrayList<>();
        head.setText("考勤系统异常日志");
        head.setBgcolor("FFFF0000");
        OapiChatSendRequest.Form form5 = new OapiChatSendRequest.Form();
        form5.setKey("日期：");
        form5.setValue(LocalDateTime.now().toString());
        body.setTitle(errInfo);
        formList.add(form5);
        body.setAuthor("天赋吉运 · 积分项目组 · 技术支持");
        oA.setBody(body);
        oA.setMessageUrl("www.baidu.com");
        oA.setHead(head);
        msg.setMsgtype("oa");
        msg.setOa(oA);
        request.setMsg(msg);
        System.out.println(request);
        try {
            OapiChatSendResponse response = client.execute(request, TokenUtils.getAccessToken(appKey, appSecret));
            //清空异常日志
            resultFilteringService.clearErrList();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Boolean makeAndSendErrorMsg(List<ErrorInfoModel> list) {
        StringBuffer alarmNames = new StringBuffer();
        for (ErrorInfoModel model : list) {
            //拼接异常信息
            String errorInfo = "【" + model.getType() + "异常】" + "\n" + "名称：" + model.getName() + "\n" + "参数：" + model.getParam() + "\n" + "结果：" + model.getResult() + "\n" + "信息：" + model.getInfo() + "\n";
            alarmNames = alarmNames.append(errorInfo);
        }
        String strNames = alarmNames.toString();
        sendErrorMessage(strNames);
        return true;
    }

    /**
     * 纪委处理考勤异常信息加分成功后发送加分（减分）通知
     */
    public boolean useDingSendWorkMessage(String userId, Integer score) {
        boolean isSuccess = false;
        //获取钉钉token
        String token = TokenUtils.getAccessToken(appKey, appSecret);
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");

        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userId);
        request.setAgentId(298955593L);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

        msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
        msg.getActionCard().setTitle("积分工作通知");

        String date = LocalDate.now().getYear() + "年" + LocalDate.now().getMonthValue() + "月" + LocalDate.now().getDayOfMonth() + "日";
        String bodyStartAddStr = "**积分工作通知**  \n  " + date + "\n  及时处理了考勤告警消息，保证了同学的生命安全，奖励你";
        String bodyStartMinunsStr = "**积分工作通知**  \n  " + date + "\n  请及时处理考勤告警消息，保证同学的生命安全，本次消费了";
        String bodyEndStr = "分，加油哦！";
        if (score > 0) {
            String body = bodyStartAddStr + score + bodyEndStr;
            msg.getActionCard().setMarkdown(body);
        } else {
            String body = bodyStartMinunsStr + score * -1 + bodyEndStr;
            msg.getActionCard().setMarkdown(body);
        }
        msg.getActionCard().setSingleTitle("查看详情");
        msg.getActionCard().setSingleUrl("http://points2.dmsd.tech/integral-mobile3/");
        msg.setMsgtype("action_card");
        request.setMsg(msg);

        try {
            OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, token);
            isSuccess = true;
        } catch (ApiException e) {
            e.printStackTrace();
        } finally {
            return isSuccess;
        }

    }


    /**
     * 功能描述: 发送工作通知
     * @Author: keer
     * @Date: 2020/7/22 14:24
     * @Param: [names, nums, cid, orgName]
     * @Return: java.lang.Boolean
     */
    public Boolean sendWorkMessage(String names, String nums, String cid,String orgName) {
        Long anentId = 298955593L;
        String highestUserId = cid;

        DingTalkClient client = new
                DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(highestUserId);
        request.setAgentId(anentId);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        OapiMessageCorpconversationAsyncsendV2Request.OA oA = new OapiMessageCorpconversationAsyncsendV2Request.OA();
        OapiMessageCorpconversationAsyncsendV2Request.Head head = new OapiMessageCorpconversationAsyncsendV2Request.Head();
        OapiMessageCorpconversationAsyncsendV2Request.Body body = new OapiMessageCorpconversationAsyncsendV2Request.Body();

        List<OapiMessageCorpconversationAsyncsendV2Request.Form> formList = new ArrayList<>();
        OapiMessageCorpconversationAsyncsendV2Request.Form form = new OapiMessageCorpconversationAsyncsendV2Request.Form();
        form.setKey("本次考勤异常人员数量：");
        form.setValue((nums + "人"));
        formList.add(form);

        body.setTitle(names);
        body.setAuthor("天赋吉运 · 积分项目组 · 技术支持");
        body.setContent("经系统检测 ，以上员工考勤异常");
        head.setText(orgName+"考勤异常通知");
        head.setBgcolor("C1C1C1");
        oA.setHead(head);
        oA.setBody(body);
        oA.setMessageUrl(alarmFrontAddress);
        msg.setMsgtype("oa");
        msg.setOa(oA);
        request.setMsg(msg);

        try {
            String accessToken = TokenUtils.getAccessToken(appKey, appSecret);
            OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request,accessToken);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return true;
    }
}
