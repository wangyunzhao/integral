package com.dingtalk.item.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmsdbj.itoo.tool.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Allusers实体
 * 用户表
 *
 * @author 王云召 
 * @version ${version}
 * @since ${version} 2019-09-11 22:13:52
 */
@ApiModel(value = "AllusersEntity:用户表")
@Data
@TableName(value = "tc_allusers")
public class AllusersEntity extends BaseEntity implements Serializable {

	/**
	 * 钉钉id
	 */
    @ApiModelProperty(value = "钉钉id")
	@Column(name = "ding_id")
	private String dingId;

	/**
	 * 用户编码
	 */
    @ApiModelProperty(value = "用户编码")
	@Column(name = "user_code")
	private String userCode;

	/**
	 * 密码
	 */
    @ApiModelProperty(value = "密码")
	@Column(name = "password")
	private String password;

	/**
	 * 用户姓名
	 */
    @ApiModelProperty(value = "用户姓名")
	@Column(name = "user_name")
	private String userName;

	/**
	 * 学校编号
	 */
    @ApiModelProperty(value = "学校编号")
	@Column(name = "school_no")
	private String schoolNo;
	/**
	 * 权限token
	 */
	private String token;
	/**
	 * qq登录唯一标示
	 */
    @ApiModelProperty(value = "qq登录唯一标示")
	@Column(name = "qq_open_id")
	private String qqOpenId;

	/**
	 * 微信统一标识
	 */
    @ApiModelProperty(value = "微信统一标识")
	@Column(name = "unionid")
	private String unionid;

	/**
	 * 微信统一标识是否有效（0/1 有效/无效）
	 */
    @ApiModelProperty(value = "微信统一标识是否有效（0/1 有效/无效）")
	@Column(name = "unionid_valid")
	private Integer unionidValid;

	/**
	 * 电子邮件
	 */
    @ApiModelProperty(value = "电子邮件")
	@Column(name = "email")
	private String email;

	/**
	 * 微信公众号唯一标识
	 */
    @ApiModelProperty(value = "微信公众号唯一标识")
	@Column(name = "wx_plant_form")
	private String wxPlantForm;

	/**
	 * 微信公众号唯一标识是否有效（0/1 有效/无效）
	 */
    @ApiModelProperty(value = "微信公众号唯一标识是否有效（0/1 有效/无效）")
	@Column(name = "wx_plant_valid")
	private Integer wxPlantValid;

	/**
	 * 移动电话
	 */
    @ApiModelProperty(value = "移动电话")
	@Column(name = "tel_num")
	private String telNum;

	/**
	 * 是否锁定(0/1 未锁定/锁定)
	 */
    @ApiModelProperty(value = "是否锁定(0/1 未锁定/锁定)")
	@Column(name = "is_lock")
	private Integer isLock;


}
