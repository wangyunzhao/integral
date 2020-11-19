package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

@Data
public class DingLeavesStatusModel {
    private String errmsg;
    private int errcode;
    private LeaveStatusListModel result;
    private boolean success;
}
