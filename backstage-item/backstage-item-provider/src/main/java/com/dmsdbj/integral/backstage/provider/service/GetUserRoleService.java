package com.dmsdbj.integral.backstage.provider.service;

import com.alibaba.fastjson.JSONObject;
import com.dmsdbj.integral.backstage.model.UserCodeModel;
import com.dmsdbj.integral.backstage.model.UserPowerModel;
import com.dmsdbj.integral.backstage.model.UserRoleModel;
import com.dmsdbj.integral.backstage.utils.http.HttpUtils;
import com.dmsdbj.integral.backstage.utils.http.ResponseWrap;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserRoleService {

    @Autowired
    private GetUserIdByUnionid getUserIdByUnionid;


    public UserRoleModel getUserRole(UserCodeModel tmpAuthCode) throws ApiException {


        String authCode = tmpAuthCode.getCode();
        JSONObject jsonObject = new JSONObject();
        String dingId = getUserIdByUnionid.getUserIdByUnionid(authCode);
        //调用地址最后放到配置文件中
        String authenticationUrl = "http://192.168.60.109:8081/auth-web/access/noPwdLogin/";
        String url = authenticationUrl+dingId;
        HttpUtils http = HttpUtils.get(url);
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
        String token = object.getJSONObject("data").getString("token");
        String userCode = object.getJSONObject("data").getString("userCode");
        String id = object.getJSONObject("data").getString("id");
        String tenantId = object.getJSONObject("data").getString("companyId");
        UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setCode(code);
        userRoleModel.setRoleId(roleId);
        userRoleModel.setName(userName);
        userRoleModel.setUserCode(userCode);
        userRoleModel.setId(id);
        userRoleModel.setToken(token);
        userRoleModel.setTenantId(tenantId);
        return userRoleModel;

    }

}
