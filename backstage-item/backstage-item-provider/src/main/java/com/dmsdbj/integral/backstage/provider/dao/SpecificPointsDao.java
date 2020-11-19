package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.SpecificPointsModel;
import com.dmsdbj.integral.backstage.pojo.BonusPointsEntity;
import com.dmsdbj.integral.backstage.pojo.SpecificPointsEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/12 10:28
 * @describe 特定人员加分管理
 */
@Repository
public interface SpecificPointsDao {
    /**
     * @description: 根据类型查询所有的特定人员加分记录
     * @param type
     * @return: java.util.List<com.dmsdbj.integral.backstage.model.SpecificPointsModel>
     * @author: 马珂
     * @time: 2020/8/12 10:31
     */
    public List<SpecificPointsModel> querySpecificPoints(Integer type);

    /**
     * @description: 根据类型模糊查询特定人员加分记录
     * @param type
     * @param content
     * @return: java.util.List<com.dmsdbj.integral.backstage.model.SpecificPointsModel>
     * @author: 马珂
     * @time: 2020/8/12 10:33
     */
    public List<SpecificPointsModel> likeQuerySpecificPoints( String content,Integer type);

    /**
     * @description:  根据id删除特定人员加分记录
     * @param specificPointsModelSet
     * @return: java.lang.Integer
     * @author: 马珂
     * @time: 2020/8/12 10:35
     */
    public Integer deleteSpecificPoints(@Param("specificPointsModelSet") Set<SpecificPointsModel> specificPointsModelSet);

    /**
     * @description: 添加多条特定人员加分记录
     * @param specificPointsModelSet
     * @return: java.lang.Integer
     * @author: 马珂
     * @time: 2020/8/12 10:37
     */
    public Integer addSpecificPoints(@Param("specificPointsModelSet") Set<SpecificPointsModel> specificPointsModelSet);

    /**
     * @description: 根据id更新某条特定人员加分记录
     * @param specificPointsEntity
     * @return: java.lang.Integer
     * @author: 马珂
     * @time: 2020/8/12 10:38
     */
    public Integer updateSpecificPoints(SpecificPointsEntity specificPointsEntity);

}
