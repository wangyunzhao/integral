package com.dmsdbj.integral.backstage.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author 齐智
 * @Date: 2020/6/20 16:06
 * @Version: 1.0
 **/
@ApiModel(value="DingReturnModel:钉钉返回消息信息")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public class DingReturnModel {
    private String accessToken;
    private Integer errcode;
    private String errmsg;

}
