package com.dingtalk.item.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/25 18:00
 * @describe
 */
@ApiModel(value = "BreachNumberEntity:违纪扣分记录表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@TableName(value = "tid_breach_number")
public class BreachNumberEntity implements Serializable {
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
     * 违纪总次数
     */
    @ApiModelProperty(value = "违纪总次数",hidden = true)
    @Column(name = "breach_number_sum")
    private int breachNumberSum;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注",hidden = true)
    @Column(name = "remark")
    private String remark;


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
     * 创建时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @ApiModelProperty(value = "创建时间",hidden = true)
    @Column(name = "create_time")
    private Date createTime;
}
