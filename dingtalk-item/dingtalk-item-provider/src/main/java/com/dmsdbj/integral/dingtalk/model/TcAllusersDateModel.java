package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

/**
 * 权限表中对应字段
 */
@Data
public class TcAllusersDateModel {
    //钉id
    private String dingid;
    //积分id
    private String id;
    //手机号
    private String usercode;
    //用户名
    private String username;
}
