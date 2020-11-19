package com.dmsdbj.integral.backstage.model;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/12 10:16
 * @describe 可赠积分model
 */
@Data
public class BonusPointsModel {
    @Column(name="id")
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="weight")
    private Integer weight;

    @Column(name="reduction_auth")
    private Integer reductionAuth;

    @Column(name="type")
    private Integer type;

    @Column(name="operator")
    private String operator;
}
