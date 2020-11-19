package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.WeightAndIntegralModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 11:15 2020/8/12
 */
@Repository("UpdateBonusPointsDao")
public interface UpdateBonusPointsDao {

    /**
     * 曹祥铭-重置每个人的权重以及可赠分值-2020年8月12日16:24:47
     * @return
     */
    int updateWeightAndIntegral();

    /**
     * 曹祥铭-设置每个人的权重，可赠分值和是否有减分权限-2020年8月12日16:24:35
     * @return
     */
    int setWeightAndIntegral();

    int setWeightAndIntegralByList(WeightAndIntegralModel weightAndIntegralModel);

    int updatePermitAuthByList(WeightAndIntegralModel weightAndIntegralModel);


    // 查询组织对应的权重、可赠积分以及是否有减分权限

    List<WeightAndIntegralModel>  queryOrgWeightAndIntegral();

    List<WeightAndIntegralModel> queryPersonWeight(List<String> ids);
}
