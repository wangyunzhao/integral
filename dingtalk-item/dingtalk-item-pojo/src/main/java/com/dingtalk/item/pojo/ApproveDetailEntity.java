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
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * ApproveDetail实体
 * 审批详情表
 *
 * @author 梁佳宝 
 * @version ${version}
 * @since ${version} 2020-06-02 10:15:13
 */
@ApiModel(value = "ApproveDetailEntity:审批详情表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tid_approve_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApproveDetailEntity extends BaseEntity implements Serializable {

//	/**
//	 * 申请人
//	 */
//	@ApiModelProperty(value = "申请人",required = true)
//	@Column(name = "name")
//	private String name;

	/**
	 * 审批实例id
	 */
	@ApiModelProperty(value = "审批实例id",required = true)
	@Column(name = "process_instance_id")
	private String processInstanceId;

	/**
	 * 审批实例标题
	 */
	@ApiModelProperty(value = "审批实例标题",required = true)
	@Column(name = "title")
	private String title;

	/**
	 * 审批创建时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
	@ApiModelProperty(value = "审批创建时间",required = true)
	@Column(name = "operate_create_time")
	private Date operateCreateTime;

	/**
	 * 审批处理完时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
	@ApiModelProperty(value = "审批处理完时间",required = true)
	@Column(name = "operate_finish_time")
	private Date operateFinishTime;

	/**
	 * 审批发起人钉钉id
	 */
	@ApiModelProperty(value = "审批发起人钉钉id",required = true)
	@Column(name = "originator_userid")
	private String originatorUserid;

	/**
	 * 请假类型
	 */
	@ApiModelProperty(value = "请假类型",required = true)
	@Column(name = "leave_type")
	private String leaveType;

	/**
	 * 审批状态，NEW(刚创建)|RUNNING(运行中)|TERMINATED(被终止)|COMPLETED(完成)|CANCELED(取消)
	 */
	@ApiModelProperty(value = "审批状态，NEW(刚创建)|RUNNING(运行中)|TERMINATED(被终止)|COMPLETED(完成)|CANCELED(取消)",required = true)
	@Column(name = "status")
	private String status;

	/**
	 * 请假开始时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
	@ApiModelProperty(value = "请假开始时间",required = true)
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * 请假结束时间
	 */
	@JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
	@ApiModelProperty(value = "请假结束时间",required = true)
	@Column(name = "end_time")
	private Date endTime;

	/**
	 * 审批结果(agree:1)和(refuse:0)
	 */
	@ApiModelProperty(value = "审批结果(agree:1)和(refuse:0)",required = true)
	@Column(name = "result")
	private String result;

	/**
	 * 调用接口返回的元数据
	 */
    @ApiModelProperty(value = "调用接口返回的元数据")
	@Column(name = "row_data")
	private String rowData;

	/**
	 * 组织id
	 */
	@ApiModelProperty(value = "组织id",required = true)
	@Column(name = "organization_id")
	private String organizationId;




}
