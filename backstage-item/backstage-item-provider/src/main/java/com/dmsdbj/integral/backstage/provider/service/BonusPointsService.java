package com.dmsdbj.integral.backstage.provider.service;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.BonusPointsModel;
import com.dmsdbj.integral.backstage.pojo.BonusPointsEntity;
import com.dmsdbj.integral.backstage.provider.dao.BonusPintsDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/12 10:27
 * @describe 可赠积分service
 */
@Service
public class BonusPointsService {

    @Autowired
    private BonusPintsDao bonusPintsDao;

    Map result = new HashMap();

    /**
     * @param type
     * @param pageNo
     * @param pageSize
     * @description: 按类型查询可赠积分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:16
     */
    public IntegralResult queryBonusPoints(Integer type, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 5 : pageSize;

        //使用分页器
        PageHelper.startPage(pageNo, pageSize);
        List<BonusPointsModel> bonusPointsModelList = bonusPintsDao.queryBonusPoints(type);
        if (bonusPointsModelList.size() > 0) {
            PageInfo<BonusPointsModel> bonusPointsModelPage = new PageInfo<>(bonusPointsModelList);

            // 处理分页的结果
            result.put("count", bonusPointsModelPage.getTotal());
            result.put("bonusPointsList", bonusPointsModelPage.getList());
            // 返回结果
            return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", result);
        } else {
            return IntegralResult.build(IntegralResult.SUCCESS, "暂无改类型的可赠积分");
        }

    }

    /**
     * @param content
     * @param type
     * @param pageNo
     * @param pageSize
     * @description: 根据类型模糊查询可赠积分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:24
     */
    public IntegralResult likeQueryBonusPoints(String content, Integer type, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 5 : pageSize;
        //使用分页器
        PageHelper.startPage(pageNo, pageSize);
        List<BonusPointsModel> bonusPointsModelList = bonusPintsDao.likeQueryBonusPoints(content, type);
        if (bonusPointsModelList.size() > 0) {
            PageInfo<BonusPointsModel> likebonusPointsModelPage = new PageInfo<>(bonusPointsModelList);
            // 处理分页的结果
            result.put("count", likebonusPointsModelPage.getTotal());
            result.put("bonusPointsList", likebonusPointsModelPage.getList());
            // 返回结果
            return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", result);
        } else {
            return IntegralResult.build(IntegralResult.SUCCESS, "暂无符合可赠积分");
        }

    }

    /**
     * @param bonusPointsModelSet
     * @description: 根据id删除可赠积分
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 12:43
     */
    public IntegralResult deleteBonusPoints(Set<BonusPointsModel> bonusPointsModelSet) {

        try {
            Integer deleteCount = bonusPintsDao.deleteBonusPoints(bonusPointsModelSet);
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
     * @param bonusPointsModelSet
     * @description: 添加可赠积分记录
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 13:16
     */
    public IntegralResult addBonusPoints(Set<BonusPointsModel> bonusPointsModelSet) {
        try {
            Integer deleteCount = bonusPintsDao.addBonusPoints(bonusPointsModelSet);
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
     * @description: 按照类型获取全部的可赠积分
     * @param type
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 13:25
     */
    public IntegralResult queryAllBonusPoints(Integer type) {
        List<BonusPointsModel> bonusPointsModelList = bonusPintsDao.queryBonusPoints(type);
        if (bonusPointsModelList.size() > 0) {
            // 返回结果
            return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", bonusPointsModelList);
        } else {
            return IntegralResult.build(IntegralResult.SUCCESS, "暂无符合可赠积分");
        }
    }


    /**
     * @description: 更新可赠积分记录
     * @param bonusPointsModel
     * @return: com.dmsdbj.cloud.tool.business.IntegralResult
     * @author: 马珂
     * @time: 2020/8/12 13:36
     */
    public IntegralResult updateBonusPoints(BonusPointsModel bonusPointsModel) {
        BonusPointsEntity bonusPointsEntity=new BonusPointsEntity();

        bonusPointsEntity.setId(bonusPointsModel.getId());
        bonusPointsEntity.setWeight(bonusPointsModel.getWeight());
        bonusPointsEntity.setIntegral(bonusPointsModel.getWeight()*100);
        bonusPointsEntity.setReductionAuth(bonusPointsModel.getReductionAuth());
        bonusPointsEntity.setOperator(bonusPointsModel.getOperator());
        try {
            Integer updateCount = bonusPintsDao.updateBonusPoints(bonusPointsEntity);
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
