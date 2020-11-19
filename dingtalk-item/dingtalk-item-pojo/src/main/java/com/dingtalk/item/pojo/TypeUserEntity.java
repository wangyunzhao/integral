package com.dingtalk.item.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TypeUserEntity:用户级别表")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode()
@TableName(value = "tid_type_user")
public class TypeUserEntity implements Serializable {
    /**
     * id
     */
    @Id
    @ApiModelProperty(value = "用户级别id",required = true,hidden = true)
    @Column(name = "id")
    private String id= IdWorker.getIdStr();
    /**
     * 用户钉钉id
     */
    @ApiModelProperty(value = "用户钉钉id",required = true)
    @Column(name = "user_ding_id")
    private String userDingId;
    /**
     * 级别
     */
    @ApiModelProperty(value = "级别",required = true)
    @Column(name = "type")
    private String type;
    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记",hidden = true)
    @Column(name = "is_delete")
    private int isDelete=0;

    /**
     * 创建日期
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间",hidden = true)
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新日期
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8"
    )
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间",hidden = true)
    @Column(name = "update_time")
    private Date updateTime;



}
