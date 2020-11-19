package com.dingtalk.item.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * ApproveDetail实体
 * 审批详情表
 *
 * @author 梁佳宝
 * @version ${version}
 * @since ${version} 2020-06-02 10:15:13
 */
@ApiModel(value = "DisciplineUserEntity:当值纪委表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@TableName(value = "tid_discipline_user")
public class DisciplineUserEntity {
    /**
     * 主键Id
     */
    @Id
    @Column(name = "id")
    protected String id;
    /**
     * 删除标记
     */
    @TableLogic
    @Column(name = "is_delete")
    private int isDelete = 0;
    /**
     * 创建日期
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @TableField(fill = FieldFill.INSERT)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新日期
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @Column(name = "update_time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 纪委钉钉id
     */
    @ApiModelProperty(value = "纪委钉钉id",required = true)
    @Column(name = "user_ding_id")
    private String userDingId;
    /**
     * 纪委姓名
     */
    @ApiModelProperty(value = "纪委钉钉id",required = true)
    @Column(name = "user_name")
    private String userName;
    /**
     * 纪委期数
     */
    @ApiModelProperty(value = "纪委期数",required = true)
    @Column(name = "level_num")
    private String levelNum;
    /**
     * 纪委姓名
     */
    @ApiModelProperty(value = "纪委组织id",required = true)
    @Column(name = "organization_id")
    private String organizationId;

}
