package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

import java.security.PrivateKey;
import java.security.Timestamp;

@Data
public class LeaveStatusModel {
    private String duration_unit;
    private int duration_percent;
    private String end_time;
    private String start_time;
    private String userid;
}
