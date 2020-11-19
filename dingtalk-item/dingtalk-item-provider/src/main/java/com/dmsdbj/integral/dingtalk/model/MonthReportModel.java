package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

@Data
public class MonthReportModel {
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 违纪次数
     */
    private String disobedientNum;
    /**
     * 违纪原因
     */
    private String disobedientReason;
    /**
     * 请假次数
     */
    private String leaveNum;
    /**
     * 请假类型
     */
    private String leaveType;
    /**
     * 请假累计时长
     */
    private String leaveTime;
    /**
     * 请假原因
     */
    private String leaveReason;

}
