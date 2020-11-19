package com.dmsdbj.integral.backstage.provider.service;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.DevelopingDocumentModel;
import com.dmsdbj.integral.backstage.model.ProjectApproveModel;
import com.dmsdbj.integral.backstage.pojo.ProjectEntity;
import com.dmsdbj.integral.backstage.provider.dao.ProjectApproveDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/8/4
 * Time: 19:53
 * Description:${DESCRIPTION}
 */
@Service
public class ProjectApproveService {

    @Autowired
    private ProjectApproveDao projectApproveDao;
    @Autowired
    private EncryptionService encryptionService;
    Map result=new HashMap();
    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/5
    * @time: 11:19
    * @description:查询所有未审批的项目
    */
    public IntegralResult queryProjectApprove(String page ,String size){
        try {

            Integer pageNo=Integer.parseInt(page);
            Integer pageSize=Integer.parseInt(size);

            pageNo = pageNo ==  null ?1:pageNo;
            pageSize=pageSize == null ?5:pageSize;


            PageHelper.startPage(pageNo,pageSize);
            List<ProjectApproveModel> approveModel=projectApproveDao.queryProjectApprove();
            PageInfo<ProjectApproveModel> projectInfo=new PageInfo<>(approveModel);

            //总数据量
            result.put("count",projectInfo.getTotal());
            result.put("data",approveModel);
            return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",result);
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"查询失败");
        }
    }

    /*
    * @author: 郝龙飞
    * @version:
    * @param: [size, page, queryLikeInfo]querylikeInfo输入的查询信息
    * @date: 2020/8/5
    * @time: 12:01
    * @description:模糊查询未审批项目
    */
    public IntegralResult queryProjectApproveLike(String page,String size,String queryLikeInfo){
        try {
            //每页的行数
            int useSize=Integer.parseInt(size);
            //分页开始的行数
            int usePage=(Integer.parseInt(page)-1)*useSize;


            Integer pageNo=Integer.parseInt(page);
            Integer pageSize=Integer.parseInt(size);

            pageNo = pageNo ==  null ?1:pageNo;
            pageSize=pageSize == null ?5:pageSize;


            PageHelper.startPage(pageNo,pageSize);
            List<ProjectApproveModel> approveModel=projectApproveDao.queryProjectApproveLike(queryLikeInfo);
            PageInfo<ProjectApproveModel> projectInfo=new PageInfo<>(approveModel);

            //总数据量
            result.put("count",projectInfo.getTotal());
            result.put("data",approveModel);

            return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",result);
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"查询失败");
        }
    }

    /*
    * @author: 郝龙飞
    * @version:
    * @param: [id]
    * @date: 2020/8/5
    * @time: 14:24
    * @description:通过审批
    */
    @Transactional()
    public IntegralResult agreeApproved(String id,String exchangeRate){
        //try {
            //根据审批id更改审批的状态
            projectApproveDao.updateApproveStatus(id);

            String englishName=projectApproveDao.queryEnglishName(id);

            ProjectEntity projectEntity = encryptionService.encodeByMode(englishName);
            //根据英文名称更新兑换比例
            projectApproveDao.updateProjectRate(englishName,exchangeRate,projectEntity.getSecretId(),
                    projectEntity.getSecretKey());
            return IntegralResult.build(IntegralResult.SUCCESS,"操作完成");
        //}catch (Exception e){
        //    return IntegralResult.build(IntegralResult.FAIL,"操作失败");
        //}
    }

    /*
    * @author: 郝龙飞
    * @version:
    * @param: [id, remark]remark是拒绝审批的理由
    * @date: 2020/8/5
    * @time: 14:29
    * @description:拒绝审批
    */
    @Transactional()
    public IntegralResult rejectApproved(String id,String remark){
        //try {
            projectApproveDao.updateApproveRemark(id,remark);

            String englishName=projectApproveDao.queryEnglishName(id);
            //根据英文名称更新兑换比例
            projectApproveDao.deleteProjectStatus(englishName);
            return IntegralResult.build(IntegralResult.SUCCESS,"操作完成");
        //}catch (Exception e){
        //    return IntegralResult.build(IntegralResult.FAIL,"操作失败");
        //}
    }
}
