package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 20:44 2020/6/11
 */

public class ErrorInfoModel {
    private String type;

    public String getType() {
        return type;
    }

    public ErrorInfoModel setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public ErrorInfoModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getParam() {
        return param;
    }

    public ErrorInfoModel setParam(String param) {
        this.param = param;
        return this;
    }

    public String getResult() {
        return result;
    }

    public ErrorInfoModel setResult(String result) {
        this.result = result;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public ErrorInfoModel setInfo(String info) {
        this.info = info;
        return this;
    }

    private String name;
    private String param;
    private String result;
    private String info;
}
