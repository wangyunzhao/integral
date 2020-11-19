package com.dmsdbj.integral.backstage.provider.service;

import com.alibaba.fastjson.JSONObject;
import com.dmsdbj.integral.backstage.model.UserPowerModel;
import com.dmsdbj.integral.backstage.utils.http.HttpUtils;
import com.dmsdbj.integral.backstage.utils.http.ResponseWrap;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UserPowerService {

    /**
     * 功能描述: 根据用户dingID判断是否有用户权限,最后返回用户的姓名，code，用户姓名，以及用户角色（项目组长，普通人员，积分组人员）-公共接口
     * @Author: keer
     * @Date: 2020/8/8 16:21
     * @Param: [userId]
     * @Return: boolean
     */
    public UserPowerModel hasPower(String dingId){

        //调用地址最后放到配置文件中
        String authenticationUrl = "http://192.168.60.109:8081/auth-web/loginThird/findLoginUserByCode/";
        String url = authenticationUrl+dingId;
        HttpUtils http = HttpUtils.get(url);
        //http.setParameter(JSON.toJSONString(dingId));
        http.addHeader("Content-Type","application/json; charset=utf-8");
        ResponseWrap responseWrap = http.execute();
        String authentication= responseWrap.getString();
        Object parse = JSONObject.parse(authentication);
        JSONObject object = (JSONObject) parse;
        String code = object.getString("code");
        if (!code.equals("0000") ){
            return null;
        }

        String userName = object.getJSONObject("data").getString("userName");
        String roleId = object.getJSONObject("data").getString("roleId");
        UserPowerModel userPowerModel = new UserPowerModel();
        userPowerModel.setCode(code);
        userPowerModel.setRole(roleId);
        userPowerModel.setUserName(userName);
        return userPowerModel;
    }
}
