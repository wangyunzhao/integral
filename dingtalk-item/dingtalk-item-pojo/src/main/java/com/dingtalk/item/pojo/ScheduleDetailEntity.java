package com.dingtalk.item.pojo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
 * ScheduleDetail实体
 * 每人每天排班详情表
 *
 * @author 梁佳宝 
 * @version ${version}
 * @since ${version} 2020-06-02 10:15:13
 */
@ApiModel(value = "ScheduleDetailEntity:每人每天排班详情表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode()
@TableName(value = "tid_schedule_detail")
public class ScheduleDetailEntity implements Serializable {

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
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名",required = true)
	@Column(name = "user_name")
	private String userName;

	/**
	 * 用户代码（登录账号）
	 */
	@ApiModelProperty(value = "用户代码（登录账号）",required = true)
	@Column(name = "user_code")
	private String userCode;

	/**
	 * 排班id
	 */
	@ApiModelProperty(value = "排班id",required = true)
	@Column(name = "plan_id")
	private String planId;

	/**
	 * 设置打卡基准时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
	@ApiModelProperty(value = "设置打卡基准时间",required = true)
	@Column(name = "plan_check_time")
	private Date planCheckTime;

	/**
	 * 考勤组id
	 */
	@ApiModelProperty(value = "考勤组id",required = true)
	@Column(name = "group_id")
	private String groupId;

	/**
	 * 班次配置id,结果集中没有就标识使用全局班次配置
	 */
    @ApiModelProperty(value = "班次配置id,结果集中没有就标识使用全局班次配置")
	@Column(name = "class_setting_id")
	private String classSettingId;

	/**
	 * 考勤班次id
	 */
    @ApiModelProperty(value = "考勤班次id")
	@Column(name = "class_id")
	private String classId;

	/**
	 * 打卡类型，OnDuty表示上班打卡，OffDuty表示下班打卡
	 */
	@ApiModelProperty(value = "打卡类型，OnDuty表示上班打卡，OffDuty表示下班打卡",required = true)
	@Column(name = "check_type")
	private String checkType;

	/**
	 * 创建时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
	@ApiModelProperty(value = "创建时间",required = true)
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 是否已经查询（0否，1是）
	 */
	@ApiModelProperty(value = "是否已经查询（0否，1是）",required = true)
	@Column(name = "is_query")
	private Integer isQuery;
//-----------------------------------------------------------------------
	/**
	 * 主键Id
	 */
	@Id
	@Column(name = "id")
	protected String id = IdWorker.getIdStr();
	/**
	 * 操作人
	 */
	@Column(name = "operator")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String operator;
	/**
	 * 删除标记
	 */
	@TableLogic
	@Column(name = "is_delete")
	private int isDelete = 0;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 更新日期
	 */
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
	)
	@Column(name = "update_time")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;


}
