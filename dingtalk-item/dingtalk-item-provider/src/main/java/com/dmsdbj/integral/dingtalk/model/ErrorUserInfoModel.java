package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 12:18 2020/6/12
 */
@Data
public class ErrorUserInfoModel {
    private String userDingId;
    private String userJifenId;
    private String userName;
    private String errorInfo;
}
