package com.dmsdbj.integral.backstage.provider.service;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.SpecificPointsModel;
import com.dmsdbj.integral.backstage.pojo.SpecificPointsEntity;
import com.dmsdbj.integral.backstage.provider.dao.SpecificPointsDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/12 10:27
 * @describe 特定人员加分servie
 */
@Service
public class SpecificPointsService {

    @Autowired
    private SpecificPointsDao specificPointsDao;

    Map result = new HashMap();

    /**
     * @param type
     * @param pageNo
     * @param pageSize
     * @description: 按类型查询特定人员加分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:16
     */
    public IntegralResult querySpecificPoints(Integer type, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 5 : pageSize;

        //使用分页器
        PageHelper.startPage(pageNo, pageSize);
        List<SpecificPointsModel> specificPointsModelList = specificPointsDao.querySpecificPoints(type);
        if (specificPointsModelList.size() > 0) {
            PageInfo<SpecificPointsModel> specificPointsModelPage = new PageInfo<>(specificPointsModelList);

            // 处理分页的结果
            result.put("count", specificPointsModelPage.getTotal());
            result.put("specificPointsList", specificPointsModelPage.getList());
            // 返回结果
            return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", result);
        } else {
            return IntegralResult.build(IntegralResult.SUCCESS, "暂无改类型的特定人员加分");
        }

    }

    /**
     * @param content
     * @param type
     * @param pageNo
     * @param pageSize
     * @description: 根据类型模糊查询特定人员加分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:24
     */
    public IntegralResult likeQuerySpecificPoints(String content, Integer type, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 5 : pageSize;
        //使用分页器
        PageHelper.startPage(pageNo, pageSize);
        List<SpecificPointsModel> specificModelList = specificPointsDao.likeQuerySpecificPoints(content, type);
        if (specificModelList.size() > 0) {
            PageInfo<SpecificPointsModel> specificPointsModelPage = new PageInfo<>(specificModelList);
            // 处理分页的结果
            result.put("count", specificPointsModelPage.getTotal());
            result.put("specificPointsList", specificPointsModelPage.getList());
            // 返回结果
            return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", result);
        } else {
            return IntegralResult.build(IntegralResult.SUCCESS, "暂无符合特定人员加分");
        }

    }

    /**
     * @param specificPointsModelSet
     * @description: 根据id删除特定人员加分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:43
     */
    public IntegralResult deleteSpecificPoints(Set<SpecificPointsModel> specificPointsModelSet) {

        try {
            Integer deleteCount = specificPointsDao.deleteSpecificPoints(specificPointsModelSet);
            if (deleteCount > 0) {
                return IntegralResult.build(IntegralResult.SUCCESS, "删除成功！");
            } else {
                return IntegralResult.build(IntegralResult.FAIL, "没有需要被删除的！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IntegralResult.build(IntegralResult.FAIL, "删除失败，请稍后再试！");
        }
    }

    /**
     * @param specificPointsModelSet
     * @description: 添加特定人员加分记录
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 13:16
     */
    public IntegralResult addSpecificPoints(Set<SpecificPointsModel> specificPointsModelSet) {
        try {
            Integer deleteCount = specificPointsDao.addSpecificPoints(specificPointsModelSet);
            if (deleteCount > 0) {
                return IntegralResult.build(IntegralResult.SUCCESS, "添加成功！");
            } else {
                return IntegralResult.build(IntegralResult.FAIL, "没有需要被添加的！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IntegralResult.build(IntegralResult.FAIL, "添加失败，请稍后再试！");
        }
    }

    /**
     * @description: 按照类型获取全部的特定人员加分
     * @param type
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 13:25
     */
    public IntegralResult queryAllSpecificPoints(Integer type) {
        List<SpecificPointsModel> specificPointsModelList = specificPointsDao.querySpecificPoints(type);
        if (specificPointsModelList.size() > 0) {
            // 返回结果
            return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", specificPointsModelList);
        } else {
            return IntegralResult.build(IntegralResult.SUCCESS, "暂无符合特定人员加分");
        }
    }


    /**
     * @description: 更新特定人员加分记录
     * @param specificPointsModel
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 13:36
     */
    public IntegralResult updateSpecificPoints(SpecificPointsModel specificPointsModel) {
        SpecificPointsEntity specificPointsEntity=new SpecificPointsEntity();

        specificPointsEntity.setId(specificPointsModel.getId());
        specificPointsEntity.setIntegral(specificPointsModel.getIntegral());
        specificPointsEntity.setOperator(specificPointsModel.getOperator());
        try {
            Integer updateCount = specificPointsDao.updateSpecificPoints(specificPointsEntity);
            if (updateCount > 0) {
                return IntegralResult.build(IntegralResult.SUCCESS, "更新成功！");
            } else {
                return IntegralResult.build(IntegralResult.FAIL, "没有需要被更新的！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IntegralResult.build(IntegralResult.FAIL, "更新失败，请稍后再试！");
        }
    }


}
