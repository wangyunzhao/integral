package com.dmsdbj.integral.dingtalk.model;

import com.dingtalk.item.pojo.AllusersEntity;
import lombok.Data;

@Data
public class CallInterfaceResultModel {
    private String code;
    private String message;
    private AllusersEntity data;
}
