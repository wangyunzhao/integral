package com.dmsdbj.integral.dingtalk.provider.service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dmsdbj.integral.dingtalk.utils.http.ResponseWrap;
import com.dmsdbj.integral.dingtalk.utils.http.TokenUtils;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 调钉钉接口获取用户信息
 *
 * @author 崔晓鸿
 * @since 2020年6月15日09:01:11
 */
@Service
@RefreshScope
public class UserInfoService {
    //钉钉唯一标识
    @Value("${AppKey}")
    private String appKey;
    @Value("${AppSecret}")
    private String appSecret;
    @Value("${permission.getUserInfo}")
    private String getUserInfoUrl;

    /**
     * 根据免登授权码获取用户信息
     * @param code 免登授权码
     * @return
     */
    public Object getUserInfo(String code) {
        DingTalkClient client = new DefaultDingTalkClient(getUserInfoUrl);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(code);
        request.setHttpMethod("GET");
        // 获取access_token
        String accessToken = TokenUtils.getAccessToken(appKey, appSecret);
        OapiUserGetuserinfoResponse response = null;
        try {
            // 执行请求
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        // 获取返回信息的body里的内容
        String userInfo = response.getBody();
        // 转为json
        JSONObject jsonObject = JSONObject.parseObject(userInfo);
        return jsonObject;
    }
}
