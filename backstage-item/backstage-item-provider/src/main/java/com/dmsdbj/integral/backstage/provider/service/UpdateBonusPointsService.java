package com.dmsdbj.integral.backstage.provider.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListIdsRequest;
import com.dingtalk.api.request.OapiUserGetDeptMemberRequest;
import com.dingtalk.api.response.OapiDepartmentListIdsResponse;
import com.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dmsdbj.integral.backstage.model.WeightAndIntegralModel;
import com.dmsdbj.integral.backstage.provider.dao.UpdateBonusPointsDao;
import com.dmsdbj.integral.backstage.utils.DingUtil;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: 曹祥铭
 * @Description: 每个月更新人员的可赠积分、减分权限
 * @Date: Create in 11:11 2020/8/12
 */
@Service
@Slf4j
public class UpdateBonusPointsService {

    @Autowired
    private UpdateBonusPointsDao updateBonusPointsDao;

    //根据父部门ID查询子部门，并存储
    List<Long> allSons=new ArrayList<>();

    /**
     *曹祥铭- 重置人员的可赠积分，以及减分权限-2020年8月12日16:27:22
     * @return
     * @throws Exception
     */
    public int  updateBonusPoints() throws Exception{

        return updateBonusPointsDao.updateWeightAndIntegral();

    }
    /**
     * 曹祥铭-更新所有配置用户的可赠积分，权重以及减分权限-2020年8月12日16:28:36
     * @return
     * @throws Exception
     */
    public int setWeightAndIntegral() throws Exception{
        return  updateBonusPointsDao.setWeightAndIntegral();
    }

    /**
     * 曹祥铭-根据根据钉钉ID批量设置可赠积分，权重以及减分权限
     */
    public void setWeightAndIntegralByList(){
        String accessToken = DingUtil.getAccessToken();
        //查询表中所有的组织信息(所有父部门的ID）
        List<WeightAndIntegralModel> weightAndIntegralModels = updateBonusPointsDao.queryOrgWeightAndIntegral();
        weightAndIntegralModels.stream().peek(a->{
            try {
                //获得父部门下的所有子部门
                querySonDep(a.getId(),accessToken);
                //查询子部门的所有成员
                allSons.stream().peek(b->{
                    OapiUserGetDeptMemberResponse rsp = getDepUserDingIds(b,accessToken);
                    List<String> userIds = rsp.getUserIds();
                    if(!userIds.isEmpty()&&userIds!=null){
                        weightAndIntegralModels.stream().peek(e->{
                            try {
//                                进行过滤，过滤出比数据库中大的才进行更新（解决多角色的问题）
                                List<WeightAndIntegralModel> weightAndIntegralModelList = queryPersonWeight(userIds);
                                List<String> needUpdate = weightAndIntegralModelList.stream().filter(k -> k.getWeight() < a.getWeight()).map(l->l.getUseId()).collect(Collectors.toList());
                                if(!needUpdate.isEmpty()&&needUpdate!=null){
                                    e.setUserDingIds(needUpdate);
                                    updateBonusPointsDao.setWeightAndIntegralByList(a);
                                }
                                //进行过滤，得到需要变更减分权限的人，以个人的配置为基准
                                List<String> needUpdateAuth = weightAndIntegralModelList.stream().filter(o -> o.getReductionAuth() < a.getReductionAuth()).map(u -> u.getUseId()).collect(Collectors.toList());
                                if(!needUpdateAuth.isEmpty()&&needUpdateAuth!=null){
                                    e.setUserDingIds(needUpdateAuth);
                                    updateBonusPointsDao.updatePermitAuthByList(a);
                                }
                            } catch (Exception ex) {
                                log.error(ex.getMessage());
                                ex.printStackTrace();
                            }
                        }).collect(Collectors.toList());
                    }
                }).collect(Collectors.toList());
                //结束一次循环清空一下allSons
                allSons.clear();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }).collect(Collectors.toList());
    }

    /**
     * 曹祥铭-调用钉钉接口查询，根据部门id查询部门下的成员的dingId
     * @param weightAndIntegralModel 实体
     * @param accessToken token
     * @return
     */
    public OapiUserGetDeptMemberResponse getDepUserDingIds(Long weightAndIntegralModel,String accessToken){
        //调用钉钉接口，查询组织ID下的所有人员
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getDeptMember");
        OapiUserGetDeptMemberRequest req = new OapiUserGetDeptMemberRequest();
        req.setDeptId(weightAndIntegralModel.toString());
        req.setHttpMethod("GET");
        OapiUserGetDeptMemberResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);

        } catch (ApiException e) {
            log.error(e.getErrMsg());
            e.printStackTrace();
        }
        return  rsp;
    }

    /**
     * 曹祥铭-根据父部门ID调用钉钉的接口查询子部门ID
     * @param accessToken
     * @param depId
     * @return
     * @throws ApiException
     */
    public List<Long> getSonDepByFatherId(String accessToken, String depId) throws ApiException{
        OapiDepartmentListIdsResponse dataFromDing = getDataFromDing("https://oapi.dingtalk.com/department/list_ids", accessToken, depId);
        return dataFromDing.getSubDeptIdList();
    }

    /**
     * 曹祥铭-调用钉钉接口的方法
     * @param url 钉钉接口地址
     * @param accessToken accessToken
     * @param depId 部门id
     * @return
     * @throws ApiException
     */
    public OapiDepartmentListIdsResponse getDataFromDing(String url,String accessToken,String depId) throws ApiException{
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiDepartmentListIdsRequest request = new OapiDepartmentListIdsRequest();
        request.setId(depId);
        request.setHttpMethod("GET");
        OapiDepartmentListIdsResponse response = client.execute(request, accessToken);
        return response;

    }

    /**
     * 曹祥铭-递归查询父部门下的所有的部门，并存在集合中
     * @param FatherId 根父部门的钉id
     * @param accessToken
     * @throws ApiException
     */
    public void querySonDep(String FatherId,String accessToken)throws ApiException{

        List<Long> sonDepByFatherId = getSonDepByFatherId(accessToken, FatherId);
        allSons.addAll(sonDepByFatherId);
        sonDepByFatherId.stream().peek(e->{
            try {
                querySonDep(e.toString(),accessToken);
            } catch (ApiException ex) {
                ex.printStackTrace();
            }
        }).collect(Collectors.toList());
    }

    /**
     * 曹祥铭-根据id查询用户的可赠积分，权重，减分权限
     * @param id id
     * @return
     */
    public List<WeightAndIntegralModel> queryPersonWeight(List<String> id){
       return updateBonusPointsDao.queryPersonWeight(id);
    }
}
