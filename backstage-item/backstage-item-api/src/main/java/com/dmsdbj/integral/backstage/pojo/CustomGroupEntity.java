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
 * CustomGroup实体
 * 自定义分组表
 *
 * @author 马珂 
 * @version ${version}
 * @since ${version} 2020-07-26 16:23:03
 */
@ApiModel(value = "CustomGroupEntity:自定义分组表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tik_custom_group")
public class CustomGroupEntity extends BaseEntity implements Serializable {

	/**
	 * 钉钉id
	 */
	@ApiModelProperty(value = "钉钉id",required = true)
	@Column(name = "ding_id")
	private String dingId;

	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名",required = true)
	@Column(name = "name")
	private String name;

	/**
	 * 分组名
	 */
	@ApiModelProperty(value = "分组名",required = true)
	@Column(name = "group_name")
	private String groupName;


}
