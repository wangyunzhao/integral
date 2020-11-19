package com.dmsdbj.integral.backstage.utils.http;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmsdbj.integral.backstage.model.CallInterfaceResultModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {
    @Value("${permission.autotoken}")
    private String autoName;

    private static String autoToken;

    public static String getAutoToken() {
        return autoToken;
    }

    @PostConstruct
    public void getAutoName() {
        autoToken = this.autoName;
    }

    //获取权限token
    public static String authToken() {
//        HttpUtils http= HttpUtils.post("https://dmsdbj.com/auth-web/access/login");
        HttpUtils http = HttpUtils.post(autoToken);
        http.addHeader("Content-Type", "application/json; charset=utf-8");
        Map<String, String> body = new HashMap<>();
        body.put("userCode", "superAdmin");
        body.put("password", "superAdmin");
        http.setParameter(JSON.toJSONString(body));
        ResponseWrap responseWrap = http.execute();
        CallInterfaceResultModel authTokenModel = JSON.parseObject(responseWrap.getString(), CallInterfaceResultModel.class);
        return authTokenModel.getData().getToken();
    }

    //获取钉钉token
    public static String getAccessToken(String appkey, String appsecret) {

        HttpUtils http = HttpUtils.get("https://oapi.dingtalk.com/gettoken?appkey=" + appkey + "&appsecret=" + appsecret);
        http.addHeader("Content-Type", "application/json; charset=utf-8");
        ResponseWrap execute = http.execute();
        String resultStr = execute.getString();
        Object parse = JSONObject.parse(resultStr);
        JSONObject jsonObject = (JSONObject) parse;
        return jsonObject.getString("access_token");

    }
}
