package com.dmsdbj.integral.backstage.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmsdbj.itoo.tool.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.*;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;

/**
 * Project实体
 * 项目表
 *
 * @author 马珂 
 * @version ${version}
 * @since ${version} 2020-07-26 16:23:03
 */
@ApiModel(value = "ProjectEntity:项目表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tik_project")
public class ProjectEntity extends BaseEntity implements Serializable {

	/**
	 * 项目名称
	 */
	@ApiModelProperty(value = "项目名称",required = true)
	@Column(name = "name")
	private String name;

	/**
	 * 英文名
	 */
	@ApiModelProperty(value = "英文名",required = true)
	@Column(name = "english_name")
	private String englishName;

	/**
	 * 用于表示API调用者的身份
	 */
    @ApiModelProperty(value = "用于表示API调用者的身份")
	@Column(name = "secret_id")
	private String secretId;

	/**
	 * 用于加密签名字符串和服务器端验证签名字符串密钥
	 */
    @ApiModelProperty(value = "用于加密签名字符串和服务器端验证签名字符串密钥")
	@Column(name = "secret_key")
	private String secretKey;

	/**
	 * 兑换比率
	 */
    @ApiModelProperty(value = "兑换比率")
	@Column(name = "exchange_rate")
	private BigDecimal exchangeRate;

	/**
	 * 创建人角色
	 */
    @ApiModelProperty(value = "创建人角色")
	@Column(name = "creater_role")
	private String createrRole;


}
