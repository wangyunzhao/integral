package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

/**
 * 加分数据类实体
 * @author 王云召
 * @since 2020年6月24日09:35:42
 */
@Data
public class AddIntegralDataModel {
    /**
     * 打卡结果表id
     */
    private String id;
    /**
     * 默认赠分人为givingUser_dingcheck
     */
    private String givingUserId = "givingUser_dingcheck";
    /**
     * 被加分人的userid
     */
    private String userId;
    /**
     * 加分分值
     */
    private Integer integral;
    /**
     * 加分原因
     */
    private String reason;
    /**
     * 加分的插件id
     */
    private String pluginId="pluginId_dingtalk";
    /**
     * 加分的类型key
     */
    private String typeKey="typeKey_dingcheck";
    /**
     * 加分类型的主键id
     */
    private String primaryId="dingkaoqin";
    /**
     * 创建人
     */
    private String creator="pluginId_dingcheck";
    /**
     * 操作人
     */
    private String operator="pluginId_dingcheck";
}
