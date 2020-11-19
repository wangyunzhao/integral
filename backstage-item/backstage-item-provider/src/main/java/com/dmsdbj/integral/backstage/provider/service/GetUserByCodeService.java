package com.dmsdbj.integral.backstage.provider.service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetUserByCodeService {

    /**
     * 钉钉唯一标识
     */
    @Value("${AppKey}")
    private String appkey;
    @Value("${AppSecret}")
    private String appsecret;

    public String getUserByCode(String tmpAuthCode) throws ApiException {

        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/sns/getuserinfo_bycode");
        OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
        //req.setTmpAuthCode(tmpAuthCode); //测试成功再使用这个
        req.setTmpAuthCode(tmpAuthCode);

        String appkey = "dingoagqocas90h3lmypdk";
        String appsecret = "EpYI_KZB3t-0NGRPrfyt_OwRlPMcn1YuA7PY51sl_pE-ogROJDXf5dHhvFIK_yG-";
        OapiSnsGetuserinfoBycodeResponse response = client.execute(req,appkey,appsecret);


        String authentication= response.getBody();
        Object parse = JSONObject.parse(authentication);
        JSONObject object = (JSONObject) parse;
        String code = object.getString("code");
        //此处做一个判断

        String dingUnionid = object.getJSONObject("user_info").getString("unionid");
        return dingUnionid;
    }
}
