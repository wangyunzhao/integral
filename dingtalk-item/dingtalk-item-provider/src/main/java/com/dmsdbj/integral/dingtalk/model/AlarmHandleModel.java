package com.dmsdbj.integral.dingtalk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 15:09 2020/6/22
 */
@Data
public class AlarmHandleModel {

    @Column(name = "id")
    private String id;

    @Column(name = "alarm_id")
    private String alarmId;

    @Column(name = "operator_ding_id")
    private String operatorDingId;

    @Column(name = "operator_name")
    private String operatorName;

    @Column(name = "operator_type")
    private int operatorType;

    @Column(name = "operate_result")
    private String operateResult;

    @Column(name = "is_handle")
    private int isHandle;

}
