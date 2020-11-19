package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.ApproveDetailEntity;
import com.dingtalk.item.pojo.DisciplineUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
* @Description 查询当月当值纪委
* @Author  wmy
* @Date   2020/7/22 11:48
* @Param
* @Return
*
*/
@Repository("disciplineUserDao")
public interface DisciplineUserDao {
    /**
     * 根据dingid查询组织id
     *
     * @param dingId 审批实体
     * @return 受影响的行数
     */
     String queryOrganation (String dingId);

    /**
     * 插入审批详情表
     *
     * @param disciplineUserEntity 审批实体
     * @return 受影响的行数
     */
    int insertDiscipline(@Param("disciplineUserEntity") List<DisciplineUserEntity> disciplineUserEntity);
    /**
     * 查询disciplineUser是否有数据
     *
     * @param
     * @return 结果集
     */
    List<DisciplineUserEntity> queryDisciplineUser();

    /**
     * 真删除disciplineUser的所有数据
     *
     * @param
     * @return 结果集
     */
    int deleteDisciplineUser();
    /**
     * 如果process_instance_id存在，删除
     *
     * @param orgId
     * @return 受影响的行数
     */
    DisciplineUserEntity queryUserInfoByOrgId(String orgId);
}
