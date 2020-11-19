package com.dmsdbj.integral.backstage.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description 项目管理
 * @Author 冯瑞姣
 * @Date: 2020-08-05 17:37
 * @Version: 1.0
 **/
@Data
public class ProjectModel {

    public String id;
    public String name;
    public String englishName;
    public String createRole;
    public String secretId;
    public String secretKey;
    public String operator;
    public Date createTime;
    public Date updateTime;
}