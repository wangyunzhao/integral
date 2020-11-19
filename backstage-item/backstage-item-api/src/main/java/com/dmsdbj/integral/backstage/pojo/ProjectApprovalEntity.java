package com.dmsdbj.integral.backstage.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmsdbj.itoo.tool.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.*;
import java.io.Serializable;
import javax.persistence.Column;

/**
 * ProjectApproval实体
 * 项目审批表
 *
 * @author 马珂 
 * @version ${version}
 * @since ${version} 2020-07-26 16:23:03
 */
@ApiModel(value = "ProjectApprovalEntity:项目审批表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tik_project_approval")
public class ProjectApprovalEntity extends BaseEntity implements Serializable {

	/**
	 * 项目名称
	 */
	@ApiModelProperty(value = "项目名称",required = true)
	@Column(name = "name")
	private String name;

	/**
	 * 英文名称
	 */
	@ApiModelProperty(value = "英文名称",required = true)
	@Column(name = "english_name")
	private String englishName;

	/**
	 * 审批发起人
	 */
	@ApiModelProperty(value = "审批发起人",required = true)
	@Column(name = "initiator")
	private String initiator;

	/**
	 * 审批状态，0代表未审批，1代表已经审批
	 */
    @ApiModelProperty(value = "审批状态，0代表未审批，1代表已经审批")
	@Column(name = "approval_status")
	private Integer approvalStatus;

	/**
	 * 审批人
	 */
    @ApiModelProperty(value = "审批人")
	@Column(name = "approver")
	private String approver;


}
