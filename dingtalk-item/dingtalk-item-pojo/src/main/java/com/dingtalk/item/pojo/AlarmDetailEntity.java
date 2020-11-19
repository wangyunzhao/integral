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
 * AlarmDetail实体
 * 报警考勤表
 *
 * @author 梁佳宝 
 * @version ${version}
 * @since ${version} 2020-06-02 10:15:13
 */
@ApiModel(value = "AlarmDetailEntity:报警考勤表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@TableName(value = "tid_alarm_detail")
public class AlarmDetailEntity implements Serializable {

	/**
	 * 主键Id
	 */
	@Id
	@Column(name = "id")
	protected String id;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人",hidden = true)
	@Column(name = "operator")
	private String operator;

	/**
	 * 删除标记
	 */
	@ApiModelProperty(value = "是否删除",hidden = true)
	@Column(name = "is_delete")
	private int isDelete = 0;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注",hidden = true)
	@Column(name = "remark")
	private String remark;

	/**
	 * 创建日期
	 */
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
	)
	@ApiModelProperty(value = "创建时间",hidden = true)
	@Column(name = "create_time")
	private Date createTime;
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
	 * 用户钉钉id
	 */
	@ApiModelProperty(value = "用户钉钉id",hidden = true)
	@Column(name = "user_ding_id")
	private String userDingId;

	/**
	 * 用户积分id
	 */
	@ApiModelProperty(value = "用户积分id",hidden = true)
	@Column(name = "user_jifen_id")
	private String userJifenId;

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名",hidden = true)
	@Column(name = "user_name")
	private String userName;

	/**
	 * 是否请假(0否 1是)
	 */
	@ApiModelProperty(value = "是否请假(0否 1是)",hidden = true)
	@Column(name = "is_leave")
	private Integer isLeave;

	/**
	 * 是否发送群告警(0:未发送,1:发送成功，-1:发送失败下次不发了)
	 */
	@ApiModelProperty(value = "是否发送群告警(0:未发送,1:发送成功，-1:发送失败下次不发了)",hidden = true)
	@Column(name = "is_send")
	private Integer isSend;

	/**
	 * 纪委处理结果(评论内容)
	 */
    @ApiModelProperty(value = "纪委处理结果(评论内容)")
	@Column(name = "result")
	private String result;

	/**
	 * 成功发送群告警时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @ApiModelProperty(value = "成功发送群告警时间",hidden = true)
	@Column(name = "send_time")
	private Date sendTime;

	/**
	 * 组织机构ID
	 */
	@ApiModelProperty(value = "组织机构ID",hidden = true)
	@Column(name = "orgazition")
	private String orgazition;

	/**
	 * 组织机构ID
	 */
	@ApiModelProperty(value = "组织机构ID",hidden = true)
	@Column(name = "orgazition")
	private String isSafe;



}
