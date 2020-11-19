package com.dmsdbj.integral.backstage.provider.service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetUseridByUnionidRequest;
import com.dingtalk.api.response.OapiUserGetUseridByUnionidResponse;
import com.dmsdbj.integral.backstage.utils.http.TokenUtils;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetUserIdByUnionid {

    /**
     * 钉钉唯一标识
     */
    @Value("${AppKey}")
    private String appkey;
    @Value("${AppSecret}")
    private String appsecret;



    @Autowired
    private GetUserByCodeService getUserByCodeService;

    public String getUserIdByUnionid(String tmpAuthCode) throws ApiException {

        //调用接口获取unionid
        String dingUnionid = getUserByCodeService.getUserByCode(tmpAuthCode);

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getUseridByUnionid");
        OapiUserGetUseridByUnionidRequest request = new OapiUserGetUseridByUnionidRequest();
        request.setUnionid(dingUnionid);
        request.setHttpMethod("GET");

//        String appkey = "dingoagqocas90h3lmypdk";
//        String appsecret = "EpYI_KZB3t-0NGRPrfyt_OwRlPMcn1YuA7PY51sl_pE-ogROJDXf5dHhvFIK_yG-";
        String accessToken = TokenUtils.getAccessToken(appkey,appsecret);

        OapiUserGetUseridByUnionidResponse response = client.execute(request, accessToken);

        String authentication= response.getBody();
        Object parse = JSONObject.parse(authentication);
        JSONObject object = (JSONObject) parse;
        String code = object.getString("code");
        //加一个判断

        String userId = object.getString("userid");
        return userId;

    }
}
