package com.dmsdbj.integral.backstage.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 19:46 2020/8/12
 */
@Data
public class WeightAndIntegralModel {
    private String id;
    private List<String> userDingIds;
    private int weight;
    private int integral;
    private int reductionAuth;
    private String useId;
}
