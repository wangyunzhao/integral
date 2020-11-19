package com.dmsdbj.integral.backstage.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrganizationModel {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "父id",hidden = true)
    private Long parentid;
    @ApiModelProperty(value = "name")
    private String name;
}
