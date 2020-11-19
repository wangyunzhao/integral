package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

@Data
public class HandleResultModel {
    /**
     * 处理id
     */
    private String operateId;
    /**
     * 处理人姓名
     */
    private String operatorName;
    /**
     * 处理人DingId
     */
    private String operatorDingId;
    /**
     * 处理人级别
     */
    private String operatorType;
    /**
     * 处理结果
     */
    private String operateResult;
    /**
     * 是否联系上
     */
    private String isHandle;
}
