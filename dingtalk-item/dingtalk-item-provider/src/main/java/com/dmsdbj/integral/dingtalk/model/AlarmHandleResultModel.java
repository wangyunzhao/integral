package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

import java.util.List;

@Data
public class AlarmHandleResultModel {
    /**
     * 告警id
     */
    private String alarmId;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户所在组织（14期）
     */
    private String userOrganization;
    /**
     * 用户状态（下班未打卡-未发审批）
     */
    private String userStatus;
    /**
     * 用户是否安全（未处理和未联系上的都为0，表示不安全，已联系上的为1，表示安全）
     */
    private String isSafe;
    /**
     * 用户违纪次数
     */
    private String disobedientNum;
    /**
     * 处理结果
     */
    private List<HandleResultModel> handleResult;
}
