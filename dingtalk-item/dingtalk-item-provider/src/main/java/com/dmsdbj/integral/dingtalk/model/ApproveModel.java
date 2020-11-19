package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;

@Data
public class ApproveModel {
    private String component_type;
    private String id;
    private String name;
    private String value;
    private String ext_value;

    public String getComponent_type() {
        return component_type;
    }

    public void setComponent_type(String component_type) {
        this.component_type = component_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExt_value() {
        return ext_value;
    }

    public void setExt_value(String ext_value) {
        this.ext_value = ext_value;
    }
}
