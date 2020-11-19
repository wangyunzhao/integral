package com.dmsdbj.integral.backstage.utils;

import com.alibaba.fastjson.JSONObject;
import com.dmsdbj.integral.backstage.model.DingReturnModel;
import com.dmsdbj.itoo.tool.httpclient.HTTPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class DingUtil {
    /**
     * 应用的唯一标识key
     */
//    @Value("${AppKey}")
    private static String appkey="dingt452prfmxlwv9dsx";
    /**
     * 应用的密钥
     */
//    @Value("${AppSecret}")
    private static String appsecret="HojvOyIB0eVdmLurfK4d8RNkxO_2JviKyT4MsKkpWVt81NkKRiP0Q0X_gXQlCKR-";

//    @Value("${AccessToken}")
    private static String accessTokenUrl="https://oapi.dingtalk.com/gettoken";

    /**
     * 获取token值
     * @return
     */
    public static  String getAccessToken() {
//          String appkey="dingt452prfmxlwv9dsx";
//        String appsecret="HojvOyIB0eVdmLurfK4d8RNkxO_2JviKyT4MsKkpWVt81NkKRiP0Q0X_gXQlCKR-";
        //判断唯一标识key和应用密钥是否为空
        if (StringUtils.isEmpty(appkey) || StringUtils.isEmpty(appsecret)) {
            log.warn("DingMessageUtil method is error,appkey or appsecret is null");
            return null;
        }
        String param = "appkey=" + appkey + "&appsecret=" + appsecret;
        //得到返回信息的AccessToken
        String sendGet = null;

        try {
            sendGet = HTTPUtils.sendGet(accessTokenUrl, param);
        } catch (Exception e){
            log.warn("send accessToken is failed");
        }
        JSONObject jsonAccessToken = JSONObject.parseObject(sendGet);
        DingReturnModel dingReturnModel=jsonAccessToken.toJavaObject(DingReturnModel.class);
        //如果返回不是0，则证明失败
        if(dingReturnModel.getErrcode()!=0)
        {
            log.warn("DingSendMessageUtil method is error,access_token is null , errmsg-{},appkey-{},appsecret-{}",dingReturnModel.getErrmsg(),appkey,appsecret);
            return null;
        }
        //返回token值
        return dingReturnModel.getAccessToken();
    }
}