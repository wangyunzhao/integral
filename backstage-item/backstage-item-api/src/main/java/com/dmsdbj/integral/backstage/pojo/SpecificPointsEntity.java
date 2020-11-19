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
 * SpecificPoints实体
 * 特定加分表
 *
 * @author 马珂 
 * @version ${version}
 * @since ${version} 2020-07-26 16:23:03
 */
@ApiModel(value = "SpecificPointsEntity:特定加分表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tik_specific_points")
public class SpecificPointsEntity extends BaseEntity implements Serializable {

	/**
	 * 人员名称或部门名称
	 */
	@ApiModelProperty(value = "人员名称或部门名称",required = true)
	@Column(name = "name")
	private String name;

	/**
	 * 特定加分分值
	 */
	@ApiModelProperty(value = "特定加分分值",required = true)
	@Column(name = "integral")
	private Integer integral;

	/**
	 * 可赠积分类型(0-按照部门加分，1-按照人员加分)
	 */
	@ApiModelProperty(value = "可赠积分类型(0-按照部门加分，1-按照人员加分)",required = true)
	@Column(name = "type")
	private Integer type;


}
