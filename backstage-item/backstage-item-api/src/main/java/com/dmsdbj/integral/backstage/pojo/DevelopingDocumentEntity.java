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
 * DevelopingDocument实体
 * 开发文档表
 *
 * @author 马珂 
 * @version ${version}
 * @since ${version} 2020-07-26 16:23:03
 */
@ApiModel(value = "DevelopingDocumentEntity:开发文档表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tik_developing_document")
public class DevelopingDocumentEntity extends BaseEntity implements Serializable {

	/**
	 * 文档内容
	 */
    @ApiModelProperty(value = "文档内容")
	@Column(name = "content")
	private String content;


}
