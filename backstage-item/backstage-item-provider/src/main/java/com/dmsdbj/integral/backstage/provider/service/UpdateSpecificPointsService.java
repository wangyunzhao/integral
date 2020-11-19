package com.dmsdbj.integral.backstage.provider.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiUserGetDeptMemberRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dmsdbj.integral.backstage.pojo.SpecificPointsEntity;
import com.dmsdbj.integral.backstage.pojo.UserEntity;
import com.dmsdbj.integral.backstage.provider.dao.UpdateSpecificPointsDao;
import com.dmsdbj.integral.backstage.utils.DingUtil;
import com.xxl.job.core.log.XxlJobLogger;
import groovy.util.logging.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author 齐智
 * @Date: 2020/8/13 9:55
 * @Version: 1.0
 **/
@Service
@Slf4j
public class UpdateSpecificPointsService {



    @Autowired
    private UpdateSpecificPointsDao updateSpecificPointsDao;



    /**
     * 根据特定人员，更新可赠积分
     */
    public boolean updateGivingIntegralWithId() {
        updateSpecificPointsDao.updateGivingIntegralWithId();
        return true;
    }

    /**
     * 根据特定人员加分表的部门更新用户的可赠的积分
     * @return
     */
    public boolean updateGivingIntegralWithDeptId() {

         //1.查询部门，得到一个List
        List<SpecificPointsEntity> specificPointsEntities = updateSpecificPointsDao.selectDept();
        //获取token值
        String accessToken = DingUtil.getAccessToken();
        //判断token是否为空
        if(Strings.isEmpty(accessToken)){
            XxlJobLogger.log(this.getClass().getSimpleName()+"--token is null ");
            return false;
        }

        List<SpecificPointsEntity> specificPointsEntityList=new ArrayList<>();
        for(SpecificPointsEntity entity: specificPointsEntities)
        {
            //获取部门列表
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
            OapiDepartmentListRequest request = new OapiDepartmentListRequest();
            request.setId(entity.getId());
            request.setHttpMethod("GET");
            OapiDepartmentListResponse response=null;
            try {
                response= client.execute(request, accessToken);
            }
            catch (Exception e)
            {
                XxlJobLogger.log(this.getClass().getSimpleName()+"--The result by getDeptId   is failed");
                return false;
            }
            //发送成功
            if(request!=null && response.getErrcode()==0)
            {
                for(OapiDepartmentListResponse.Department department: response.getDepartment())
                {
                    SpecificPointsEntity specificPointsEntity=new SpecificPointsEntity();
                    specificPointsEntity.setId(department.getId()+"");
                    specificPointsEntity.setIntegral(entity.getIntegral());

                    specificPointsEntityList.add(specificPointsEntity);
                }
            }

        }
        List<UserEntity> userEntities =new ArrayList<>();
        for(SpecificPointsEntity dept : specificPointsEntityList)
        {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getDeptMember");
            OapiUserGetDeptMemberRequest req = new OapiUserGetDeptMemberRequest();
            req.setDeptId(dept.getId());
            req.setHttpMethod("GET");
            OapiUserGetDeptMemberResponse rsp = null;
            try {
                rsp = client.execute(req, accessToken);
            }
            catch (Exception e)
            {
                //打印日志
                XxlJobLogger.log(this.getClass().getSimpleName()+"--getDeptId is failed");
                return false;
            }
            if(rsp==null) {//如果返回结果为null,返回
                XxlJobLogger.log(this.getClass().getSimpleName()+"--The result by getDeptId   is failed");
                return false;
            }
            //如果发送成功
            if(rsp.getErrcode()!=null && rsp.getUserIds()!=null && rsp.getErrcode()==0){
                for(String dingId : rsp.getUserIds())
                {
                    UserEntity userEntity=new UserEntity();
                    userEntity.setDingId(dingId);//设置钉钉id
                    userEntity.setGivingIntegral(dept.getIntegral());
                    userEntities.add(userEntity);
                }
            }

        }
        //更新表
        updateSpecificPointsDao.updateGivingIntegralWithDeptId(userEntities);
        return true;
    }
}
