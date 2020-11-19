package com.dmsdbj.integral.backstage.provider.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dmsdbj.integral.backstage.model.DingOrganizationModel;
import com.dmsdbj.integral.backstage.model.OrganizationModel;
import com.dmsdbj.integral.backstage.model.OrganizationUserModel;
import com.dmsdbj.integral.backstage.model.UserListModel;
import com.dmsdbj.integral.backstage.utils.cache.JSONUtils;
import com.dmsdbj.integral.backstage.utils.cache.RedisContants;
import com.dmsdbj.integral.backstage.utils.cache.RedisUtil;
import com.dmsdbj.integral.backstage.utils.cache.http.HttpUtils;
import com.dmsdbj.integral.backstage.utils.cache.http.ResponseWrap;
import com.rabbitmq.tools.json.JSONUtil;
import com.taobao.api.ApiException;
import javafx.beans.value.ObservableBooleanValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: wmy
 * Date: 2020/8/11 15:00
 * Description:调用钉钉接口查询
 */
@Service
public class OrganizationUsersService {
    /**
     * 钉钉唯一标识
     */
    @Value("${AppKey}")
    private String appKey;
    @Value("${AppSecret}")
    private String appSecret;
    @Value("${permission.department}")
    private String queryDepartment;
    @Value("${permission.user}")
    private String queryUser;
    @Autowired
    private RedisUtil redisUtil;
    /**
    * @Description
    * @Author  wmy 调用钉钉接口查询组织id,查询各个组织下的所有人
    * @Date   2020/8/11 16:06
    * @Param
    * @Return
    * @Exception
    *
    */


    public List<OrganizationUserModel> selectOrganizationUser(){
        if (CollectionUtils.isEmpty(JSONObject.parseArray(redisUtil.get(RedisContants.organizationsCompanyUser)))){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setId("1");
        request.setHttpMethod("GET");
        String accessToken=getAccessToken();
        List<OapiDepartmentListResponse.Department> department;

        try {
            OapiDepartmentListResponse response = client.execute(request, accessToken);
            department = response.getDepartment();
            //过滤出行政部和全体在校生
            Set<OapiDepartmentListResponse.Department> Org = department.stream().filter(Department -> Department.getParentid()==58451033L || Department.getId()==59125175).collect(Collectors.toSet());


//            List<OrganizationUserModel> dingOrganization = Org.stream().map(temp -> {
//                OrganizationUserModel organizationModel = new OrganizationUserModel();
//                organizationModel.setId(temp.getId());
//                organizationModel.setName(temp.getName());
//                return organizationModel;
//            }).collect(Collectors.toList());

            List<OrganizationUserModel> organizationUserlist=new ArrayList<>();
            //存放所有user

            List<UserListModel> userInfos = new ArrayList<>();

            //获取每个组织结构的人员
            for (OapiDepartmentListResponse.Department dingOrg : Org) {
                OrganizationUserModel organizationUserModel=new OrganizationUserModel();
                organizationUserModel.setId(dingOrg.getId());
                organizationUserModel.setName(dingOrg.getName());
                userInfos=(getOrgUserinfo(accessToken, dingOrg.getId()));
                organizationUserModel.setUserList(userInfos);
                organizationUserlist.add(organizationUserModel);



            }
            redisUtil.set(RedisContants.organizationsCompanyUser, JSONUtils.toJSONString(organizationUserlist));
            return organizationUserlist;

        } catch (ApiException e) {
            e.printStackTrace();
        }
        }

        return JSON.parseArray(redisUtil.get(RedisContants.organizationsCompanyUser), OrganizationUserModel.class);

    }

  public List<OrganizationUserModel> queryOrganizationUsers(){
      //获取钉钉token
      String accessToken = getAccessToken();
      //存放所有user
      List<UserListModel> userInfos = new ArrayList<>();
      //调用钉钉接口获取组织机构

      List<DingOrganizationModel> dingOrganization=getDingDepartmtent(accessToken);
      //把组织结构
      dingOrganization = dingOrganization.stream().filter(item -> item.getParentid() ==100610510L).collect(Collectors.toList());
      List<OrganizationUserModel> organizationUserlist=new ArrayList<>();

      //获取每个组织结构的人员
      for (DingOrganizationModel dingOrg : dingOrganization) {
          OrganizationUserModel organizationUserModel=new OrganizationUserModel();
          organizationUserModel.setId(dingOrg.getId());
          organizationUserModel.setName(dingOrg.getName());
          organizationUserlist.add(organizationUserModel);
          userInfos=(getOrgUserinfo(accessToken, dingOrg.getId()));
          organizationUserModel.setUserList(userInfos);
          organizationUserlist.add(organizationUserModel);


      }

      return  organizationUserlist;

  }
    /**
     * 根据特定组织id获取钉钉所有部门(组织机构)
     *
     * @param accessToken token
     * @author: wmy
     * @return: List<DingOrganizationModel>
     * @date: 2020-06-10 15:20:13
     */
    private List<DingOrganizationModel> getDingDepartmtent(String accessToken) {
        HttpUtils http = HttpUtils.get("https://oapi.dingtalk.com/department/list?access_token="+accessToken);
        ResponseWrap responseWrap = http.execute();
        Object parse = JSONObject.parse(responseWrap.getString());
        JSONObject object = (JSONObject) parse;
        String department = object.getString("department");
        return JSONObject.parseArray(department, DingOrganizationModel.class);
    }

    /**
     * 查询某个组织机构的人员id+name
     *
     * @param accessToken    token
     * @param organizationId 组织机构id
     * @author: wmy
     * @return: List<UserListModel>  用户信息集合
     * @date: 2020-06-10 15:20:13
     */
    private List<UserListModel> getOrgUserinfo(String accessToken, Long organizationId) {
        HttpUtils http = HttpUtils.get("https://oapi.dingtalk.com/user/simplelist?access_token=" + accessToken + "&department_id=" + organizationId);
        ResponseWrap responseWrap = http.execute();
        Object parse = JSONObject.parse(responseWrap.getString());
        JSONObject object = (JSONObject) parse;
        String userInfo = object.getString("userlist");
        return JSONObject.parseArray(userInfo, UserListModel.class);
    }
    /**
     * 获取accesstoken
     *
     * @author: wmy
     * @return: String   token
     * @date: 2020-06-10 15:20:13
     */
    private String getAccessToken() {
        HttpUtils http = HttpUtils.get("https://oapi.dingtalk.com/gettoken?appkey=" + appKey + "&appsecret=" + appSecret);
        ResponseWrap responseWrap = http.execute();
        Object parse = JSONObject.parse(responseWrap.getString());
        JSONObject object = (JSONObject) parse;
        return object.getString("access_token");
    }

}
