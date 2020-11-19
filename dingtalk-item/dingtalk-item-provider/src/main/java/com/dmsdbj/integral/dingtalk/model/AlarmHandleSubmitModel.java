package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

@Data
public class AlarmHandleSubmitModel {
     //处理人钉钉id
    private String operatorDingId;
    //报警id
    private String alarmId;
    //处理结果
    private String  operateResult;
    //是否提交到上级
    private  String isSubmitSuperior;
    //是否安全
    private String isSafe;

}
