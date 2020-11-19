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
 * BonusPoints实体
 * 可赠积分表
 *
 * @author 马珂 
 * @version ${version}
 * @since ${version} 2020-07-26 16:23:03
 */
@ApiModel(value = "BonusPointsEntity:可赠积分表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tik_bonus_points")
public class BonusPointsEntity extends BaseEntity implements Serializable {

	/**
	 * 人员名称或部门名称
	 */
	@ApiModelProperty(value = "人员名称或部门名称",required = true)
	@Column(name = "name")
	private String name;

	/**
	 * 可赠积分分值
	 */
	@ApiModelProperty(value = "可赠积分分值",required = true)
	@Column(name = "integral")
	private Integer integral;

	/**
	 * 权重
	 */
	@ApiModelProperty(value = "权重",required = true)
	@Column(name = "weight")
	private Integer weight;

	/**
	 * 是否有减分权限(0-无权限，1-有权限）
	 */
	@ApiModelProperty(value = "是否有减分权限(0-无权限，1-有权限）",required = true)
	@Column(name = "reduction_auth")
	private Integer reductionAuth;

	/**
	 * 可赠积分类型(0-按照部门加分，1-按照人员加分)
	 */
	@ApiModelProperty(value = "可赠积分类型(0-按照部门加分，1-按照人员加分)",required = true)
	@Column(name = "type")
	private Integer type;


}
