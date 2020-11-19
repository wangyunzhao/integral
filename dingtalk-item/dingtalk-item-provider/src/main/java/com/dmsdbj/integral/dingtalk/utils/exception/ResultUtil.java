package com.dmsdbj.integral.dingtalk.utils.exception;

import lombok.Data;

@Data
public class ResultUtil {
    String code;
    String message;

    public ResultUtil(String code, String mes) {
        this.code = code;
        this.message = mes;
    }
}
