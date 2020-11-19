package com.dingtalk.item.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "AlarmHandleEntity:用户处理异常结果表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode()
@TableName(value = "tid_alarm_handle")
public class AlarmHandleEntity implements Serializable {
    /**
     * id
     */
    @Id
    @ApiModelProperty(value = "告警处理id",required = true)
    @Column(name = "id")
    private String id = IdWorker.getIdStr();

    /**
     * 报警id
     */
    @ApiModelProperty(value = "报警id",required = true)
    @Column(name = "alarm_id")
    private String alarmId;
    /**
     * 处理人钉钉id
     */
    @ApiModelProperty(value = "处理人钉钉id",required = true)
    @Column(name = "operator_ding_id")
    private String operatorDingId;
    /**
     * 处理人姓名
     */
    @ApiModelProperty(value = "处理人姓名")
    @Column(name = "operator_name")
    private String operatorName;
    /**
     * 处理人级别
     */
    @ApiModelProperty(value = "处理人级别",hidden = true)
    @Column(name = "operator_type")
    private String operatorType;

    /**
     * 处理结果
     */
    @ApiModelProperty(value = "处理结果",required = true)
    @Column(name = "operate_result")
    private String operateResult;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记",hidden = true)
    @Column(name = "is_delete")
    private int isDelete=0;

    /**
     * 创建日期
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新日期
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新日期",hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否处理
     */
    @ApiModelProperty(value = "是否处理",hidden = true)
    @Column(name = "is_handle")
    private int isHandle=0;

    /**
     * 是否计算分值
     */
    @ApiModelProperty(value = "是否计算",hidden = true)
    @Column(name = "is_calu")
    private int isCalu=0;

    /**
     * 加分分值
     */
    @ApiModelProperty(value = "加分分值",hidden = true)
    @Column(name = "integral")
    private int integral;

    /**
     * 加分分值
     */
    @ApiModelProperty(value = "加分分值",hidden = true)
    @Column(name = "is_success")
    private int isSuccess = 0;
}
