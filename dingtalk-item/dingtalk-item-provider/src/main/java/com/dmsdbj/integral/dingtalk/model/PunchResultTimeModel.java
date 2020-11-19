package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

@Data
public class PunchResultTimeModel {

    //应打卡时间
    private  String baseCheckTime;
    //考勤类型
    private  String checkType;
    //用户钉钉id
    private  String userId;
    //    private  String corpId;
    private  String groupId;
    //位置结构
    private  String locationResult;
    //排班id
    private  String planId;
    //打卡记录id
    private  String recordId;
    //时间结果 normal:正常 NotSigned:未打卡 Late：迟到;
    private  String timeResult;
    //实际打卡时间
    private  String userCheckTime;
   //审批实例id
    private  String procInstId;
}
