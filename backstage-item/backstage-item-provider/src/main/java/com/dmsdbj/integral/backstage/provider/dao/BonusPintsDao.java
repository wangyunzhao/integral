package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.BonusPointsModel;
import com.dmsdbj.integral.backstage.pojo.BonusPointsEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/12 10:28
 * @describe 可赠积分管理
 */
@Repository
public interface BonusPintsDao {
    /**
     * @description: 根据类型查询所有的可赠积分记录
     * @param type
     * @return: java.util.List<com.dmsdbj.integral.backstage.model.BonusPointsModel>
     * @author: 马珂
     * @time: 2020/8/12 10:31
     */
    public List<BonusPointsModel> queryBonusPoints(Integer type);

    /**
     * @description: 根据类型模糊查询可赠积分记录
     * @param type
     * @param content
     * @return: java.util.List<com.dmsdbj.integral.backstage.model.BonusPointsModel>
     * @author: 马珂
     * @time: 2020/8/12 10:33
     */
    public List<BonusPointsModel> likeQueryBonusPoints( String content,Integer type);

    /**
     * @description:  根据id删除可赠积分
     * @param bonusPointsModelSet
     * @return: java.lang.Integer
     * @author: 马珂
     * @time: 2020/8/12 10:35
     */
    public Integer deleteBonusPoints(@Param("bonusPointsModelSet") Set<BonusPointsModel> bonusPointsModelSet);

    /**
     * @description: 添加多条可赠积分记录
     * @param bonusPointsModelSet
     * @return: java.lang.Integer
     * @author: 马珂
     * @time: 2020/8/12 10:37
     */
    public Integer addBonusPoints(@Param("bonusPointsModelSet") Set<BonusPointsModel> bonusPointsModelSet);

    /**
     * @description: 根据id更新某条可赠积分记录
     * @param bonusPointsEntity
     * @return: java.lang.Integer
     * @author: 马珂
     * @time: 2020/8/12 10:38
     */
    public Integer updateBonusPoints( BonusPointsEntity bonusPointsEntity);

}
