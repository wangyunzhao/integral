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
 * GradeRule实体
 * 加分规则
 *
 * @author 梁佳宝 
 * @version ${version}
 * @since ${version} 2020-06-02 10:15:13
 */
@ApiModel(value = "GradeRuleEntity:加分规则")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tid_grade_rule")
public class GradeRuleEntity extends BaseEntity implements Serializable {

	/**
	 * 规则ID
	 */
    @ApiModelProperty(value = "规则ID")
	@Column(name = "rule_name")
	private String ruleName;

	/**
	 * 资源ID，人或组织
	 */
    @ApiModelProperty(value = "资源ID，人或组织")
	@Column(name = "integral")
	private Integer integral;

	/**
	 * 打卡时间结果
	 */
    @ApiModelProperty(value = "打卡时间结果")
	@Column(name = "time_result")
	private String timeResult;


}
