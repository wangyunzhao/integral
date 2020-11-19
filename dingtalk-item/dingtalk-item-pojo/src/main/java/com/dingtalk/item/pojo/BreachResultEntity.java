package com.dingtalk.item.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dmsdbj.itoo.tool.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.*;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BreachResult实体
 * 违纪扣分记录表
 *
 * @author 梁佳宝
 * @version ${version}
 * @since ${version} 2020年8月23日 15点42分
 */
@ApiModel(value = "BreachResultEntity:违纪扣分记录表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@TableName(value = "tid_breach_result")
public class BreachResultEntity implements Serializable {

    /**
     * 主键Id
     */
    @Id
    @Column(name = "id")
    protected String id;

    /**
     * 用户积分id
     */
    @ApiModelProperty(value = "用户积分id",hidden = true)
    @Column(name = "user_jifen_id")
    private String userJifenId;

    /**
     * 加分分值
     */
    @ApiModelProperty(value = "加分分值",hidden = true)
    @Column(name = "integral")
    private int integral=0;

    /**
     * 扣分原因
     */
    @ApiModelProperty(value = "扣分原因",hidden = true)
    @Column(name = "reason")
    private String reason;

    /**
     * 违纪次数
     */
    @ApiModelProperty(value = "违纪次数",hidden = true)
    @Column(name = "breach_number")
    private int breachNumber;
    /**
     * 更新日期
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @ApiModelProperty(value = "更新时间",hidden = true)
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "是否删除",hidden = true)
    @Column(name = "is_delete")
    private int isDelete = 0;

    /**
     * 是否加分(0-未加分，1-已加分)
     */
    @ApiModelProperty(value = "是否加分(0-未加分，1-已加分)",hidden = true)
    @Column(name = "is_success")
    private int isSuccess = 0;


    /**
     * 加分成功时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @ApiModelProperty(value = "加分成功时间",hidden = true)
    @Column(name = "success_time")
    private Date successTime;

    /**
     * 计算时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @ApiModelProperty(value = "计算时间",hidden = true)
    @Column(name = "calculate_time")
    private Date calculateTime;

    /**
     * 创建时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @ApiModelProperty(value = "创建时间",hidden = true)
    @Column(name = "create_time")
    private Date createTime;


}
