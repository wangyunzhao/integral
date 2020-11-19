package com.dmsdbj.integral.backstage.model;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/12 10:22
 * @describe 特定人员加分model
 */
@Data
public class SpecificPointsModel {
    @Column(name="id")
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="integral")
    private Integer integral;

    @Column(name="type")
    private Integer type;

    @Column(name="operator")
    private String operator;
}
