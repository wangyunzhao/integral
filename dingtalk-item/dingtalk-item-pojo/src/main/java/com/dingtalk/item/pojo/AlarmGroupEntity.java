package com.dingtalk.item.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmsdbj.itoo.tool.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.*;
import java.io.Serializable;
import javax.persistence.Column;

/**
 * AlarmGroup实体
 * 告警分组表
 *
 * @author 梁佳宝 
 * @version ${version}
 * @since ${version} 2020-06-02 10:15:13
 */
@ApiModel(value = "AlarmGroupEntity:告警分组表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tid_alarm_group")
public class AlarmGroupEntity extends BaseEntity implements Serializable {

	/**
	 * 钉钉异常群名称
	 */
    @ApiModelProperty(value = "钉钉异常群名称")
	@Column(name = "group_name")
	private String groupName;

	/**
	 * 钉钉异常群ID
	 */
    @ApiModelProperty(value = "钉钉异常群ID")
	@Column(name = "group_id")
	private String groupId;

	/**
	 * 组织机构ID
	 */
    @ApiModelProperty(value = "组织机构ID")
	@Column(name = "organization_id")
	private String organizationId;


}
