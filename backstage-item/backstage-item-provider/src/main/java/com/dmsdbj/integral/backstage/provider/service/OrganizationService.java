package com.dmsdbj.integral.backstage.provider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.OrganizationModel;
import com.dmsdbj.integral.backstage.model.OrganizationUserModel;
import com.dmsdbj.integral.backstage.utils.cache.JSONUtils;
import com.dmsdbj.integral.backstage.utils.cache.RedisContants;
import com.dmsdbj.integral.backstage.utils.cache.RedisUtil;
import com.dmsdbj.integral.backstage.utils.http.HttpUtils;
import com.dmsdbj.integral.backstage.utils.http.ResponseWrap;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 组织结构树
 *
 * @author 崔晓鸿
 * @since 2020年8月13日08:19:29
 */
@Service
public class OrganizationService {
    @Value("${AppKey}")
    private String appKey;
    @Value("${AppSecret}")
    private String appSecret;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询组织结构
     *
     * @author 崔晓鸿
     * @since 2020年8月13日08:19:56
     */
    public Object selectOrganization() {
        // 先从redis获取组织结构 redis有直接返回

        if (redisUtil.hasKey(RedisContants.organizationsCompany)) {
            String organizations = redisUtil.get(RedisContants.organizationsCompany);
            return JSON.parseObject(organizations);
        }
        // 调用钉钉接口获取组织结构
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setId("1");
        request.setHttpMethod("GET");
        String accessToken = getAccessToken();
        List<OapiDepartmentListResponse.Department> department;
        Map organizationMap = new HashMap();
        try {

            OapiDepartmentListResponse response = client.execute(request, accessToken);
            department = response.getDepartment();
            // 过滤获取各期执行CEO，各期执行CEO的父id 100610510
            List<OapiDepartmentListResponse.Department> ceo = department.stream().filter(Department -> Department.getParentid() == 100610510L).collect(Collectors.toList());
            // 过滤多余字段，只要id和name
            List<OrganizationModel> ceoFilter = ceo.stream().map(temp -> {
                OrganizationModel organizationModel = new OrganizationModel();
                organizationModel.setId(temp.getId());
                organizationModel.setName(temp.getName());
                return organizationModel;
            }).collect(Collectors.toList());
            // 构造各期执行CEO的map
            Map cepMap = new HashMap();
            cepMap.put("pName", "各期执行CEO");
            cepMap.put("pList", ceoFilter);
            // 过滤获取各期工作委员会，各期工作委员会的父id 100521487
            List<OapiDepartmentListResponse.Department> commission = department.stream().filter(Department -> Department.getParentid() == 100521487L).collect(Collectors.toList());
            // 过滤多余字段，只要id和name
            List<OrganizationModel> commissionFilter = commission.stream().map(temp -> {
                OrganizationModel organizationModel = new OrganizationModel();
                organizationModel.setId(temp.getId());
                organizationModel.setName(temp.getName());
                return organizationModel;
            }).collect(Collectors.toList());
            // 构造各期工作委员会的map
            Map commissionMap = new HashMap();
            commissionMap.put("pName", "各期工作委员会");
            commissionMap.put("pList", commissionFilter);
            // 构造组织结构树的map
            organizationMap.put("commission", commissionMap);
            organizationMap.put("executeCEO", cepMap);
            redisUtil.set(RedisContants.organizationsCompany, JSONUtils.toJSONString(organizationMap));
            redisUtil.expire(RedisContants.organizationsCompany,86400);

        } catch (ApiException e) {
            e.printStackTrace();
        }

        return organizationMap;

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
