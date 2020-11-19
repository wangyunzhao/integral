package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.TypeUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.List;
/**
 * 用户级别表操作
 *
 * @author 崔晓鸿
 * @since 2020年6月24日09:20:45
 */
@Repository("typeUserDao")
public interface TypeUserDao {
    /**
     * 添加用户级别
     *
     * @param typeUserEntities
     * @return 返回行数
     * @author 崔晓鸿
     * @since 2020年6月24日10:26:50
     */
    int insertTypeUser(@Param("typeUserEntities") List<TypeUserEntity> typeUserEntities);

    /**
     * 更新用户级别
     *
     * @param typeUserEntity
     * @return 返回行数
     * @author 崔晓鸿
     * @since 2020年6月44日10:26:54
     */
    int updateTypeUser(@Param("typeUserEntity") TypeUserEntity typeUserEntity);

    /**
     * 查询户级别
     *
     * @param userDingId
     * @return TypeUserEntity
     * @author 崔晓鸿
     * @since 2020年6月44日10:26:54
     */
    TypeUserEntity selectTypeUserByUserDingId(String userDingId);

    List<String> selectAllTypeUserByType(int type);
}
