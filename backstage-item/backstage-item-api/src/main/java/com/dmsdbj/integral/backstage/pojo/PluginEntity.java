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
 * Plugin实体
 * 插件表
 *
 * @author 马珂 
 * @version ${version}
 * @since ${version} 2020-07-26 16:23:03
 */
@ApiModel(value = "PluginEntity:插件表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tik_plugin")
public class PluginEntity extends BaseEntity implements Serializable {

	/**
	 * 项目id
	 */
	@ApiModelProperty(value = "项目id",required = true)
	@Column(name = "project_id")
	private String projectId;

	/**
	 * 插件名字
	 */
    @ApiModelProperty(value = "插件名字")
	@Column(name = "name")
	private String name;

	/**
	 * 插件图标url
	 */
    @ApiModelProperty(value = "插件图标url")
	@Column(name = "icon_url")
	private String iconUrl;

	/**
	 * 插件页面url
	 */
    @ApiModelProperty(value = "插件页面url")
	@Column(name = "page_url")
	private String pageUrl;

	/**
	 * 插件图片url
	 */
    @ApiModelProperty(value = "插件图片url")
	@Column(name = "image_url")
	private String imageUrl;

	/**
	 * 插件描述
	 */
    @ApiModelProperty(value = "插件描述")
	@Column(name = "description")
	private String description;

	/**
	 * 插件描述
	 */
	@ApiModelProperty(value = "是否全部人员")
	@Column(name = "is_everybody")
	private Integer isEverybody;

}
