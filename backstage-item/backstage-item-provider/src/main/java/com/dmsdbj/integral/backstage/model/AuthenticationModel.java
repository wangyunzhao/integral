package com.dmsdbj.integral.backstage.model;

import lombok.Data;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 8:12 2020/7/27
 */
@Data
public class AuthenticationModel {
    private String secretId;
    private String secretKey;
    private String appName;
}
