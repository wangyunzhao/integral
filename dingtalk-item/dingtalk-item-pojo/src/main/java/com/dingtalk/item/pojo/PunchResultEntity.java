package com.dingtalk.item.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.dmsdbj.itoo.tool.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.*;
import java.io.Serializable;
import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * PunchResult实体
 * 打卡结果表
 *
 * @author 梁佳宝 
 * @version ${version}
 * @since ${version} 2020-06-02 10:15:13
 */
@ApiModel(value = "PunchResultEntity:打卡结果表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tid_punch_result")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PunchResultEntity extends BaseEntity implements Serializable {

	/**
	 * 用户积分id
	 */
	@ApiModelProperty(value = "用户积分id",required = true)
	@Column(name = "user_jifen_id")
	private String userJifenId;

	/**
	 * 用户钉钉id
	 */
	@ApiModelProperty(value = "用户钉钉id",required = true)
	@Column(name = "user_ding_id")
	private String userId;

	/**
	 * 应打卡时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
	@ApiModelProperty(value = "应打卡时间",required = true)
	@Column(name = "base_check_time")
	private LocalDateTime baseCheckTime;

	/**
	 * 实际打卡时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
	@ApiModelProperty(value = "实际打卡时间",required = true)
	@Column(name = "user_check_time")
	private Date userCheckTime;

	/**
	 * 考勤类型(OnDuty：上班 OffDuty：下班)
	 */
	@ApiModelProperty(value = "考勤类型(OnDuty：上班 OffDuty：下班)",required = true)
	@Column(name = "check_type")
	private String checkType;

	/**
	 * 时间结果(Normal：正常 Early：早退; Late：迟到;SeriousLate：严重迟到；Absenteeism：旷工迟到；
	 */
	@ApiModelProperty(value = "时间结果(Normal：正常 Early：早退; Late：迟到;SeriousLate：严重迟到；Absenteeism：旷工迟到；",required = true)
	@Column(name = "time_result")
	private String timeResult;

	/**
	 * 实际得分
	 */
    @ApiModelProperty(value = "实际得分")
	@Column(name = "integral")
	private Integer integral;

	/**
	 * 是否加分(0:未加分,1:已经加分)
	 */
    @ApiModelProperty(value = "是否加分(0:未加分,1:已经加分)")
	@Column(name = "is_success")
	private Integer isSuccess;

	/**
	 * 加分成功时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @ApiModelProperty(value = "加分成功时间")
	@Column(name = "success_time")
	private Date successTime;

	/**
	 * 是否计算（0为未计算，1已计算）
	 */
    @ApiModelProperty(value = "是否计算（0为未计算，1已计算）")
	@Column(name = "is_calculate")
	private Integer isCalculate;

	/**
	 * 计算时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @ApiModelProperty(value = "计算时间")
	@Column(name = "calculate_time")
	private Date calculateTime;

	/**
	 * 关联的审批实例id，当该字段非空时，表示打卡记录与请假、加班等审批有关。可以与 获取单个审批数据配合使用
	 */
    @ApiModelProperty(value = "关联的审批实例id，当该字段非空时，表示打卡记录与请假、加班等审批有关。可以与 获取单个审批数据配合使用")
	@Column(name = "proc_inst_id")
	private String procInstId;

	/**
	 * 考勤组id
	 */
	@ApiModelProperty(value = "考勤组id",required = true)
	@Column(name = "group_id")
	private String groupId;

	/**
	 * 排班id
	 */
	@ApiModelProperty(value = "排班id",required = true)
	@Column(name = "plan_id")
	private String planId="0";

	/**
	 * 打卡记录id
	 */
	@ApiModelProperty(value = "打卡记录id",required = true)
	@Column(name = "record_id")
	private String recordId;

	/**
	 * 位置结果(Normal：范围内；Outside：范围外；NotSigned：未打卡)
	 */
	@ApiModelProperty(value = "位置结果(Normal：范围内；Outside：范围外；NotSigned：未打卡)",required = true)
	@Column(name = "location_result")
	private String locationResult;

	/**
	 * 审批实例id，当该字段非空时，表示打卡记录与请假、加班等审批有关
	 */
    @ApiModelProperty(value = "审批实例id，当该字段非空时，表示打卡记录与请假、加班等审批有关")
	@Column(name = "approve_id")
	private String approveId;


}
