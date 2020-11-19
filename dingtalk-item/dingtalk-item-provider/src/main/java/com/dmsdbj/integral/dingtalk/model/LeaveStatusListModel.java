package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

import java.util.List;

@Data
public class LeaveStatusListModel {
    private boolean has_more;
    private List<LeaveStatusModel> leaves_tatus;
}
