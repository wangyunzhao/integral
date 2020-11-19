package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

import java.security.SecureRandom;

@Data
public class AlarmInfoModel {
    //告警信息处理表id
    private String id;
    //告警信息表id
    private String alarmId;
    //违纪人姓名
    private String userName;
    //违纪人备注（违纪详情）
    private String remark;
    //纪委对违纪人的处理结果
    private String result;
}
